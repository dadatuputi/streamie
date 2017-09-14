package co.bantamstudio.streamie.justintv;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.activity.StreamActivity;
import co.bantamstudio.streamie.auth.justintv.model.JtvStreamsWrapper;
import co.bantamstudio.streamie.auth.justintv.model.JustinTVStream;
import co.bantamstudio.streamie.auth.model.Stream;
import co.bantamstudio.streamie.fragment.StreamDetailFragment;
import co.bantamstudio.streamie.justintv.api.JustinTVApi;
import co.bantamstudio.streamie.justintv.api.JustinTVStreamRequest;

public class JustinTVStreamDetailFragment extends StreamDetailFragment {

    @Override
    protected void parseJson(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mStream = mapper.readValue(mJson, JustinTVStream.class);
            updateView(mStream);
        } catch (Exception e) {
            executeLoader(false);
        }
    }

    @Override
    protected void executeLoader(boolean isRefresh) {
        Assert.notNull(mKey);

        List<String> channel = new ArrayList<String>(1);
        channel.add(mKey);
        JustinTVStreamRequest request = new JustinTVStreamRequest(null, channel, null, null, 0, 0);
        mPullLayout.setRefreshing(true);

        long cacheLen = isRefresh? DurationInMillis.ALWAYS_EXPIRED: request.getCacheLength();

        mSpiceManager.execute(request, request.getCacheKey(), cacheLen, new RequestListener<JtvStreamsWrapper>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                // DO NOTHING
                Toast.makeText(getActivity(), "invalid stream", Toast.LENGTH_LONG).show();
                getActivity().onBackPressed();
            }

            @Override
            public void onRequestSuccess(JtvStreamsWrapper result) {
                Assert.notNull(result);
                if (CollectionUtils.isEmpty(result.getStreams())) {
                    // STREAM IS OFFLINE
                    Toast.makeText(getActivity(), "stream offline", Toast.LENGTH_LONG).show();
                    getActivity().onBackPressed();
                } else {
                    mStream = result.getStreams().get(0);
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
			if (mStream.getChatEmbed() == null)
				mStream.setChatEmbed(JustinTVApi.getChatEmbed("100%", "100%", key, mApplication.isChatAutoScroll()));
		default:
			break;
		}
		
		// BUILD URL
		// Consider using jericho html parser if this is buggy
		String embed = mStream.getEmbed();
		embed = embed.replaceAll(JustinTVApi.STREAM_AUTOPLAY_REGEX_PATTERN,
				JustinTVApi.STREAM_AUTOPLAY_REGEX_REPLACEMENT);
		embed = embed.replaceAll(JustinTVApi.STREAM_VOLUME_REGEX_PATTERN,
				JustinTVApi.STREAM_VOLUME_REGEX_REPLACEMENT);
		
		if (mApplication.getOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
			embed = embed.replaceAll(JustinTVApi.STREAM_HEIGHT_REGEX_PATTERN, JustinTVApi.STREAM_HEIGHT_REGEX_REPLACEMENT_50);
		else
			embed = embed.replaceAll(JustinTVApi.STREAM_HEIGHT_REGEX_PATTERN, JustinTVApi.STREAM_HEIGHT_REGEX_REPLACEMENT);
		
		embed = embed.replaceAll(JustinTVApi.STREAM_WIDTH_REGEX_PATTERN,
				JustinTVApi.STREAM_WIDTH_REGEX_REPLACEMENT);
		embed = JustinTVApi.STREAM_HTML_OPEN + embed + JustinTVApi.STREAM_HTML_CLOSE;
		mStream.setEmbed(embed);
		
		switch (playType) {
		case FLASH:
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
			mIntent.putExtra(Stream.TYPE, JustinTVStream.class.getName());

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

	@Override
	protected void addToFavorites(String channel) {
		// TODO IMPLEMENT JUSTINTV FAVORITES
		Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.justin_tv_favorites), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void removeFromFavorites(String channel) {
		// TODO Auto-generated method stub
        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.justin_tv_favorites), Toast.LENGTH_SHORT).show();
    }

}
