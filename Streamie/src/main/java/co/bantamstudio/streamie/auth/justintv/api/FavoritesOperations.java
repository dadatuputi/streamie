package co.bantamstudio.streamie.auth.justintv.api;

import co.bantamstudio.streamie.auth.justintv.model.JustinTVProfile;

public interface FavoritesOperations {
	JustinTVProfile followChannel(String channel);
	
	String unFollowChannel(String channel);
}
