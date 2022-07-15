package com.beval.server.service;

import com.beval.server.dto.response.PostDTO;

import java.util.List;

public interface PostService {
    List<PostDTO> getAllPostsForSubreddit(String subredditId);
}
