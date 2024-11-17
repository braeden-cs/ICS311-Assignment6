package org.example;

import java.sql.Date;
import java.sql.Time;

public class Comment {
    private User creator;
    private String content;
    private Time creationTime;
    private Date date;
    private Post parentPost;

    public Comment(User creator, String content, Time creationTime, Date date) {
        this.creator = creator;
        this.content = content;
        this.creationTime = creationTime;
        this.date = date;
    }

    public String getContent(){
        return content;
    }

    public Time getCreationTime() {
        return creationTime;
    }

    public Date getDate() {
        return date;
    }

    public User getCreator() {
        return creator;
    }

    public Post getParentPost() {
        return parentPost;
    }

    public void setParentPost(Post post) {
        this.parentPost = post;
    }
}
