package de.mueller.franz;

import spark.Spark;

import static spark.Spark.*;

public class Server {
	HandleRssFeed handleRssFeed = new HandleRssFeed();

	public void startWebserver() {
		// set timeout to 5 minutes (if DeArrow servers have a bad day again :P)
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
		exception(IllegalArgumentException.class, (exception, request, response) ->
				response.redirect("/wrong_url")
		);
	}

	public void stopWebserver() {
		stop();
	}
}
