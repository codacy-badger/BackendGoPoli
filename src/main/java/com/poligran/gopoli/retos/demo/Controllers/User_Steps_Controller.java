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
import com.poligran.gopoli.retos.demo.Entities.User;
import com.poligran.gopoli.retos.demo.Entities.User_Steps;
import com.poligran.gopoli.retos.demo.Repositories.Role_Repository;
import com.poligran.gopoli.retos.demo.Repositories.User_Repository;
import com.poligran.gopoli.retos.demo.Repositories.User_Steps_Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class User_Steps_Controller {


    private final User_Repository user_repository;
    private final User_Steps_Repository user_steps_repository;


    @PostMapping("/steps")
    public ResponseEntity<?> contadorPasos(@RequestParam int id, @RequestParam long steps) {


        User_Steps user_steps = user_steps_repository.findById(id).orElse(null);
        User usuario = user_repository.findById(id).orElse(null);


        if (usuario != null) {
            if (user_steps == null) {
                User_Steps userSteps = new User_Steps();
                userSteps.setUser(usuario);
                userSteps.setId(id);
                userSteps.setSteps(steps);
                user_steps_repository.save(userSteps);
                return ResponseEntity.status(HttpStatus.CREATED).body(userSteps.getSteps());

            } else {
                long pasosAntiguos = user_steps.getSteps() + steps;
                user_steps.setSteps(pasosAntiguos);
                user_steps_repository.save(user_steps);
                return ResponseEntity.status(HttpStatus.OK).body(user_steps.getSteps());

            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
