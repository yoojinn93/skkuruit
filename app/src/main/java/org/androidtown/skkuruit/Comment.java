package org.androidtown.skkuruit;

/**
 * Created by genie on 2017. 6. 10..
 */
public class Comment {
    private int targetEvent;
    private String cmtUser;
    private String cmtContent;
    private String cmtDate;


    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Comment(int targetEvent, String cmtUser, String cmtContent, String cmtDate) {
        this.targetEvent = targetEvent;
        this.cmtUser = cmtUser;
        this.cmtContent = cmtContent;
        this.cmtDate = cmtDate;
    }

    public void setTargetEvent(int targetEvent) {
        this.targetEvent = targetEvent;
    }
    public int getTargetEvent() {
        return targetEvent;
    }

    public void setCmtUser(String cmtUser) {
        this.cmtUser = cmtUser;
    }
    public String getCmtUser() {
        return cmtUser;
    }

    public void setCmtContent(String cmtContent) {
        this.cmtContent = cmtContent;
    }
    public String getCmtContent() {
        return cmtContent;
    }

    public void setCmtDate(String cmtDate) {
        this.cmtDate = cmtDate;
    }
    public String getCmtDate() {
        return cmtDate;
    }

}
