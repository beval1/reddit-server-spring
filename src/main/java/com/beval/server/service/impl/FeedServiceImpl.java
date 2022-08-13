package com.beval.server.service.impl;

import com.beval.server.dto.response.AbstractUpvotableDTO;
import com.beval.server.dto.response.CommentDTO;
import com.beval.server.dto.response.PostDTO;
import com.beval.server.exception.NotAuthorizedException;
import com.beval.server.model.entity.CommentEntity;
import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.SubredditEntity;
import com.beval.server.model.entity.UserEntity;
import com.beval.server.repository.CommentRepository;
import com.beval.server.repository.PostRepository;
import com.beval.server.repository.SubredditRepository;
import com.beval.server.repository.UserRepository;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.FeedService;
import com.beval.server.utils.VotingUtility;
import io.micrometer.core.annotation.Timed;
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
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final VotingUtility votingUtility;

    public FeedServiceImpl(UserRepository userRepository, SubredditRepository subredditRepository,
                           PostRepository postRepository, CommentRepository commentRepository,
                           ModelMapper modelMapper, VotingUtility votingUtility) {
        this.userRepository = userRepository;
        this.subredditRepository = subredditRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.modelMapper = modelMapper;
        this.votingUtility = votingUtility;
    }

    @SneakyThrows
    @Transactional
    @Timed(value = "get-feed.time", description = "Time taken to return feed for the user")
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
            PostEntity postEntity = totalFetchedPosts.get(i);
            PostDTO postDTO = postDTOS.get(i);
            votingUtility.setUpvotedAndDownvotedForUser(postEntity, postDTO, userEntity);
            votingUtility.setVotes(postEntity, postDTO);
            CommentEntity commentEntity = commentRepository.findFirstByPostOrderByCreatedOnDesc(postEntity);
            CommentDTO commentDTO = modelMapper.map(commentEntity, CommentDTO.class);
            postDTO.setAuthor(commentDTO.getAuthor());
            postDTO.setContent(commentDTO.getContent());
            postDTO.setCommentsCount(commentRepository.countAllByPost(postEntity));
        }
        //finally sort all DTOs by... upvotes?
        postDTOS = postDTOS.stream()
                    .sorted(Comparator.comparingInt(AbstractUpvotableDTO::getVotes)).toList();

        return postDTOS;
    }
}
