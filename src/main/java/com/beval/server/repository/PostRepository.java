package com.beval.server.repository;

import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.SubredditEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {
    Page<PostEntity> findAllBySubredditOrderByCreatedOn(SubredditEntity subredditId, Pageable pageable);
}
