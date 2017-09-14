package co.bantamstudio.streamie.auth.twitch.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import co.bantamstudio.streamie.auth.model.Profile;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchProfile implements Profile {
	@JsonProperty
    private String name;
	@JsonProperty String created_at;
	@JsonProperty String updated_at;
	@JsonProperty
    private TwitchLinks _links;
	@JsonProperty
    private String logo;
	@JsonProperty
    private long _id;
	@JsonProperty
    private String display_name;
	@JsonProperty
    private String email;
	@JsonProperty boolean partnered;
	@JsonProperty boolean staff;
	
	@JsonIgnore
	@Override
	public String getUsername() {
		return name;
	}
	
	@JsonIgnore
	@Override
	public String getName() {
		return display_name;
	}

	@JsonIgnore
	@Override
	public String getPicture() {
		return logo;
	}

	@JsonIgnore
	@Override
	public long getId() {
		return _id;
	}

	@JsonIgnore
	@Override
	public String getProfileUrl() {
		return _links.getSelf();
	}

	@JsonIgnore
	@Override
	public String getPrimaryTagline() {
		return getName();
	}

	@JsonIgnore
	@Override
	public String getSecondaryTagline() {
		if (!getName().equalsIgnoreCase(getUsername())) {
			return getUsername();
		} else {
			return email;
		}
			
	}
}
