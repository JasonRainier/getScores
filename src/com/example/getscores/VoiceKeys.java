package com.example.getscores;

import java.util.HashMap;

public class VoiceKeys {
	public static HashMap<String, String> SportKeys = new HashMap<String, String>();
	public static HashMap<String, String> SoccerLeagueKeys = new HashMap<String, String>();
	static {
		
		SportKeys.put("NFL", "http://scores.espn.go.com/nfl/scoreboard");
		SportKeys.put("Football", "http://scores.espn.go.com/nfl/scoreboard");
		
		SportKeys.put("MLB", "http://scores.espn.go.com/mlb/scoreboard");
		SportKeys.put("Baseball", "http://scores.espn.go.com/mlb/scoreboard");

		SportKeys.put("NBA", "http://scores.espn.go.com/nba/scoreboard");
		SportKeys.put("Basketball", "http://scores.espn.go.com/nba/scoreboard");

		SportKeys.put("NHL", "http://scores.espn.go.com/nhl/scoreboard");
		SportKeys.put("Hockey", "http://scores.espn.go.com/nhl/scoreboard");

		SportKeys.put("College Football", "http://scores.espn.go.com/college-football/scoreboard");
		SportKeys.put("College Basketball", "http://scores.espn.go.com/ncb/scoreboard");
		
		//SportKeys.put("Soccer", "blank");
		
		SoccerLeagueKeys.put("Premier League", "http://www.espnfc.com/scores/_/league/eng.1?cc=5901");
		SoccerLeagueKeys.put("MLS", "http://www.espnfc.com/scores/_/league/eng.1/barclays-premier-league?cc=5901");
		SoccerLeagueKeys.put("La Liga", "http://www.espnfc.com/scores/_/league/esp.1/spanish-la-liga?cc=5901");
		SoccerLeagueKeys.put("Serie A", "http://www.espnfc.com/scores/_/league/ita.1/date/20140428?cc=5901");
		SoccerLeagueKeys.put("Bundesliga", "http://www.espnfc.com/scores/_/league/ger.1/german-bundesliga?cc=5901");
		SoccerLeagueKeys.put("Champions League", "http://www.espnfc.com/scores/_/league/uefa.champions?cc=5901");
		SoccerLeagueKeys.put("World Cup", "http://www.espnfc.com/scores/_/league/fifa.world?cc=5901");
		
	}

}
