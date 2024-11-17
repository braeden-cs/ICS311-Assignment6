package org.example;

import java.util.List;
import java.util.ArrayList;

public class User {
    private String username;
    private int age;
    private String gender;
    private String region;
    private List<String> connections;
    private List<Post> authoredPosts;
    private List<Post> readPosts;
    private List<Comment> comments;

    public User (String username, int age, String gender, String region, List<String> connections, List<Post> authoredPosts, List<Post> readPosts, List<Comment> comments) {
        this.username = username;
        this.age = age;
        this.gender = gender;
        this.region = region;
        this.connections = connections != null ? connections : new ArrayList<>();
        this.authoredPosts = authoredPosts != null ? new ArrayList<>(authoredPosts) : new ArrayList<>();
        this.readPosts = readPosts != null ? new ArrayList<>(readPosts) : new ArrayList<>();
        this.comments = comments != null ? new ArrayList<>(comments) : new ArrayList<>();
    }

    // Add getters
    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getRegion() {
        return region;
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
