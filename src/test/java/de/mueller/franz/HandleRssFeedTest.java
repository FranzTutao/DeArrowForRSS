package de.mueller.franz;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class HandleRssFeedTest {

	public static final String TEST_FEED = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\r\n" +
			"<feed xmlns=\"http://www.w3.org/2005/Atom\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\r\r\n" +
			"  <title>Example</title>\r\r\n" +
			"  <link rel=\"alternate\" href=\"https://yewtu.be/channel/UCBa659QWEk1AI4Tg--mrJ2A\" />\r\r\n" +
			"  <link rel=\"self\" href=\"https://yewtu.be/feed/channel/UCBa659QWEk1AI4Tg--mrJ2A\" />\r\r\n" +
			"  <author>\r\r\n" +
			"    <name>Example</name>\r\r\n" +
			"    <uri>https://yewtu.be/channel/UCBa659QWEk1AI4Tg--mrJ2A</uri>\r\r\n" +
			"  </author>\r\r\n" +
			"  <id>yt:channel:UCBa659QWEk1AI4Tg--mrJ2A</id>\r\r\n" +
			"  <icon>https://yt3.googleusercontent.com/ytc/AIf8zZQZqvzmRs8KizC2KIwe2ISv3jCLWu8LOfW3ZTL2YA=s900-c-k-c0x00ffffff-no-rj</icon>\r\r\n" +
			"  <yt:channelId xmlns:yt=\"http://www.youtube.com/xml/schemas/2015\">UCBa659QWEk1AI4Tg--mrJ2A</yt:channelId>\r\r\n" +
			"  <entry>\r\r\n" +
			"    <title>After ten years, it's time to stop making videos.</title>\r\r\n" +
			"    <link rel=\"alternate\" href=\"https://yewtu.be/watch?v=7DKv5H5Frt0\" />\r\r\n" +
			"    <author>\r\r\n" +
			"      <name>Example</name>\r\r\n" +
			"      <uri>https://yewtu.be/channel/UCBa659QWEk1AI4Tg--mrJ2A</uri>\r\r\n" +
			"    </author>\r\r\n" +
			"    <id>yt:video:7DKv5H5Frt0</id>\r\r\n" +
			"    <updated>2024-01-01T16:00:06Z</updated>\r\r\n" +
			"    <published>2024-01-01T16:00:06Z</published>\r\r\n" +
			"    <content type=\"xhtml\">\r\r\n" +
			"      <div xmlns=\"http://www.w3.org/1999/xhtml\">\r\r\n" +
			"        <a href=\"https://yewtu.be/watch?v=7DKv5H5Frt0\">\r\r\n" +
			"          <img src=\"https://yewtu.be/vi/7DKv5H5Frt0/mqdefault.jpg\" />\r\r\n" +
			"        </a>\r\r\n" +
			"        <p style=\"word-break:break-word;white-space:pre-wrap\">Find me here</p>\r\r\n" +
			"      </div>\r\r\n" +
			"    </content>\r\r\n" +
			"    <yt:videoId xmlns:yt=\"http://www.youtube.com/xml/schemas/2015\">7DKv5H5Frt0</yt:videoId>\r\r\n" +
			"    <yt:channelId xmlns:yt=\"http://www.youtube.com/xml/schemas/2015\">UCBa659QWEk1AI4Tg--mrJ2A</yt:channelId>\r\r\n" +
			"    <media:group xmlns:media=\"http://search.yahoo.com/mrss/\">\r\r\n" +
			"      <media:title>After ten years, it's time to stop making videos.</media:title>\r\r\n" +
			"      <media:thumbnail url=\"https://yewtu.be/vi/7DKv5H5Frt0/mqdefault.jpg\" width=\"320\" height=\"180\" />\r\r\n" +
			"      <media:description>Description</media:description>\r\r\n" +
			"    </media:group>\r\r\n" +
			"    <media:community xmlns:media=\"http://search.yahoo.com/mrss/\">\r\r\n" +
			"      <media:statistics views=\"9792294\" />\r\r\n" +
			"    </media:community>\r\r\n" +
			"    <dc:creator>Example</dc:creator>\r\r\n" +
			"    <dc:date>2024-01-01T16:00:06Z</dc:date>\r\r\n" +
			"  </entry>\r\r\n" +
			"</feed>\r\r\n";
	private static final String WRITTEN_TO_FILE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
			"<feed xmlns=\"http://www.w3.org/2005/Atom\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\r\n" +
			"  <title>Example</title>\r\n" +
			"  <link rel=\"alternate\" href=\"https://yewtu.be/channel/UCBa659QWEk1AI4Tg--mrJ2A\" />\r\n" +
			"  <link rel=\"self\" href=\"https://yewtu.be/feed/channel/UCBa659QWEk1AI4Tg--mrJ2A\" />\r\n" +
			"  <author>\r\n" +
			"    <name>Example</name>\r\n" +
			"    <uri>https://yewtu.be/channel/UCBa659QWEk1AI4Tg--mrJ2A</uri>\r\n" +
			"  </author>\r\n" +
			"  <id>yt:channel:UCBa659QWEk1AI4Tg--mrJ2A</id>\r\n" +
			"  <icon>https://yt3.googleusercontent.com/ytc/AIf8zZQZqvzmRs8KizC2KIwe2ISv3jCLWu8LOfW3ZTL2YA=s900-c-k-c0x00ffffff-no-rj</icon>\r\n" +
			"  <yt:channelId xmlns:yt=\"http://www.youtube.com/xml/schemas/2015\">UCBa659QWEk1AI4Tg--mrJ2A</yt:channelId>\r\n" +
			"  <entry>\r\n" +
			"    <title>After ten years, it's time to stop making videos.</title>\r\n" +
			"    <link rel=\"alternate\" href=\"https://yewtu.be/watch?v=7DKv5H5Frt0\" />\r\n" +
			"    <author>\r\n" +
			"      <name>Example</name>\r\n" +
			"      <uri>https://yewtu.be/channel/UCBa659QWEk1AI4Tg--mrJ2A</uri>\r\n" +
			"    </author>\r\n" +
			"    <id>yt:video:7DKv5H5Frt0</id>\r\n" +
			"    <updated>2024-01-01T16:00:06Z</updated>\r\n" +
			"    <published>2024-01-01T16:00:06Z</published>\r\n" +
			"    <content type=\"xhtml\">\r\n" +
			"      <div xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" +
			"        <a href=\"https://yewtu.be/watch?v=7DKv5H5Frt0\">\r\n" +
			"          <img src=\"https://yewtu.be/vi/7DKv5H5Frt0/mqdefault.jpg\" />\r\n" +
			"        </a>\r\n" +
			"        <p style=\"word-break:break-word;white-space:pre-wrap\">Find me here</p>\r\n" +
			"      </div>\r\n" +
			"    </content>\r\n" +
			"    <yt:videoId xmlns:yt=\"http://www.youtube.com/xml/schemas/2015\">7DKv5H5Frt0</yt:videoId>\r\n" +
			"    <yt:channelId xmlns:yt=\"http://www.youtube.com/xml/schemas/2015\">UCBa659QWEk1AI4Tg--mrJ2A</yt:channelId>\r\n" +
			"    <media:group xmlns:media=\"http://search.yahoo.com/mrss/\">\r\n" +
			"      <media:title>After ten years, it's time to stop making videos.</media:title>\r\n" +
			"      <media:thumbnail url=\"https://yewtu.be/vi/7DKv5H5Frt0/mqdefault.jpg\" width=\"320\" height=\"180\" />\r\n" +
			"      <media:description>Description</media:description>\r\n" +
			"    </media:group>\r\n" +
			"    <media:community xmlns:media=\"http://search.yahoo.com/mrss/\">\r\n" +
			"      <media:statistics views=\"9792294\" />\r\n" +
			"    </media:community>\r\n" +
			"    <dc:creator>Example</dc:creator>\r\n" +
			"    <dc:date>2024-01-01T16:00:06Z</dc:date>\r\n" +
			"  </entry>\r\n" +
			"</feed>\r\n";

	private static final String CHANGED_CONTENT = "<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">\\r\n\" +\r\n" +
			"\t\t\t\"        <a href=\\\"https://yewtu.be/watch?v=3kwDVw0u4Kw\\\">\\r\n\" +\r\n" +
			"\t\t\t\"          <img src=\\\"https://example.com\\\"/>\\r\n\" +\r\n" +
			"\t\t\t\"        </a>\\r\n\" +\r\n" +
			"\t\t\t\"     </div>";

	HandleRssFeed handleRssFeed = new HandleRssFeed();
	String invidiousUrl = "https://yewtu.be/feed/channel/UCBa659QWEk1AI4Tg--mrJ2A";
	String youTubeUrl = "https://www.youtube.com/feeds/videos.xml?channel_id=UCBa659QWEk1AI4Tg--mrJ2A";
	private SyndFeed syndFeed;
	private SyndEntry feedEntry;


	@BeforeEach
	public void init() throws FeedException, IOException {
		syndFeed = handleRssFeed.readFeed(new StringReader(TEST_FEED));
		feedEntry = syndFeed.getEntries().getFirst();
	}

	@Test
	void readFeed() {
		assertThrows(IllegalArgumentException.class, () -> {
			handleRssFeed.readFeed("");
		});
		assertThrows(IllegalArgumentException.class, () -> {
			handleRssFeed.readFeed("https://example.com");
		});
		assertDoesNotThrow(() -> {
			handleRssFeed.readFeed(invidiousUrl);
		});
		assertDoesNotThrow(() -> {
			handleRssFeed.readFeed(youTubeUrl);
		});
	}

	@Test
	void editEntry() {
		handleRssFeed.editEntry(feedEntry, "newTitle", "https://example.com");
		assertEquals("newTitle", feedEntry.getTitle());
		assertEquals("https://example.com", handleRssFeed.getMediaThumbnailUrl(feedEntry));
	}

	@Test
	void writeFeedToFile() throws FeedException, IOException {
		handleRssFeed.writeFeedToFile(syndFeed);
		assertEquals(WRITTEN_TO_FILE, Files.readString(Path.of(syndFeed.getTitle() + "'s DeArrowedYouTubeFeed.xml")));
	}

	@Test
	void readFeedFromFile() throws FeedException, IOException {
		// redundant?
		// still has problem
		assertEquals(syndFeed, handleRssFeed.readFeedFromFile("Example's DeArrowedYouTubeFeed.xml"));
	}

	@Test
	void getMediaThumbnailUrl() throws FeedException, IOException {
		SyndFeed feed = handleRssFeed.readFeed(invidiousUrl);
		for (SyndEntry entry : feed.getEntries()) {
			handleRssFeed.setMediaThumbnailUrl(entry, "PLACEHOLDERPLACEHOLDERPLACEHOLDER");
		}
		handleRssFeed.writeFeedToFile(feed);
	}

	@Test
	void setMediaThumbnailUrl() {
	}

	public File checkFileExists(String url) {
		SyndFeed syndFeed = handleRssFeed.readFeed(url);
		String fileName = syndFeed.getTitle() + "'s DeArrowedYouTubeFeed.xml";
		return new File(fileName);
	}

	@Test
	void createFeed() throws FeedException, IOException {
		String url = "https://www.youtube.com/feeds/videos.xml?channel_id=UCBa659QWEk1AI4Tg--mrJ2A";
		File file = checkFileExists(url);
		assertTrue(file.exists());
		handleRssFeed.readFeedFromFile(file.getName());

		url = "https://yewtu.be/feed/channel/UCBa659QWEk1AI4Tg--mrJ2A";
		file = checkFileExists(url);
		assertTrue(file.exists());
		handleRssFeed.readFeedFromFile(file.getName());

		url = "https://example.com";
		file = checkFileExists(url);
		assertFalse(file.exists());
		handleRssFeed.readFeedFromFile(file.getName());
	}
}