package org.example;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.view.Viewer;
import java.util.*;

public class SocialNetworkVisualizer {
    private Graph graph;
    private Map<String, Node> userNodes;
    private Map<String, Node> postNodes;
    private ImportanceCriteria currentCriteria;
    private Map<String, Post> postsById;
    
    public enum ImportanceCriteria {
        VIEWS,
        COMMENTS,
        COMBINED
    }

    public SocialNetworkVisualizer() {
        System.setProperty("org.graphstream.ui", "swing");
        graph = new SingleGraph("Social Network");
        userNodes = new HashMap<>();
        postNodes = new HashMap<>();
        postsById = new HashMap<>();
        currentCriteria = ImportanceCriteria.VIEWS;
        
        setupStyles();
    }

private void setupStyles() {
        String css = String.join("\n",
            "node {",
            "   size: 35px;",
            "   text-size: 16;",
            "   text-color: black;",
            "   text-background-mode: rounded-box;",
            "   text-background-color: white;",
            "   text-padding: 5px;",
            "   text-offset: 0px, -5px;",
            "   stroke-mode: plain;",
            "   stroke-width: 2px;",
            "}",
            "node.user {",
            "   fill-color: rgb(100,149,237);",
            "   stroke-color: rgb(70,130,220);",
            "   shape: circle;",
            "   size: 40px;",
            "   z-index: 1;",
            "}",
            "node.post {",
            "   fill-color: rgb(255,228,196);",  // Lighter peach color
            "   stroke-color: rgb(235,198,165);",
            "   shape: rounded-box;",  // Changed to rounded box
            "   size: 45px;",
            "   z-index: 0;",
            "}",
            "node.important {",
            "   fill-color: rgb(255,99,71);",
            "   stroke-color: rgb(220,20,60);",
            "   stroke-width: 3px;",
            "   size: 50px;",
            "   z-index: 2;",
            "}",
            "edge {",
            "   shape: line;",
            "   arrow-size: 15px, 6px;",
            "}",
            "edge.authorship {",
            "   fill-color: rgb(34,139,34);",
            "   size: 3px;",
            "   z-index: 1;",
            "}",
            "edge.viewing {",
            "   fill-color: rgb(169,169,169);",
            "   size: 2px;",
            "   z-index: 0;",
            "}"
        );

        graph.setAttribute("ui.stylesheet", css);
        graph.setAttribute("ui.quality", 4);
        graph.setAttribute("ui.antialias");
    }

    // Add this method to control the layout
    private void applyLayout() {
        for (Node node : graph) {
            // Add some random positioning to avoid overlaps
            double x = Math.random() * 100 - 50;
            double y = Math.random() * 100 - 50;
            node.setAttribute("xy", x, y);
        }

        // Optional: Add force-directed layout
        SpringBox layout = new SpringBox();
        layout.setForce(0.5);
        layout.setQuality(0.9);
        layout.compute();
    }

    public void display() {
        applyLayout();  // Add this line
        Viewer viewer = graph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
    }

    private String truncateContent(String content) {
        return content.length() > 25 ? 
            content.substring(0, 22) + "..." : 
            content;
    }

    public void createUser(User user) {
        String nodeId = "user_" + user.getUsername();
        if (!userNodes.containsKey(nodeId)) {
            Node node = graph.addNode(nodeId);
            node.setAttribute("ui.class", "user");
            node.setAttribute("ui.label", user.getUsername());
            userNodes.put(nodeId, node);
            
            // Add authored posts
            if (user.getAuthoredPosts() != null) {
                for (Post post : user.getAuthoredPosts()) {
                    createPost(post);
                    createAuthorship(user, post);
                }
            }
        }
    }

    public void createPost(Post post) {
        String nodeId = "post_" + post.hashCode();
        if (!postNodes.containsKey(nodeId)) {
            Node node = graph.addNode(nodeId);
            node.setAttribute("ui.class", "post");
            node.setAttribute("ui.label", truncateContent(post.getContent()));
            postNodes.put(nodeId, node);
            postsById.put(String.valueOf(post.hashCode()), post);
        }
    }

