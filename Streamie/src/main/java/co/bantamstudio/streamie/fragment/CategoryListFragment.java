package co.bantamstudio.streamie.fragment;

import java.util.Set;

import co.bantamstudio.streamie.auth.model.CategoriesWrapper;
import co.bantamstudio.streamie.auth.model.Stream;

import co.bantamstudio.streamie.adapter.CategoryAdapter;

import android.os.Bundle;

public abstract class CategoryListFragment extends StreamieListFragment {

	protected CategoryAdapter mAdapter;


    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null && (mAdapter == null || mAdapter.isEmpty())) {
            setFirstStreamieRequest();
            executeLoader(false);
        } else {
            handleLoadFinished();
        }
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

    protected abstract void parseResults(CategoriesWrapper results) throws Stream.EmptyStreamException;

}


