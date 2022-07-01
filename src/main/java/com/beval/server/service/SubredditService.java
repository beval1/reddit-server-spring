package com.beval.server.service;

import com.beval.server.model.entity.Subreddit;

import java.util.List;

public interface SubredditService {
    List<Subreddit> getAllSubreddits();
}
