package co.bantamstudio.streamie.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

import org.springframework.social.connect.ConnectionRepository;

import co.bantamstudio.streamie.HelperUtils;
import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import lombok.Getter;

public abstract class AbstractAuthFragment extends SherlockFragment {
	@Getter
    private WebView webView;
	protected StreamieApplication mApplication;
    protected ConnectionRepository mConnectionRepository;
	protected final SpiceManager mSpiceManager = new SpiceManager( JacksonSpringAndroidSpiceService.class );
    private HelperUtils.FragmentManager mFragmentManager;
    private HelperUtils.DrawerHandler mDrawerManager;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frame_layout_waiting, null);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpiceManager.start(getActivity());
    }

    @Override
    public void onAttach(Activity activity) {
        // MAKE SURE THAT THE ACTIVITY IS A FRAGMENTPUSHER
        super.onAttach(activity);
        try {
            mFragmentManager = (HelperUtils.FragmentManager) activity;
            mDrawerManager = (HelperUtils.DrawerHandler) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentManager");
        }
    }

    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mApplication = (StreamieApplication) getActivity().getApplication();
        FrameLayout mContainer = (FrameLayout) getActivity().findViewById(R.id.container);

		mConnectionRepository = mApplication.getConnectionRepository();
		
		CookieSyncManager.createInstance(getActivity());
		this.webView = new WebView(getActivity());
		getWebView().setBackgroundColor(0x00000000);
		//getWebView().getSettings().setJavaScriptEnabled(true);
		//getWebView().getSettings().setPluginState(PluginState.ON);
		getWebView().getSettings().setSupportZoom(false);
		getWebView().getSettings().setLoadWithOverviewMode(true);
		getWebView().getSettings().setDefaultZoom(ZoomDensity.FAR);
		getWebView().getSettings().setSavePassword(false);
		
		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				getActivity().setTitle("Loading...");
				getActivity().setProgress(progress * 100);
				if (progress == 100) {
					getActivity().setTitle(R.string.app_name);
				}
			}
		});
		
		mContainer.addView(getWebView());
	}
	
	@Override
	public void onDestroy() {
		mSpiceManager.shouldStop();
		super.onDestroy();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		CookieManager.getInstance().removeAllCookie();
	}
	
	@Override
	public void onStart() {
		super.onStart();

		getActivity().getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);
	}
	
	protected void onCanceled() {
        Toast.makeText(mApplication, getAuthUnsuccessful(), Toast.LENGTH_LONG).show();
        //mFragmentManager.popFragment(this);
        getActivity().onBackPressed();
   	}

	protected void onAuthenticated(String token) {
        Toast.makeText(mApplication, getAuthSuccessful(), Toast.LENGTH_LONG).show();
		mDrawerManager.refreshDrawer();
        //mFragmentManager.popFragment(this);
        getActivity().onBackPressed();
	}
	
	protected abstract String getAuthSuccessful();
	protected abstract String getAuthUnsuccessful();
}
