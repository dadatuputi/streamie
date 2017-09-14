package co.bantamstudio.streamie.justintv;

import co.bantamstudio.streamie.auth.model.StreamsWrapper;
import co.bantamstudio.streamie.auth.model.Stream.EmptyStreamException;


import co.bantamstudio.streamie.fragment.FavoriteListFragment;

public class JustinTVFavoriteListFragment extends FavoriteListFragment {

//	@Override
//	protected List<RestClient> getRestClient(String key) {
//		
//		List<RestClient> clients = new ArrayList<RestClient>();
//		
//		if (mApplication.useLocalFavorites()) {
//			getSherlockActivity().getSupportActionBar().setTitle(getActivity().getResources().getString(R.string.local_favorites));
//			LinkedHashSet<String> favorites = mApplication.getFavorites();
//			for (String channel : favorites) {
//				clients.add(JtvApi.getFavoriteChannel(channel));
//			}
//		} else {
//			String keyNew = (key != null && !key.equalsIgnoreCase(""))?key + "'s ":"";
//			getSherlockActivity().getSupportActionBar().setTitle(keyNew + "Justin.tv Favorites");
//			clients.add(JtvApi.getFavorites(key, LIMIT_PER_REQUEST, mOffset, !mApplication.isOfflineFavorites()));
//		}
//		
//		return clients;
//	}
	
//	@Override
//	protected void parseResults(List<String> data) throws JsonSyntaxException,
//			EmptyStreamException {
//		Gson gson = new Gson();
//		
//		if (data.isEmpty()) {
//			if (mApplication.isOfflineFavorites())
//				throw new EmptyStreamException(getActivity().getResources().getString(R.string.favorites_empty_offline_enabled));
//			else
//				throw new EmptyStreamException(getActivity().getResources().getString(R.string.favorites_empty));
//		}
//		
//		ArrayList<Stream> streams = new ArrayList<Stream>();
//		for (String channel: data) {
//			streams.add(gson.fromJson(channel, JtvStream.class));
//		}
//		
//		if (streams == null || streams.isEmpty())
//			throw new EmptyStreamException(getActivity().getResources().getString(R.string.favorites_empty));	
//		
//		Collections.sort(streams);
//		Collections.reverse(streams);
//
//		if (mAdapter == null) {
//			// FIRST TIME
//			mAdapter = new StreamAdapter(getActivity(), R.layout.list_item_stream, R.id.listItem, streams);
//			this.setListAdapter(mAdapter);
//			mOffset += streams.size();
//			
//		} else if (mLoadMore) {
//			
//			// REMOVE LOADING ITEM
//			if (mLoading != null)
//				mAdapter.remove(mLoading);
//			
//			// ADD DATA TO EXISTING ADAPTER
//			for (Stream stream : streams){
//				mAdapter.add(stream);
//			}
//			mAdapter.notifyDataSetChanged();
//			
//			mOffset += streams.size();
//			
//			mLoadMore = false;
//			
//		} else {
//			mAdapter.setData(streams);
//		}
//		
//		mHasMore = LIMIT_PER_REQUEST > 0 ? streams.size() >= LIMIT_PER_REQUEST: false;
//	}
	
	@Override
	protected void parseResults(StreamsWrapper result)
			throws EmptyStreamException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void executeLoader(boolean isRefresh) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setFirstStreamieRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setNextStreamieRequest() {
		// TODO Auto-generated method stub
		
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
