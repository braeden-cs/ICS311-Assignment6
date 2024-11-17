package org.example;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class Post {
    private String content;
    private Time creationTime;
    private Date date;
    private List<User> views;

    public Post(String content, Time creationTime, Date date, List<User> views) {
        this.content = content;
        this.creationTime = creationTime;
        this.date = date;
        this.views = views;
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

    public int getAmountOfViews() {
        return views.size();
    }
}
