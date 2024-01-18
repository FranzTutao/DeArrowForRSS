package de.mueller.franz;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * helper class to pass Json and use in java with gson
 */
public class Response {
	@SerializedName("titles")
	private List<Title> titles;

	@SerializedName("thumbnails")
	private List<Thumbnail> thumbnails;

	@SerializedName("randomTime")
	private double randomTime;

	public List<Title> getTitles() {
		return titles;
	}

	public void setTitles(List<Title> titles) {
		this.titles = titles;
	}

	public List<Thumbnail> getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(List<Thumbnail> thumbnails) {
		this.thumbnails = thumbnails;
	}

	public double getRandomTime() {
		return randomTime;
	}

	public void setRandomTime(double randomTime) {
		this.randomTime = randomTime;
	}

	public Double getVideoDuration() {
		return videoDuration;
	}

	public void setVideoDuration(Double videoDuration) {
		this.videoDuration = videoDuration;
	}

	@SerializedName("videoDuration")
	private Double videoDuration;


	public static class Title {
		private String title;
		private boolean original;
		private int votes;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public boolean isOriginal() {
			return original;
		}

		public void setOriginal(boolean original) {
			this.original = original;
		}

		public int getVotes() {
			return votes;
		}

		public void setVotes(int votes) {
			this.votes = votes;
		}

		public boolean isLocked() {
			return locked;
		}

		public void setLocked(boolean locked) {
			this.locked = locked;
		}

		public String getUUID() {
			return UUID;
		}

		public void setUUID(String UUID) {
			this.UUID = UUID;
		}

		private boolean locked;
		private String UUID;
	}


	public static class Thumbnail {
		public Double getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(Double timestamp) {
			this.timestamp = timestamp;
		}

		public boolean isOriginal() {
			return original;
		}

		public void setOriginal(boolean original) {
			this.original = original;
		}

		public int getVotes() {
			return votes;
		}

		public void setVotes(int votes) {
			this.votes = votes;
		}

		public boolean isLocked() {
			return locked;
		}

		public void setLocked(boolean locked) {
			this.locked = locked;
		}

		public String getUUID() {
			return UUID;
		}

		public void setUUID(String UUID) {
			this.UUID = UUID;
		}

		private Double timestamp;
		private boolean original;
		private int votes;
		private boolean locked;
		private String UUID;
	}
}
