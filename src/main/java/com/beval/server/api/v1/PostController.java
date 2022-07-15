package com.beval.server.api.v1;

import com.beval.server.dto.response.PostDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.service.PostService;
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
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/subreddits/{subredditId}/posts")
    public ResponseEntity<ResponseDTO> getAllPostsForSubreddit(
            @PathVariable(value = "subredditId") String subredditId){

        List<PostDTO> posts = postService.getAllPostsForSubreddit(subredditId);

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO
                        .builder()
                        .content(posts)
                        .message(String
                                .format("Successfully retrieved all posts for subreddit with id: %s",
                                subredditId))
                        .build()

        );
    }
}
