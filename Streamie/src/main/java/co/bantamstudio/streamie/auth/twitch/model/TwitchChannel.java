package co.bantamstudio.streamie.auth.twitch.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import co.bantamstudio.streamie.auth.model.Channel;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchChannel implements Channel {
	@JsonProperty @Getter private String created_at;
	@JsonProperty @Getter private String url;
	@JsonProperty @Getter private String updated_at;
	@JsonProperty @Getter private String status;
	@JsonProperty @Getter private boolean mature;
	@JsonProperty @Getter private long _id;
	@JsonProperty @Getter private String display_name;
	@JsonProperty @Getter private String logo;
	@JsonProperty @Getter private String background;
	@JsonProperty @Getter private String video_banner;
	@JsonProperty @Getter private String banner;
	@JsonProperty @Getter private List<TwitchTeam> teams;
	@JsonProperty @Getter private String name;
	@JsonProperty @Getter private TwitchLinks _links;
	@JsonProperty @Getter private String game;
	@JsonProperty @Getter private String error;
	@JsonProperty @Getter @Setter private String embed;
	
	@JsonIgnore
	@Override
	public String getUsername() {
		return name;
	}
}