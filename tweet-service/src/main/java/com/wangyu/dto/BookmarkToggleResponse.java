package com.wangyu.dto;

public class BookmarkToggleResponse {

    private boolean bookmarked;

    public BookmarkToggleResponse() {
    }

    public BookmarkToggleResponse(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }
}
