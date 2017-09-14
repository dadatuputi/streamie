package co.bantamstudio.streamie.justintv;

import android.os.Bundle;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.adapter.StreamAdapter;
import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.justintv.api.JustinTV;
import co.bantamstudio.streamie.auth.justintv.model.JtvStreamsWrapper;
import co.bantamstudio.streamie.auth.model.Stream;
import co.bantamstudio.streamie.auth.model.Stream.EmptyStreamException;
import co.bantamstudio.streamie.auth.model.StreamsWrapper;
import co.bantamstudio.streamie.fragment.StreamListFragment;
import co.bantamstudio.streamie.justintv.api.JustinTVStreamRequest;

public class JustinTVStreamListFragment extends StreamListFragment {
	
	protected String mNextLink;
	StreamieRequest<JtvStreamsWrapper> mStreamieRequest;
	protected JustinTV mJustinTV;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setOnScrollListener(this);
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

        mSpiceManager.execute(mStreamieRequest,  mStreamieRequest.getCacheKey(), cacheLen, new RequestListener<JtvStreamsWrapper>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                handleException(spiceException);
            }

            @Override
            public void onRequestSuccess(JtvStreamsWrapper result) {

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
		mStreamieRequest = new JustinTVStreamRequest(mKey, mApplication.getLanguage(), LIMIT_PER_REQUEST, mOffset);
	}
	
	@Override
	protected void setNextStreamieRequest() {
        setFirstStreamieRequest();
	}

	@Override
	protected void parseResults(StreamsWrapper results) throws EmptyStreamException {
		if (results == null || results.getStreams() == null || results.getStreams().isEmpty())
			throw new EmptyStreamException("Stream results are empty");
		
		ArrayList<Stream> result = new ArrayList<Stream>(results.getStreams());
		
		// FILTER STREAMS
		List<Stream> filtered = filterMature(result);
		
		if (filtered == null || filtered.isEmpty())
			throw new EmptyStreamException("Stream results are empty");
				
		if (mAdapter == null) {
			// FIRST TIME
			mAdapter = new StreamAdapter(getActivity(), R.layout.list_item_stream, R.id.listItemText, filtered);
			this.setListAdapter(mAdapter);

		} else if (mLoadingMore) {

			// REMOVE LOADING ITEM
			if (mLoading != null)
				mAdapter.remove(mLoading);

			// ADD DATA TO EXISTING ADAPTER
			for (Stream stream : filtered) {
				mAdapter.add(stream);
			}
			mAdapter.notifyDataSetChanged();

			mLoadingMore = false;

		} else {
			mAdapter.setData(filtered);
		}

		mOffset += result.size();
		mHasMore = result.size() >= LIMIT_PER_REQUEST;
	}

	@Override
	protected void addToFavorites(String channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void removeFromFavorites(String channel) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isFavorite(String channel) {
		// TODO Auto-generated method stub
		return false;
	}
}
