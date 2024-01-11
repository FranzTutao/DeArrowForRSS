package de.mueller.franz;


import com.rometools.rome.feed.synd.SyndEnclosure;
import com.rometools.rome.feed.synd.SyndEnclosureImpl;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import com.rometools.rome.io.XmlReader;
import org.jdom2.Element;

import java.io.*;
import java.net.URL;
import java.util.List;

public class HandleRssFeed {
	/**
	 * gets rss feed for further processing
	 * @param url to rss feed
	 * @return SyndFeed
	 * @throws IOException
	 * @throws FeedException
	 */
	public SyndFeed readFeed(String url) throws IOException, FeedException {
		return new SyndFeedInput().build(new XmlReader(new URL(url)));
	}

	/**
	 * change RSS entry to DeArrowed version
	 * @param entry entry to be edited
	 * @param alternateTitle DeArrows Title or null
	 * @param alternateImageUrl DeArrows image url or null
	 */
	public void editEntry(SyndEntry entry, String alternateTitle, String alternateImageUrl) {
		if (alternateTitle!= null) {
			entry.setTitle(alternateTitle);
		}
		if (alternateImageUrl!= null) {
			SyndEnclosureImpl enclosure = new SyndEnclosureImpl();
			enclosure.setUrl(alternateImageUrl);
			enclosure.setType("image/webp");
			// set image
			entry.setEnclosures(List.of(enclosure));
		}
	}

	/**
	 * save feed to hard drive
	 * @param feed feed to be saved
	 * @throws IOException
	 * @throws FeedException
	 */
	public void writeFeedToFile(SyndFeed feed) throws IOException, FeedException {
		SyndFeedOutput output = new SyndFeedOutput();
		String channelName = feed.getTitle();
		output.output(feed, new FileWriter(String.format("%s's DeArrowedYouTubeFeed.xml", channelName)));
	}

	/**
	 * read feed from file
	 * @param fileName
	 * @return SyndFeed
	 * @throws IOException
	 * @throws FeedException
	 */
	public SyndFeed readFeedFromFile(String fileName) throws IOException, FeedException {
		SyndFeedInput input = new SyndFeedInput();
		return input.build(new File(fileName));
	}

	/**
	 * idk
	 * @param entry
	 * @param newUrl
	 */
	public static void setMediaThumbnailUrl(SyndEntry entry, String newUrl) {
		List<Element> foreignMarkupList = entry.getForeignMarkup();
		for (Element foreignMarkup : foreignMarkupList) {
			if ("group".equals(foreignMarkup.getName()) && "media".equals(foreignMarkup.getNamespacePrefix())) {
				Element mediaThumbnail = foreignMarkup.getChild("thumbnail", foreignMarkup.getNamespace("media"));
				if (mediaThumbnail != null) {
					mediaThumbnail.setAttribute("url", newUrl);
				}
			}
		}
	}
	/**
	 * idk
	 * @param entry
	 * @return
	 */
	public static String getMediaThumbnailUrl(SyndEntry entry) {
		List<Element> foreignMarkupList = entry.getForeignMarkup();
		for (Element foreignMarkup : foreignMarkupList) {
			if ("group".equals(foreignMarkup.getName()) && "media".equals(foreignMarkup.getNamespacePrefix())) {
				Element mediaThumbnail = foreignMarkup.getChild("thumbnail", foreignMarkup.getNamespace("media"));
				if (mediaThumbnail != null) {
					return mediaThumbnail.getAttributeValue("url");
				}
			}
		}
		return null;
	}
}