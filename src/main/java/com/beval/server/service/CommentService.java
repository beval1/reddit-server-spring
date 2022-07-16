package com.beval.server.service;

import com.beval.server.dto.payload.CreateCommentDTO;
import com.beval.server.dto.response.CommentDTO;
import com.beval.server.model.entity.CommentEntity;
import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.security.UserPrincipal;

import java.util.List;

public interface CommentService {
    CommentEntity addComment(String content, PostEntity post, UserEntity author);
    CommentEntity addReply(String content, PostEntity post, UserEntity author, CommentEntity parent);
    List<CommentDTO> getAllCommentsForPostAndParentComment(String postId, String commentId);

    void createComment(String postId, CreateCommentDTO createCommentDTO, UserPrincipal userPrincipal);

    void createReply(String postId, String commentId, CreateCommentDTO createCommentDTO, UserPrincipal userPrincipal);
}
