package co.bantamstudio.streamie.auth.twitch.connect;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import co.bantamstudio.streamie.auth.twitch.api.Twitch;

public class TwitchConnectionFactory extends OAuth2ConnectionFactory<Twitch> {

	public TwitchConnectionFactory(String clientId, String clientSecret) {
		super("twitch",  new TwitchServiceProvider(clientId, clientSecret), new TwitchAdapter());
	}
}