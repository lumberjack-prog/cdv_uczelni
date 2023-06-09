package com.example.cdv_uczelni;

import com.example.cdv_uczelni.model.*;
import com.example.cdv_uczelni.service.impl.UniversityService;
import com.example.cdv_uczelni.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;


@SpringBootApplication
@Slf4j
public class CdvUniversityApplication {

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(CdvUniversityApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(UserService userService, UniversityService universityService) {
        return args -> {

            UserDto admin = userService.findUserByUsername("admin");

            if (admin == null) {
                User user = new User();
                user.setUsername("admin");
                user.setEmail("admin@gmail.com");
                user.setPassword(passwordEncoder.encode("pass"));

                userService.save(user);
            }

//            University university = new University();
//            university.setCity("Poznan");
//            university.setName("CDV");
//            university.setType("technical");
//            Faculty faculty = new Faculty();
//            faculty.setName("WYDZIAŁ INFORMATYKI I TELEKOMUNIKACJI");
//            Major major = new Major();
//            major.setName("Informatyka");
//            major.setType("techniczne");
//            faculty.setMajors(Set.of(major));
//            university.setFaculties(List.of(faculty));
//
//            universityService.save(university);

        };
    }

}
