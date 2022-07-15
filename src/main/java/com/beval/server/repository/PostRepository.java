package com.beval.server.repository;

import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.SubredditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    List<PostEntity> findAllBySubredditOrderByCreatedOn(SubredditEntity subredditId);
}
