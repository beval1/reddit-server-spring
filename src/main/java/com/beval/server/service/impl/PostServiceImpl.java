package com.beval.server.service.impl;

import com.beval.server.dto.response.PostDTO;
import com.beval.server.exception.ResourceNotFoundException;
import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.SubredditEntity;
import com.beval.server.repository.PostRepository;
import com.beval.server.repository.SubredditRepository;
import com.beval.server.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, SubredditRepository subredditRepository, ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.subredditRepository = subredditRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PostDTO> getAllPostsForSubreddit(String subredditId) {
        SubredditEntity subredditEntity = subredditRepository.findById(Long.parseLong(subredditId))
                .orElseThrow(ResourceNotFoundException::new);

        List<PostEntity> postEntities = postRepository.findAllBySubredditOrderByCreatedOn(subredditEntity);
        return Arrays.asList(modelMapper.map(postEntities, PostDTO[].class));
    }
}
