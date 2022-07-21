package com.beval.server.service;

import com.beval.server.dto.payload.CreatePostDTO;
import com.beval.server.dto.response.PageableDTO;
import com.beval.server.dto.response.PostDTO;
import com.beval.server.security.UserPrincipal;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PageableDTO<PostDTO> getAllPostsForSubreddit(String subredditId, UserPrincipal userPrincipal, Pageable pageable);

    void createPostForSubreddit(CreatePostDTO createPostDTO, UserPrincipal principal, String subredditId);
    void deletePost(String postId);

    void upvotePost(String postId, UserPrincipal userPrincipal);

    void downvotePost(String postId, UserPrincipal userPrincipal);

    void unvotePost(String postId, UserPrincipal userPrincipal);
}
