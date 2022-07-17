package com.beval.server.service.impl;

import com.beval.server.dto.payload.CreateSubredditDTO;
import com.beval.server.dto.response.SubredditDTO;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.exception.ResourceNotFoundException;
import com.beval.server.model.entity.SubredditEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.SubredditRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.SubredditService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@Service
public class SubredditServiceImpl implements SubredditService {
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public SubredditServiceImpl(SubredditRepository subredditRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.subredditRepository = subredditRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SubredditDTO> getAllSubreddits() {
        List<SubredditEntity> subredditEntities = subredditRepository.findAll();
        return Arrays.asList(modelMapper.map(subredditEntities, SubredditDTO[].class));
    }

    @Override
    public void createSubreddit(CreateSubredditDTO createSubredditDTO, UserPrincipal principal) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(principal.getUsername(), principal.getUsername())
                .orElseThrow(NotAuthorizedException::new);

        SubredditEntity subredditEntity = SubredditEntity
                .builder()
                .title(createSubredditDTO.getTitle())
                .description(createSubredditDTO.getDescription())
                .admins(List.of(userEntity))
                .build();
        subredditRepository.save(subredditEntity);
    }

    @Override
    @Transactional
    public void updateSubreddit(Long subredditId, CreateSubredditDTO createSubredditDTO, UserPrincipal principal) {
        SubredditEntity subredditEntity = subredditRepository.findById(subredditId)
                .orElseThrow(ResourceNotFoundException::new);

        if (createSubredditDTO.getDescription() != null){
            subredditEntity.setTitle(createSubredditDTO.getTitle());
        }
        if (createSubredditDTO.getTitle() != null){
            subredditEntity.setDescription(createSubredditDTO.getDescription());
        }
    }
}
