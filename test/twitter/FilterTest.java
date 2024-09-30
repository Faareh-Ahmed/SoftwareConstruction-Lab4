package twitter;

import static org.junit.Assert.*;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class FilterTest {
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T12:00:00Z");

    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "alyssa", "loving the new Twitter features", d3);

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }

    // Test cases for writtenBy()

    @Test
    public void testWrittenByNoTweets() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(), "alyssa");
        assertTrue("expected empty list", writtenBy.isEmpty());
    }

    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet1", writtenBy.contains(tweet1));
    }

    @Test
    public void testWrittenByMultipleTweetsMultipleResults() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3), "alyssa");
        assertEquals("expected two tweets", 2, writtenBy.size());
        assertTrue("expected list to contain tweet1 and tweet3", writtenBy.containsAll(Arrays.asList(tweet1, tweet3)));
    }

    @Test
    public void testWrittenByDifferentCase() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "ALYSSA");
        assertTrue("expected empty list, case-sensitive", writtenBy.isEmpty());
    }

    // Test cases for inTimespan()

    @Test
    public void testInTimespanNoTweets() {
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(), new Timespan(d1, d2));
        assertTrue("expected empty list", inTimespan.isEmpty());
    }

    @Test
    public void testInTimespanAllTweetsWithin() {
        Timespan span = new Timespan(d1, d3);
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), span);
        assertEquals("expected both tweets", 2, inTimespan.size());
    }

    @Test
    public void testInTimespanSomeTweetsWithin() {
        Timespan span = new Timespan(d1, d2);
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), span);
        assertEquals("expected two tweets", 2, inTimespan.size());
        assertTrue("expected list to contain tweet1 and tweet2", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
    }

    @Test
    public void testInTimespanOnBoundary() {
        Timespan span = new Timespan(d1, d2);
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3), span);
        assertTrue("expected tweet1 and tweet2", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertFalse("did not expect tweet3", inTimespan.contains(tweet3));
    }

    // Test cases for containing()

    @Test
    public void testContainingNoTweets() {
        List<Tweet> containing = Filter.containing(Arrays.asList(), Arrays.asList("talk"));
        assertTrue("expected empty list", containing.isEmpty());
    }

    @Test
    public void testContainingNoWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList());
        assertTrue("expected empty list", containing.isEmpty());
    }

    @Test
    public void testContainingOneWord() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        assertEquals("expected two tweets", 2, containing.size());
    }

    @Test
    public void testContainingCaseInsensitive() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("TALK"));
        assertEquals("expected two tweets", 2, containing.size());
    }

    @Test
    public void testContainingNoMatches() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("java"));
        assertTrue("expected empty list", containing.isEmpty());
    }
}