    public void createAuthorship(User user, Post post) {
        String edgeId = "auth_" + user.getUsername() + "_" + post.hashCode();
        if (graph.getEdge(edgeId) == null) {
            Edge edge = graph.addEdge(edgeId, 
                "user_" + user.getUsername(), 
                "post_" + post.hashCode(), 
                true);
            edge.setAttribute("ui.class", "authorship");
        }
    }

    public void createView(User user, Post post) {
        String edgeId = "view_" + user.getUsername() + "_" + post.hashCode();
        if (graph.getEdge(edgeId) == null) {
            Edge edge = graph.addEdge(edgeId, 
                "user_" + user.getUsername(), 
                "post_" + post.hashCode(), 
                true);
            edge.setAttribute("ui.class", "viewing");
        }
    }

    public void setImportanceCriteria(ImportanceCriteria criteria) {
        this.currentCriteria = criteria;
        updateImportantPosts();
    }

    private void updateImportantPosts() {
        // Reset all posts to normal styling
        postNodes.values().forEach(node -> 
            node.setAttribute("ui.class", "post"));
        
        // Find and highlight important posts
        List<Post> importantPosts = findImportantPosts(currentCriteria);
        
        // Highlight important posts
        for (Post post : importantPosts) {
            Node node = postNodes.get("post_" + post.hashCode());
            if (node != null) {
                node.setAttribute("ui.class", "post important");
            }
        }
    }

    private List<Post> findImportantPosts(ImportanceCriteria criteria) {
        List<Post> allPosts = new ArrayList<>(postsById.values());

        // Sort posts by importance
        allPosts.sort((p1, p2) -> {
            double score1 = calculateImportanceScore(p1, criteria);
            double score2 = calculateImportanceScore(p2, criteria);
            return Double.compare(score2, score1);
        });

        // Return top 25% of posts
        int topCount = Math.max(1, allPosts.size() / 4);
        return allPosts.subList(0, topCount);
    }

    private double calculateImportanceScore(Post post, ImportanceCriteria criteria) {
        switch (criteria) {
            case VIEWS:
                return post.getAmountOfViews();
            case COMMENTS:
                return post.getComments().size();
            case COMBINED:
                double viewScore = post.getAmountOfViews();
                double commentScore = post.getComments().size() * 2; // Comments weighted more
                double viewRateScore = post.getViewRate() * 100; // View rate weighted most
                return viewScore + commentScore + viewRateScore;
            default:
                return 0.0;
        }
    }

    // Example usage
    public static void main(String[] args) {
        // Create test data
        TestData testData = new TestData();
        SocialNetwork network = testData.generateTestNetwork();

        // Initialize visualizer
        SocialNetworkVisualizer visualizer = new SocialNetworkVisualizer();

        // Add data to visualizer
        for (User user : network.getUsers()) {
            visualizer.createUser(user);
        }

        // Add viewing relationships
        for (User user : network.getUsers()) {
            if (user.getReadPosts() != null) {
                for (Post post : user.getReadPosts()) {
                    visualizer.createView(user, post);
                }
            }
        }

        // Display the graph
        visualizer.display();

        // Test different importance criteria
        try {
            Thread.sleep(3000);
            System.out.println("Switching to Views importance...");
            visualizer.setImportanceCriteria(ImportanceCriteria.VIEWS);
            
            Thread.sleep(3000);
            System.out.println("Switching to Comments importance...");
            visualizer.setImportanceCriteria(ImportanceCriteria.COMMENTS);
            
            Thread.sleep(3000);
            System.out.println("Switching to Combined importance...");
            visualizer.setImportanceCriteria(ImportanceCriteria.COMBINED);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Keep program running
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Press Enter to exit");
            scanner.nextLine();
        }
    }
}