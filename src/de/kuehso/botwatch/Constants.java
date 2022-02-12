package de.kuehso.botwatch;

/**
 * Containing all the constants, e.g. urls.
 * 
 * @since 11.02.22 
 * @author kuehso
 */
public class Constants {
	
	// Connection constants
	final static String TWITCH_INSIGHTS_URL = "https://api.twitchinsights.net/v1/bots/all";
	final static String VIEWER_LIST_URL_TEMPLATE = "http://tmi.twitch.tv/group/user/{0}/chatters";
	
	// Parsing constants
	final static String JSON_ELEMENT_CHATTERS = "chatters";
	final static String JSON_ELEMENT_VIEWERS = "viewers";
	final static String JSON_ELEMENT_BOTS = "bots";
	
	private Constants() {
		// No instance needed. Like ever.
	}
	
}
