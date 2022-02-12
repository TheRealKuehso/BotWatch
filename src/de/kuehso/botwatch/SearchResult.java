package de.kuehso.botwatch;

import java.util.ArrayList;
import java.util.List;

/**
 * The result of the search on twitchinsights.net.
 * 
 * @author kuehso
 * @since 11.02.22
 */
public class SearchResult {
	
	private List<String> botNames;
	
	public SearchResult(List<String> botNames) {
		getBotNames().addAll(botNames);
	}
	
	/**
	 * Returns all users identified as bots during the search.
	 * @return A list of "user" names.
	 */
	public List<String> getBotNames() {
		if (botNames == null) {
			botNames = new ArrayList<>();
		}
		return botNames;
	}
	
	@Override
	public String toString() {
		final var sb = new StringBuffer();
		getBotNames().forEach((b) -> sb.append(b + "\r\n"));
		return sb.toString();
	}

}
