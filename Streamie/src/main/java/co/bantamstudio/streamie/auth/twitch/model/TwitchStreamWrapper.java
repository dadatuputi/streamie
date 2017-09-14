package co.bantamstudio.streamie.auth.twitch.model;

import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Getter;

public class TwitchStreamWrapper {
	@JsonProperty @Getter private TwitchLinks _links;
	@JsonProperty @Getter private TwitchStream stream;
}
