package de.kuehso.botwatch;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class BotChecker {
	
	private final static String URL = "https://api.twitchinsights.net/v1/bots/all";
	private final static String VIEWER_URL_TEMPLATE = "http://tmi.twitch.tv/group/user/{0}/chatters";
	
	public static List<String> check(String channelName) throws Exception {
		final List<String> foundBotNames = new ArrayList<>();
		final List<String> viewerNames = new ArrayList<>();
		
		final URL viewerUrl = new URL(MessageFormat.format(VIEWER_URL_TEMPLATE, channelName));
		final URLConnection viewerRequest = viewerUrl.openConnection();
		viewerRequest.connect();
		final JsonParser viewerParser = new JsonParser();
		final JsonElement viewerRoot = viewerParser.parse(new InputStreamReader((InputStream) viewerRequest.getContent()));
		final JsonObject viewerObject = viewerRoot.getAsJsonObject();
		final JsonObject chatterListObject = viewerObject.getAsJsonObject("chatters");
		final JsonArray viewers = chatterListObject.getAsJsonArray("viewers");

		for (int i = 0; i < viewers.size(); i++) {
			final JsonElement viewer = viewers.get(i);
			viewerNames.add(viewer.getAsString());
		}
		
		final URL url = new URL(URL);
		final URLConnection request = url.openConnection();
		request.connect();
		final JsonParser jp = new JsonParser();
		final JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
		final JsonObject rootobj = root.getAsJsonObject();
		final JsonArray bots = rootobj.getAsJsonArray("bots");
		final List<String> botNames = new ArrayList<>();
		for (int i = 0; i < bots.size(); i++) {
			final JsonElement bot = bots.get(i);
			botNames.add(bot.getAsJsonArray().get(0).toString().replace("\"", ""));
		}
		botNames.forEach((b) -> {
			if (viewerNames.contains(b)) {
				foundBotNames.add(b);
			}
		});
		return foundBotNames;
	}

}
