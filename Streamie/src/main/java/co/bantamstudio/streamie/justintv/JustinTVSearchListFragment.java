package co.bantamstudio.streamie.justintv;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;

import org.springframework.util.StringUtils;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.justintv.api.JustinTVSearchRequest;

public class JustinTVSearchListFragment extends JustinTVStreamListFragment implements OnQueryTextListener{

	private MenuItem mSearchItem;
	private SearchView mSearchView;
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		
		// SUBMIT QUERY IF PRESENT
		if (StringUtils.hasLength(mKey)) {
			mSearchView.setQuery(mKey, true);
		}
		
		// SEARCH MENU ITEM
		inflater.inflate(R.menu.search, menu);
		mSearchItem = menu.findItem(R.id.menu_search);
		mSearchItem.expandActionView();
		
		mSearchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
		mSearchView.setOnQueryTextListener(this);
		mSearchView.setQueryHint(getResources().getString(R.string.justin_tv_search_channels_hint));
		mSearchView.requestFocus();
        InputMethodManager inputMethodManager =  (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(mSearchView.getApplicationWindowToken(),     InputMethodManager.SHOW_FORCED, 0);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        if (mAdapter == null || mAdapter.isEmpty())
            setEmptyText(getActivity().getResources().getString(R.string.justin_tv_search_default_empty_text));
    }

	@Override
	public boolean onQueryTextSubmit(String query) {
		// RESET PARAMETERS
		if (mAdapter != null) {
			mAdapter.clear();
			mAdapter.notifyDataSetChanged();
		}
		mOffset = 0;
		mKey = query;
		setFirstStreamieRequest();
		executeLoader(false);

        // HIDE KEYBOARD
        InputMethodManager inputMethodManager =  (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(mSearchView.getApplicationWindowToken(), 0, 0);

        return true;
	}
	
	@Override
	public boolean onQueryTextChange(String arg0) {
		// DO NOTHING
		return false;
	}
	
	@Override
	public void setEmptyText(CharSequence text) {
		if (mStreamieRequest != null)
			super.setEmptyText(getResources().getString(R.string.no_results));
		else
			super.setEmptyText(text);
	}
	
	@Override
	protected void setFirstStreamieRequest() {
		if (mKey != null)
			mStreamieRequest = new JustinTVSearchRequest(mKey, LIMIT_PER_REQUEST, mOffset);
	}
}
