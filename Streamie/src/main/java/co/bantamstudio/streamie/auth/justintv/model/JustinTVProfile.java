package co.bantamstudio.streamie.auth.justintv.model;

import lombok.Getter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import co.bantamstudio.streamie.auth.model.Profile;
import org.springframework.util.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JustinTVProfile implements Profile{
	@JsonProperty private boolean broadcaster;
	@JsonProperty private long id;
	@JsonProperty private String login;
	@JsonProperty @Getter private String name;
	@JsonProperty private String profile_about;
	@JsonProperty private String favorite_quotes;
	@JsonProperty private String location;
	@JsonProperty private String sex;
	@JsonProperty private String profile_background_color;
	@JsonProperty private String profile_link_color;
	@JsonProperty private String profile_header_text_color;
	@JsonProperty private String profile_header_bg_color;
	@JsonProperty private String profile_header_border_color;
	@JsonProperty private String profile_url;
	@JsonProperty private String image_url_huge;
	@JsonProperty private String image_url_large;
	@JsonProperty private String image_url_medium;
	@JsonProperty private String image_url_small;
	@JsonProperty private String image_url_tiny;
	
	@Override
	public String getUsername() {
		return login;
	}

	@Override
	public String getPicture() {
		return image_url_small;
	}

	@Override
	public String getProfileUrl() {
		return profile_url;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getPrimaryTagline() {
		if (StringUtils.hasText(getName()))
			return getName();
		else
			return getUsername();
	}

	@Override
	public String getSecondaryTagline() {
		if (StringUtils.hasText(location))
			return location;
		else if (StringUtils.hasText(getName()))
			return getUsername();
		else {
			return null;
		}
	}
}
