package com.beval.server.api.v1;

import com.beval.server.dto.response.CommentDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.beval.server.config.AppConstants.API_BASE;

@RestController
@RequestMapping(value = API_BASE)
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(value = "/posts/{postId}/comments")
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

    @GetMapping(value = "/posts/{postId}/comments/{commentId}")
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
}
