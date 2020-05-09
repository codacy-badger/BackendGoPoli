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

import com.poligran.gopoli.retos.demo.Converter.UserDTOConverter;
import com.poligran.gopoli.retos.demo.DTO.UserDTO;
import com.poligran.gopoli.retos.demo.Entities.Type_User;
import com.poligran.gopoli.retos.demo.Entities.User;
import com.poligran.gopoli.retos.demo.Objects.UserIdentityAvailability;
import com.poligran.gopoli.retos.demo.Repositories.Type_User_Repository;
import com.poligran.gopoli.retos.demo.Repositories.User_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class User_Controller {

    @Autowired
    private final User_Repository user_repository;

    private final UserDTOConverter userDTOConverter;
    private final Type_User_Repository typeUser_repository;



    @GetMapping("/users")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> authenticateUser() {

        List<User> user = user_repository.findAll();

        return ResponseEntity.ok(user);

    }

    @GetMapping("/user/checkUsernameAvailability")
    public Boolean checkUsernameAvailability(@RequestParam String username) {
        return !user_repository.existsByUsername(username);

    }

    @GetMapping("/user/checkEmailAvailability")
    public Boolean checkEmailAvailability(String email) {
        return !user_repository.existsByEmail(email);

    }






/*    @PostMapping("/usuario")
    public ResponseEntity<?> nuevoUsuario
            (@RequestParam String firts_name,
                              @RequestParam String last_name,
                              @RequestParam String email, @RequestParam String password,
                              @RequestParam long phone_number,
                              @RequestParam int rol) {


        try {

            User usuario = new User();
            usuario.setEmail(email);
         //   usuario.set(passwordEncoder.encode(password));

            usuario.setFirts_name(firts_name);
            usuario.setLast_name(last_name);
            usuario.setPhone_number(phone_number);

            Type_User role = typeUser_repository.findById(rol).orElse(null);
            usuario.setTypeUser(role);


         //  UserDTO usuarioDTO = userDTOConverter.convertToDto(usuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(user_repository.save(usuario));

        } catch (DataIntegrityViolationException exception) {

            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Hay un problema al crear el usuario.");

        }
    }*/




}


