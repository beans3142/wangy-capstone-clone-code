package com.wangyu.dto;

import com.wangyu.entity.Tweet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TweetCreateRequest {

    @NotNull(message = "작성자 ID는 필수입니다")
    private Long authorId;

    @NotBlank(message = "본문은 비어 있을 수 없습니다")
    @Size(max = Tweet.MAX_CONTENT_LENGTH, message = "본문은 " + Tweet.MAX_CONTENT_LENGTH + "자를 초과할 수 없습니다")
    private String content;

    private String imageUrl;

    public TweetCreateRequest() {
    }

    public TweetCreateRequest(Long authorId, String content, String imageUrl) {
        this.authorId = authorId;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
