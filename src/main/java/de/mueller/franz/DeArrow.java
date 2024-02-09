package de.mueller.franz;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * class responsible for all things with the DeArrow api and data formatting
 */
public class DeArrow {

	/**
	 * get DeArrow information
	 *
	 * @param videoID id of the individual video
	 * @return response as String
	 * @throws IOException
	 */
	public String getInitialInformation(String videoID) throws IOException {
		String apiUrl = "https://sponsor.ajay.app/api/branding";

		// Encode the videoID parameter
		String encodedVideoID = URLEncoder.encode(videoID, StandardCharsets.UTF_8);

		// Construct the URL with query parameters
		String queryParameters = String.format("?videoID=%s", encodedVideoID);
		String completeUrl = apiUrl + queryParameters;

		// connect to api and return response if successful
		HttpURLConnection con = initializeConnection(completeUrl);
		if (con == null) return null;

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
			StringBuilder response = new StringBuilder();
			String responseLine;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			return response.toString();
		}
	}

	/**
	 * helper class representing the extracted information
	 */
	public static class ProcessedInformation {
		private final String title;
		private final String url;

		public String getTitle() {
			return title;
		}

		public String getUrl() {
			return url;
		}

		public ProcessedInformation(String title, String url) {
			this.title = title;
			this.url = url;
		}
	}

	/**
	 * post process information from DeArrow api
	 *
	 * @param jsonResponse api response from DeArrow
	 * @return title and url as ProcessedInformation (null if not existing)
	 */
	public ProcessedInformation processInformation(String videoID, String jsonResponse) throws IOException {
		// convert json
		Gson gson = new Gson();
		Response response = gson.fromJson(jsonResponse, Response.class);
		String title = "";
		String url = "";
		if (response == null) return new ProcessedInformation(null, null);
		if (response.getTitles().isEmpty() || response.getTitles() == null) {
			title = null;
		}
		if (response.getThumbnails().isEmpty() || response.getThumbnails() == null) {
			url = null;
		}
		if (title != null) {
			List<Response.Title> titles = response.getTitles();
			// check if title is valid
			Response.Title firstTitle = titles.getFirst();
			if (firstTitle.isLocked() || firstTitle.getVotes() >= 0) {
				// title is good
				title = firstTitle.getTitle();
			} else {
				title = null;
			}
		}
		if (url != null) {
			List<Response.Thumbnail> thumbnails = response.getThumbnails();

			// find valid thumbnail
			Response.Thumbnail firstThumbnail = thumbnails.getFirst();

			if (firstThumbnail.isLocked() || firstThumbnail.getVotes() >= 0) {
				// thumbnail is good
				// check for timestamp
				if (firstThumbnail.getTimestamp() != null) {
					url = getImageInformation(videoID, firstThumbnail.getTimestamp());
					// check for video duration
				} else if (response.getVideoDuration() != null) {
					double number = response.getVideoDuration() * response.getRandomTime();
					url = getImageInformation(videoID, number);
				} else {
					// InnerTube failed us
					url = null;
				}
			} else {
				url = null;
			}
		}
		return new ProcessedInformation(title, url);
	}

	/**
	 * fetches thumbnail image
	 *
	 * @param videoID id of video in question
	 * @param number  Thumbnail timestamp (DeArrow internal number)
	 * @return image url or null
	 * @throws IOException
	 */
	public String getImageInformation(String videoID, double number) throws IOException {
		// String apiUrl = "https://dearrow-thumb.minibomba.pro/api/v1/getThumbnail";
		String apiUrl = "https://dearrow-thumb.ajay.app/api/v1/getThumbnail";

		// Encode the videoID parameter
		String encodedVideoID = URLEncoder.encode(videoID, StandardCharsets.UTF_8);

		// Construct the URL with query parameters
		String queryParameters = String.format("?videoID=%s&time=%s", encodedVideoID, number);
		String completeUrl = apiUrl + queryParameters;
		// get api response and return complete url if successful
		HttpURLConnection con = initializeConnection(completeUrl);
		if (con == null) return null;
		else return completeUrl;
	}

	/**
	 * create GET connection to provided url with json as request/ response
	 *
	 * @param completeUrl url you want to connect to (please encode UTF-8)
	 * @return connection ready to go or null if connection is not ok
	 * @throws IOException
	 */
	private HttpURLConnection initializeConnection(String completeUrl) throws IOException {
		URL url = new URL(completeUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		// handle everything that's not 200
		if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
			return null;
		}
		return con;
	}
}
