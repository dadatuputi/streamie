package co.bantamstudio.streamie.auth.twitch.api.impl;

import org.springframework.social.MissingAuthorizationException;

abstract class AbstractTwitchOperations {
	
	private final boolean isAuthorized;
	
	AbstractTwitchOperations(boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}

	void requireAuthorization() {
		if (!isAuthorized) {
			throw new MissingAuthorizationException();
		}
	}
}
