package sk.ukf.opizza.service;

import sk.ukf.opizza.entity.Tag;
import java.util.List;

public interface TagService {
    List<Tag> getAllTags();
    Tag getTagById(int id);
    void saveTag(Tag tag);
    void deleteTag(int id);
}