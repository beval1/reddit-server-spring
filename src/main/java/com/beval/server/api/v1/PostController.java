package com.beval.server.api.v1;

import com.beval.server.dto.payload.CreatePostDTO;
import com.beval.server.dto.response.PostDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.beval.server.config.AppConstants.API_BASE;

@RestController
@RequestMapping(value = API_BASE)
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/posts/{subredditId}")
    public ResponseEntity<ResponseDTO> getAllPostsForSubreddit(
            @PathVariable(value = "subredditId") String subredditId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        List<PostDTO> posts = postService.getAllPostsForSubreddit(subredditId, userPrincipal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .content(posts)
                                .message(String
                                        .format("Successfully retrieved all posts for subreddit with id: %s",
                                                subredditId))
                                .build()

                );
    }

    @PostMapping(value = "/posts/{subredditId}")
    public ResponseEntity<ResponseDTO> createPost(
            @RequestBody CreatePostDTO createPostDTO,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable(value = "subredditId") String subredditId) {

         postService.createPostForSubreddit(createPostDTO, userPrincipal, subredditId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Successfully created post")
                                .build()

                );
    }

    @DeleteMapping(value = "/posts/post/{postId}")
    public ResponseEntity<ResponseDTO> createPost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable(value = "postId") String postId
    ) {

        postService.deletePost(postId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Successfully deleted post")
                                .build()

                );
    }

    @PostMapping(value = "/posts/post/{postId}/upvote")
    public ResponseEntity<ResponseDTO> upvoteComment(
            @PathVariable String postId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        postService.upvotePost(postId, userPrincipal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Upvoted successfully!")
                                .build()
                );
    }

    @PostMapping(value = "/posts/post/{postId}/downvote")
    public ResponseEntity<ResponseDTO> downVoteComment(
            @PathVariable String postId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        postService.downvotePost(postId, userPrincipal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Downvoted successfully!")
                                .build()
                );
    }

    @PostMapping(value = "/posts/post/{postId}/unvote")
    public ResponseEntity<ResponseDTO> unvoteComment(
            @PathVariable String postId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        postService.unvotePost(postId, userPrincipal);

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
