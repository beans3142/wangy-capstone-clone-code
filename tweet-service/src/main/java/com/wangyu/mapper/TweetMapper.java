package com.wangyu.mapper;

import java.util.List;

import com.wangyu.BasicMapper;
import com.wangyu.dto.TweetResponse;
import com.wangyu.entity.Tweet;
import com.wangyu.repository.TweetProjection;
import org.springframework.stereotype.Component;

@Component
public class TweetMapper implements BasicMapper {

    public TweetResponse toResponse(Tweet tweet) {
        return convertToDto(tweet, TweetResponse.class);
    }

    public TweetResponse toResponse(TweetProjection projection) {
        return convertToDto(projection, TweetResponse.class);
    }

    public List<TweetResponse> toResponseList(List<TweetProjection> projections) {
        return convertToDtoList(projections, TweetResponse.class);
    }
}
