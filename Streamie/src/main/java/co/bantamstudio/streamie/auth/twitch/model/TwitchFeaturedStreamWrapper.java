package co.bantamstudio.streamie.auth.twitch.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
class TwitchFeaturedStreamWrapper {
	@JsonProperty @Getter private String image;
	@JsonProperty @Getter private TwitchStream stream;
	@JsonProperty @Getter private String text;
}
