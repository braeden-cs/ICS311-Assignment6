package org.example;

import java.util.List;

public class User {
    private String username;
    private List<String> connections;
    private List<Post> authoredPosts;
    private List<Post> readPosts;
    private List<Comment> comments;

    public User (String username, List<String> connections, List<Post> authoredPosts, List<Post> readPosts, List<Comment> comments) {
        this.username = username;
        this.connections = connections;
        this.authoredPosts = authoredPosts;
        this.readPosts = readPosts;
        this.comments = comments;
    }

    // Add getters
    public String getUsername() {
        return username;
    }

    public List<Post> getAuthoredPosts() {
        return authoredPosts;
    }

    public List<String> getConnections() {
        return connections;
    }

    public List<Post> getReadPosts() {
        return readPosts;
    }

    public List<Comment> getComments() {
        return comments;
    }
}
