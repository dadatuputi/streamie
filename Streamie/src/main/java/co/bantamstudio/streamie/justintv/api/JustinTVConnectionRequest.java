package co.bantamstudio.streamie.justintv.api;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuthToken;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.justintv.api.JustinTV;
import co.bantamstudio.streamie.auth.justintv.connect.JustinTVConnectionFactory;

public class JustinTVConnectionRequest extends StreamieRequest<Void> {

	private final ConnectionRepository mConnectionRepository;
	private final JustinTVConnectionFactory mConnectionFactory;
	private final OAuthToken mRequestToken;
	private final String mOauthTokenString;

	public JustinTVConnectionRequest(OAuthToken requestToken, String oauthTokenString, JustinTVConnectionFactory connectionFactory, ConnectionRepository connectionRepository ) {
		super(Void.class);
		mRequestToken = requestToken;
		mOauthTokenString = oauthTokenString;
		mConnectionFactory = connectionFactory;
		mConnectionRepository = connectionRepository;
	}

	@Override
	public Void loadDataFromNetwork() throws Exception {
		
		AuthorizedRequestToken authorizedRequestToken = new AuthorizedRequestToken(mRequestToken, mOauthTokenString);

		OAuthToken accessToken = mConnectionFactory.getOAuthOperations().exchangeForAccessToken(
				authorizedRequestToken, null);
		
		Connection<JustinTV> connection = mConnectionFactory.createConnection(accessToken);
		
		try {
			mConnectionRepository.addConnection(connection);
		} catch (DuplicateConnectionException e) {
			// CONNECTION ALREADY EXISTS, DO NOTHING
		}
		
		return null;
	}

	@Override
	public Object getCacheKey() {
		return null;
	}
	
	
}
