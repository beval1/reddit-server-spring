package com.beval.server.utils;

import com.beval.server.dto.response.PostDTO;
import com.beval.server.dto.response.SubredditDTO;
import com.beval.server.model.entity.PostEntity;
import com.beval.server.model.entity.SubredditEntity;
import com.beval.server.model.entity.UserEntity;

public interface EntityMappingUtility {
    PostDTO mapPost(PostEntity postEntity, UserEntity userEntity);
    SubredditDTO mapSubreddit(SubredditEntity subredditEntity);
}
