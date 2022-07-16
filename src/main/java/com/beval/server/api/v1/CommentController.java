package com.beval.server.api.v1;

import com.beval.server.dto.payload.CreateCommentDTO;
import com.beval.server.dto.payload.CreateSubredditDTO;
import com.beval.server.dto.response.CommentDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.model.entity.CommentEntity;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.beval.server.config.AppConstants.API_BASE;

@RestController
@RequestMapping(value = API_BASE)
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(value = "/comments/{postId}")
    public ResponseEntity<ResponseDTO> getAllCommentsForPost(@PathVariable String postId) {
        List<CommentDTO> comments = commentService.getAllCommentsForPostAndParentComment(postId, null);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .content(comments)
                                .build()
                );
    }

    @GetMapping(value = "/comments/{postId}/comment/{commentId}")
    public ResponseEntity<ResponseDTO> getAllRepliesForComment(@PathVariable String postId, @PathVariable String commentId) {
        List<CommentDTO> comments = commentService.getAllCommentsForPostAndParentComment(postId, commentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .content(comments)
                                .build()
                );
    }

    @PostMapping(value = "/comments/{postId}")
    public ResponseEntity<ResponseDTO> createComment(
            @RequestBody CreateCommentDTO createCommentDTO,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable String postId) {
        commentService.createComment(postId, createCommentDTO, userPrincipal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Successfully created comment!")
                                .build()
                );
    }

    @PostMapping(value = "/comments/{postId}/comment/{commentId}")
    public ResponseEntity<ResponseDTO> createReply(
            @PathVariable String postId,
            @PathVariable String commentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreateCommentDTO createCommentDTO) {
        commentService.createReply(postId, commentId, createCommentDTO, userPrincipal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Reply created successfully!")
                                .build()
                );
    }
}
