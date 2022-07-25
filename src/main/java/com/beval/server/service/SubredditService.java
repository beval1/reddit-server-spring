package com.beval.server.service;

import com.beval.server.dto.payload.CreateSubredditDTO;
import com.beval.server.dto.response.PageableDTO;
import com.beval.server.dto.response.SubredditDTO;
import com.beval.server.security.UserPrincipal;
import org.springframework.data.domain.Pageable;

public interface SubredditService {
    PageableDTO<SubredditDTO> getAllSubreddits(Pageable pageable);
    void createSubreddit(CreateSubredditDTO createSubredditDTO, UserPrincipal principal);
    void updateSubreddit(Long subredditId, CreateSubredditDTO createSubredditDTO, UserPrincipal principal);
}
