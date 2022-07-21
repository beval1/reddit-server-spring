package com.beval.server.service;

import com.beval.server.dto.payload.CreateCommentDTO;
import com.beval.server.dto.response.CommentDTO;
import com.beval.server.dto.response.PageableDTO;
import com.beval.server.security.UserPrincipal;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    PageableDTO<CommentDTO> getAllCommentsForPostAndParentComment(String postId, String commentId,
                                                                  UserPrincipal principal, Pageable pageable);

    void createComment(String postId, CreateCommentDTO createCommentDTO, UserPrincipal userPrincipal);

    void createReply(String postId, String commentId, CreateCommentDTO createCommentDTO, UserPrincipal userPrincipal);

    void updateCommentOrReply(String commentId, CreateCommentDTO createCommentDTO);
    void deleteCommentOrReply(String commentId);

    void upvoteComment(String commentId, UserPrincipal userPrincipal);
    void downvoteComment(String commentId, UserPrincipal userPrincipal);
    void unvoteComment(String commentId, UserPrincipal userPrincipal);
}
