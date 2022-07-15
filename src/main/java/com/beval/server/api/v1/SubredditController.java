package com.beval.server.api.v1;

import com.beval.server.dto.response.ResponseDTO;
import com.beval.server.dto.response.SubredditDTO;
import com.beval.server.service.SubredditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.beval.server.config.AppConstants.API_BASE;

@RestController
@RequestMapping(value=API_BASE)
public class SubredditController {
    private final SubredditService subredditService;

    public SubredditController(SubredditService subredditService) {
        this.subredditService = subredditService;
    }

    @GetMapping("/subreddits")
    public ResponseEntity<ResponseDTO> getAllSubreddits(){
        List<SubredditDTO> subredditDTOS = subredditService.getAllSubreddits();
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseDTO
                        .builder()
                        .content(subredditDTOS)
                        .build()
        );
    }
}
