package com.beval.server.api.v1;

import com.beval.server.model.entity.Subreddit;
import com.beval.server.service.SubredditService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/api/v1/subreddits")
public class SubredditController {
    private final SubredditService subredditService;

    public SubredditController(SubredditService subredditService) {
        this.subredditService = subredditService;
    }

    @GetMapping("/get-all")
    public List<Subreddit> getAllSubreddits(){
        return subredditService.getAllSubreddits();
    }
}
