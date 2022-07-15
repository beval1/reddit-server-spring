package com.beval.server.service.impl;

import com.beval.server.dto.response.CommentDTO;
import com.beval.server.dto.response.SubredditDTO;
import com.beval.server.model.entity.CommentEntity;
import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.CommentRepository;
import com.beval.server.repository.PostRepository;
import com.beval.server.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository,
                              ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    @Override
    public void addComment(String content, PostEntity post, UserEntity author) {
        commentRepository.save(
                CommentEntity
                        .builder()
                        .author(author)
                        .post(post)
                        .content(content)
                        .build()
        );
    }

    @Override
    public List<CommentDTO> getAllCommentsForPost(String postId) {
        PostEntity post = postRepository.findById(Long.parseLong(postId)).orElseThrow(RuntimeException::new);
        List<CommentEntity> commentEntities =  commentRepository.findAllCommentsByPostAndParentCommentIsNull(post);
        return Arrays.asList(modelMapper.map(commentEntities, CommentDTO[].class));
    }


}
