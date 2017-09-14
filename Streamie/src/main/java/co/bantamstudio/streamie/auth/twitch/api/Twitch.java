package co.bantamstudio.streamie.auth.twitch.api;

import org.springframework.social.ApiBinding;

public interface Twitch extends ApiBinding {

	FollowsOperations followsOperations();

    UserOperations userOperations();
    
    ChannelOperations channelOperations();
}
