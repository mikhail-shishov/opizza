package sk.ukf.opizza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OpizzaApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpizzaApplication.class, args);
	}

}
