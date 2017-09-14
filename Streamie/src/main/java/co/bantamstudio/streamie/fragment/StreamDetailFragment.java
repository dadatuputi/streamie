package co.bantamstudio.streamie.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.activity.AdobeFlashActivity;
import co.bantamstudio.streamie.ad.AdLoader;
import co.bantamstudio.streamie.ad.StreamieAd;
import co.bantamstudio.streamie.ad.StreamieAd.AdKeyword;
import co.bantamstudio.streamie.auth.model.Stream;
import co.bantamstudio.streamie.auth.twitch.model.TwitchStream;
import co.bantamstudio.streamie.twitch.TwitchStreamDetailFragment;
import uk.co.senab.actionbarpulltorefresh.extras.actionbarsherlock.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.Options;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public abstract class StreamDetailFragment extends SherlockFragment implements
		AdKeyword,
        OnRefreshListener,
        StreamieAd.StreamPlayer{

	protected StreamieApplication mApplication;
	protected MenuItem mRefreshMenu;
	protected Stream mStream;
	private TextView mStreamName;
	private TextView mBroadcasterName;
	private TextView mNumViewers;
	private TextView mShortDescription;
	private TextView mSubtitle;
	private TextView mDescription;
	private LinearLayout mDescriptions;
	private ImageView mScreenshot;
	private ProgressBar mImageProgressSpinner;
	private ImageLoader mImageLoader;
	private FrameLayout mPlayImage;
	protected Intent mIntent;
	protected boolean clickedPlay;
	private DisplayImageOptions mDisplayOptions;
	private StreamieAd mAds;
	private MenuItem mFavoriteMenu;
	private ImageView mProfileImage;
	private String mTitle;
	private ImageView mPlayButton;
	protected String mJson;
	private PLAY_TYPE mPlayType;
	private Button mPlayButtonFlash;
	private Button mPlayButtonHLS;
    protected PullToRefreshLayout mPullLayout;
    protected String mKey;
    protected final SpiceManager mSpiceManager = new SpiceManager( JacksonSpringAndroidSpiceService.class );
    private String mSubtitleString;

    protected enum PLAY_TYPE { FLASH, FLASH_BROWSER, HLS }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mSpiceManager.start(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        mSpiceManager.shouldStop();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set title
        if (StringUtils.hasLength(mTitle))
            ((SherlockFragmentActivity) getActivity()).getSupportActionBar()
                    .setTitle(mTitle);
        if (StringUtils.hasLength(mSubtitleString))
            ((SherlockFragmentActivity) getActivity()).getSupportActionBar()
                    .setSubtitle(mSubtitleString);
        else
            ((SherlockFragmentActivity) getActivity()).getSupportActionBar()
                    .setSubtitle(null);
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        // PREPARE AD LOADER
        mAds = AdLoader.initialize(getActivity());
        mApplication = (StreamieApplication) getActivity().getApplication();

        // GET CHANNEL FROM ARGUMENTS
        if (getArguments() != null) {
            mTitle = getArguments().getString(StreamieApplication.TITLE);
            mSubtitleString = getArguments().getString(StreamieApplication.SUBTITLE);
            mKey = getArguments().getString(StreamieApplication.KEY);
            mJson = getArguments().getString(Stream.SERIALIZED_JSON);
        }

        if (mStream == null) {
            if (StringUtils.hasLength(mJson)){
                // TRY TO BUILD STREAM FROM JSON
                parseJson(mJson);
            } else {
                // BUILD STREAM FROM KEY
                executeLoader(false);
            }
        } else {
            updateView(mStream);
        }



        this.setHasOptionsMenu(true);
	}
	
	@Override
	public void onPause() {
		super.onPause();

        if (mAds != null)
            mAds.onPause();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// CREATE FAVORITES MENU
        if (this instanceof TwitchStreamDetailFragment) {
            inflater.inflate(R.menu.favorite, menu);
            final MenuItem favorite = menu.findItem(R.id.menu_favorite);
            favorite.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
    //				if (mApplication.isFavorite(mStream.getUsername())) {
    //					// TOGGLE OFF
    //					toggleStar(false, mStream.getUsername());
    //				} else {
    //					// TOGGLE ON
    //					toggleStar(true, mStream.getUsername());
    //				}
    //				return true;
                    addToFavorites(mStream.getUsername());
                    return true;
                }
			
    //			private void toggleStar(boolean activate, String channel) {
    //
    //				if (activate) {
    //					favorite.setIcon(R.drawable.ic_menu_star_selected);
    //					addToFavorites(mStream.getUsername());
    //				} else {
    //					favorite.setIcon(R.drawable.ic_menu_star);
    //					removeFromFavorites(mStream.getUsername());
    //				}
    //
    //				String toastMessage = activate? getActivity().getResources().getString(R.string.context_added_to_favorites) : getActivity().getResources().getString(R.string.context_removed_from_favorites);
    //				toastMessage = String.format(toastMessage, mStream.getUsername());
    //				Toast toast = Toast.makeText(StreamDetailFragment.this.getActivity(), toastMessage, Toast.LENGTH_SHORT);
    //				toast.show();
    //			}
            });
        }
		
