package co.bantamstudio.streamie.auth.justintv.api;

import org.springframework.social.ApiBinding;

public interface JustinTV extends ApiBinding {

	FavoritesOperations followsOperations();

    UserOperations userOperations();
    
    ChannelOperations channelOperations();
}
