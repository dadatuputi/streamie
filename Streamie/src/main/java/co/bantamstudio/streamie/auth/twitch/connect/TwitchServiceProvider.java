package co.bantamstudio.streamie.auth.twitch.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

import co.bantamstudio.streamie.auth.twitch.api.Twitch;
import co.bantamstudio.streamie.auth.twitch.api.impl.TwitchTemplate;

class TwitchServiceProvider extends
		AbstractOAuth2ServiceProvider<Twitch> {

	public TwitchServiceProvider(String clientId, String clientSecret) {
		super(new OAuth2Template(clientId,
				clientSecret,
				"https://api.twitch.tv/kraken/oauth2/authorize",
				"https://api.twitch.tv/kraken/oauth2/token"));
	}

	@Override
	public Twitch getApi(String accessToken) {
		return new TwitchTemplate(accessToken);
	}
}