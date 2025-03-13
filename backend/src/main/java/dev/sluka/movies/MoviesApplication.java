package dev.sluka.movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@EnableJpaRepositories(basePackages = "dev.sluka.movies.Repository")
@EntityScan(basePackages = "dev.sluka.movies.Entity")
public class MoviesApplication {

	public static void main(String[] args) {SpringApplication.run(MoviesApplication.class, args);}

	@GetMapping("/")
	public String apiRoot() {
		return "Welcome to the Movies API";
	}

	// @GetMapping ("/csrf")
	// public CsrfToken getToken (HttpServletRequest request) {
	// 	return (CsrfToken) request.getAttribute("_csrf");

	// }
	

}
