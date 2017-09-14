package co.bantamstudio.streamie.twitch.api;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.oauth2.AccessGrant;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.twitch.api.Twitch;
import co.bantamstudio.streamie.auth.twitch.connect.TwitchConnectionFactory;

public class TwitchConnectionRequest extends StreamieRequest<Void> {

	private final ConnectionRepository mConnectionRepository;
	private final TwitchConnectionFactory mConnectionFactory;
	private final AccessGrant mAccessGrant;

	public TwitchConnectionRequest(AccessGrant accessGrant, TwitchConnectionFactory connectionFactory, ConnectionRepository connectionRepository ) {
		super(Void.class);
		mConnectionRepository = connectionRepository;
		mConnectionFactory = connectionFactory;
		mAccessGrant = accessGrant;
	}

	@Override
	public Void loadDataFromNetwork() throws Exception {
		
		Connection<Twitch> connection = mConnectionFactory.createConnection(mAccessGrant);
		
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
