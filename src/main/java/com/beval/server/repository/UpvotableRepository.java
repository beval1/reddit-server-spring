package com.beval.server.repository;

import com.beval.server.model.entity.UpvotableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpvotableRepository extends JpaRepository<UpvotableEntity, Long> {
}
