package com.beval.server.service;

import com.beval.server.dto.payload.CreatePostDTO;
import com.beval.server.dto.response.PostDTO;
import com.beval.server.security.UserPrincipal;

import java.util.List;

public interface PostService {
    List<PostDTO> getAllPostsForSubreddit(String subredditId);

    void createPostForSubreddit(CreatePostDTO createPostDTO, UserPrincipal principal, String subredditId);
}
