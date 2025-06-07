package com.example.codingCamp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import jakarta.transaction.Transactional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

import com.example.codingCamp.profile.model.Student;
import com.example.codingCamp.profile.model.Teacher;
import com.example.codingCamp.profile.model.Parent;
import com.example.codingCamp.profile.model.Role;
import com.example.codingCamp.profile.model.UserModel;
import com.example.codingCamp.profile.repository.RoleRepository;
import com.example.codingCamp.profile.repository.UserRepository;
import com.example.codingCamp.profile.service.UserService;


@SpringBootApplication
@EnableAsync
public class CodingCampApplication {

	public static void main(String[] args) {
		System.out.println("DATABASE_URL = " + System.getenv("DATABASE_URL"));
		SpringApplication.run(CodingCampApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run(
			RoleRepository roleRepository,
			UserRepository userRepository,
			UserService userService) {
		return args -> {
			// SUPER_ADMIN role
			if (roleRepository.findByRole("SUPER_ADMIN").isEmpty()) {
				Role role = new Role();
				role.setRole("SUPER_ADMIN");
				roleRepository.save(role);
			}

			if (userRepository.findByUsername("admin_budi") == null) {
				UserModel user = new UserModel();
				user.setName("Admin Budi");
				user.setUsername("admin_budi");
				user.setEmail("bprasetyo21@outlook.com");
				user.setPhone("081234567890");
				user.setPassword(userService.hashPassword("budi"));
				user.setRole(roleRepository.findByRole("SUPER_ADMIN").orElse(null));
				userRepository.save(user);
			}

			// STUDENT role
			if (roleRepository.findByRole("STUDENT").isEmpty()) {
				Role role = new Role();
				role.setRole("STUDENT");
				roleRepository.save(role);
			}

			if (userRepository.findByUsername("student_rini") == null) {
				Student student = new Student();
				student.setName("Rini Student");
				student.setUsername("student_rini");
				student.setEmail("rini.student@example.com");
				student.setPhone("081234567895");
				student.setPassword(userService.hashPassword("rini!"));
				student.setRole(roleRepository.findByRole("STUDENT").orElse(null));
				// daftarNilai dan orangTua bisa null dulu saat faker
				userRepository.save(student);
			}

			// TEACHER role
			if (roleRepository.findByRole("TEACHER").isEmpty()) {
				Role role = new Role();
				role.setRole("TEACHER");
				roleRepository.save(role);
			}

			if (userRepository.findByUsername("teacher_budi") == null) {
				Teacher teacher = new Teacher();
				teacher.setName("Budi Teacher");
				teacher.setUsername("teacher_budi");
				teacher.setEmail("budi.teacher@example.com");
				teacher.setPhone("081234567896");
				teacher.setPassword(userService.hashPassword("teacher!"));
				teacher.setRole(roleRepository.findByRole("TEACHER").orElse(null));
				userRepository.save(teacher);
			}

			// PARENT role
			if (roleRepository.findByRole("PARENT").isEmpty()) {
				Role role = new Role();
				role.setRole("PARENT");
				roleRepository.save(role);
			}

			if (userRepository.findByUsername("parent_ani") == null) {
				Parent parent = new Parent();
				parent.setName("Ani Parent");
				parent.setUsername("parent_ani");
				parent.setEmail("ani.parent@example.com");
				parent.setPhone("081234567897");
				parent.setPassword(userService.hashPassword("ParentAni2025!"));
				parent.setRole(roleRepository.findByRole("PARENT").orElse(null));


				Student anak = (Student) userRepository.findByUsername("student_rini");
				parent.setAnak(anak);

				userRepository.save(parent);
			}
		};
	}

}
