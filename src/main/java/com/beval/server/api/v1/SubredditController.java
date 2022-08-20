package com.beval.server.api.v1;

import com.beval.server.dto.payload.CreateSubredditDTO;
import com.beval.server.dto.response.PageableDTO;
import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.dto.response.SubredditDTO;
import com.beval.server.security.UserPrincipal;
import com.beval.server.service.SubredditService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.beval.server.config.AppConstants.*;

@RestController
@RequestMapping(value = API_BASE)
public class SubredditController {
    private final SubredditService subredditService;

    public SubredditController(SubredditService subredditService) {
        this.subredditService = subredditService;
    }

    @GetMapping("/subreddits")
    public ResponseEntity<ResponseDTO> getAllSubreddits(
            @PageableDefault(page = PAGEABLE_DEFAULT_PAGE_NUMBER, size = PAGEABLE_DEFAULT_PAGE_SIZE)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "createdOn", direction = Sort.Direction.DESC),
            }) Pageable pageable
    ) {
        PageableDTO<SubredditDTO> subredditDTOS = subredditService.getAllSubreddits(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .content(subredditDTOS)
                                .build()
                );
    }

    @GetMapping("/subreddits/subreddit/{subredditId}")
    public ResponseEntity<ResponseDTO> getSpecificSubreddit(@PathVariable("subredditId") Long subredditId) {
        SubredditDTO subredditDTO = subredditService.getSpecificSubreddit(subredditId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .content(subredditDTO)
                                .build()
                );
    }

    @PostMapping("/subreddits")
    public ResponseEntity<ResponseDTO> createSubreddit(@Valid @RequestBody CreateSubredditDTO createSubredditDTO, @AuthenticationPrincipal UserPrincipal principal) {
        subredditService.createSubreddit(createSubredditDTO, principal);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Subreddit created successfully!")
                                .build()
                );
    }

    @PostMapping("/subreddits/join/{subredditId}")
    public ResponseEntity<ResponseDTO> joinSubreddit(@AuthenticationPrincipal UserPrincipal principal,
                                                     @PathVariable Long subredditId) {
        subredditService.joinSubreddit(principal, subredditId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Subreddit joined successfully!")
                                .build()
                );
    }

    @PatchMapping("/subreddits/subreddit/{subredditId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') OR @securityExpressionUtilityImpl.isSubredditModerator(#subredditId, principal)")
    public ResponseEntity<ResponseDTO> updateSubreddit(
            @PathVariable Long subredditId,
            @Valid @RequestBody CreateSubredditDTO createSubredditDTO,
            @AuthenticationPrincipal UserPrincipal principal
    ) {

        subredditService.updateSubreddit(subredditId, createSubredditDTO, principal);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        ResponseDTO
                                .builder()
                                .message("Subreddit created successfully")
                                .build()
                );
    }

}
