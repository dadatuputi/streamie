package co.bantamstudio.streamie.twitch;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.adapter.CategoryAdapter;
import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.model.CategoriesWrapper;
import co.bantamstudio.streamie.auth.model.Category;
import co.bantamstudio.streamie.auth.model.CategoryLoading;
import co.bantamstudio.streamie.auth.model.Stream.EmptyStreamException;
import co.bantamstudio.streamie.auth.twitch.model.TwitchGame;
import co.bantamstudio.streamie.auth.twitch.model.TwitchGameWrapper;
import co.bantamstudio.streamie.auth.twitch.model.TwitchGamesWrapper;
import co.bantamstudio.streamie.fragment.CategoryListFragment;
import co.bantamstudio.streamie.twitch.api.TwitchCategoriesRequest;

public class TwitchCategoryListFragment extends CategoryListFragment implements OnScrollListener {
	
	private String mNextLink;
	private boolean mLoadingMore = false;
	private CategoryLoading mLoading;
	StreamieRequest<CategoriesWrapper> mStreamieRequest;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setOnScrollListener(this);
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mAdapter != null) {
			int lastVisible = view.getLastVisiblePosition();
			
			// ONLY RESET LOADER IF WE'RE NOT WAITING FOR THE LOADER TO RETURN
			boolean loadMore = !mLoadingMore && (mNextLink != null && lastVisible + 1 >= totalItemCount);
			if (loadMore) {
				// TELL LOADERMANAGER TO LOAD MORE
				setNextStreamieRequest();
				executeLoader(false);
				
				mLoadingMore = true;
				
				// ADD SPINNER ITEM TO BOTTOM
				if (mLoading == null)
					mLoading = new CategoryLoading();
				mAdapter.add(mLoading);
				view.setSelection(mAdapter.getPosition(mLoading));
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// DO NOTHING
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (mAdapter != null && !(mAdapter.getItem(position) instanceof CategoryLoading)) {
			// START CATEGORY ACTIVITY
            Bundle bundle = new Bundle();
            bundle.putString(StreamieApplication.KEY, mAdapter.getItem(position).getKey());
            bundle.putString(StreamieApplication.TITLE, getActivity().getResources().getString(R.string.twitch));
            bundle.putString(StreamieApplication.SUBTITLE, mAdapter.getItem(position).getName());
            listener.pushFragment(TwitchStreamListFragment.class,
                    bundle);
		}
	}

	@Override
	protected void setFirstStreamieRequest() {
		mStreamieRequest = new TwitchCategoriesRequest(LIMIT_PER_REQUEST, 0);
	}
	protected void setNextStreamieRequest() {
		mStreamieRequest = new TwitchCategoriesRequest(mNextLink);
	}

    @Override
    protected void parseResults(CategoriesWrapper results) throws EmptyStreamException {
		
		// ADD DATA TO EXISTING SET
		
		if (mAdapter == null) { // FIRST TIME

			mAdapter = new CategoryAdapter(getActivity(), R.id.listItemText, buildList(results, true));
			this.setListAdapter(mAdapter);
			
		} else if (mLoadingMore) { // ADD DATA TO EXISTING SET
			
			// REMOVE LOADING ITEM
			if (mLoading != null)
				mAdapter.remove(mLoading);
			
			// ADD DATA TO EXISTING ADAPTER
			List<Category> result =  buildList(results, false);
			for (Category category : result){
				mAdapter.add(category);
			}
			mAdapter.notifyDataSetChanged();
			mLoadingMore = false;
			
		} else {
			mAdapter.setData(buildList(results, true));
		}
	}
	
	// BUILD UP LIST FOR ADAPTER
    List<Category> buildList(CategoriesWrapper results, boolean firstRequest) throws EmptyStreamException{
		if (results == null || CollectionUtils.isEmpty(results.getCategories()))
			throw new EmptyStreamException("Game results are empty");
		
		List<Category> result = new ArrayList<Category>(results.getCategories());
		
		if (firstRequest)
			result.add(0, new TwitchGameWrapper(new TwitchGame(TwitchGame.ALL_TWITCH_STREAMS)));
		
		// PREPARE FRAGMENT FOR LOADING MORE DATA
		mNextLink = ((TwitchGamesWrapper)results).get_links().getNext();
		
		return result;
	}

	@Override
	protected void executeLoader(boolean isRefresh) {
        try {
        Assert.notNull(mStreamieRequest);
        } catch (IllegalArgumentException e) {
            return;
        }

        mPullToRefreshLayout.setRefreshing(true);

        long cacheLen = isRefresh? DurationInMillis.ALWAYS_EXPIRED: mStreamieRequest.getCacheLength();

        mSpiceManager.execute(mStreamieRequest, mStreamieRequest.getCacheKey(), cacheLen, new RequestListener<CategoriesWrapper>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                handleException(spiceException);
            }

            @Override
            public void onRequestSuccess(CategoriesWrapper result) {

                try {
                    parseResults(result);
                } catch (EmptyStreamException e) {
                    handleException(e);
                }

                handleLoadFinished();
            }
        });
	}

	protected void handleException(Exception e) {
		setEmptyText("Error: " + e.getMessage());
		
		mLoadingMore  = false;
		
		// REMOVE LOADING ITEM
		if (mAdapter != null && mLoading != null)
			mAdapter.remove(mLoading);
	}
}
