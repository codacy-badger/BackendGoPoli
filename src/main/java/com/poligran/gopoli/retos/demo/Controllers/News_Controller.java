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


import com.poligran.gopoli.retos.demo.Entities.News_Feed;
import com.poligran.gopoli.retos.demo.Repositories.News_Feed_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class News_Controller {

    @Autowired
    private  News_Feed_Repository news_feed_repository;


    @GetMapping ("/feed")
    public ResponseEntity<?> getFeed () {
        List<News_Feed> feed = news_feed_repository.findAll();
        return ResponseEntity.ok(feed);
    }

    @PostMapping ("/addnews")
    public ResponseEntity<?> addFeed (@RequestParam String description,
                                      @RequestParam String picture_url,
                                      @RequestParam String title) {
        return ResponseEntity.ok(news_feed_repository.save(new News_Feed(title, description, picture_url)));
    }
}
