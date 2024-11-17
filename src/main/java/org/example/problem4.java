package org.example;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.*;
import java.sql.Time;
import java.util.*;
import java.util.List;

public class problem4 {

    public static void main(String[] args) throws IOException {
        String filePath = "data.txt";
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            User currentUser = null;
            List<Post> currentPosts = new ArrayList<>();
            List<Comment> currentComments = new ArrayList<>();
            Map<String,User> userMap = new HashMap<>();

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("User:")) {
                    if (currentUser != null) {
                        currentUser.getAuthoredPosts().addAll(currentPosts);
                        currentUser.getAuthoredPosts().forEach(post -> post.getAmountOfViews());
                        users.add(currentUser);
                    }

                    String username = line.split(":")[1].trim();
                    currentUser = new User(username,0, null, null, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                    currentPosts = new ArrayList<>();
                    userMap.put(username, currentUser);
                } else if (line.startsWith("Post:")) {
                    String[] postParts = line.split(":", 2)[1].split("\\|");
                    String content = postParts[0].trim();
                    String creationTime = postParts[1].trim();
                    List<User> views = new ArrayList<>();
                    if (postParts.length > 2) {
                        String[] viewUsers = postParts[2].trim().split(",");
                        for (String viewUsername : viewUsers) {
                            if (userMap.containsKey(viewUsername.trim())) {
                                views.add(userMap.get(viewUsername.trim()));
                            }
                        }
                    }
                    currentPosts.add(new Post(content, creationTime, views, null));
                } else if (line.startsWith("Comment:")) {
                    String[] commentParts = line.split(":", 2)[1].split("\\|");
                    String commentContent = commentParts[0].trim();
                    String creatorName = commentParts[1].trim();
                    Time creationTime = Time.valueOf(commentParts[2].trim());
                    Date creationDate = java.sql.Date.valueOf(commentParts[3].trim());
                    User creator = userMap.get(creatorName);

                    currentComments.add(new Comment(creator,commentContent,creationTime, (java.sql.Date) creationDate));
                }
            }
            if (currentUser != null) {
                currentUser.getAuthoredPosts().addAll(currentPosts);
                users.add(currentUser);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter output = new PrintWriter("output.txt");
        Map<String, Integer> wordFrequency = new HashMap<>();
        System.out.println("TRENDING POSTS REPORT: ");
        for (User user : users) {
            for (Post post : user.getAuthoredPosts()) {
                if (post.getViewRate() > 100) {
                    String[] words = post.getContent().split(" ");
                    for (String word : words) {
                        word = word.replaceAll("\\p{Punct}", "");
                        if (!wordFrequency.containsKey(word)){
                            int i = 1;
                            wordFrequency.put(word, i);
                        } else {
                            int i = wordFrequency.get(word);
                            wordFrequency.put(word, i+1);
                        }
                    }
                    System.out.println("Username: " + user.getUsername());
                    System.out.println("Authored Posts: ");
                    System.out.println(" - Content: " + post.getContent());
                    System.out.println("   Views: " + post.getAmountOfViews());
                    System.out.println("   View Rate: " + post.getViewRate());
                }
            }
        }
        wordFrequency.forEach((k,v) -> output.println(v + ": " + k));
        output.close();

        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load("output.txt");
        final Dimension dimension = new Dimension(1000, 1000);
        final WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(500));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 100));
        wordCloud.build(wordFrequencies);
        wordCloud.writeToFile("wordcloud.png");
    }

}
