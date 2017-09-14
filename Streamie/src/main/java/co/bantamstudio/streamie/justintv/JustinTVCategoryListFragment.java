package co.bantamstudio.streamie.justintv;
import co.bantamstudio.streamie.R;

import java.util.ArrayList;
import java.util.Collections;

import co.bantamstudio.streamie.auth.justintv.model.JtvCategoriesWrapper;
import co.bantamstudio.streamie.auth.justintv.model.JtvCategory;
import co.bantamstudio.streamie.auth.model.CategoriesWrapper;
import co.bantamstudio.streamie.auth.model.Category;
import co.bantamstudio.streamie.auth.model.Stream.EmptyStreamException;
import org.springframework.util.Assert;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import co.bantamstudio.streamie.adapter.CategoryAdapter;
import co.bantamstudio.streamie.fragment.CategoryListFragment;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.justintv.api.JustinTVCategoryRequest;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class JustinTVCategoryListFragment extends CategoryListFragment {

	private JustinTVCategoryRequest mStreamieRequest;

	@Override
	protected void parseResults(CategoriesWrapper results)
			throws EmptyStreamException {

		if (results == null || results.getCategories() == null
				|| results.getCategories().isEmpty())
			throw new EmptyStreamException("Category results are empty");

		ArrayList<Category> result = new ArrayList<Category>(
				results.getCategories());

		// REMOVE GAMING CATEGORY
		int index = Integer.MAX_VALUE;
		for (Category category : result) {
			if (category.getName().equalsIgnoreCase("gaming")) {
				index = result.indexOf(category);
				break;
			}
		}
		if (index < result.size())
			result.remove(index);

		// ADD ALL CATEGORY
		// Get total count of subText for "All Categories"
		int total_viewers = 0;
		int total_channels = 0;
		for (Category category : result) {
			total_viewers += category.getViewersCount();
			total_channels += category.getChannelsCount();
		}
		result.add(new JtvCategory(JtvCategory.ALL,
				getActivity().getResources().getString(R.string.all_streams),
                total_channels,
                total_viewers));

        // SORT LIST BY VIEWER COUNT
        Collections.sort(result);
        Collections.reverse(result);

		if (mAdapter == null) {
			mAdapter = new CategoryAdapter(getActivity(), R.id.listItemText,result);
			this.setListAdapter(mAdapter);
		} else {
			mAdapter.setData(result);
		}
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (mAdapter != null) {

			// PUSH CATEGORY FRAGMENT
            Bundle bundle = new Bundle();
            bundle.putString(StreamieApplication.KEY, mAdapter.getItem(position).getKey());
            bundle.putString(StreamieApplication.TITLE, getActivity().getResources().getString(R.string.justin_tv));
            bundle.putString(StreamieApplication.SUBTITLE, mAdapter.getItem(position).getName());
            listener.pushFragment(JustinTVStreamListFragment.class,
                    bundle);
        }
	}

	@Override
	protected void setFirstStreamieRequest() {
		mStreamieRequest = new JustinTVCategoryRequest();
	}

    @Override
    protected void setNextStreamieRequest() {
        // DO NOTHING
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

		mSpiceManager.execute(mStreamieRequest, mStreamieRequest.getCacheKey(), cacheLen, new RequestListener<JtvCategoriesWrapper>() {
			@Override
			public void onRequestFailure(SpiceException spiceException) {
				handleException(spiceException);
			}
			
			@Override
			public void onRequestSuccess(JtvCategoriesWrapper result) {

				try {
					parseResults(result);
				} catch (EmptyStreamException e) {
					handleException(e);
				}
				
				handleLoadFinished();
			}
		});
	}
}