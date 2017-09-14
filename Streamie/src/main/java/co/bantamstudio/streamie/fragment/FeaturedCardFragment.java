package co.bantamstudio.streamie.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import co.bantamstudio.streamie.HelperUtils;
import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.ad.AdLoader;
import co.bantamstudio.streamie.ad.StreamieAd;
import co.bantamstudio.streamie.adapter.CardAdapter;
import co.bantamstudio.streamie.auth.justintv.model.JtvStreamsWrapper;
import co.bantamstudio.streamie.auth.model.Stream;
import co.bantamstudio.streamie.auth.twitch.api.Twitch;
import co.bantamstudio.streamie.auth.twitch.model.TwitchChannelWrapper;
import co.bantamstudio.streamie.auth.twitch.model.TwitchFeaturedStreamsWrapper;
import co.bantamstudio.streamie.card.LargeTile;
import co.bantamstudio.streamie.card.Tile.ReverseCardComparator;
import co.bantamstudio.streamie.justintv.api.JustinTVStreamRequest;
import co.bantamstudio.streamie.twitch.api.TwitchFeaturedStreamsRequest;
import co.bantamstudio.streamie.twitch.api.TwitchFollowsPutRequest;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public class FeaturedCardFragment extends SherlockFragment implements OnRefreshListener {
	
	private final SpiceManager mSpiceManager = new SpiceManager( JacksonSpringAndroidSpiceService.class );
	private CardAdapter mAdapter;
	private GridView mGridView;
	private TwitchFeaturedStreamsRequest mTwitchRequest;
	private JustinTVStreamRequest mJustinRequest;
	private final static int MAX_STREAMS_PER_PROVIDER = 15;
	private View mSelectedView;
	private LargeTile mSelectedTile;
    private PullToRefreshLayout mPullLayout;
	private RelativeLayout mRelativeLayout;
	private boolean mTwitchLoaded;
	private boolean mJustinTVLoaded;
    private HelperUtils.FragmentManager listener;
    private String mTitle;
    private StreamieAd mAds;
    private LinearLayout mAdContainer;
    private ConnectionRepository mConnectionRepository;


    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_cards, null);
        mAdContainer = (LinearLayout) view.findViewById(R.id.ad_layout);
		mPullLayout = (PullToRefreshLayout) view.findViewById(R.id.ptr_layout);
        Assert.notNull(mPullLayout);

        ActionBarPullToRefresh.from(getActivity())
                .options(Options.create()
                                .scrollDistance(.5f)
                                .build())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullLayout);

		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstance) {
		mSpiceManager.start(getActivity());
        mConnectionRepository = ((StreamieApplication) getActivity().getApplication()).getConnectionRepository();
        super.onCreate(savedInstance);
		this.setHasOptionsMenu(true);

        // GET TITLE
        if (getArguments() != null)
            mTitle = getArguments().getString(StreamieApplication.TITLE);
	}

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        if (StringUtils.hasLength(mTitle))
            ((SherlockFragmentActivity) getActivity()).getSupportActionBar()
                .setTitle(mTitle);

        // CLEAR SUBTITLE
        ((SherlockFragmentActivity) getActivity()).getSupportActionBar()
                .setSubtitle(null);
    }

    @Override
    public void onAttach(Activity activity) {
        // MAKE SURE THAT THE ACTIVITY IS A FRAGMENTPUSHER
        super.onAttach(activity);
        try {
            listener = (HelperUtils.FragmentManager) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentManager");
        }
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initialize(savedInstanceState);
	}
	
	private void initialize(Bundle savedInstanceState) {
        // PREPARE AD LOADER
        mAds = AdLoader.initialize(getActivity());

        if (mAdapter == null)
		    mAdapter = new CardAdapter(getActivity(), mSpiceManager);
		mGridView = (GridView) getView().findViewById(R.id.gridView1);

        if (savedInstanceState == null && mAdapter.isEmpty()) {
            populateCards(false);
        } else {
            setLoading(false);
        }

        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
                // ANIMATE
                final LargeTile tile = (LargeTile) mGridView.getItemAtPosition(arg2);
                Assert.notNull(tile);

                if (tile.isExpanded()) {
                    // CLOSE
                    tile.closeCard(view);
                    mSelectedView = null;
                    mSelectedTile = null;
                } else {
                    // CLOSE EXISTING OPEN
                    if (mSelectedView != null && mSelectedTile != null) {
                        mSelectedTile.closeCard(mSelectedView);
                    }
                    // OPEN
                    tile.openCard(view);
                    mSelectedView = view;
                    mSelectedTile = tile;
                }
            }
        });

    }

	@Override
	public void onDestroy() {
		mSpiceManager.shouldStop();
		super.onDestroy();
        mAds.onStop();
	}
	
	private void setLoading(boolean loading) {
        if (loading) {
            mPullLayout.setRefreshing(true);
            mGridView.setVisibility(View.GONE);
            mTwitchLoaded = false;
            mJustinTVLoaded = false;
        } else if (!loading && mTwitchLoaded && mJustinTVLoaded) {
            // SHOW GRID ONCE BOTH ARE LOADED
            mGridView.setVisibility(View.VISIBLE);
            registerForContextMenu(mGridView);
            mPullLayout.setRefreshComplete();
            // TELL AD TO LOAD
            loadAd();
        }
	}

    private void loadAd() {
        if (mAds != null) {
            mAds.loadBanner(mAdContainer, mAdapter.getKeywords());
        }
    }

    @Override
    public void onRefreshStarted(View view) {
        populateCards(true);
    }

    private void populateCards(boolean forceRefresh){
        setLoading(true);
        mTwitchLoaded = false;
        mJustinTVLoaded = false;
        mAdapter.clear();

        mTwitchRequest = new TwitchFeaturedStreamsRequest(0, 0);
        long twitchCacheLen = forceRefresh?DurationInMillis.NEVER:mTwitchRequest.getCacheLength();
        mSpiceManager.execute(mTwitchRequest, mTwitchRequest.getCacheKey(), twitchCacheLen, new RequestListener<TwitchFeaturedStreamsWrapper>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                System.out.println(spiceException.getMessage());
                mTwitchLoaded = true;
            }

            @Override
            public void onRequestSuccess(TwitchFeaturedStreamsWrapper result) {
                int count = 0;
                for (Stream stream : result.getStreams()) {
                    if (count < MAX_STREAMS_PER_PROVIDER)
                        mAdapter.add(new LargeTile(stream, listener));
                    else
                        break;
                    count++;
                }
                mAdapter.sort(new ReverseCardComparator());
                mTwitchLoaded = true;
                setLoading(false);
            }
        });

        mJustinRequest = new JustinTVStreamRequest(null, null, 60, 0);
        long justinCacheLen = forceRefresh?DurationInMillis.NEVER:mJustinRequest.getCacheLength();
        mSpiceManager.execute(mJustinRequest, mJustinRequest.getCacheKey(), justinCacheLen, new RequestListener<JtvStreamsWrapper>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                System.out.println(spiceException.getMessage());
                mJustinTVLoaded = true;
            }

            @Override
            public void onRequestSuccess(JtvStreamsWrapper result) {
                int count = 0;
                for (Stream stream : result.getStreams()) {
                    if (count < MAX_STREAMS_PER_PROVIDER)
                        mAdapter.add(new LargeTile(stream, listener));
                    else
                        break;
                    count++;
                }
                mAdapter.sort(new ReverseCardComparator());
                mJustinTVLoaded = true;
                setLoading(false);
            }
        });
    }

    private static boolean isConnected(ConnectionRepository connectionRepository) {
        return connectionRepository.findPrimaryConnection(Twitch.class) != null;
    }

    // CONTEXT MENU FOR FAVORITES
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        final int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
        final String channel = mAdapter.getItem(position).getStream().getUsername();
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
    }
}
