package co.bantamstudio.streamie.auth.twitch.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Getter;

public class TwitchStreamArrayWrapper {
	@JsonProperty @Getter private TwitchLinks _links;
	@JsonProperty @Getter private List<TwitchStream> streams;
}