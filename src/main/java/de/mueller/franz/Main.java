package de.mueller.franz;

import spark.Spark;

import static spark.Spark.*;

public class Main {
	// TODO main -> Server
	public static void main(String[] args) {
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

		Spark.webSocketIdleTimeoutMillis((60 * 1000) * 5);
		// initialize web server for http://localhost:4567/DeArrow/
		get("/DeArrow/*", (request, response) -> {
			// get the inputted YouTube/ Invidious url
			String feedUrl = request.raw().getRequestURL().toString();
			String queryString = request.raw().getQueryString();
			if (queryString != null) {
				feedUrl += "?" + queryString;
			}
			feedUrl = feedUrl.replace("http://localhost:4567/DeArrow/", "");
			feedUrl = feedUrl.replace("http://localhost:4567/dearrow/", "");
			// respond
			response.type("text/xml");
			return handleRssFeed.createModifiedFeed(feedUrl);

		});
		// handle internal error (500)
		internalServerError((request, response) -> {
			response.type("text/html");
			return "<html>" +
					"<body>" +
					"<img src=\"https://http.cat/500\" alt=\"https://http.cat/500\" width=\"747\" height=\"598\">" +
					"</body>" +
					"</html>";
		});
		// handle wrong address (404)
		notFound((request, response) -> {
			response.type("text/html");
			return "<html>" +
					"<body>" +
					"<img src=\"https://http.cat/404\" alt=\"https://http.cat/404\" width=\"747\" height=\"598\">" +
					"</body>" +
					"</html>";
		});
		// handle IllegalArgumentException (thrown when url is wrong)
		get("/wrong_url", (request, response) -> {
			response.type("text/html");
			return "Please provide a valid feed url";
		});
		exception(IllegalArgumentException.class, (exception, request, response) -> {
			response.redirect("/wrong_url");
		});
	}
}