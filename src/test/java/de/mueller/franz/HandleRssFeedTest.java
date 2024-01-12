package de.mueller.franz;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HandleRssFeedTest {

	public static final String TEST_FEED = " " +
			"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
			"<feed xmlns:yt=\"http://www.youtube.com/xml/schemas/2015\" xmlns:media=\"http://search.yahoo.com/mrss/\" xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en-US\">" +
			"<entry>\n" +
			"    <id>yt:video:3kwDVw0u4Kw</id>\n" +
			"    <yt:videoId>3kwDVw0u4Kw</yt:videoId>\n" +
			"    <yt:channelId>UCBa659QWEk1AI4Tg--mrJ2A</yt:channelId>\n" +
			"    <title>Spherical houses weren't a great idea.</title>\n" +
			"    <link rel=\"alternate\" href=\"https://yewtu.be/watch?v=3kwDVw0u4Kw\"/>\n" +
			"    <author>\n" +
			"      <name>Tom Scott</name>\n" +
			"      <uri>https://yewtu.be/channel/UCBa659QWEk1AI4Tg--mrJ2A</uri>\n" +
			"    </author>\n" +
			"    <content type=\"xhtml\">\n" +
			"      <div xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
			"        <a href=\"https://yewtu.be/watch?v=3kwDVw0u4Kw\">\n" +
			"          <img src=\"https://yewtu.be/vi/3kwDVw0u4Kw/mqdefault.jpg\"/>\n" +
			"        </a>\n" +
			"     </div>\n" +
			"    </content>\n" +
			"    <published>2023-09-25T15:00:06+00:00</published>\n" +
			"    <media:group>\n" +
			"      <media:title>Spherical houses weren't a great idea.</media:title>\n" +
			"      <media:thumbnail url=\"https://yewtu.be/vi/3kwDVw0u4Kw/mqdefault.jpg\" width=\"320\" height=\"180\"/>\n" +
			"      <media:description></media:description>\n" +
			"    </media:group>\n" +
			"    <media:community>\n" +
			"      <media:statistics views=\"3466641\"/>\n" +
			"    </media:community>\n" +
			"  </entry>\n" +
			"</feed>";


	private static final String CHANGED_CONTENT = "<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">\\n\" +\n" +
			"\t\t\t\"        <a href=\\\"https://yewtu.be/watch?v=3kwDVw0u4Kw\\\">\\n\" +\n" +
			"\t\t\t\"          <img src=\\\"https://example.com\\\"/>\\n\" +\n" +
			"\t\t\t\"        </a>\\n\" +\n" +
			"\t\t\t\"     </div>";

	HandleRssFeed handleRssFeed = new HandleRssFeed();
	String url = "https://yewtu.be/feed/channel/UCBa659QWEk1AI4Tg--mrJ2A";
	String ytUrl = "https://www.youtube.com/feeds/videos.xml?channel_id=UCBa659QWEk1AI4Tg--mrJ2A";
	private SyndFeed syndFeed;
	private SyndEntry feedEntry;


	@BeforeEach
	public void init() throws FeedException, IOException {
		syndFeed = handleRssFeed.readFeed(new StringReader(TEST_FEED));
		feedEntry = syndFeed.getEntries().getFirst();
	}

	@Test
	void readFeed() throws FeedException, IOException {
		handleRssFeed.readFeed("nahh");
	}

	@Test
	void editEntry() throws FeedException, IOException {

		handleRssFeed.editEntry(feedEntry, "newTitle", "https://example.com");
		assertEquals("newTitle", feedEntry.getTitle());
		assertEquals("https://example.com", handleRssFeed.getMediaThumbnailUrl(feedEntry));
	}

	@Test
	void writeFeedToFile() {
	}

	@Test
	void readFeedFromFile() {
	}

	@Test
	void setMediaThumbnailUrlTest() throws FeedException, IOException {
		SyndFeed feed = handleRssFeed.readFeed(url);
		for (SyndEntry entry : feed.getEntries()) {
			handleRssFeed.setMediaThumbnailUrl(entry, "PLACEHOLDERPLACEHOLDERPLACEHOLDER");
		}
	handleRssFeed.writeFeedToFile(feed);
	}


	@Test
	void getMediaThumbnailUrl() {
	}
}