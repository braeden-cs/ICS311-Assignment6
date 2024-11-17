package org.example;

import java.util.*;
import java.sql.Time;
import java.sql.Date;

public class TestData {
    public SocialNetwork generateTestNetwork() {
        SocialNetwork network = new SocialNetwork();
        
        // Create lists for all users
        List<String> aliceConnections = new ArrayList<>();
        List<Post> alicePosts = new ArrayList<>();
        List<Post> aliceReadPosts = new ArrayList<>();
        List<Comment> aliceComments = new ArrayList<>();
        
        List<String> bobConnections = new ArrayList<>();
        List<Post> bobPosts = new ArrayList<>();
        List<Post> bobReadPosts = new ArrayList<>();
        List<Comment> bobComments = new ArrayList<>();
        
        List<String> charlieConnections = new ArrayList<>();
        List<Post> charliePosts = new ArrayList<>();
        List<Post> charlieReadPosts = new ArrayList<>();
        List<Comment> charlieComments = new ArrayList<>();

        // Create users
        User alice = new User("Alice", 30, "Female", "Hawaii", aliceConnections, alicePosts, aliceReadPosts, aliceComments);
        User bob = new User("Bob", 28, "Male", "California", bobConnections, bobPosts, bobReadPosts, bobComments);
        User charlie = new User("Charlie", 35, "Male", "New York", charlieConnections, charliePosts, charlieReadPosts, charlieComments);

        // Create posts with empty view lists and better titles
        Post alicePost = new Post(
    "Important announcement about new features!", 
    "2024-11-15T10:00", 
            new ArrayList<>(),
            alice
        );

        Post bobPost = new Post(
            "Discussion: Social Network Analysis", 
            "2024-11-15T11:00", 
            new ArrayList<>(),
            bob
        );

        Post charliePost = new Post(
            "Breaking news: Tech Innovation",
            "2024-11-15T12:00", 
            new ArrayList<>(),
            charlie
        );

        // Add posts to authors
        alicePosts.add(alicePost);
        bobPosts.add(bobPost);
        charliePosts.add(charliePost);

        // Create viewing relationships (who viewed which posts)
        // Bob and Charlie view Alice's post
        alicePost.getViews().add(bob);
        alicePost.getViews().add(charlie);
        bobReadPosts.add(alicePost);
        charlieReadPosts.add(alicePost);

        // Alice and Charlie view Bob's post
        bobPost.getViews().add(alice);
        bobPost.getViews().add(charlie);
        aliceReadPosts.add(bobPost);
        charlieReadPosts.add(bobPost);

        // Alice and Bob view Charlie's post
        charliePost.getViews().add(alice);
        charliePost.getViews().add(bob);
        aliceReadPosts.add(charliePost);
        bobReadPosts.add(charliePost);

        // Add comments with timestamps
        Time currentTime = new Time(System.currentTimeMillis());
        Date currentDate = new Date(System.currentTimeMillis());

        // Comments on Alice's post
        Comment bobCommentOnAlice = new Comment(
            bob, 
            "Great announcement! This will be very useful.", 
            currentTime, 
            currentDate
        );
        Comment charlieCommentOnAlice = new Comment(
            charlie, 
            "Thanks for sharing these new features!", 
            currentTime, 
            currentDate
        );
        bobComments.add(bobCommentOnAlice);
        charlieComments.add(charlieCommentOnAlice);

        // Comments on Bob's post
        Comment aliceCommentOnBob = new Comment(
            alice, 
            "Interesting analysis! Let's discuss more.", 
            currentTime, 
            currentDate
        );
        Comment charlieCommentOnBob = new Comment(
            charlie, 
            "Great insights on network structures.", 
            currentTime, 
            currentDate
        );
        aliceComments.add(aliceCommentOnBob);
        charlieComments.add(charlieCommentOnBob);

        // Comments on Charlie's post
        Comment aliceCommentOnCharlie = new Comment(
            alice, 
            "Exciting news! Can't wait to see more.", 
            currentTime, 
            currentDate
        );
        Comment bobCommentOnCharlie = new Comment(
            bob, 
            "This will change everything!", 
            currentTime, 
            currentDate
        );
        aliceComments.add(aliceCommentOnCharlie);
        bobComments.add(bobCommentOnCharlie);

        // Add users to network
        network.addUser(alice);
        network.addUser(bob);
        network.addUser(charlie);
        
        return network;
    }
}