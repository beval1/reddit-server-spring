package com.beval.server.repository;

import com.beval.server.model.entity.SubredditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubredditRepository extends JpaRepository<SubredditEntity, Long> {
}
