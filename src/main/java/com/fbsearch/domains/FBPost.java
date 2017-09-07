package com.fbsearch.domains;

import java.util.ArrayList;
import java.util.List;

public class FBPost {

    private String postId;
    private String postMessage;
    private int totalLike;
    private List<FBComment> commentList = new ArrayList<FBComment>();
    private String updateTime;

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public List<FBComment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<FBComment> commentList) {
        this.commentList = commentList;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    
}
