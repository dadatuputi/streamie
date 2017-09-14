package co.bantamstudio.streamie.auth.twitch.api;

import java.net.URI;

import co.bantamstudio.streamie.auth.twitch.model.TwitchChannelWrapper;
import co.bantamstudio.streamie.auth.twitch.model.TwitchStreamsWrapper;

public interface FollowsOperations {
	
	TwitchStreamsWrapper getFollows(URI uri);
	TwitchStreamsWrapper getFollows(String url);
	TwitchStreamsWrapper getFollows(int limit, int offset); 

	TwitchChannelWrapper followChannel(String user, String channel);
	
	String unFollowChannel(String user, String channel);
}
