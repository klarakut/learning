package learning.securitydemo;

import learning.securitydemo.models.Role;
import learning.securitydemo.models.User;
import learning.securitydemo.service.RoleService;
import learning.securitydemo.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SecurityDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityDemoApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run (UserService userService, RoleService roleService){
		return args -> {
			roleService.saveRole(new Role("ROLE_USER"));
			roleService.saveRole(new Role("ROLE_MANAGER"));
			roleService.saveRole(new Role("ROLE_ADMIN"));
			roleService.saveRole(new Role("ROLE_SUPER_ADMIN"));

			//userService.saveUser(new User("John Travolta","john","john@mail.com","1234", new ArrayList<>()));
			//userService.saveUser(new User("Will S","will","will@mail.com","1234", new ArrayList<>()));
			// userService.saveUser(new User("John Travolta","john","1234", new ArrayList<>()));
			// userService.saveUser(new User("Will S","will","1234", new ArrayList<>()));
			userService.saveUser(new User("Samantha","sam","1234", new ArrayList<>()));

			// userService.addRoleToUser("john","ROLE_USER");
			// userService.addRoleToUser("will","ROLE_ADMIN");
			userService.addRoleToUser("sam","ROLE_USER");
		};
	}
}
