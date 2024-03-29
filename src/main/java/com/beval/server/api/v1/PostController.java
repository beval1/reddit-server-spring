package com.beval.server.api.v1;

import com.beval.server.dto.payload.CreatePostDTO;
import com.beval.server.dto.payload.ImageUploadPayloadDTO;
import com.beval.server.dto.response.PageableDTO;
import com.beval.server.dto.response.PostDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.PostService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static com.beval.server.config.AppConstants.*;

@RestController
@RequestMapping(value = API_BASE)
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/posts/{subredditId}")
    public ResponseEntity<ResponseDTO> getAllPostsForSubreddit(
            @PathVariable(value = "subredditId") Long subredditId,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(page = PAGEABLE_DEFAULT_PAGE_NUMBER, size = PAGEABLE_DEFAULT_PAGE_SIZE)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "createdOn", direction = Sort.Direction.DESC),
            }) Pageable pageable) {

        PageableDTO<PostDTO> posts = postService.getAllPostsForSubreddit(subredditId, userPrincipal, pageable);

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

    @GetMapping(value = "/posts/post/{postId}")
    public ResponseEntity<ResponseDTO> getSpecificPost(
            @PathVariable(value = "postId") Long postId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        PostDTO post = postService.getSpecificPost(postId, userPrincipal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .content(post)
                                .message(String
                                        .format("Successfully retrieved post with id: %s",
                                                postId))
                                .build()

                );
    }

    @PostMapping(value = "/posts/text-post/{subredditId}")
    public ResponseEntity<ResponseDTO> createTextPost(
            @Valid @RequestBody CreatePostDTO createPostDTO,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable(value = "subredditId") Long subredditId) {

        postService.createPostForSubreddit(createPostDTO, userPrincipal, subredditId, "text");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Successfully created post")
                                .build()

                );
    }

    @PostMapping(value = "/posts/image-post/{subredditId}")
    public ResponseEntity<ResponseDTO> createImagePost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable(value = "subredditId") Long subredditId,
            @RequestParam String postTitle,
            @RequestPart MultipartFile file) {

        CreatePostDTO createPostDTO = CreatePostDTO.builder()
                .image(ImageUploadPayloadDTO.builder().multipartFile(file).build())
                .title(postTitle)
                .build();
        postService.createPostForSubreddit(createPostDTO, userPrincipal, subredditId, "image");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Successfully created post")
                                .build()

                );
    }

    @PostMapping(value = "/posts/link-post/{subredditId}")
    public ResponseEntity<ResponseDTO> createLinkPost(
            @Valid @RequestBody CreatePostDTO createPostDTO,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable(value = "subredditId") Long subredditId) {

        postService.createPostForSubreddit(createPostDTO, userPrincipal, subredditId, "link");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Successfully created post")
                                .build()

                );
    }

    @DeleteMapping(value = "/posts/post/{postId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR @securityExpressionUtilityImpl.isResourceOwner(#postId, principal)" +
            "OR @securityExpressionUtilityImpl.isSubredditModeratorOfPost(#postId, principal)")
    public ResponseEntity<ResponseDTO> deletePost(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable(value = "postId") Long postId
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
    public ResponseEntity<ResponseDTO> upvotePost(
            @PathVariable Long postId,
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
    public ResponseEntity<ResponseDTO> downvotePost(
            @PathVariable Long postId,
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
    public ResponseEntity<ResponseDTO> unvotePost(
            @PathVariable Long postId,
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
