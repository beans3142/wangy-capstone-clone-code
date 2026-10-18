package com.wangyu.dto;

public class UserProfileResponse {

    private Long id;
    private String nickname;
    private String bio;
    private String profileImageUrl;

    public UserProfileResponse() {
    }

    public UserProfileResponse(Long id, String nickname, String bio, String profileImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.bio = bio;
        this.profileImageUrl = profileImageUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
