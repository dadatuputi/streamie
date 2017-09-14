package co.bantamstudio.streamie.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import co.bantamstudio.streamie.adapter.StreamAdapter;
import co.bantamstudio.streamie.auth.model.Stream;
import co.bantamstudio.streamie.auth.model.StreamLoading;
import co.bantamstudio.streamie.auth.model.StreamsWrapper;

public abstract class StreamListFragment extends StreamieListFragment implements
		OnScrollListener {

	protected StreamAdapter mAdapter;
	protected StreamLoading mLoading;
	protected ConnectionRepository mConnectionRepository;
	protected OAuth2ConnectionFactory<?> mFactory;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnScrollListener(this);

        if (savedInstanceState == null && (mAdapter == null || mAdapter.isEmpty())) {
            setFirstStreamieRequest();
            executeLoader(false);
        } else {
            handleLoadFinished();
        }
    }

    @Override
    protected void handleException(Exception e) {
        super.handleException(e);

        // REMOVE LOADING ITEM
        if (mAdapter != null && mLoading != null)
            mAdapter.remove(mLoading);
    }

    @Override
    protected void initialize() {
        super.initialize();
        mConnectionRepository = mApplication.getConnectionRepository();
    }
	
    @Override
    protected void handleLoadFinished() {
        super.handleLoadFinished();

        //  LOAD ADS
        if (mAds != null) {
            mAds.loadBanner(mAdContainer, getKeywords());
        }
    }

    @Override
	public Set<String> getKeywords() {
		if (mAdapter != null)
			return mAdapter.getKeywords();
		else
			return null;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		StreamAdapter sa = (StreamAdapter) getListAdapter();
		Stream stream = sa.getItem(position);
		stream.loadStream(getActivity(), listener);
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mAdapter != null) {
			int lastVisible = view.getLastVisiblePosition();
			
			// ONLY RESET LOADER IF WE'RE NOT WAITING FOR THE LOADER TO RETURN
			boolean loadMore = !mLoadingMore && lastVisible + 1 >= totalItemCount;
			if (loadMore && mHasMore) {
				// TELL LOADERMANAGER TO LOAD MORE
				setNextStreamieRequest();
				executeLoader(false);
				
				mLoadingMore = true;
				
				// ADD SPINNER ITEM TO BOTTOM
				if (mLoading == null)
					mLoading = new StreamLoading();
				mAdapter.add(mLoading);
				view.setSelection(mAdapter.getPosition(mLoading));
			}
		}
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// DO NOTHING
	}
	
	protected List<Stream> filterMature(List<Stream> streams) {
		if (mApplication.isMature()) {
			// FILTER MATURE STREAMS
			List<Stream> filtered = new ArrayList<Stream>();

			for (Stream stream : streams) {
				if (!stream.isMature())
					filtered.add(stream);
			}
			return filtered;
		} else {
			return streams;
		}
	}

    protected abstract void addToFavorites(String channel);

    protected abstract void removeFromFavorites(String channel);

    protected abstract boolean isFavorite(String channel);

    protected abstract void parseResults(StreamsWrapper results) throws Stream.EmptyStreamException;

}
