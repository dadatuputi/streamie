package co.bantamstudio.streamie.twitch;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.Assert;

import java.io.IOException;

import co.bantamstudio.streamie.activity.StreamActivity;
import co.bantamstudio.streamie.auth.model.Stream;
import co.bantamstudio.streamie.auth.twitch.api.Twitch;
import co.bantamstudio.streamie.auth.twitch.model.TwitchChannelWrapper;
import co.bantamstudio.streamie.auth.twitch.model.TwitchStream;
import co.bantamstudio.streamie.auth.twitch.model.TwitchStreamWrapper;
import co.bantamstudio.streamie.fragment.StreamDetailFragment;
import co.bantamstudio.streamie.justintv.api.JustinTVApi;
import co.bantamstudio.streamie.twitch.api.TwitchApi;
import co.bantamstudio.streamie.twitch.api.TwitchFollowsPutRequest;
import co.bantamstudio.streamie.twitch.api.TwitchStreamRequest;

public class TwitchStreamDetailFragment extends StreamDetailFragment {


    private ConnectionRepository mConnectionRepository;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mConnectionRepository = mApplication.getConnectionRepository();
    }

    @Override
    protected void parseJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mStream = mapper.readValue(json, TwitchStream.class);
            updateView(mStream);
        } catch (Exception e) {
            executeLoader(false);
        }
    }

    @Override
    protected void executeLoader(boolean isRefresh) {
        Assert.notNull(mKey);

        TwitchStreamRequest request = new TwitchStreamRequest(mKey);
        mPullLayout.setRefreshing(true);

        long cacheLen = isRefresh? DurationInMillis.ALWAYS_EXPIRED: request.getCacheLength();

        mSpiceManager.execute(request, request.getCacheKey(), cacheLen, new RequestListener<TwitchStreamWrapper>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Toast.makeText(getActivity(), "invalid stream", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
            }

            @Override
            public void onRequestSuccess(TwitchStreamWrapper result) {
                Assert.notNull(result);
                if (result.getStream() == null) {
                    // STREAM IS OFFLINE
                    Toast.makeText(getActivity(), "TEST stream offline", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                } else {
                    mStream = result.getStream();
                    updateView(mStream);
                }
            }
        });
    }

	@Override
	protected void playStream(String key, PLAY_TYPE playType) {

		loading(false);
		
		// SET CHAT UP
		switch (playType) {
		case FLASH:
		case HLS:
			if (mStream.getChatEmbed() == null)
				mStream.setChatEmbed(TwitchApi.getChatEmbed("100%", "100%", key, mApplication.isChatAutoScroll()));
		default:
			break;
		}

		// BUILD URL
		switch (playType) {
		case FLASH_BROWSER:
		case FLASH:
			// BUILD FLASH EMBED
			String height = "100%";
			String width = "100%";
			String volume = "50";
			if (mApplication.getOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
				height = "\\\"50%\\\"";
			}
			
			String embed = TwitchApi.getEmbed(height, width, volume, key);
			embed = JustinTVApi.STREAM_HTML_OPEN + embed + JustinTVApi.STREAM_HTML_CLOSE;
			mStream.setEmbed(embed);
			break;
		case HLS:
			// BUILD HLS LINK
			String hlsString = "http://www.twitch.tv/" + mStream.getUsername() + "/hls";
			mStream.setHls(hlsString);
			break;
		default:
			break;
		}
		
		// SET UP INTENT
		switch (playType) {
		case FLASH:
		case HLS:
			mIntent = new Intent(getActivity().getApplicationContext(),StreamActivity.class);
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = "";
			try {
				jsonString = mapper.writeValueAsString(mStream);
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mIntent.putExtra(Stream.SERIALIZED_JSON, jsonString);
			mIntent.putExtra(Stream.TYPE, TwitchStream.class.getName());
			
			break;
		case FLASH_BROWSER:
			// OPEN IN EXTERNAL BROWSER IF SET
			mIntent = mApplication.openInBrowserPath(mStream.getEmbed(), getActivity());
			break;
		default:
			return;
		}
		
		startActivity(mIntent);
	}

    private static boolean isConnected(ConnectionRepository connectionRepository) {
        return connectionRepository.findPrimaryConnection(Twitch.class) != null;
    }
	
	@Override
	protected void addToFavorites(final String channel) {
        if (isConnected(mConnectionRepository)) {
            Twitch twitch = mConnectionRepository.findPrimaryConnection(Twitch.class)
                    .getApi();
            TwitchFollowsPutRequest request = new TwitchFollowsPutRequest(twitch, channel);
            mSpiceManager.execute(request, request.getCacheKey(), request.getCacheLength(), new RequestListener<TwitchChannelWrapper>() {
                @Override
                public void onRequestFailure(SpiceException e) {
                    Toast.makeText(getActivity(), "Could not add favorite: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onRequestSuccess(TwitchChannelWrapper twitchChannelWrapper) {
                    Toast.makeText(getActivity(), channel + " successfully added to favorites", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), "You cannot add favorites if you haven't connected to Twitch", Toast.LENGTH_LONG).show();
        }
	}

	@Override
	protected void removeFromFavorites(final String channel) {
//        if (isConnected(mConnectionRepository)) {
//            Twitch twitch = mConnectionRepository.findPrimaryConnection(Twitch.class)
//                    .getApi();
//            TwitchFollowsDeleteRequest request = new TwitchFollowsDeleteRequest(twitch, channel);
//            mSpiceManager.execute(request, request.getCacheKey(), request.getCacheLength(), new RequestListener<String>() {
//                @Override
//                public void onRequestFailure(SpiceException e) {
//                    Toast.makeText(getActivity(), "Could not add favorite: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onRequestSuccess(String message) {
//                    Toast.makeText(getActivity(), channel + " successfully added to favorites", Toast.LENGTH_LONG).show();
//                }
//            });
//        } else {
//            Toast.makeText(getActivity(), "You cannot add favorites if you haven't connected to Twitch", Toast.LENGTH_LONG).show();
//        }
	}
}
