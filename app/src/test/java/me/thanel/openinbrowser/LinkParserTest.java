package me.thanel.openinbrowser;

import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class LinkParserTest {

    @Test
    public void parseText_whenGivenBasicLink_returnsUri() {
        assertEquals(Uri.parse("http://www.google.com"), LinkParser.parseText("http://www.google.com"));
        assertEquals(Uri.parse("https://www.google.com"), LinkParser.parseText("https://www.google.com"));
        assertEquals(Uri.parse("http://www.google.com"), LinkParser.parseText("www.google.com"));
        assertEquals(Uri.parse("http://google.com"), LinkParser.parseText("google.com"));
    }

    @Test
    public void parseText_whenGivenLinkWithText_returnsExtractedUri() {
        assertEquals(Uri.parse("https://www.google.com"), LinkParser.parseText("https://www.google.com."));
        assertEquals(Uri.parse("https://www.google.com"), LinkParser.parseText("https://www.google.com. After"));
        assertEquals(Uri.parse("https://www.google.com"), LinkParser.parseText(".https://www.google.com"));
        assertEquals(Uri.parse("https://www.google.com"), LinkParser.parseText("Before https://www.google.com"));
    }

    @Test
    public void parseText_whenGivenQuotedLink_returnsExtractedUri() {
        assertEquals(Uri.parse("http://www.google.com"), LinkParser.parseText("'www.google.com'"));
        assertEquals(Uri.parse("http://www.google.com"), LinkParser.parseText("\"www.google.com\""));
        assertEquals(Uri.parse("http://www.google.com"), LinkParser.parseText("\nwww.google.com\n"));
    }

    @Test
    public void parseText_whenGivenMultipleLinks_returnsFirstExtractedUri() {
        assertEquals(Uri.parse("https://www.google.com"), LinkParser.parseText("https://www.google.com\nhttps://wikipedia.com"));
    }

    @Test
    public void parseText_whenGivenMultilineTextWithLink_returnsExtractedUri() {
        assertEquals(Uri.parse("https://www.google.com"), LinkParser.parseText("An example shared text\nWhich contains link at end - https://www.google.com"));
    }

    @Test
    public void parseText_whenGivenRegularText_returnsGoogleSearchUri() {
        assertEquals(Uri.parse("https://google.com/search?q=Search%20me"), LinkParser.parseText("Search me"));
    }

    @Test
    public void parseText_whenGivenMultilineText_returnsGoogleSearchUri() {
        assertEquals(Uri.parse("https://google.com/search?q=Search%20me%0Aincluding%0Aall%20lines"), LinkParser.parseText("Search me\nincluding\nall lines"));
    }
}
