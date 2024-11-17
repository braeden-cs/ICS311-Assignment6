package org.example;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;

public class SocialNetworkVisualizer {
    private Graph graph;
    private Map<String, Node> userNodes;
    private Map<String, Node> postNodes;
    private ImportanceCriteria currentCriteria;
    private Map<String, Post> postsById;
    private JFrame controlPanel;
    private Map<String, Double> postScores;
    
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
        postScores = new HashMap<>();
        currentCriteria = ImportanceCriteria.COMBINED;
        
        setupStyles();
        createControlPanel();
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
            "   fill-color: rgb(255,228,196);",
            "   stroke-color: rgb(235,198,165);",
            "   shape: rounded-box;",
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

    private void createControlPanel() {
        controlPanel = new JFrame("Social Network Analysis Controls");
        controlPanel.setSize(400, 300);
        controlPanel.setLayout(new BorderLayout());

        // Create importance criteria selector
        JPanel criteriaPanel = new JPanel();
        criteriaPanel.setBorder(BorderFactory.createTitledBorder("Importance Criteria"));
        ButtonGroup group = new ButtonGroup();
        
        JRadioButton viewsButton = new JRadioButton("Views");
        JRadioButton commentsButton = new JRadioButton("Comments");
        JRadioButton combinedButton = new JRadioButton("Combined");
        
        group.add(viewsButton);
        group.add(commentsButton);
        group.add(combinedButton);
        
        criteriaPanel.add(viewsButton);
        criteriaPanel.add(commentsButton);
        criteriaPanel.add(combinedButton);

        // Create stats panel
        JPanel statsPanel = new JPanel();
        statsPanel.setBorder(BorderFactory.createTitledBorder("Post Statistics"));
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        JTextArea statsArea = new JTextArea(8, 30);
        statsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statsArea);
        statsPanel.add(scrollPane);

        // Add action listeners
        ActionListener criteriaListener = e -> {
            if (e.getSource() == viewsButton) {
                setImportanceCriteria(ImportanceCriteria.VIEWS);
            } else if (e.getSource() == commentsButton) {
                setImportanceCriteria(ImportanceCriteria.COMMENTS);
            } else {
                setImportanceCriteria(ImportanceCriteria.COMBINED);
            }
            updateStatsDisplay(statsArea);
            updateImportantPosts();  // Make sure visualization updates
        };

        viewsButton.addActionListener(criteriaListener);
        commentsButton.addActionListener(criteriaListener);
        combinedButton.addActionListener(criteriaListener);

        // Default selection
        combinedButton.setSelected(true);

        // Add refresh button with enhanced action
        JButton refreshButton = new JButton("Refresh Analysis");
        refreshButton.addActionListener(e -> {
            System.out.println("Refreshing analysis..."); // Debug output
            ImportanceCriteria currentSelection = ImportanceCriteria.COMBINED;
            if (viewsButton.isSelected()) {
                currentSelection = ImportanceCriteria.VIEWS;
            } else if (commentsButton.isSelected()) {
                currentSelection = ImportanceCriteria.COMMENTS;
            }
            
            setImportanceCriteria(currentSelection);
            updateImportantPosts();
            updateStatsDisplay(statsArea);
            graph.setAttribute("ui.refresh"); // Force graph refresh
        });

        // Add panels to frame
        controlPanel.add(criteriaPanel, BorderLayout.NORTH);
        controlPanel.add(statsPanel, BorderLayout.CENTER);
        controlPanel.add(refreshButton, BorderLayout.SOUTH);
    }

    public void setImportanceCriteria(ImportanceCriteria criteria) {
        System.out.println("Setting importance criteria to: " + criteria); // Debug output
        this.currentCriteria = criteria;
        updateImportantPosts();
    }

    private void updateImportantPosts() {
        System.out.println("Updating important posts..."); // Debug output
        
        // Reset all posts to normal styling
        postNodes.values().forEach(node -> {
            node.setAttribute("ui.class", "post");
            System.out.println("Reset node: " + node.getId());
        });
        
        // Calculate and store scores
        postScores.clear();
        for (String postId : postsById.keySet()) {
            Post post = postsById.get(postId);
            double score = calculateImportanceScore(post, currentCriteria);
            postScores.put(postId, score);
            System.out.println("Post: " + post.getContent() + " Score: " + score);
        }

        // Find and highlight important posts
        List<Post> importantPosts = findImportantPosts(currentCriteria);
        
        // Highlight important posts
        for (Post post : importantPosts) {
            Node node = postNodes.get("post_" + post.hashCode());
            if (node != null) {
                node.setAttribute("ui.class", "post important");
                System.out.println("Highlighted post: " + post.getContent());
            }
        }
        
        // Force graph refresh
        graph.setAttribute("ui.refresh");
    }

    private void updateStatsDisplay(JTextArea statsArea) {
        StringBuilder stats = new StringBuilder();
        stats.append("Current Importance Criteria: ").append(currentCriteria).append("\n\n");

        List<Map.Entry<String, Double>> sortedPosts = new ArrayList<>(postScores.entrySet());
        sortedPosts.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        for (Map.Entry<String, Double> entry : sortedPosts) {
            Post post = postsById.get(entry.getKey());
            if (post != null) {
                stats.append("Post: ").append(truncateContent(post.getContent())).append("\n");
                stats.append("Views: ").append(post.getAmountOfViews()).append("\n");
                stats.append("Comments: ").append(post.getComments().size()).append("\n");
                stats.append("View Rate: ").append(String.format("%.2f", post.getViewRate())).append("\n");
                stats.append("Importance Score: ").append(String.format("%.2f", entry.getValue())).append("\n\n");
            }
        }

        statsArea.setText(stats.toString());
        statsArea.setCaretPosition(0); // Scroll to top
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

    private String truncateContent(String content) {
        return content.length() > 25 ? 
            content.substring(0, 22) + "..." : 
            content;
    }

    public void display() {
        Viewer viewer = graph.display();
        viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);
        
        // Show control panel - position it in the center of the screen initially
        try {
            // Get the screen size
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            
            // Position the control panel on the right side of the screen
            int x = (screenSize.width - controlPanel.getWidth() - 20);  // 20 pixels from right edge
            int y = (screenSize.height - controlPanel.getHeight()) / 2;  // Vertically centered
            
            controlPanel.setLocation(x, y);
            controlPanel.setVisible(true);
            
            // Optional: Add window listener to keep control panel visible
            controlPanel.addWindowListener(new WindowAdapter() {
                @Override
                public void windowIconified(WindowEvent e) {
                    controlPanel.setExtendedState(JFrame.NORMAL);
                }
            });
            
        } catch (Exception e) {
            // Fallback: center the control panel on screen
            controlPanel.setLocationRelativeTo(null);
            controlPanel.setVisible(true);
        }
    }

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

        // Keep program running
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Press Enter to exit");
            scanner.nextLine();
        }
    }
}