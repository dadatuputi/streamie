package co.bantamstudio.streamie.auth.twitch.model;

import lombok.Getter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchGame {
    public static final String ALL_TWITCH_STREAMS = "All Twitch Games";
	@JsonProperty @Getter private long _id;
	@JsonProperty @Getter private long giantbomb_id;
	@JsonProperty @Getter private String name;
	@JsonProperty @Getter private GameBoxArt box;
	@JsonProperty @Getter private GameLogoArt logo;

	public TwitchGame(String game) {
		this.name = game;
	}
	
	public TwitchGame() {}


	@Override
	public String toString() {
		return name;
	}
}