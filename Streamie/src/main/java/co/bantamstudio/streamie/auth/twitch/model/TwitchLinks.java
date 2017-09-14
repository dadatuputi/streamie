package co.bantamstudio.streamie.auth.twitch.model;

import lombok.Getter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchLinks {
	@JsonProperty @Getter private String next;
	@JsonProperty @Getter private String self;
	@JsonProperty @Getter private String commercial;
	@JsonProperty @Getter private String videos;
	@JsonProperty @Getter private String chat;
	@JsonProperty @Getter private String stream_key;
	@JsonProperty @Getter private String features; 
	@JsonProperty @Getter private String summary;
	@JsonProperty @Getter private String featured;
	@JsonProperty @Getter private String followed;
	@JsonProperty @Getter private String follows;
	@JsonProperty @Getter private String subscriptions;
	@JsonProperty @Getter private String editors;
}
