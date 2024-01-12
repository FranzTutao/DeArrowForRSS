package de.mueller.franz;


import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.SyndFeedOutput;
import com.rometools.rome.io.XmlReader;
import org.jdom2.Element;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.List;

public class HandleRssFeed {
	/**
	 * gets rss feed for further processing
	 *
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
	 *
	 * @param entry             entry to be edited
	 * @param alternateTitle    DeArrows Title or null
	 * @param alternateImageUrl DeArrows image url or null
	 */
	public void editEntry(SyndEntry entry, String alternateTitle, String alternateImageUrl) {
		if (alternateTitle != null) {
			entry.setTitle(alternateTitle);
		}
		if (alternateImageUrl != null) {
			setMediaThumbnailUrl(entry, alternateImageUrl);
		}
	}

	/**
	 * save feed to hard drive
	 *
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
	 *
	 * @param fileName
	 * @return SyndFeed
	 * @throws IOException
	 * @throws FeedException
	 */
	public SyndFeed readFeedFromFile(String fileName) throws IOException, FeedException {
		return this.readFeed(new FileReader(fileName));
	}

	public SyndFeed readFeed(Reader content) throws IOException, FeedException {
		SyndFeedInput input = new SyndFeedInput();
		return input.build(content);
	}

	/**
	 * replace url for media:thumbnail element with jdom2
	 *
	 * @param entry
	 * @param newUrl
	 */
	public void setMediaThumbnailUrl(SyndEntry entry, String newUrl) {
		// yt and invidious
		List<Element> foreignMarkupList = entry.getForeignMarkup();
		for (Element foreignMarkup : foreignMarkupList) {
			if ("group".equals(foreignMarkup.getName()) && "media".equals(foreignMarkup.getNamespacePrefix())) {
				Element mediaThumbnail = foreignMarkup.getChild("thumbnail", foreignMarkup.getNamespace("media"));
				if (mediaThumbnail != null) {
					mediaThumbnail.setAttribute("url", newUrl);
				}
			}
		}
		// check for invidious specifically
		for (SyndContent content : entry.getContents()) {
			Document document = Jsoup.parse(content.getValue(), "", Parser.xmlParser());
			Elements imgElements = document.select("a img");
			// Get the src attribute value
			for (org.jsoup.nodes.Element imgElement : imgElements) {
				imgElement.attr("src", newUrl);
			}
			// update content object
			content.setValue(document.outerHtml());
		}
	}

	/**
	 * get url for media:thumbnail element with jdom2
	 *
	 * @param entry
	 * @return url from media:thumbnail
	 */
	public String getMediaThumbnailUrl(SyndEntry entry) {
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