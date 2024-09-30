package twitter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Filter {

    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        List<Tweet> result = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if (tweet.getAuthor().equals(username)) {
                result.add(tweet);
            }
        }
        return result;
    }

    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        List<Tweet> result = new ArrayList<>();
        for (Tweet tweet : tweets) {
            if (!tweet.getTimestamp().isBefore(timespan.getStart()) && !tweet.getTimestamp().isAfter(timespan.getEnd())) {
                result.add(tweet);
            }
        }
        return result;
    }

    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        List<Tweet> result = new ArrayList<>();
        Set<String> wordSet = new HashSet<>();
        for (String word : words) {
            wordSet.add(word.toLowerCase());
        }
        for (Tweet tweet : tweets) {
            String[] tweetWords = tweet.getText().toLowerCase().split("\\s+");
            for (String word : tweetWords) {
                if (wordSet.contains(word)) {
                    result.add(tweet);
                    break;
                }
            }
        }
        return result;
    }
}
