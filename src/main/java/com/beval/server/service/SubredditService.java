package com.beval.server.service;

import com.beval.server.dto.payload.CreateSubredditDTO;
import com.beval.server.dto.response.SubredditDTO;
import com.beval.server.security.UserPrincipal;

import java.util.List;

public interface SubredditService {
    List<SubredditDTO> getAllSubreddits();
    void createSubreddit(CreateSubredditDTO createSubredditDTO, UserPrincipal principal);
}
