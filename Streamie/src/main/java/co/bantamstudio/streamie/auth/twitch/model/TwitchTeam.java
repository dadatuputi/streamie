package co.bantamstudio.streamie.auth.twitch.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchTeam {
	@JsonProperty @Getter private String created_at;
	@JsonProperty @Getter private String info;
	@JsonProperty @Getter private String updated_at;
	@JsonProperty @Getter private long _id;
	@JsonProperty @Getter private String display_name;
	@JsonProperty @Getter private String background;
	@JsonProperty @Getter private String logo;
	@JsonProperty @Getter private String banner;
	@JsonProperty @Getter private String name;
	@JsonProperty @Getter private TwitchLinks _links;
}