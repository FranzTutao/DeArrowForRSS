package de.mueller.franz;

import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.jdom2.Element;

import java.io.IOException;
import java.util.List;

public class Main {
	public static void main(String[] args) throws IOException, FeedException {
		// DeArrow: https://wiki.sponsor.ajay.app/w/API_Docs/DeArrow
		// ROME: https://rometools.github.io/rome/

		// ToDo fixImage?; fix corrective check; speed up request; better understand api request

		String rssFeedURL = "https://www.youtube.com/feeds/videos.xml?channel_id=UCBa659QWEk1AI4Tg--mrJ2A";
		HandleRssFeed handleRssFeed = new HandleRssFeed();
		DeArrow deArrow = new DeArrow();

		SyndFeed feed = handleRssFeed.readFeed(rssFeedURL);
		/*for (SyndEntry entry : feed.getEntries()) {
			// get vidID from entry
			String videoId = entry.getUri().substring(entry.getUri().lastIndexOf('=') + 1);
			videoId = videoId.substring(videoId.lastIndexOf(":") + 1);
			DeArrow.ProcessedInformation processedInformation = deArrow.processInformation(videoId, deArrow.getInitialInformation(videoId));
			handleRssFeed.editEntry(entry, processedInformation.getTitle(), processedInformation.getUrl());
		}
		handleRssFeed.writeFeedToFile(feed);*/

		/*SyndFeed fileFeed = handleRssFeed.readFeedFromFile("Franz3's DeArrowedYouTubeFeed.xml");
		for (SyndEntry entry : fileFeed.getEntries()) {
			System.out.println("Title: " + entry.getTitle() + " URL: " + entry.getEnclosures().getFirst().getUrl());
		}*/
		HandleRssFeed.setMediaThumbnailUrl(feed.getEntries().get(2), "PLACEHOLDER PLACEHOLDER PLACEHOLDER");
		System.out.println(HandleRssFeed.getMediaThumbnailUrl(feed.getEntries().get(2)));

	}
}