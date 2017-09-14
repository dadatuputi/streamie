package co.bantamstudio.streamie.auth.justintv.connect;

import org.springframework.social.ApiException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UserProfileBuilder;

import co.bantamstudio.streamie.auth.justintv.api.JustinTV;
import co.bantamstudio.streamie.auth.justintv.model.JustinTVProfile;

class JustinTVAdapter implements ApiAdapter<JustinTV> {

	@Override
	public boolean test(JustinTV justin) {
		try {
			justin.userOperations().getUserProfile();
			return true;
		} catch (ApiException e) {
			return false;
		}
	}

	@Override
	public void setConnectionValues(JustinTV justin, ConnectionValues values) {
		JustinTVProfile profile = justin.userOperations().getUserProfile();
		values.setProviderUserId(Long.toString(profile.getId()));
		values.setDisplayName(profile.getName());
		values.setProfileUrl(profile.getProfileUrl());
		values.setImageUrl(profile.getPicture());
	}

	@Override
	public UserProfile fetchUserProfile(JustinTV justin) {
		JustinTVProfile profile = justin.userOperations().getUserProfile();
		return new UserProfileBuilder().setName(profile.getName()).setUsername(profile.getUsername()).build();
	}

	@Override
	public void updateStatus(JustinTV justin, String message) {
		justin.channelOperations().updateStatus(message);
	}
}
