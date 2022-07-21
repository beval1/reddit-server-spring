package com.beval.server.api.v1;

import com.beval.server.dto.payload.CreateCommentDTO;
import com.beval.server.dto.response.CommentDTO;
import com.beval.server.dto.response.PageableDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.CommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.beval.server.config.AppConstants.*;

@RestController
@RequestMapping(value = API_BASE)
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(value = "/comments/{postId}")
    public ResponseEntity<ResponseDTO> getAllCommentsForPost(@PathVariable String postId,
                                                             @AuthenticationPrincipal UserPrincipal principal,
                                                             @PageableDefault(page = PAGEABLE_DEFAULT_PAGE_NUMBER, size = PAGEABLE_DEFAULT_PAGE_SIZE)
                                                             @SortDefault.SortDefaults({
                                                                     @SortDefault(sort = "createdOn",
                                                                             direction = Sort.Direction.DESC),
                                                             }) Pageable pageable) {
        PageableDTO<CommentDTO> comments = commentService.getAllCommentsForPostAndParentComment(
                postId, null, principal, pageable);
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
    public ResponseEntity<ResponseDTO> getAllRepliesForComment(@PathVariable String postId,
                                                               @PathVariable String commentId,
                                                               @AuthenticationPrincipal UserPrincipal principal,
                                                               @PageableDefault(page = PAGEABLE_DEFAULT_PAGE_NUMBER, size = PAGEABLE_DEFAULT_PAGE_SIZE)
                                                               @SortDefault.SortDefaults({
                                                                       @SortDefault(sort = "createdOn", direction = Sort.Direction.DESC),
                                                               }) Pageable pageable) {
        PageableDTO<CommentDTO> comments = commentService.getAllCommentsForPostAndParentComment(postId, commentId,
                principal, pageable);
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

    @PreAuthorize("hasRole('ROLE_ADMIN') OR @securityExpressionUtilityImpl.isResourceOwner(#commentId, principal)")
    @PatchMapping(value = "/comments/comment/{commentId}")
    public ResponseEntity<ResponseDTO> updateCommentOrReply(
            @PathVariable String commentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody CreateCommentDTO createCommentDTO) {

        commentService.updateCommentOrReply(commentId, createCommentDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Updated successfully")
                                .build()
                );
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') OR @securityExpressionUtilityImpl.isResourceOwner(#commentId, principal)")
    @DeleteMapping(value = "/comments/comment/{commentId}")
    public ResponseEntity<ResponseDTO> deleteCommentOrReply(
            @PathVariable String commentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        commentService.deleteCommentOrReply(commentId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Deleted successfully")
                                .build()
                );
    }

    @PostMapping(value = "/comments/comment/{commentId}/upvote")
    public ResponseEntity<ResponseDTO> upvoteComment(
            @PathVariable String commentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        commentService.upvoteComment(commentId, userPrincipal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Upvoted successfully!")
                                .build()
                );
    }

    @PostMapping(value = "/comments/comment/{commentId}/downvote")
    public ResponseEntity<ResponseDTO> downVoteComment(
            @PathVariable String commentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        commentService.downvoteComment(commentId, userPrincipal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Downvoted successfully!")
                                .build()
                );
    }

    @PostMapping(value = "/comments/comment/{commentId}/unvote")
    public ResponseEntity<ResponseDTO> unvoteComment(
            @PathVariable String commentId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        commentService.unvoteComment(commentId, userPrincipal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Unvoted successfully!")
                                .build()
                );
    }
}
