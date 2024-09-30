package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;

import org.junit.Test;

public class ExtractTest {

    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "charlie", "Hey @alyssa, how's it going?", d3);
    private static final Tweet tweet4 = new Tweet(4, "dave", "Talking to @alyssa and @bbitdiddle!", d3);

    // Test case for getTimespan 
//     Test case for getTimespan of 2 Tweets having different timestamps

    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
//    Test case for getTimespan of 1 Tweet
    @Test
    public void testGetTimespanOneTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        
        assertEquals("expected start and end", d1, timespan.getStart());
        assertEquals("expected start and end", d1, timespan.getEnd());
    }
    
//    Test case for getTimespan of 2 Tweets having same timestamp
    @Test
    public void testGetTimespanMultipleTweetsSameTimestamp() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet2, tweet3));
        
        assertEquals("expected start", d2, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }

    // Test case for getMentionedUsers
//    Test with a Tweet that has no Mentions
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
//    Test with a Tweet with 1 Mention
    @Test
    public void testGetMentionedUsersOneMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet3));
        
        assertEquals("expected one user", 1, mentionedUsers.size());
        assertTrue("expected to find alyssa", mentionedUsers.contains("alyssa"));
    }
    
//   Test with a Tweet having more than 1 Mentions
    @Test
    public void testGetMentionedUsersMultipleMentions() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet4));
        
        assertEquals("expected two users", 2, mentionedUsers.size());
        assertTrue("expected to find alyssa", mentionedUsers.contains("alyssa"));
        assertTrue("expected to find bbitdiddle", mentionedUsers.contains("bbitdiddle"));
    }
    
//    Test with Case Sensitive Mentions
    @Test
    public void testGetMentionedUsersCaseInsensitive() {
        Tweet mixedCaseTweet = new Tweet(5, "eve", "Hi @ALYSSA", d2);
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(mixedCaseTweet));
        
        assertEquals("expected one user", 1, mentionedUsers.size());
        assertTrue("expected to find alyssa", mentionedUsers.contains("alyssa"));
    }
}
