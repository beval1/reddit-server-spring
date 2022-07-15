package com.beval.server.service.impl;

import com.beval.server.model.entity.CommentEntity;
import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.CommentRepository;
import com.beval.server.service.CommentService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    @Override
    public void addComment(String content, PostEntity post, UserEntity author) {
        CommentEntity commentEntity = commentRepository.save(
                CommentEntity
                        .builder()
                        .author(author)
                        .content(content)
                        .build());

        if (post.getComments() == null) {
            post.setComments(new ArrayList<>());
        }

        post.getComments().add(commentEntity);
    }
}
