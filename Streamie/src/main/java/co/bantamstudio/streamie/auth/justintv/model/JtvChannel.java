package co.bantamstudio.streamie.auth.justintv.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import co.bantamstudio.streamie.auth.model.Channel;


import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JtvChannel implements Channel {
	
	@JsonProperty @Getter private String language;
	@JsonProperty @Getter private String title;
	@JsonProperty @Getter private String subcategory;
	@JsonProperty @Getter private String category;
	@JsonProperty @Getter private boolean embed_enabled;
	@JsonProperty @Getter private String status;
	@JsonProperty @Getter private boolean mature;
	@JsonProperty @Getter private String timezone;
	@JsonProperty @Getter private int views_count;
	@JsonProperty @Getter private String id;
	@JsonProperty @Getter @Setter private String embed_code;
	@JsonProperty @Getter private String channel_url;
	@JsonProperty @Getter private String category_title;
	@JsonProperty @Getter private String subcategory_title;
	@JsonProperty @Getter private String login;
	@JsonProperty @Getter private boolean producer;
	@JsonProperty @Getter private String image_url_tiny;
	@JsonProperty @Getter private String image_url_small;
	@JsonProperty @Getter private String image_url_medium;
	@JsonProperty @Getter private String image_url_large;
	@JsonProperty @Getter private String image_url_huge;
	@JsonProperty @Getter private String screen_cap_url_small;
	@JsonProperty @Getter private String screen_cap_url_medium;
	@JsonProperty @Getter @Setter private String screen_cap_url_large;
	@JsonProperty @Getter private String screen_cap_url_huge;
	@JsonProperty @Getter private String error;
	@JsonProperty @Getter private String meta_game;

	// AVAILABLE THROUGH JUSTIN.TV CHANNEL/SHOW API
	@Getter private boolean anonymous_chatters_allowed;
	@Getter private String channel_link_color;
	@Getter private String channel_column_color;
	@Getter private String channel_background_color;
	@Getter private String channel_text_color;
	@Getter private String channel_header_image_url;
	@Getter private String description;
	@Getter private String about;
	
	@JsonIgnore
	@Override
	public String getUsername() {
		return login;
	}
}
