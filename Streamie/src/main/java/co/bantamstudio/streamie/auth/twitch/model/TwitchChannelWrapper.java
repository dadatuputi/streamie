package co.bantamstudio.streamie.auth.twitch.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchChannelWrapper {
	@JsonProperty @Getter private TwitchLinks _links;
	@JsonProperty @Getter private TwitchChannel channel;
}
