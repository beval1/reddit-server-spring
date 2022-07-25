package com.beval.server.service;

import com.beval.server.dto.payload.CreateCommentDTO;
import com.beval.server.dto.response.CommentDTO;
import com.beval.server.dto.response.PageableDTO;
import com.beval.server.security.UserPrincipal;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    PageableDTO<CommentDTO> getAllCommentsForPostAndParentComment(Long postId, Long commentId,
                                                                  UserPrincipal principal, Pageable pageable);

    void createComment(Long postId, CreateCommentDTO createCommentDTO, UserPrincipal userPrincipal);

    void createReply(Long commentId, CreateCommentDTO createCommentDTO, UserPrincipal userPrincipal);

    void updateCommentOrReply(Long commentId, CreateCommentDTO createCommentDTO, UserPrincipal userPrincipal);
    void deleteCommentOrReply(Long commentId);

    void upvoteComment(Long commentId, UserPrincipal userPrincipal);
    void downvoteComment(Long commentId, UserPrincipal userPrincipal);
    void unvoteComment(Long commentId, UserPrincipal userPrincipal);
}
