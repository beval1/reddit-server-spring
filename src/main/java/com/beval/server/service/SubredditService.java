package com.beval.server.service;

import com.beval.server.dto.response.SubredditDTO;

import java.util.List;

public interface SubredditService {
    List<SubredditDTO> getAllSubreddits();
}
