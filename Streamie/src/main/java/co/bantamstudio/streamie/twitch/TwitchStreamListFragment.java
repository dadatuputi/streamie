package co.bantamstudio.streamie.twitch;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.adapter.StreamAdapter;
import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.model.Stream;
import co.bantamstudio.streamie.auth.model.Stream.EmptyStreamException;
import co.bantamstudio.streamie.auth.model.StreamsWrapper;
import co.bantamstudio.streamie.auth.twitch.api.Twitch;
import co.bantamstudio.streamie.auth.twitch.model.TwitchChannelWrapper;
import co.bantamstudio.streamie.auth.twitch.model.TwitchStreamsWrapper;
import co.bantamstudio.streamie.fragment.StreamListFragment;
import co.bantamstudio.streamie.twitch.api.TwitchFollowsDeleteRequest;
import co.bantamstudio.streamie.twitch.api.TwitchFollowsPutRequest;
import co.bantamstudio.streamie.twitch.api.TwitchStreamsRequest;

public class TwitchStreamListFragment extends StreamListFragment {
	
	String mNextLink;
	StreamieRequest<TwitchStreamsWrapper> mStreamieRequest;
	Twitch mTwitch;
	
	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		
		mFactory = mApplication.getTwitchConnectionFactory();
		Connection<Twitch> connection = mConnectionRepository.findPrimaryConnection(Twitch.class);
		
		if (connection != null)
			mTwitch = connection.getApi();
	}

	@Override
	protected void executeLoader(boolean isRefresh) {
        try {
            Assert.notNull(mStreamieRequest);
        } catch (IllegalArgumentException e) {
            return;
        }

        mPullToRefreshLayout.setRefreshing(true);

        if (isRefresh) {
            mOffset = OFFSET_INITIAL;
            setFirstStreamieRequest();
        }
        long cacheLen = isRefresh? DurationInMillis.ALWAYS_EXPIRED: mStreamieRequest.getCacheLength();

        mSpiceManager.execute(mStreamieRequest, mStreamieRequest.getCacheKey(), cacheLen, new RequestListener<TwitchStreamsWrapper>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                handleException(spiceException);
            }

            @Override
            public void onRequestSuccess(TwitchStreamsWrapper result) {

                try {
                    parseResults(result);
                } catch (EmptyStreamException e) {
                    handleException(e);
                }

                handleLoadFinished();
            }
        });
	}

	@Override
	protected void setFirstStreamieRequest() {
		mStreamieRequest = new TwitchStreamsRequest(mKey, LIMIT_PER_REQUEST, mOffset);
	}
	
	@Override
	protected void setNextStreamieRequest() {
		mStreamieRequest = new TwitchStreamsRequest(mNextLink);
	}
	
	@Override
	protected void parseResults(StreamsWrapper results) throws EmptyStreamException {
			
		if (results == null || CollectionUtils.isEmpty(results.getStreams()))
			throw new EmptyStreamException(getActivity().getResources().getString(R.string.stream_results_empty));
		
		ArrayList<Stream> result = new ArrayList<Stream>(results.getStreams());
		
		// FILTER STREAMS
		List<Stream> filtered = filterMature(result);
		
		if (CollectionUtils.isEmpty(filtered))
			throw new EmptyStreamException(getActivity().getResources().getString(R.string.stream_results_empty));
		
		// PREPARE FRAGMENT FOR LOADING MORE DATA
		mNextLink = ((TwitchStreamsWrapper)results).get_links().getNext();
		
		if (mAdapter == null) {
			// FIRST TIME
			mAdapter = new StreamAdapter(getActivity(), R.layout.list_item_stream, R.id.listItemText, filtered);
			this.setListAdapter(mAdapter);
			mOffset += result.size();

		} else if (mLoadingMore) {

			// REMOVE LOADING ITEM
			if (mLoading != null)
				mAdapter.remove(mLoading);

			// ADD DATA TO EXISTING ADAPTER
			for (Stream stream : filtered) {
				mAdapter.add(stream);
			}
			mAdapter.notifyDataSetChanged();

			mOffset += result.size();

			mLoadingMore = false;

		} else {
			mAdapter.setData(filtered);
		}

		mHasMore = result.size() >= LIMIT_PER_REQUEST;
	}

	@Override
	protected void addToFavorites(String channel) {
		
		if (mTwitch != null) {
			TwitchFollowsPutRequest putRequest = new TwitchFollowsPutRequest(mTwitch, channel);
			mSpiceManager.execute(putRequest, new RequestListener<TwitchChannelWrapper>() {
	
				@Override
				public void onRequestFailure(SpiceException spiceException) {
					Toast.makeText(getActivity().getApplicationContext(), "Could not add favorite: " + spiceException.getLocalizedMessage(), Toast.LENGTH_LONG).show();
				}
	
				@Override
				public void onRequestSuccess(TwitchChannelWrapper result) {
					Toast.makeText(getActivity().getApplicationContext(), "Channel " + result.getChannel().getName() + " added to follows list", Toast.LENGTH_LONG).show();
				}
			});
		}
	}

	@Override
	protected void removeFromFavorites(final String channel) {
		
		if (mTwitch != null) {
			TwitchFollowsDeleteRequest deleteRequest = new TwitchFollowsDeleteRequest(mTwitch, channel);
			mSpiceManager.execute(deleteRequest, new RequestListener<String>() {
	
				@Override
				public void onRequestFailure(SpiceException spiceException) {
					Toast.makeText(getActivity().getApplicationContext(), "Could not remove favorite: " + spiceException.getLocalizedMessage(), Toast.LENGTH_LONG).show();
				}
	
				@Override
				public void onRequestSuccess(String result) {
					Toast.makeText(getActivity().getApplicationContext(), "Channel " + channel + " removed", Toast.LENGTH_LONG).show();
				}
			});
		}
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
            // ADD TO FAVORITES
            android.view.MenuItem fav = menu.add(getResources().getString(R.string.context_add_to_favorites));
            fav.setOnMenuItemClickListener(new android.view.MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(android.view.MenuItem item) {
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
                    return true;
                }
            });
        }
//            if (!isFavorite(channel)) {
//                // ADD TO FAVORITES
//                android.view.MenuItem fav = menu.add(getResources().getString(R.string.context_add_to_favorites));
//                fav.setOnMenuItemClickListener(new android.view.MenuItem.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(android.view.MenuItem item) {
//                        // ADD TO FAVORITES
//                        addToFavorites(channel);
//                        // SHOW TOAST
//                        String message = getResources().getString((R.string.context_added_to_favorites));
//                        message = String.format(message, channel);
//                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                });
//            } else {
//                // REMOVE FROM FAVORITES
//                android.view.MenuItem fav = menu.add(getResources().getString(R.string.context_remove_from_favorites));
//                fav.setOnMenuItemClickListener(new android.view.MenuItem.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(android.view.MenuItem item) {
//                        // REMOVE FROM FAVORITES
//                        removeFromFavorites(channel);
//                        // SHOW TOAST
//                        String message = getResources().getString((R.string.context_removed_from_favorites));
//                        message = String.format(message, channel);
//                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                });
//            }
    }

	@Override
	protected boolean isFavorite(String channel) {
		return false;
	}
}