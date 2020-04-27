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
import com.poligran.gopoli.retos.demo.DTO.CreateUserDTO;
import com.poligran.gopoli.retos.demo.Entities.Role;
import com.poligran.gopoli.retos.demo.Entities.User;
import com.poligran.gopoli.retos.demo.DTO.UserDTO;
import com.poligran.gopoli.retos.demo.Repositories.Role_Repository;
import com.poligran.gopoli.retos.demo.Repositories.User_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class User_Controller {


    private final User_Repository user_repository;
    private final UserDTOConverter userDTOConverter;


    private final Role_Repository role_repository;




    @GetMapping("/usuarios")
    public ResponseEntity<?> obtenerTodos() {
        List<User> users = user_repository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();

        } else {


            List<UserDTO> dtoList =
                    users.stream()
                            .map(userDTOConverter::convertToDto)
                            .collect(Collectors.toList());

            return ResponseEntity.ok(dtoList);
        }
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<?> obtenerUno(@PathVariable int id) {

        User result =  user_repository.findById(id).orElse(null);

        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(result);
        }
    }



    // Creación del Usuario //


    @PostMapping("/usuario")
    public ResponseEntity<User> nuevoUsuario
            (@RequestParam String firts_name,
             @RequestParam String last_name,
             @RequestParam String email,
             @RequestParam long phone_number,
             @RequestParam int rol) {

        //CreateUserDTO user = user_repository.save(nuevo);
        //return ResponseEntity.status(HttpStatus.CREATED).body(user);

        User nuevoUser = new User();
        nuevoUser.setFirts_name(firts_name);
        nuevoUser.setLast_name(last_name);
        nuevoUser.setEmail(email);
        nuevoUser.setPhone_number(phone_number);

        //Se obtiene el objeto rol //
        Role role = role_repository.findById(rol).orElse(null);

        // Se asigna el rol al objeto User //
        nuevoUser.setRole(role);

        return ResponseEntity.status(HttpStatus.CREATED).body(user_repository.save(nuevoUser));


    }

    @PutMapping("/producto/{id}")
    public ResponseEntity<?> editarProducto(@RequestBody User user, @PathVariable int id) {


       return user_repository.findById(id).map(p -> {

            p.setFirts_name(user.getFirts_name());
            p.setLast_name(user.getLast_name());
            p.setEmail(user.getEmail());
            p.setPhone_number(user.getPhone_number());

            return ResponseEntity.ok(user_repository.save(p));


        }).orElseGet(() -> {
            return ResponseEntity.notFound().build();
        });



    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> borrarUsuario(@PathVariable int id) {
        user_repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }








}
