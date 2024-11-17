package org.example;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;

public class Post {
    private String content;
    private User author;
    private List<User> views;
    private String creationTime;
    private List<Comment> comments;

    public Post(String content, String creationTime, List<User> views, User author) {
        this.content = content;
        this.creationTime = creationTime;
        this.views = views != null ? views : new ArrayList<>();
        this.comments = new ArrayList<>();
        this.author = author;
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

    public User getAuthor() {
        return author;
    }

    public long getViewRate() {
        String currentTime = "2024-11-17T02:30";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime creationDateTime = LocalDateTime.parse(creationTime, formatter);
        LocalDateTime currentDateTime = LocalDateTime.parse(currentTime, formatter);

        Duration duration = Duration.between(creationDateTime, currentDateTime);
        long totalHours = duration.toHours();
        return (views.size() * 1000)/totalHours;
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

    public static List<Post> filterPosts(List<Post> posts, Predicate<User> userCriteria, Predicate<Post> postCriteria) {
        List<Post> filteredPosts = new ArrayList<>();
        for (Post post : posts) {
            if (userCriteria.test(post.getAuthor()) && postCriteria.test(post)) {
                filteredPosts.add(post);
            }
        }
        return filteredPosts;
    }
}
