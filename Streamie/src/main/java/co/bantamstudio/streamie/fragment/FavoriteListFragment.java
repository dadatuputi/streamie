package co.bantamstudio.streamie.fragment;

import org.springframework.social.connect.ConnectionRepository;

import co.bantamstudio.streamie.adapter.KeywordAdapter;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public abstract class FavoriteListFragment extends StreamListFragment implements OnScrollListener,
		KeywordAdapter {

	protected ConnectionRepository mConnectionRepository;
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// DO NOTHING
	}
}
