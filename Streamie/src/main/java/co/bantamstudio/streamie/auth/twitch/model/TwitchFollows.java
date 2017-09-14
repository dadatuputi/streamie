package co.bantamstudio.streamie.auth.twitch.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchFollows {
	@JsonProperty @Getter private int _total;
	@JsonProperty @Getter private List<TwitchStream> follows;
	@JsonProperty @Getter private TwitchLinks _links;
}
