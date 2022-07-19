package com.beval.server.repository;

import com.beval.server.model.entity.CommentEntity;
import com.beval.server.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllCommentsByPostAndParentComment(PostEntity post, CommentEntity commentEntity);
    boolean existsByParentComment(CommentEntity commentEntity);
    int countAllByPostAndParentComment(PostEntity post, CommentEntity commentEntity);
    void deleteAllByPostId(Long postId);

    List<CommentEntity> findAllByPost(PostEntity postEntity);
}
