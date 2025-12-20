package sk.ukf.opizza.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sk.ukf.opizza.config.UserPrincipal;
import sk.ukf.opizza.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class ItemController {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @Autowired
    public ItemController(CourseService courseService, EnrollmentService enrollmentService) {
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping("/")
    public String viewHomePage(Model model) {
        List<Course> courseList = courseService.getAllActiveCourses();
        model.addAttribute("courses", courseList);
        return "course/index";
    }

    // Detail kurzu - kontroluje, či je používateľ prihlásený a či má kurz zakúpený (enrollment)
    @GetMapping("/course/{id}")
    public String viewCourseDetail(@PathVariable("id") int id, Model model, Authentication authentication) {
        Course course = courseService.getCourseById(id);
        int enrolledStudents = enrollmentService.findAllUsersCountByCourse(course);
        List<Lesson> lessons = courseService.findLessonsByCourse(id);

        // Zistenie stavu prihlásenia zo Spring Security objektu Authentication
        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        boolean isEnrolled = false;
        boolean isOwner = false;

        Enrollment enrollmentEntity = null;

        if (isAuthenticated) {
            // Získanie prihláseného používateľa z nášho UserPrincipal (vytvoreného v Security konfigu)
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            User user = principal.getUser();

            // Kontrola, či je prihlásený používateľ autorom kurzu (učiteľom)
            isOwner = course.getTeacher() != null && course.getTeacher().getId() == user.getId();

            // Kontrola, či si študent kurz už zakúpil
            enrollmentEntity = enrollmentService.findByCourseAndUser(course, user);
            isEnrolled = enrollmentEntity != null;
        }

        // Poslanie všetkých vlajok do Thymeleafu pre podmienené zobrazenie tlačidiel (th:if)
        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        model.addAttribute("enrolledStudents", enrolledStudents);
        model.addAttribute("isAuthenticated", isAuthenticated);
        model.addAttribute("enrollment", enrollmentEntity);
        model.addAttribute("isEnrolled", isEnrolled);
        model.addAttribute("isOwner", isOwner);

        return "course/detail";
    }

    // Stránka pre sledovanie konkrétnej lekcie
    @GetMapping("/course/{courseId}/learn/lesson/{lessonId}")
    public String learnLesson(@PathVariable int courseId, @PathVariable int lessonId, Model model, Authentication authentication) {

        Course course = courseService.getCourseById(courseId);
        List<Lesson> lessons = courseService.findLessonsByCourse(courseId);

        // Nájdenie konkrétnej lekcie v zozname pomocou Java Stream API
        Lesson lesson = lessons.stream()
                .filter(l -> l.getId() == lessonId)
                .findFirst()
                .orElse(null);

        if (lesson == null) {
            return "redirect:/course/" + courseId;
        }

        boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
        boolean isOwner = false;
        boolean isEnrolled = false;

        if (isAuthenticated) {
            User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
            isOwner = course.getTeacher() != null && course.getTeacher().getId() == user.getId();
            isEnrolled = enrollmentService.findByCourseAndUser(course, user) != null;
        }

        // Zabezpečenie obsahu: Ak nie si majiteľ ani zapísaný študent, nemôžeš lekciu vidieť
        if (!isOwner && !isEnrolled) {
            return "redirect:/course/" + courseId;
        }

        model.addAttribute("course", course);
        model.addAttribute("lesson", lesson);
        model.addAttribute("lessons", lessons);
        model.addAttribute("isOwner", isOwner);

        return "course/learn";
    }

    // Spracovanie prihlásenia do kurzu (analógia k odoslaniu objednávky pizze)
    @PostMapping("/course/{courseId}/enroll")
    public String enrollStudentToCourse(@PathVariable int courseId, @RequestParam String coursePassword, Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        enrollmentService.enrollStudentToCourse(courseId, user.getId(), coursePassword);

        return "redirect:/course/" + courseId;
    }

    // Zoznam kurzov, ktoré si prihlásený študent kúpil
    @GetMapping("/student/my-courses")
    public String viewEnrolledCourses(Model model, Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        List<Course> enrolledCourses = enrollmentService.findAllCoursesByUser(user.getId());
        model.addAttribute("courses", enrolledCourses);
        return "course/my-courses";
    }

    // Zoznam kurzov, ktoré prihlásený učiteľ vytvoril
    @GetMapping("/teacher/my-learning")
    public String viewTeachingCourses(Model model, Authentication authentication) {
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();
        List<Course> teachingCourses = courseService.findUserCourses(user.getId());
        model.addAttribute("courses", teachingCourses);
        return "course/my-learning";
    }

    // Zobrazenie prázdneho formulára pre nový kurz (analógia k "Pridať novú pizzu")
    @GetMapping("/teacher/add-course")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "course/form";
    }

    // Uloženie nového alebo upraveného kurzu
    @PostMapping("/teacher/save-course")
    public String saveCourse(@ModelAttribute Course course, Authentication authentication) {
        User teacher = ((UserPrincipal) authentication.getPrincipal()).getUser();
        course.setTeacher(teacher); // Nastavenie aktuálneho používateľa ako autora
        courseService.saveCourse(course);
        return "redirect:/teacher/my-learning";
    }

    // Soft delete kurzu (nastaví deleted_at v DB namiesto úplného vymazania)
    @PostMapping("/course/delete/{id}")
    public String deleteCourse(@PathVariable int id, Authentication authentication) {
        Course course = courseService.getCourseById(id);
        User user = ((UserPrincipal) authentication.getPrincipal()).getUser();

        // Bezpečnostná poistka: Kurz môže zmazať len ten, kto ho vytvoril
        if (course.getTeacher().getId() != (user.getId())) {
            throw new RuntimeException("Nemáte oprávnenie zmazať tento kurz");
        }

        courseService.softDeleteCourse(id);
        return "redirect:/teacher/my-learning";
    }

    // Načítanie existujúceho kurzu do formulára pre úpravu
    @GetMapping("/course/edit/{id}")
    public String showEditCourseForm(@PathVariable int id, Model model, Authentication authenticationl) {
        Course course = courseService.getCourseById(id);
        User user = ((UserPrincipal) authenticationl.getPrincipal()).getUser();

        if (course.getTeacher().getId() != (user.getId())) {
            throw new RuntimeException("Nemáte oprávnenie upravovať tento kurz");
        }

        model.addAttribute("course", course);
        return "course/form";
    }
}