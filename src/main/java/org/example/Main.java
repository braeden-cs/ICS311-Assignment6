package org.example;

import java.util.Scanner;

public class Main {
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

        // Display the visualization
        System.out.println("\nDisplaying social network visualization...");
        visualizer.display();

        // Keep program running
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Press Enter to exit");
            scanner.nextLine();
        }
    }
}