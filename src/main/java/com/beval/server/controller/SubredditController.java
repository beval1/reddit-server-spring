package com.beval.server.controller;

import com.beval.server.model.entity.Subreddit;
import com.beval.server.service.SubredditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/subreddits")
public class SubredditController {
    private final SubredditService subredditService;

    @Autowired
    public SubredditController(SubredditService subredditService) {
        this.subredditService = subredditService;
    }

    @GetMapping("/get-all")
    public List<Subreddit> getAllSubreddits(){
        return subredditService.getAllSubreddits();
    }
}
