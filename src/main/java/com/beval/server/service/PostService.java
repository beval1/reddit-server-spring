package com.beval.server.service;

import com.beval.server.dto.payload.CreatePostDTO;
import com.beval.server.dto.response.PageableDTO;
import com.beval.server.dto.response.PostDTO;
import com.beval.server.security.UserPrincipal;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PageableDTO<PostDTO> getAllPostsForSubreddit(Long subredditId, UserPrincipal userPrincipal, Pageable pageable);

    void createPostForSubreddit(CreatePostDTO createPostDTO, UserPrincipal principal, Long subredditId);
    void deletePost(Long postId);

    void upvotePost(Long postId, UserPrincipal userPrincipal);

    void downvotePost(Long postId, UserPrincipal userPrincipal);

    void unvotePost(Long postId, UserPrincipal userPrincipal);
}
