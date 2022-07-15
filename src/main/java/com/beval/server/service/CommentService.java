package com.beval.server.service;

import com.beval.server.dto.response.CommentDTO;
import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.UserEntity;

import java.util.List;

public interface CommentService {
    void addComment(String content, PostEntity post, UserEntity author);
    List<CommentDTO> getAllCommentsForPost(String postId);
}
