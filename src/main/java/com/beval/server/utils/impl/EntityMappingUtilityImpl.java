package com.beval.server.utils.impl;

import com.beval.server.dto.response.CommentDTO;
import com.beval.server.dto.response.PostDTO;
import com.beval.server.model.entity.CommentEntity;
import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.CommentRepository;
import com.beval.server.utils.EntityMappingUtility;
import com.beval.server.utils.VotingUtility;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EntityMappingUtilityImpl implements EntityMappingUtility {
    private final ModelMapper modelMapper;
    private final VotingUtility votingUtility;
    private final CommentRepository commentRepository;

    public EntityMappingUtilityImpl(ModelMapper modelMapper, VotingUtility votingUtility,
                                    CommentRepository commentRepository) {
        this.modelMapper = modelMapper;
        this.votingUtility = votingUtility;
        this.commentRepository = commentRepository;
    }

    @Override
    public PostDTO mapPost(PostEntity postEntity, UserEntity userEntity) {
        PostDTO postDTO = modelMapper.map(postEntity, PostDTO.class);
        votingUtility.setUpvotedAndDownvotedForUser(postEntity, postDTO, userEntity);
        votingUtility.setVotes(postEntity, postDTO);
        CommentEntity commentEntity = commentRepository.findFirstByPostOrderByCreatedOnDesc(postEntity);
        CommentDTO commentDTO = modelMapper.map(commentEntity, CommentDTO.class);
        postDTO.setAuthor(commentDTO.getAuthor());
        postDTO.setContent(commentDTO.getContent());
        postDTO.setCommentsCount(commentRepository.countAllByPost(postEntity));
        return postDTO;
    }
}
