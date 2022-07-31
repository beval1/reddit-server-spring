package com.beval.server.service;

import com.beval.server.dto.response.PostDTO;
import com.beval.server.security.UserPrincipal;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedService {
    List<PostDTO> getFeed(UserPrincipal userPrincipal, Pageable pageable);
}
