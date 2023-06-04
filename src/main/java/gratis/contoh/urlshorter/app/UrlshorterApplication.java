package gratis.contoh.urlshorter.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UrlshorterApplication {

	public static void main(String[] args) {
		SpringApplication.run(UrlshorterApplication.class, args);
	}

}
