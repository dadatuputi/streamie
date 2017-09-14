package co.bantamstudio.streamie.auth.justintv.api.impl;

import org.springframework.social.MissingAuthorizationException;

abstract class AbstractJustinTVOperations {
	
	private final boolean isAuthorized;
	
	AbstractJustinTVOperations(boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}

	void requireAuthorization() {
		if (!isAuthorized) {
			throw new MissingAuthorizationException();
		}
	}
}
