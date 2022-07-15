package com.beval.server.service;

import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.UserEntity;

public interface CommentService {
    void addComment(String content, PostEntity post, UserEntity author);
}
