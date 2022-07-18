package com.beval.server.repository;

import com.beval.server.model.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseEntityRepository extends JpaRepository<BaseEntity, Long> {
}
