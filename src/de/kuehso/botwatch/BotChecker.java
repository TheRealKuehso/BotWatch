package de.kuehso.botwatch;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The main class of the tool.
 * 
 * @author kuehso
 */
public class BotChecker {

	/**
	 * The magic happens in here. <br>
	 * All the viewers are fetch from twitch. <br>
	 * Each "viewer" is checked on twitchinsights.net. <br>
	 * Every "viewer" indentified as a bot is added to the search result.
	 * 
	 * @param channelName The name of the channel to check for bots.
	 * @return A {@link SearchResult} containing all bot names.
	 * @throws Exception Or does it?
	 */
	public static SearchResult check(String channelName) throws Exception {
		final List<String> viewerNames = fetchViewers(channelName);
		final List<String> botNames = fetchBotNames();

		final List<String> foundBots = viewerNames.stream().filter((v) -> botNames.contains(v))
				.collect(Collectors.toList());

		return new SearchResult(foundBots);
	}

	/**
	 * Fetches all bots listed on twitchinsights.net
	 * 
	 * @return A list of bot names or - on exception - an empty list.
	 */
	private static List<String> fetchBotNames() {
		try {
			final List<String> botNames = new ArrayList<>();
			final URL url = new URL(Constants.TWITCH_INSIGHTS_URL);
			
			final URLConnection request = url.openConnection();
			request.connect();
			
			final JsonArray bots = parseBotJson(request);
			bots.forEach((b) -> botNames.add(b.getAsJsonArray().get(0).toString().replace("\"", "")));
			
			return botNames;
		} catch (MalformedURLException e) {
			JOptionPane.showOptionDialog(null, "Fehler beim Erstellen der Anfrage an twitchinsights.", "Fehler",
					JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
		} catch (IOException e) {
			JOptionPane.showOptionDialog(null, "Fehler während der Verarbeitung der Antwort von twitchinsights.", "Fehler",
					JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
		}
		return Collections.emptyList();
	}

	/**
	 * Fetches the viewer list for the given channel.
	 * 
	 * @param channelName The channel to fetch the viewers for.
	 * @return The list of viewers or - on exception - an empty list.
	 */
	private static List<String> fetchViewers(String channelName) {
		try {
			final List<String> viewerNames = new ArrayList<>();
			final String viewListUrl = MessageFormat.format(Constants.VIEWER_LIST_URL_TEMPLATE,
					channelName.trim().toLowerCase());
			final URL viewerUrl = new URL(viewListUrl);

			final URLConnection viewerRequest = viewerUrl.openConnection();
			viewerRequest.connect();

			final JsonArray viewers = parseViewerJson(viewerRequest);
			viewers.forEach((v) -> viewerNames.add(v.getAsString()));

			return viewerNames;
		} catch (MalformedURLException e) {
			JOptionPane.showOptionDialog(null, "Fehler beim Erstellen der Anfrage an Twitch.", "Fehler",
					JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
		} catch (IOException e) {
			JOptionPane.showOptionDialog(null, "Fehler während der Verarbeitung der Antwort von Twitch.", "Fehler",
					JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
		}
		return Collections.emptyList();
	}

	/**
	 * Parses the response of the given request.
	 * 
	 * @param viewerRequest The request to parse.
	 * @return A {@link JsonArray} of viewer names.
	 * 
	 * @throws IOException When something goes wrong while parsing.
	 */
	private static JsonArray parseViewerJson(final URLConnection viewerRequest) throws IOException {
		final JsonElement viewerRoot = JsonParser
				.parseReader(new InputStreamReader((InputStream) viewerRequest.getContent()));
		final JsonObject viewerObject = viewerRoot.getAsJsonObject();
		final JsonObject chatterListObject = viewerObject.getAsJsonObject(Constants.JSON_ELEMENT_CHATTERS);
		final JsonArray viewers = chatterListObject.getAsJsonArray(Constants.JSON_ELEMENT_VIEWERS);
		return viewers;
	}
	
	/**
	 * Parses the response of the given request.
	 * 
	 * @param viewerRequest The request to parse.
	 * @return A {@link JsonArray} of bot names.
	 * 
	 * @throws IOException When something goes wrong while parsing.
	 */
	private static JsonArray parseBotJson(final URLConnection request) throws IOException {
		final JsonElement rootElement = JsonParser
				.parseReader(new InputStreamReader((InputStream) request.getContent()));
		final JsonObject rootobj = rootElement.getAsJsonObject();
		final JsonArray bots = rootobj.getAsJsonArray(Constants.JSON_ELEMENT_BOTS);
		return bots;
	}

}
