/*
 *
 *  * Copyright (c) 2020. [Kevin Paul Montealegre Melo]
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in
 *  * all copies or substantial portions of the Software.
 *
 */

package com.poligran.gopoli.retos.demo.Controllers;


import com.poligran.gopoli.retos.demo.Auth.ApiResponse;
import com.poligran.gopoli.retos.demo.Entities.Role;
import com.poligran.gopoli.retos.demo.Entities.RoleName;
import com.poligran.gopoli.retos.demo.Entities.Type_User;
import com.poligran.gopoli.retos.demo.Entities.User;
import com.poligran.gopoli.retos.demo.Exceptions.AppException;
import com.poligran.gopoli.retos.demo.Repositories.Role_Repository;
import com.poligran.gopoli.retos.demo.Repositories.Type_User_Repository;
import com.poligran.gopoli.retos.demo.Repositories.User_Repository;
import com.poligran.gopoli.retos.demo.Security.JwtAuthenticationResponseUser;
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

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;


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
        User user = userRepository.findByUsernameOrEmail(email, email).orElse(null);



        JwtAuthenticationResponseUser jwtAuthenticationResponseUser =
                new JwtAuthenticationResponseUser(jwt, user.getId(), user.getFirts_name(),
                        user.getLast_name(),
                        user.getPhone_number(),
                        Integer.toString(user.getTypeUser().getId()),
                        user.getUsername(),
                        user.getEmail());


        return ResponseEntity.ok(jwtAuthenticationResponseUser);



    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(
            @RequestParam String firts_name,
            @RequestParam String last_name,
            @RequestParam String phone_number,
            @RequestParam int type_user,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) throws IOException {

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


    /*    sendEmail(email);
*/
        return ResponseEntity.created(location).body("User registered successfully");


    }

   /* void sendEmail(String email) throws IOException {

        Email from = new Email("test@example.com");
        String subject = "Sending with SendGrid is Fun";
        Email to = new Email("test@example.com");
        Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw ex;
        }


    }
*/

}
