package co.bantamstudio.streamie.justintv.api;

import org.springframework.social.oauth1.OAuthToken;
import org.springframework.util.MultiValueMap;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.justintv.connect.JustinTVConnectionFactory;

public class JustinTVConnectionTokenRequest extends StreamieRequest<OAuthToken> {

	private final JustinTVConnectionFactory mConnectionFactory;
	private final MultiValueMap<String, String> mAdditionalParameters;
	private final String mCallbackUrl;

	public JustinTVConnectionTokenRequest(JustinTVConnectionFactory connectionFactory, String callbackUrl, MultiValueMap<String, String> additionalParameters ) {
		super(OAuthToken.class);
		mConnectionFactory = connectionFactory;
		mCallbackUrl = callbackUrl;
		mAdditionalParameters = additionalParameters;
	}

	@Override
	public OAuthToken loadDataFromNetwork() throws Exception {
		
		return mConnectionFactory.getOAuthOperations().fetchRequestToken(mCallbackUrl, mAdditionalParameters);
		
	}

	@Override
	public Object getCacheKey() {
		return null;
	}
	
	
}
