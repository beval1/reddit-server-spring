package com.beval.reddit_server_spring.service;

import com.beval.reddit_server_spring.model.entity.Subreddit;

import java.util.List;

public interface SubredditService {
    List<Subreddit> getAllSubreddits();
}
