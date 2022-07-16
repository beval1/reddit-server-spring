package com.beval.server.service;

import com.beval.server.dto.payload.CreateCommentDTO;
import com.beval.server.dto.response.CommentDTO;
import com.beval.server.security.UserPrincipal;

import java.util.List;

public interface CommentService {
    List<CommentDTO> getAllCommentsForPostAndParentComment(String postId, String commentId);

    void createComment(String postId, CreateCommentDTO createCommentDTO, UserPrincipal userPrincipal);

    void createReply(String postId, String commentId, CreateCommentDTO createCommentDTO, UserPrincipal userPrincipal);

    void updateCommentOrReply(String commentId, CreateCommentDTO createCommentDTO);
    void deleteCommentOrReply(String commentId);
}
