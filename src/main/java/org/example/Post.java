package org.example;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Post {
    private String content;
    private List<User> views;
    private String creationTime;

    public Post(String content, String creationTime, List<User> views) {
        this.content = content;
        this.creationTime = creationTime;
        this.views = views;
    }

    public String getContent(){
        return content;
    }

    public int getAmountOfViews() {
        return views.size();
    }

    public long getViewRate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime creationDateTime = LocalDateTime.parse(creationTime, formatter);

        Duration duration = Duration.between(creationDateTime, currentDateTime);
        long totalSeconds = duration.toMinutes();
        return views.size()/totalSeconds;
    }
}
