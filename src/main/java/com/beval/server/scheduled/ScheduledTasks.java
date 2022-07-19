package com.beval.server.scheduled;

import com.beval.server.model.entity.PostEntity;
import com.beval.server.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static com.beval.server.config.AppConstants.POST_MONTHS_TO_ARCHIVE;


@Component
@Slf4j
public class ScheduledTasks {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private final PostRepository postRepository;

    public ScheduledTasks(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Scheduled(cron = "@daily")
    public void archiveOldPosts() {
        log.info("Starting the job of archiving posts...");
        log.info("The time now: {}", dateFormat.format(new Date()));
        List<PostEntity> postEntities = postRepository.findAll();
        int archivedPostsCounter = 0;
        for (int i = 0; i < postEntities.size(); i++) {
            PostEntity post = postEntities.get(i);
            long monthsPassed = ChronoUnit.MONTHS.between(post.getCreatedOn(), LocalDateTime.now());
            if (monthsPassed >= POST_MONTHS_TO_ARCHIVE && !post.isArchived()){
                post.setArchived(true);
                archivedPostsCounter++;
            }
        }
        log.info("Total archived posts: {}", archivedPostsCounter);
    }

}
