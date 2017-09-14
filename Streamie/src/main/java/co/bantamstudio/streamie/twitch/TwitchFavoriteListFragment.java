package co.bantamstudio.streamie.twitch;

import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.Assert;

import java.util.List;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.auth.model.Stream;
import co.bantamstudio.streamie.auth.twitch.api.Twitch;
import co.bantamstudio.streamie.twitch.api.TwitchFollowsDeleteRequest;
import co.bantamstudio.streamie.twitch.api.TwitchFollowsGetRequest;

public class TwitchFavoriteListFragment extends TwitchStreamListFragment {

	@Override
	protected void setFirstStreamieRequest() {
		mStreamieRequest = new TwitchFollowsGetRequest(mTwitch, LIMIT_PER_REQUEST, mOffset);
	}

	@Override
	protected void setNextStreamieRequest() {
		mStreamieRequest = new TwitchFollowsGetRequest(mTwitch, mNextLink);
	}

	@Override
	protected boolean isFavorite(String channel) {
		// ALWAYS TRUE FOR FAVORITES
		return true;
	}
	
	@Override
	protected List<Stream> filterMature(List<Stream> streams) {
		// WE DON'T FILTER FAVORITES
		return streams;
	}

    private static boolean isConnected(ConnectionRepository connectionRepository) {
        return connectionRepository.findPrimaryConnection(Twitch.class) != null;
    }

    // CONTEXT MENU FOR FAVORITES
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        final int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        final String channel = mAdapter.getItem(position).getUsername();
        Assert.hasText(channel);

        if (isConnected(mConnectionRepository)) {
            // REMOVE FROM FAVORITES
            android.view.MenuItem fav = menu.add(getResources().getString(R.string.context_remove_from_favorites));
            fav.setOnMenuItemClickListener(new android.view.MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(android.view.MenuItem item) {
                    Twitch twitch = mConnectionRepository.findPrimaryConnection(Twitch.class)
                            .getApi();
                    TwitchFollowsDeleteRequest request = new TwitchFollowsDeleteRequest(twitch, channel);
                    mSpiceManager.execute(request, request.getCacheKey(), request.getCacheLength(), new RequestListener<String>() {
                        @Override
                        public void onRequestFailure(SpiceException e) {
                            Toast.makeText(getActivity(), "Could not add favorite: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onRequestSuccess(String string) {
                            Toast.makeText(getActivity(), channel + " removed from favorites", Toast.LENGTH_LONG).show();
                            executeLoader(true);
                        }
                    });
                    return true;
                }
            });
        }
    }
}
