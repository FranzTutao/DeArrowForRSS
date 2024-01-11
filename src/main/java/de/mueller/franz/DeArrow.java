package de.mueller.franz;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class DeArrow {
	HandleRssFeed handleRssFeed = new HandleRssFeed();

	/**
	 * get DeArrow information
	 *
	 * @param videoID
	 * @return response
	 * @throws IOException
	 */
	public String getInitialInformation(String videoID) throws IOException {
		String apiUrl = "https://sponsor.ajay.app/api/branding";

		// Encode the videoID parameter
		String encodedVideoID = URLEncoder.encode(videoID, "UTF-8");

		// Construct the URL with query parameters
		String queryParameters = String.format("?videoID=%s", encodedVideoID);
		String completeUrl = apiUrl + queryParameters;

		URL url = new URL(completeUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		// handle everything that's not 200
		if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {
			System.out.println("FUCKED");
			return null;
		}
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(con.getInputStream(), "utf-8"))) {
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
		private String title;
		private String url;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public ProcessedInformation(String title, String url) {
			this.title = title;
			this.url = url;
		}
	}

	/**
	 * post process information from DeArrow api
	 *
	 * @param jsonResponse
	 * @return title and url as ProcessedInformation (null if not existing)
	 */
	public ProcessedInformation processInformation(String videoID, String jsonResponse) throws IOException {
		// convert json
		Gson gson = new Gson();
		Response response = gson.fromJson(jsonResponse, Response.class);
		if (response == null) return new ProcessedInformation(null, null);
		List<Response.Title> titles = response.getTitles();
		List<Response.Thumbnail> thumbnails = response.getThumbnails();
		// check if title is valid
		String title;
		String url;
		Response.Title firstTitle = titles.getFirst();
		if (firstTitle.isLocked() || firstTitle.getVotes() >= 0) {
			// title is good
			title = firstTitle.getTitle();
		} else {
			title = null;
		}
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
				// probably gg wp, could try using InnerTube, return null, fuck off, default to original thumbnail
				url = null;
			}
		} else {
			url = null;
		}
		return new ProcessedInformation(title, url);
	}

	/**
	 * fetches thumbnail image
	 *
	 * @param videoID
	 * @param number
	 * @return image url or null
	 * @throws IOException
	 */
	public String getImageInformation(String videoID, double number) throws IOException {
		// String apiUrl = "https://dearrow-thumb.minibomba.pro/api/v1/getThumbnail";
		String apiUrl = "https://dearrow-thumb.ajay.app/api/v1/getThumbnail";

		// Encode the videoID parameter
		String encodedVideoID = URLEncoder.encode(videoID, "UTF-8");

		// Construct the URL with query parameters
		String queryParameters = String.format("?videoID=%s&time=%s", encodedVideoID, number);
		String completeUrl = apiUrl + queryParameters;

		URL url = new URL(completeUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");

		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(con.getInputStream(), "utf-8"))) {
			StringBuilder response = new StringBuilder();
			String responseLine;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				return completeUrl;
			} else return null;
		}
	}
}
