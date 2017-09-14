package co.bantamstudio.streamie.auth.twitch.model;

import lombok.Getter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchPreview {
	@JsonProperty @Getter private String template;
	@JsonProperty @Getter private String medium;
	@JsonProperty @Getter private String large;
	@JsonProperty @Getter private String small;
}
