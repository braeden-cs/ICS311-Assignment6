package org.example;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

public class Post {
    private String content;
    private List<User> views;
    private String creationTime;
    private List<Comment> comments;

    public Post(String content, String creationTime, List<User> views) {
        this.content = content;
        this.creationTime = creationTime;
        this.views = views;
        this.comments = new ArrayList<>();
    }

    public String getContent(){
        return content;
    }

    public int getAmountOfViews() {
        return views.size();
    }

    public String getCreationTime() {
        return creationTime;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public long getViewRate() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime creationDateTime = LocalDateTime.parse(creationTime, formatter);

        Duration duration = Duration.between(creationDateTime, currentDateTime);
        long totalSeconds = duration.toMinutes();
        return views.size()/totalSeconds;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public double getImportanceScore() {
        double viewScore = getAmountOfViews();
        double commentScore = comments.size() * 2; // Comments weighted more than views
        return viewScore + commentScore;
    }

    public List<User> getViews() {
        return views;
    }
}
