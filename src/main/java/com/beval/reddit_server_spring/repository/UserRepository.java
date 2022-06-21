package com.beval.reddit_server_spring.repository;

import com.beval.reddit_server_spring.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource()
public interface UserRepository extends JpaRepository<User, Long> {
}
