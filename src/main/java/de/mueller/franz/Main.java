package de.mueller.franz;

import com.rometools.rome.io.FeedException;
import spark.Spark;

import java.io.IOException;
import java.net.URISyntaxException;

import static spark.Spark.get;
import static spark.Spark.internalServerError;

public class Main {
	public static void main(String[] args) throws IOException, FeedException, URISyntaxException {
		// DeArrow: https://wiki.sponsor.ajay.app/w/API_Docs/DeArrow
		// ROME: https://rometools.github.io/rome/
		// Spark: https://sparkjava.com/

		// ToDo make dynamic and exposed to web; speed up image request; write tests
		HandleRssFeed handleRssFeed = new HandleRssFeed();
		// Tom Scott
		// String rssFeedURL = "https://www.youtube.com/feeds/videos.xml?channel_id=UCBa659QWEk1AI4Tg--mrJ2A";
		String rssFeedURL = "https://yewtu.be/feed/channel/UCBa659QWEk1AI4Tg--mrJ2A";
		// Franz3
		// String rssFeedURL = "https://yewtu.be/feed/channel/UCBa659QWEk1AI4Tg--mrJ2A";

		// secure("deploy/keystore.jks", "password", null, null);

		Spark.webSocketIdleTimeoutMillis((60 * 1000) * 5);
		// initialize web server
		get("/DeArrow/*", (request, response) -> {

			String url = request.splat()[0];
			url = url.replaceFirst("/", "//");
			response.type("text/xml");
			return handleRssFeed.createModifiedFeed(url);
			// return url;
		});
		internalServerError((request, response) -> {
			response.type("text/html");
			return "<html><body><img src=\"https://http.cat/500\" alt=\"https://http.cat/500\" width=\"747\" height=\"598\"></body></html>";
		});
	}
}