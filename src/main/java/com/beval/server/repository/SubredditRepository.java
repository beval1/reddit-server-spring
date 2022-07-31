package com.beval.server.repository;

import com.beval.server.model.entity.SubredditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SubredditRepository extends JpaRepository<SubredditEntity, Long> {
    @Query(value = "SELECT * FROM subreddits ORDER BY random() LIMIT :limit", nativeQuery = true)
    Set<SubredditEntity> findRandomSubreddits(@Param("limit") int limit);
}
