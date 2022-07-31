package com.beval.server.api.v1;

import com.beval.server.dto.response.PostDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.FeedService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.beval.server.config.AppConstants.*;

@RestController
@RequestMapping(path = API_BASE)
public class FeedController {

    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping(value = "/get-feed")
    public ResponseEntity<ResponseDTO> getFeed(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                               @PageableDefault(page = PAGEABLE_DEFAULT_PAGE_NUMBER, size = PAGEABLE_DEFAULT_PAGE_SIZE)
                                               @SortDefault.SortDefaults({
                                                       @SortDefault(sort = "createdOn", direction = Sort.Direction.DESC),
                                               })Pageable pageable){

        List<PostDTO> postFeed =  feedService.getFeed(userPrincipal, pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .content(postFeed)
                                .message("Feed fetched successfully!")
                                .build()
                );
    }
}
