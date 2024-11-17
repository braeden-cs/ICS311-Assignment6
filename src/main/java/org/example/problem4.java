package org.example;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.palette.ColorPalette;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class problem4 {

    public static void main(String[] args) throws IOException {
        HashMap<String, User> users;
        List<User> views1 = new ArrayList<>();
        List<String> connections1 = new ArrayList<>();
        List<Post> authoredPosts1 = new ArrayList<>();
        List<Post> readPosts1 = new ArrayList<>();
        List<Comment> comments1 = new ArrayList<>();

        User user1 = new User("John", 20, "Male", "California", connections1,authoredPosts1, readPosts1, comments1);
        User user2 = new User("Doe", 27, "Male", "Hawaii", null, null, null, null);

        Post post1 = new Post("hello, this is post 1", "2024-11-15T04:00" , views1, user1);
        views1.add(user2);
        authoredPosts1.add(post1);

        final FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(300);
        final List<WordFrequency> wordFrequencies = frequencyAnalyzer.load("lyrics.txt");
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
