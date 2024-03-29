package com.beval.server.service.impl;

import com.beval.server.dto.payload.CreateSubredditDTO;
import com.beval.server.dto.response.PageableDTO;
import com.beval.server.dto.response.SubredditDTO;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.exception.ResourceNotFoundException;
import com.beval.server.model.entity.SubredditEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.SubredditRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.SubredditService;
import com.beval.server.utils.EntityMappingUtility;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SubredditServiceImpl implements SubredditService {
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EntityMappingUtility entityMappingUtility;

    public SubredditServiceImpl(SubredditRepository subredditRepository, UserRepository userRepository,
                                ModelMapper modelMapper, EntityMappingUtility entityMappingUtility) {
        this.subredditRepository = subredditRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.entityMappingUtility = entityMappingUtility;
    }

    @Override
    public PageableDTO<SubredditDTO> getAllSubreddits(Pageable pageable) {
        Page<SubredditEntity> subredditEntities = subredditRepository.findAll(pageable);
        List<SubredditDTO> subredditDTOS = new ArrayList<>();
        subredditEntities.forEach(s -> subredditDTOS.add(entityMappingUtility.mapSubreddit(s)));
        return PageableDTO
                .<SubredditDTO>builder()
                .pageContent(subredditDTOS)
                .pageNo(subredditEntities.getNumber() + 1)
                .pageSize(subredditEntities.getSize())
                .pageElements(subredditDTOS.size())
                .totalElements(subredditEntities.getTotalElements())
                .totalPages(subredditEntities.getTotalPages())
                .last(subredditEntities.isLast())
                .first(subredditEntities.isFirst())
                .sortedBy(pageable.getSort().toString())
                .build();
    }

    @Override
    public void createSubreddit(CreateSubredditDTO createSubredditDTO, UserPrincipal principal) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(principal.getUsername(), principal.getUsername())
                .orElseThrow(NotAuthorizedException::new);

        SubredditEntity subredditEntity = SubredditEntity
                .builder()
                .name(createSubredditDTO.getTitle())
                .description(createSubredditDTO.getDescription())
                .moderators(Set.of(userEntity))
                .build();
        subredditRepository.save(subredditEntity);
    }

    @Override
    @Transactional
    public void updateSubreddit(Long subredditId, CreateSubredditDTO createSubredditDTO, UserPrincipal principal) {
        SubredditEntity subredditEntity = subredditRepository.findById(subredditId)
                .orElseThrow(ResourceNotFoundException::new);

        if (createSubredditDTO.getDescription() != null) {
            subredditEntity.setName(createSubredditDTO.getTitle());
        }
        if (createSubredditDTO.getTitle() != null) {
            subredditEntity.setDescription(createSubredditDTO.getDescription());
        }
    }

    @Override
    @Transactional
    public void joinSubreddit(UserPrincipal principal, Long subredditId) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(principal.getUsername(), principal.getUsername())
                .orElseThrow(NotAuthorizedException::new);
        SubredditEntity subredditEntity = subredditRepository.findById(subredditId)
                .orElseThrow(ResourceNotFoundException::new);

        userEntity.getSubreddits().add(subredditEntity);
        subredditEntity.getMembers().add(userEntity);
    }

    @Override
    @Transactional
    public SubredditDTO getSpecificSubreddit(Long subredditId) {
        SubredditEntity subredditEntity = subredditRepository.findById(subredditId)
                .orElseThrow(ResourceNotFoundException::new);
        return entityMappingUtility.mapSubreddit(subredditEntity);
    }
}
