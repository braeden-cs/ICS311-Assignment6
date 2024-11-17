package org.example;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class problem3 {

    public static void generateWordCloud(List<Post> posts, Predicate<User> userCriteria, Predicate<Post> postCriteria) throws IOException {
        List<Post> filteredPosts = Post.filterPosts(posts, userCriteria, postCriteria);

        String combinedContent = filteredPosts.stream()
                .map(Post::getContent)
                .collect(Collectors.joining(" "));

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(combinedContent.getBytes());

        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load(byteArrayInputStream);

        final Dimension dimension = new Dimension(1000, 1000);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(500));
        wordCloud.setColorPalette(new ColorPalette(
                new Color(0x4055F1),
                new Color(0x408DF1),
                new Color(0x40AAF1),
                new Color(0x40C5F1),
                new Color(0x40D3F1),
                new Color(0xFFFFFF)
        ));
        
        wordCloud.setFontScalar(new LinearFontScalar(10, 200));  // Set a bigger max font size for larger words

        // Build and save the word cloud to a file
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("commonWordsInPosts-wordcloud.png");
    }

    public static void main(String[] args) {
        // Create sample users
        User user1 = new User("John", 25, "Male", "Hawaii", null, null, null, null);
        User user2 = new User("Mary", 23, "Female", "California", null, null, null, null);
        User user3 = new User("Alice", 30, "Female", "Hawaii", null, null, null, null);
        User user4 = new User("Bob", 28, "Male", "Hawaii", null, null, null, null);
        User user5 = new User("Charlie", 35, "Male", "California", null, null, null, null);

        // Create sample posts with more content
        Post post1 = new Post("Hello world! This is a test post from John in Hawaii. Let's talk about tech and programming!", "2024-11-01T10:00", null, user1);
        Post post2 = new Post("This is a post containing random words for testing. I love coding in Java and exploring new algorithms!", "2024-11-02T15:00", null, user2);
        Post post3 = new Post("This will only be included for the Hawaii region. We are testing word cloud generation with varied content.", "2024-11-03T20:00", null, user1);
        Post post4 = new Post("Alice here! Discussing social media trends and the impact of technology on communication. Exciting times ahead!", "2024-11-04T09:00", null, user3);
        Post post5 = new Post("Post by Bob in Hawaii, exploring the best beaches and the incredible food in Honolulu. Can't wait for the weekend!", "2024-11-05T12:00", null, user4);
        Post post6 = new Post("Charlie from California sharing thoughts on fitness and well-being. Emphasizing the importance of mental health and exercise!", "2024-11-06T08:00", null, user5);
        Post post7 = new Post("This post is all about Java programming. Understanding complex data structures like trees, graphs, and heaps!", "2024-11-07T10:00", null, user2);
        Post post8 = new Post("Exploring the beauty of Hawaii and its diverse culture. From the beaches to the mountains, there's so much to see!", "2024-11-08T16:00", null, user1);
        Post post9 = new Post("Tech enthusiasts are always looking for ways to innovate. I am learning a lot about new algorithms in my computer science course!", "2024-11-09T11:00", null, user3);
        Post post10 = new Post("This post by Bob explores the Hawaiian lifestyle, food, and culture. Truly inspiring!", "2024-11-10T14:00", null, user4);

        // Combine posts into a list
        List<Post> posts = Arrays.asList(post1, post2, post3, post4, post5, post6, post7, post8, post9, post10);

        // Define user and post criteria for filtering
        Predicate<User> userCriteria = user -> user.getRegion().equals("Hawaii");
        Predicate<Post> postCriteria = post -> post.getContent().contains("post");

        try {
            // Generate word cloud
            generateWordCloud(posts, userCriteria, postCriteria);
            System.out.println("Word cloud generated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
