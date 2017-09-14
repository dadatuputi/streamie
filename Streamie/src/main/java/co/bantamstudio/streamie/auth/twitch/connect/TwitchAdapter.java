package co.bantamstudio.streamie.auth.twitch.connect;

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;

import co.bantamstudio.streamie.auth.twitch.api.Twitch;
import co.bantamstudio.streamie.auth.twitch.model.TwitchProfile;

class TwitchAdapter implements ApiAdapter<Twitch> {

	@Override
	public boolean test(Twitch twitch) {
		try {
			twitch.userOperations().getUserProfile();
			return true;
		} catch (ApiException e) {
			return false;
		}
	}

	@Override
	public void setConnectionValues(Twitch twitch, ConnectionValues values) {
		TwitchProfile profile = twitch.userOperations().getUserProfile();
		values.setProviderUserId(Long.toString(profile.getId()));
		values.setDisplayName(profile.getName());
		values.setProfileUrl(profile.getProfileUrl());
		values.setImageUrl(profile.getPicture());
	}

	@Override
	public UserProfile fetchUserProfile(Twitch twitch) {
		TwitchProfile profile = twitch.userOperations().getUserProfile();
		return new UserProfileBuilder().setName(profile.getName()).setUsername(profile.getName()).build();
	}

	@Override
	public void updateStatus(Twitch twitch, String message) {
		updateStatus(twitch, message, null);
	}
	
	void updateStatus(Twitch twitch, String message, String game) {
		twitch.channelOperations().updateStatus(message, game);
	}

}
