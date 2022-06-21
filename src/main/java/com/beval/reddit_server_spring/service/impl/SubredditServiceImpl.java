package com.beval.reddit_server_spring.service.impl;

import com.beval.reddit_server_spring.model.entity.Subreddit;
import com.beval.reddit_server_spring.repository.SubredditRepository;
import com.beval.reddit_server_spring.service.SubredditService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubredditServiceImpl implements SubredditService {
    private final SubredditRepository subredditRepository;

    public SubredditServiceImpl(SubredditRepository subredditRepository) {
        this.subredditRepository = subredditRepository;
    }

    @Override
    public List<Subreddit> getAllSubreddits() {
        return subredditRepository.findAll();
    }
}
