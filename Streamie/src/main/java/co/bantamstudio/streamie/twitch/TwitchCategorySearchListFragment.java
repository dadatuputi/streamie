package co.bantamstudio.streamie.twitch;
import co.bantamstudio.streamie.R;

import java.util.ArrayList;
import java.util.List;

import co.bantamstudio.streamie.auth.model.CategoriesWrapper;
import co.bantamstudio.streamie.auth.model.Category;
import co.bantamstudio.streamie.auth.model.Stream.EmptyStreamException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import android.content.Context;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import co.bantamstudio.streamie.twitch.api.TwitchCategorySearchRequest;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;

public class TwitchCategorySearchListFragment extends TwitchCategoryListFragment implements OnQueryTextListener {
	
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
		mSearchView.setQueryHint(getResources().getString(R.string.twitch_search_games_hint));
		mSearchView.requestFocus();
        InputMethodManager inputMethodManager =  (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(mSearchView.getApplicationWindowToken(),     InputMethodManager.SHOW_FORCED, 0);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        if (mAdapter == null || mAdapter.isEmpty())
            setEmptyText(getActivity().getResources().getString(R.string.search_default_empty_text));
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
		executeLoader(true);

        // HIDE KEYBOARD
        InputMethodManager inputMethodManager =  (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(mSearchView.getApplicationWindowToken(), 0, 0);

		return true;
	}
	
	@Override
	protected void setFirstStreamieRequest() {
		if (StringUtils.hasLength(mKey))
			mStreamieRequest = new TwitchCategorySearchRequest(mKey, LIMIT_PER_REQUEST, mOffset);
	}
	protected void setNextStreamieRequest() {
		// GAME SEARCH RESULTS AREN'T PAGINATED
		mStreamieRequest = null;
	}

	@Override
	public void setEmptyText(CharSequence text) {
        if (mStreamieRequest != null)
            super.setEmptyText(getResources().getString(R.string.no_results));
        else
            super.setEmptyText(text);
	}
	
	@Override
	public boolean onQueryTextChange(String arg0) {
		// DO NOTHING
		return false;
	}

	@Override
	protected List<Category> buildList(CategoriesWrapper results,
			boolean firstRequest) throws EmptyStreamException {
		if (results == null || CollectionUtils.isEmpty(results.getCategories()))
			throw new EmptyStreamException("Game results are empty");

		return new ArrayList<Category>(results.getCategories());
	}

}
