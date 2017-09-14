package co.bantamstudio.streamie.auth.twitch.model;

import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchStreamMeta implements Comparable<Object> {
	@JsonProperty @Getter private String node;
	@JsonProperty @Getter private String needed_info;
	@JsonProperty @Getter private String play;
	@JsonProperty @Getter private String meta_game;
	@JsonProperty @Getter private int video_height;
	@JsonProperty @Getter private float bitrate;
	@JsonProperty @Getter private int broadcast_part;
	@JsonProperty @Getter private int rank;
	@JsonProperty @Getter private boolean persistent;
	@JsonProperty @Getter private String cluster;
	@JsonProperty @Getter @Setter private String token;
	@Getter @Setter String rtmpPath;
	@Getter @Setter String swfUrl;
	@Getter @Setter String playPath;
	@JsonProperty @Getter private String connect;
	@JsonProperty @Getter private long broadcast_id;
	@JsonProperty @Getter private String type;
	@JsonProperty @Getter private String display;
	@JsonProperty @Getter private String find_type;
	
	@Override
	public int compareTo(Object arg0) {
		TwitchStreamMeta alien;
		try {
			alien = (TwitchStreamMeta) arg0;
			return (alien.getBitrate() > getBitrate())? -1 : (alien.getBitrate() == getBitrate())? 0 : 1;
		} catch (Exception e) {
			return 1;
		}
		
	}

}