//		if (mApplication.isFavorite(mStream.getUsername()))
//			favorite.setIcon(R.drawable.ic_menu_star_selected);

		
		// SHARE BUTTON
		inflater.inflate(R.menu.share, menu);
		MenuItem shareMenu = menu.findItem(R.id.menu_share);
		shareMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				if (mStream != null && mStream.getUrl() != null && mStream.getTitle() != null) {
					mApplication.shareStream(StreamDetailFragment.this.getActivity(), mStream.getTitle(), mStream.getUrl());
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public Set<String> getKeywords() {
        Set<String> keywords = new HashSet<String>();
        if (mStream instanceof TwitchStream && mStream.getGame() != null)
            keywords.add(mStream.getGame());
        else
            keywords.add(mStream.getTitle());
        return keywords;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_stream_details, null);

        // SET UP PULL TO REFRESH
        mPullLayout = (PullToRefreshLayout) view.findViewById(R.id.ptr_layout);
        Assert.notNull(mPullLayout);

        ActionBarPullToRefresh.from(getActivity())
                .options(Options.create()
                        .scrollDistance(.5f)
                        .build())
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullLayout);
        mPullLayout.setRefreshing(true);


        mStreamName = (TextView) view.findViewById(R.id.streamDetailStreamName);
		mBroadcasterName = (TextView) view.findViewById(R.id.streamDetailBroadcaster);
		mProfileImage = (ImageView) view.findViewById(R.id.streamDetailProfileImage);
		mNumViewers = (TextView) view.findViewById(R.id.streamDetailStreamViewers);
		mShortDescription = (TextView) view.findViewById(R.id.streamDetailShortDescription);
		mSubtitle = (TextView) view.findViewById(R.id.streamDetailsSubtitle);
		mDescription = (TextView) view.findViewById(R.id.streamDetailsDescription);
		mDescriptions = (LinearLayout) view.findViewById(R.id.streamDetailDescriptions);
		mScreenshot = (ImageView) view.findViewById(R.id.streamDetailsImage);
		mImageProgressSpinner = (ProgressBar) view.findViewById(R.id.imageProgressBar);
		mImageLoader = ImageLoader.getInstance();
		mPlayImage = (FrameLayout) view.findViewById(R.id.streamDetailsPlayImage);
		mPlayButtonFlash = (Button) view.findViewById(R.id.streamDetailButtonViewFlash);
		mPlayButtonHLS = (Button) view.findViewById(R.id.streamDetailButtonViewHLS);
		mPlayButton = (ImageView) view.findViewById(R.id.play_button);
		mPlayImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

                if (mStream != null && !mStream.isOnline()) {
                    // DO NOTHING IF OFFLINE
                    return;
                } else if (!mApplication.isAdobeFlashInstalled()) {
                    // FLASH NOT INSTALLED
                    Intent intent = new Intent(getActivity().getApplicationContext(), AdobeFlashActivity.class);
                    startActivity(intent);
                    return;
                }

                if (mApplication.isBrowserPlaybackEnabled()) {
                    // USE DIFFERENT BROWSER
                    mPlayType = PLAY_TYPE.FLASH_BROWSER;
                } else  {
                    // PLAY WITH REGULAR FLAH WEBVIEW
                    mPlayType = PLAY_TYPE.FLASH;
                }

                prepareStream();
			}
		});
		mPlayButtonFlash.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPlayImage.performClick();
			}
		});
		mPlayButtonHLS.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mStream != null && mStream.isOnline()) {
					mPlayType = PLAY_TYPE.HLS;
					prepareStream();
				}
			}
		});
		
		
		
		mDisplayOptions = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.ARGB_8888).delayBeforeLoading(1000)
				.displayer(new FadeInBitmapDisplayer(500)).build();

		return view;
	}
	
	@Override
	public void onStart() {
        mAds.prepareInterstitial(this, getKeywords());
		super.onStart();
	}

    protected abstract void executeLoader(boolean isRefresh);
    protected abstract void parseJson(String json);

	protected void updateView(Stream stream) {

        Assert.notNull(stream);

        // LOAD SCREENSHOT
        mImageLoader.displayImage(
                stream.getScreenshot(),
                mScreenshot, mDisplayOptions, new ImageLoadingListener() {

                    @Override
                    public void onLoadingCancelled(String arg0, View arg1) {
                        mImageProgressSpinner.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String arg0, View arg1,
                            Bitmap arg2) {
                        mImageProgressSpinner.setVisibility(View.GONE);

                    }

                    @Override
                    public void onLoadingFailed(String arg0, View arg1,
                            FailReason arg2) {
                        mImageProgressSpinner.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingStarted(String arg0, View arg1) {
                        mImageProgressSpinner.setVisibility(View.VISIBLE);
                    }
                });

        // SET SUBTITLE
        if (StringUtils.hasLength(stream.getTitle()) &&
                StringUtils.hasLength(stream.getChannelTitle()) &&
                !stream.getChannelTitle().equalsIgnoreCase(stream.getTitle())) {
            mSubtitle.setText(stream.getChannelTitle());
        } else {
            mSubtitle.setVisibility(View.GONE);
        }

        // SET NUM VIEWERS
        if (stream.isOnline()) {
            mNumViewers.setText(Integer.toString(stream.getViewers()));
            mNumViewers.setVisibility(View.VISIBLE);
        } else {
            mNumViewers.setVisibility(View.GONE);
        }

        // SET ICON BASED ON THEME
        if (mApplication.isDark())
            mNumViewers.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.viewers_inverted, 0, 0, 0);
        else
            mNumViewers.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.viewers, 0, 0, 0);

        // SET BROADCASTER INFO
        if (StringUtils.hasLength(stream.getNiceUsername()))
            mBroadcasterName.setText(stream.getNiceUsername());
        else
            mBroadcasterName.setText(stream.getUsername());

        if (StringUtils.hasLength(stream.getProfileImage())){
            mImageLoader.displayImage(stream.getProfileImage(), mProfileImage, StreamieApplication.getDisplayImageOptions());
        }

        // SET DESCRIPTIONS
        if (StringUtils.hasLength(stream.getShortDescription())) {
            mShortDescription.setText(Html.fromHtml(stream.getShortDescription()));
            mShortDescription.setMovementMethod(LinkMovementMethod.getInstance());
        }
        if (StringUtils.hasLength(stream.getDescription())) {
            mDescription.setText(Html.fromHtml(stream.getDescription()));
            mDescription.setMovementMethod(LinkMovementMethod.getInstance());
        }
        if (StringUtils.hasLength(stream.getShortDescription()) || StringUtils.hasLength(stream.getDescription()))
            mDescriptions.setVisibility(View.VISIBLE);

        // CHECK IF CHANNEL IS FAVORITE
        if (mApplication.isFavorite(stream.getUsername()) && mFavoriteMenu != null) {
            mFavoriteMenu.setIcon(R.drawable.ic_menu_star_selected);
        }

        // SET BUTTONS
        if (stream.isOnline()) {
            mPlayButtonFlash.setVisibility(View.VISIBLE);
        } else {
            mPlayButtonFlash.setVisibility(View.GONE);
        }
        if (mApplication.isHLS(getClass()) && stream.isOnline()) {
            mPlayButtonHLS.setVisibility(View.VISIBLE);
        } else {
            mPlayButtonHLS.setVisibility(View.GONE);
        }

        loading(false);
	}

	void prepareStream() {
    	loading(true);
		mAds.displayInterstitial();
	}

    @Override
    public void playStream() {
        Assert.notNull(mStream);
        Assert.notNull(mPlayType);
        Assert.hasLength(mStream.getUsername());
        playStream(mStream.getUsername(), mPlayType);
    }

    protected abstract void playStream(String channel, PLAY_TYPE playType);
	
	protected abstract void addToFavorites(String channel);
	
	protected abstract void removeFromFavorites(String channel);

	protected void loading(boolean isLoading) {
        mPullLayout.setRefreshing(isLoading);
        getView().setVisibility(isLoading?View.GONE:View.VISIBLE);
	}

    @Override
    public void onRefreshStarted(View view) {
        loading(true);
        executeLoader(true);
    }
}
