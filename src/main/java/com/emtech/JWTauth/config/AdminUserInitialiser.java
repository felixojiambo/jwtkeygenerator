//package com.emtech.JWTauth.config;
//import com.emtech.JWTauth.models.Role;
//import com.emtech.JWTauth.models.User;
//import com.emtech.JWTauth.repository.UserRepository;
//import com.emtech.JWTauth.services.UserDetailsServiceImp;
//import jakarta.annotation.PostConstruct;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Lazy;
//
//import java.util.List;
//
//
//@Configuration
//public class AdminUserInitialiser {
//@Autowired
//    private final UserDetailsServiceImp userDetailsServiceImp;
//    private final UserRepository userRepository; // Add this line
//
//    @Autowired
//    public AdminUserInitialiser(UserDetailsServiceImp userDetailsServiceImp, UserRepository userRepository) {
//        this.userDetailsServiceImp = userDetailsServiceImp;
//        this.userRepository = userRepository;
//    }
//
//    @PostConstruct
//    public void initializeAdminUser() {
//        List<User> users = userRepository.findAll();
//        boolean adminExists = users.stream().anyMatch(user -> user.getRole() == Role.ADMIN);
//
//        if (!adminExists) {
//            User adminUser = new User();
//            adminUser.setFirstName("Admin");
//            adminUser.setLastName("User");
//            adminUser.setEmail("felix@gmail.com");
//
//            adminUser.setPassword("password");
//            adminUser.setRole(Role.ADMIN);
//
//            userRepository.save(adminUser);
//            System.out.println("Admin user created.");
//        } else {
//            System.out.println("Admin user already exists.");
//        }
//    }
//}
