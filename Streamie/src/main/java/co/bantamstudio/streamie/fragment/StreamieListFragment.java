package co.bantamstudio.streamie.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import co.bantamstudio.streamie.HelperUtils;
import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.ad.AdLoader;
import co.bantamstudio.streamie.ad.StreamieAd;
import lombok.Getter;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public abstract class StreamieListFragment extends SherlockListFragment implements
        StreamieAd.AdKeyword, OnRefreshListener {

    protected static final int LIMIT_PER_REQUEST = 25;
    protected static final int OFFSET_INITIAL = 0;
    protected int mOffset = OFFSET_INITIAL;
    protected StreamieApplication mApplication;
    protected String mKey;
    protected boolean mHasMore = true;
    protected boolean mLoadingMore = false;
    protected final SpiceManager mSpiceManager = new SpiceManager( JacksonSpringAndroidSpiceService.class );
    protected PullToRefreshLayout mPullToRefreshLayout;
    @Getter protected HelperUtils.FragmentManager listener;
    private String mTitle;
    private String mSubtitle;
    ViewGroup mAdContainer;
    StreamieAd mAds;
    private TextView mErrorText;
    private ListView mListView;
    private HelperUtils.DrawerHandler mDrawerHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_layout_pull_ad, null);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mAdContainer = (ViewGroup) view.findViewById(R.id.ad_layout);
        mErrorText = (TextView) view.findViewById(R.id.emptyText);
        mPullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.ptr_layout);
        Assert.notNull(mPullToRefreshLayout);

        ActionBarPullToRefresh.from(getActivity())
                .options(Options.create()
                        .scrollDistance(.5f)
                        .build())
                .theseChildrenArePullable(android.R.id.list)
                .listener(this)
                .setup(mPullToRefreshLayout);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        mSpiceManager.start(getActivity());
        super.onCreate(savedInstance);
        this.setHasOptionsMenu(true);
        initialize();
    }

    void initialize(){
        mApplication = (StreamieApplication) getActivity().getApplication();

        if (getArguments() != null) {
            mTitle = getArguments().getString(StreamieApplication.TITLE);
            mSubtitle = getArguments().getString(StreamieApplication.SUBTITLE);
            mKey = getArguments().getString(StreamieApplication.KEY);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        if (StringUtils.hasLength(mTitle))
            ((SherlockFragmentActivity) getActivity()).getSupportActionBar()
                    .setTitle(mTitle);
        if (StringUtils.hasLength(mSubtitle))
            ((SherlockFragmentActivity) getActivity()).getSupportActionBar()
                    .setSubtitle(mSubtitle);
        else
            ((SherlockFragmentActivity) getActivity()).getSupportActionBar()
                    .setSubtitle(null);
    }

    @Override
    public void onDestroy() {
        mSpiceManager.shouldStop();
        super.onDestroy();
        mAds.onStop();
    }

    @Override
    public void onAttach(Activity activity) {
        // MAKE SURE THAT THE ACTIVITY IS A FRAGMENTPUSHER
        super.onAttach(activity);
        try {
            listener = (HelperUtils.FragmentManager) activity;
            mDrawerHandler = (HelperUtils.DrawerHandler) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentManager");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // PREPARE AD LOADER
        mAds = AdLoader.initialize(getActivity());
    }


    protected void handleException(Exception e) {
        mLoadingMore  = false;
        handleLoadFinished();
        setEmptyText(e.getMessage());
    }

    void handleLoadFinished() {
        mPullToRefreshLayout.setRefreshComplete();
        registerForContextMenu(getListView());
    }

    @Override
    public void onRefreshStarted(View view) {
        mOffset = OFFSET_INITIAL;
        setFirstStreamieRequest();
        executeLoader(true);
    }

    @Override
    public void setEmptyText(CharSequence text) {
        mListView.setVisibility(View.GONE);
        mErrorText.setVisibility(View.VISIBLE);
        mErrorText.setText(text);
    }

    @Override
    public void setListAdapter(ListAdapter adapter) {
        mErrorText.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);
        super.setListAdapter(adapter);
    }




    @Override
    public abstract void onListItemClick(ListView l, View v, int position, long id);

    protected abstract void setFirstStreamieRequest();

    protected abstract void setNextStreamieRequest();

    protected abstract void executeLoader(boolean isRefresh);

}
