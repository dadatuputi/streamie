package co.bantamstudio.streamie.auth.justintv.connect;

import org.springframework.social.oauth1.AbstractOAuth1ServiceProvider;
import org.springframework.social.oauth1.OAuth1Template;

import co.bantamstudio.streamie.auth.justintv.api.JustinTV;
import co.bantamstudio.streamie.auth.justintv.api.impl.JustinTVTemplate;

class JustinTVServiceProvider extends
		AbstractOAuth1ServiceProvider<JustinTV> {

	public JustinTVServiceProvider(String clientId, String clientSecret) {
		super(clientId, clientSecret, new OAuth1Template(clientId, clientSecret, 
				"http://www.justin.tv/oauth/request_token",
				"http://www.justin.tv/oauth/authorize",
				"http://www.justin.tv/oauth/access_token"));
	}

	@Override
	public JustinTV getApi(String accessToken, String secret) {
		return new JustinTVTemplate(getConsumerKey(), getConsumerSecret(), accessToken, secret);
	}
}