package de.mueller.franz;

public class Main {
	public static void main(String[] args) {
		Server server = new Server();
		// Test URLs:
		// Tom Scott:
		// https://www.youtube.com/feeds/videos.xml?channel_id=UCBa659QWEk1AI4Tg--mrJ2A
		// https://yewtu.be/feed/channel/UCBa659QWEk1AI4Tg--mrJ2A
		// Franz3:
		// https://yewtu.be/feed/channel/UCBa659QWEk1AI4Tg--mrJ2A

		// start server
		server.startWebserver();
	}
}