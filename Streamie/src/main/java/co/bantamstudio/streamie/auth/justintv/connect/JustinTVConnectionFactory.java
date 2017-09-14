package co.bantamstudio.streamie.auth.justintv.connect;

import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import co.bantamstudio.streamie.auth.justintv.api.JustinTV;

public class JustinTVConnectionFactory extends OAuth1ConnectionFactory<JustinTV> {

	public JustinTVConnectionFactory(String clientId, String clientSecret) {
		super("justintv",  new JustinTVServiceProvider(clientId, clientSecret), new JustinTVAdapter());
	}
}