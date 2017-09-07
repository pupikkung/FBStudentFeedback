package com.fbsearch.domains;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FBGroup {

    private String groupId;
    private String groupName;
    private String groupDesc;
    private Date startDt;
    private Date endDate;
    private List<FBPost> postList = new ArrayList<FBPost>();
    private int numOfMembers;

    public FBGroup(String groupName, String groupDesc) {
        this.groupName = groupName;
        this.groupDesc = groupDesc;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
    }

    public Date getStartDt() {
        return startDt;
    }

    public void setStartDt(Date startDt) {
        this.startDt = startDt;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<FBPost> getPostList() {
        return postList;
    }

    public void setPostList(List<FBPost> postList) {
        this.postList = postList;
    }

    public int getNumOfMembers() {
        return numOfMembers;
    }

    public void setNumOfMembers(int numOfMembers) {
        this.numOfMembers = numOfMembers;
    }
    
}
