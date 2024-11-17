package org.example;

import java.sql.Date;
import java.sql.Time;

public class Post {
    private String content;
    private Time creationTime;
    private Date date;

    public Post(String content, Time creationTime, Date date) {
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
}
