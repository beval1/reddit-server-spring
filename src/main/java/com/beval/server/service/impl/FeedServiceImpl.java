package com.beval.server.service.impl;

import com.beval.server.dto.response.AbstractUpvotableDTO;
import com.beval.server.dto.response.PostDTO;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.SubredditEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.PostRepository;
import com.beval.server.repository.SubredditRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.FeedService;
import com.beval.server.utils.VotingUtility;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class FeedServiceImpl implements FeedService {

    private final UserRepository userRepository;
    private final SubredditRepository subredditRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final VotingUtility votingUtility;

    public FeedServiceImpl(UserRepository userRepository, SubredditRepository subredditRepository,
                           PostRepository postRepository, ModelMapper modelMapper, VotingUtility votingUtility) {
        this.userRepository = userRepository;
        this.subredditRepository = subredditRepository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.votingUtility = votingUtility;
    }

    @SneakyThrows
    @Transactional
    @Override
    public List<PostDTO> getFeed(UserPrincipal userPrincipal, Pageable pageable) {
        Set<SubredditEntity> subreddits = null;
        UserEntity userEntity = null;
        if (userPrincipal != null){
            userEntity = userRepository.findByUsernameOrEmail(userPrincipal.getUsername(),
                    userPrincipal.getUsername()).orElseThrow(NotAuthorizedException::new);
            subreddits = userEntity.getSubreddits();
            //TODO: maybe add some new subreddits here, in which the user hasn't joined?
        } else {
            //randomly pick subreddits... with the most members???
            subreddits = subredditRepository.findRandomSubreddits(10);
        }

        //Now get subreddit posts based on chronology - sort by createdOn
        //fetch random number of those posts for each subreddit
        //Sort sort = Sort.by("createdOn").descending();
        //Random rnd = SecureRandom.getInstanceStrong();
        List<PostEntity> totalFetchedPosts = new ArrayList<>();
        for (SubredditEntity sub: subreddits) {
            //int rndPageSize = rnd.nextInt(5, 10);
            //Pageable pageable = PageRequest.of(1, 10, sort);
            Page<PostEntity> posts = postRepository.findAllBySubreddit(sub, pageable);
            totalFetchedPosts.addAll(posts.getContent());
        }

        //map page to PostDTO
        List<PostDTO> postDTOS = Arrays.asList(modelMapper.map(totalFetchedPosts, PostDTO[].class));
        for (int i = 0; i < postDTOS.size(); i++) {
            votingUtility.setUpvotedAndDownvotedForUser(totalFetchedPosts.get(i), postDTOS.get(i), userEntity);
            votingUtility.setVotes(totalFetchedPosts.get(i), postDTOS.get(i));
        }
        //finally sort all DTOs by... upvotes?
        postDTOS = postDTOS.stream()
                    .sorted(Comparator.comparingInt(AbstractUpvotableDTO::getVotes)).toList();

        return postDTOS;
    }
}