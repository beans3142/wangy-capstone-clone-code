package com.wangyu.dto;

public class RetweetToggleResponse {

    private boolean retweeted;
    private long retweetCount;

    public RetweetToggleResponse() {
    }

    public RetweetToggleResponse(boolean retweeted, long retweetCount) {
        this.retweeted = retweeted;
        this.retweetCount = retweetCount;
    }

    public boolean isRetweeted() {
        return retweeted;
    }

    public void setRetweeted(boolean retweeted) {
        this.retweeted = retweeted;
    }

    public long getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(long retweetCount) {
        this.retweetCount = retweetCount;
    }
}
