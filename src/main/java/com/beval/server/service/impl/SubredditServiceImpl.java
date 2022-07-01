package com.beval.server.service.impl;

import com.beval.server.model.entity.Subreddit;
import com.beval.server.repository.SubredditRepository;
import com.beval.server.service.SubredditService;
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
