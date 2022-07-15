package com.beval.server.service.impl;

import com.beval.server.dto.response.SubredditDTO;
import com.beval.server.model.entity.SubredditEntity;
import com.beval.server.repository.SubredditRepository;
import com.beval.server.service.SubredditService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SubredditServiceImpl implements SubredditService {
    private final SubredditRepository subredditRepository;
    private final ModelMapper modelMapper;

    public SubredditServiceImpl(SubredditRepository subredditRepository, ModelMapper modelMapper) {
        this.subredditRepository = subredditRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SubredditDTO> getAllSubreddits() {
        List<SubredditEntity> subredditEntities = subredditRepository.findAll();
        return Arrays.asList(modelMapper.map(subredditEntities, SubredditDTO[].class));
    }
}
