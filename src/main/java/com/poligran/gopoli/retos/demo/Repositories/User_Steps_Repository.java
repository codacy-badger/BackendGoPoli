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

package com.poligran.gopoli.retos.demo.Repositories;

import com.poligran.gopoli.retos.demo.Entities.User_Steps;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface User_Steps_Repository extends JpaRepository<User_Steps, Integer> {

    public List<User_Steps> findAllByOrderByStepsDesc();


}
