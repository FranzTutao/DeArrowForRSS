package de.mueller.franz;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException, FeedException {
		// DeArrow: https://wiki.sponsor.ajay.app/w/API_Docs/DeArrow
		// ROME: https://rometools.github.io/rome/

		// ToDo make dynamic and exposed to web; better understand api request; write tests

		// Tom Scott
		// String rssFeedURL = "https://www.youtube.com/feeds/videos.xml?channel_id=UCBa659QWEk1AI4Tg--mrJ2A";
		// String rssFeedURL = "https://yewtu.be/feed/channel/UCBa659QWEk1AI4Tg--mrJ2A";
		// Franz3
		String rssFeedURL = "https://yewtu.be/feed/channel/UCXSxFFavTLTKzKkudovN2wQ";

		HandleRssFeed handleRssFeed = new HandleRssFeed();
		DeArrow deArrow = new DeArrow();

		SyndFeed feed = handleRssFeed.readFeed(rssFeedURL);
		for (SyndEntry entry : feed.getEntries()) {
			// get vidID from entry
			String videoId = entry.getUri().substring(entry.getUri().lastIndexOf('=') + 1);
			videoId = videoId.substring(videoId.lastIndexOf(":") + 1);
			// get info
			DeArrow.ProcessedInformation processedInformation = deArrow.processInformation(videoId, deArrow.getInitialInformation(videoId));
			// change rss feed
			handleRssFeed.editEntry(entry, processedInformation.getTitle(), processedInformation.getUrl());
		}
		handleRssFeed.writeFeedToFile(feed);
	}
}