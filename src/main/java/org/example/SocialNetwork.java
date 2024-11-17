package org.example;

import java.util.ArrayList;
import java.util.List;

public class SocialNetwork {
    private List<User> users;
    private List<Post> allPosts;
    
    public SocialNetwork() {
        this.users = new ArrayList<>();
        this.allPosts = new ArrayList<>();
    }
    
    public void addUser(User user) {
        users.add(user);
        if (user.getAuthoredPosts() != null) {
            allPosts.addAll(user.getAuthoredPosts());
        }
    }
    
    public List<User> getUsers() {
        return users;
    }

    public List<Post> getAllPosts() {
        return allPosts;
    }

    public int getTotalComments() {
        int totalComments = 0;
        for (User user : users) {
            if (user.getComments() != null) {
                totalComments += user.getComments().size();
            }
        }
        return totalComments;
    }

    public int getTotalViews() {
        int totalViews = 0;
        for (Post post : allPosts) {
            totalViews += post.getAmountOfViews();
        }
        return totalViews;
    }

    public Post findPostById(String postId) {
        for (Post post : allPosts) {
            if (String.valueOf(post.hashCode()).equals(postId)) {
                return post;
            }
        }
        return null;
    }
}