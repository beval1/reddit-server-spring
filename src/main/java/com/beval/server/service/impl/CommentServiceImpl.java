package com.beval.server.service.impl;

import com.beval.server.dto.response.CommentDTO;
import com.beval.server.exception.ResourceNotFoundException;
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
    public CommentEntity addComment(String content, PostEntity post, UserEntity author) {
        return commentRepository.save(
                CommentEntity
                        .builder()
                        .author(author)
                        .post(post)
                        .content(content)
                        .build()
        );
    }

    @Transactional
    @Override
    public CommentEntity addReply(String content, PostEntity post, UserEntity author, CommentEntity parent) {
        return commentRepository.save(
                CommentEntity
                        .builder()
                        .author(author)
                        .post(post)
                        .content(content)
                        .parentComment(parent)
                        .build()
        );
    }

    @Override
    public List<CommentDTO> getAllCommentsForPostAndParentComment(String postId, String commentId) {
        PostEntity post = postRepository.findById(Long.parseLong(postId))
                .orElseThrow(ResourceNotFoundException::new);

        CommentEntity commentEntity = null;
        if (commentId != null) {
            commentEntity = commentRepository.findById(Long.parseLong(commentId))
                    .orElseThrow(ResourceNotFoundException::new);
        }
        List<CommentEntity> commentEntities = commentRepository
                .findAllCommentsByPostAndParentComment(post, commentEntity);


        List<CommentDTO> commentDTOS = Arrays.asList(modelMapper.map(commentEntities, CommentDTO[].class));
        for (int i = 0; i < commentDTOS.size(); i++) {
            List<CommentEntity> repliesEntities = commentRepository
                    .findAllCommentsByPostAndParentComment(post, commentEntities.get(i));
            List<CommentDTO> repliesDTOs = Arrays.asList(modelMapper.map(repliesEntities, CommentDTO[].class));
            commentDTOS.get(i).setReplies(repliesDTOs);
            commentDTOS.get(i).setRepliesCount(repliesDTOs.size());
            for (int k = 0; k < repliesDTOs.size(); k++) {
                int repliesCount = commentRepository
                        .countAllByPostAndParentComment(post, repliesEntities.get(k));
                repliesDTOs.get(k).setRepliesCount(repliesCount);
            }
        }
        return commentDTOS;
    }


}
