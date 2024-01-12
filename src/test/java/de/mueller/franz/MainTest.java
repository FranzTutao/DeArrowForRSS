package de.mueller.franz;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

	@Test
	void mainTest() throws FeedException, IOException {

		// test for setMediaThumbnailUrl for yt url
		HandleRssFeed handleRssFeed = new HandleRssFeed();
		String rssFeedURL = "https://www.youtube.com/feeds/videos.xml?channel_id=UCBa659QWEk1AI4Tg--mrJ2A";
		SyndFeed feed = handleRssFeed.readFeed(rssFeedURL);
		handleRssFeed.setMediaThumbnailUrl(feed.getEntries().get(2), "PLACEHOLDER PLACEHOLDER PLACEHOLDER");
		System.out.println(handleRssFeed.getMediaThumbnailUrl(feed.getEntries().get(2)));

		// get title and image url for yt url
		SyndFeed fileFeed = handleRssFeed.readFeedFromFile("Tom Scott's DeArrowedYouTubeFeed.xml");
		for (SyndEntry entry : fileFeed.getEntries()) {
			System.out.println("Title: " + entry.getTitle() + " URL: " + handleRssFeed.getMediaThumbnailUrl(entry));
		}
	}
}