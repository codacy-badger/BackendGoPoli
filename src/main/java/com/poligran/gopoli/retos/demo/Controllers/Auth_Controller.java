package com.poligran.gopoli.retos.demo.Controllers;


import com.poligran.gopoli.retos.demo.Auth.ApiResponse;
import com.poligran.gopoli.retos.demo.Entities.Role;
import com.poligran.gopoli.retos.demo.Entities.RoleName;
import com.poligran.gopoli.retos.demo.Entities.Type_User;
import com.poligran.gopoli.retos.demo.Entities.User;
import com.poligran.gopoli.retos.demo.Errors.AppException;
import com.poligran.gopoli.retos.demo.Repositories.Role_Repository;
import com.poligran.gopoli.retos.demo.Repositories.Type_User_Repository;
import com.poligran.gopoli.retos.demo.Repositories.User_Repository;
import com.poligran.gopoli.retos.demo.Security.JwtAuthenticationResponse;
import com.poligran.gopoli.retos.demo.Security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class Auth_Controller {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    User_Repository userRepository;

    @Autowired
    Role_Repository roleRepository;

    @Autowired
    Type_User_Repository type_user_repository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestParam String email, @RequestParam String password) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));


    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(
            @RequestParam String firts_name,
            @RequestParam String last_name,
            @RequestParam String phone_number,
            @RequestParam int type_user,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password


    ) {

        //  @RequestBody SignUpRequest signUpRequest

        if (userRepository.existsByUsername(username)) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(email)) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account

        Type_User typeUser = type_user_repository.findById(type_user).orElse(null);

        User user = new User(firts_name, last_name, phone_number, typeUser, username, email, password);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));

    }


}
