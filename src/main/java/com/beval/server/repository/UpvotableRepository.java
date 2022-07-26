package com.beval.server.repository;

import com.beval.server.model.entity.UpvotableEntity;
import com.beval.server.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpvotableRepository extends JpaRepository<UpvotableEntity, Long> {
    List<UpvotableEntity> findAllByAuthor(UserEntity userEntity);
}
