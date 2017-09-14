package co.bantamstudio.streamie.ad;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;

import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.AdTargetingOptions;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;
import com.google.ads.InterstitialAd;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import lombok.Setter;

public class AdsFree implements StreamieAd, AdListener{

	@Setter private Activity activity;
	private boolean mFirstAdReceived = false;
	private AdView mAdView;
    private InterstitialAd mInterstitial;
	private AdLayout mAmazonAd;
    private StreamPlayer mStreamPlayer;
    private boolean mShouldShowInterstitial = false;
    private static final String UNIT_ID = "ENTER AD ID";
    private static final int YOUNGEST = 13;
    private static final int OLDEST = 30;

	@Override
	public void prepareInterstitial(StreamPlayer player, Set<String> keywords) {
		// PREPARE INTERSTITIAL
        mStreamPlayer = player;
		mInterstitial = new InterstitialAd(activity, UNIT_ID);
        AdRequest request = getDefaultAdRequest();
        if (keywords != null)
            request.addKeywords(keywords);
        mInterstitial.loadAd(request);
        mInterstitial.setAdListener(this);
	}

    @Override
    public void displayInterstitial() {
        if (mInterstitial.isReady()) {
            // SHOW AD IF LOADED
            mInterstitial.show();
        } else {
            // SET FLAG SO IT WILL DISPLAY WHEN IT'S READY
            mShouldShowInterstitial = true;
        }
    }

    @Override
	public boolean loadBanner(ViewGroup parent, Set<String> keywords) {

        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // USE GOOGLE ADS
            if (mAdView == null) {
                mAdView = new AdView(activity, com.google.ads.AdSize.SMART_BANNER, UNIT_ID);

                com.google.ads.AdRequest request = getDefaultAdRequest();
                if (keywords != null)
                    request.addKeywords(keywords);
                parent.addView(mAdView);
                mAdView.loadAd(new  com.google.ads.AdRequest());
                mAdView.setAdListener(this);
            } else {
                reloadBanner(keywords);
            }
        } else {
            // SET UP AMAZON ADS
            initializeAmazonApps(activity);

            // AMAZON ADS
            if (mAmazonAd == null){
                mAmazonAd = new AdLayout(activity);
                parent.addView(mAmazonAd);
                mAmazonAd.loadAd(getAmazonAdTargetingOptions());
            } else {
                reloadBanner(keywords);
            }
        }

		return true;
	}
	
	@Override
	public void reloadBanner(Set<String> keywords) {
		if (mAdView != null) {
			com.google.ads.AdRequest request = getDefaultAdRequest();
			if (keywords != null)
				request.addKeywords(keywords);
			mAdView.loadAd(request);
			mAdView.setAdListener(this);
		}
		if (mAmazonAd != null){
			mAmazonAd.loadAd(getAmazonAdTargetingOptions());
		}
	}

	@Override
	public void onDismissScreen(com.google.ads.Ad arg0) {
        mStreamPlayer.playStream();
	}

	@Override
	public void onFailedToReceiveAd(com.google.ads.Ad arg0, ErrorCode arg1) {
        if (arg0 == mInterstitial){
            mStreamPlayer.playStream();
        }

//		if (!mFirstAdReceived) {
//			// Hide adview and show custom ad
//			if (mAdView != null)
//				mAdView.setVisibility(View.GONE);
//			if (mAdContainer != null){
//				LinearLayout mAdCustom = (LinearLayout) View.inflate(activity, co.bantamstudio.streamie.R.layout.ad_custom, mAdContainer);
//				mAdCustom.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						// LOAD MARKET
//						Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(StreamieApplication.STREAMIE_PRO_LINK));
//						activity.startActivity(intent);
//					}
//				});
//			}
//		}
	}

	@Override
	public void onLeaveApplication(com.google.ads.Ad arg0) {
		// DO NOTHING
	}

	@Override
	public void onPresentScreen(com.google.ads.Ad arg0) {
		// DO NOTHING
	}

	@Override
	public void onReceiveAd(com.google.ads.Ad arg0) {

        if (arg0 == mAdView) {
            mFirstAdReceived = true;
            mAdView.setVisibility(View.VISIBLE);
        } else if (arg0 == mInterstitial && mShouldShowInterstitial) {
            mInterstitial.show();
            mShouldShowInterstitial = false;
        }
	}

	@Override
	public void onStop() {
		if (mAdView != null)
			mAdView.destroy();
		if (mAmazonAd != null)
			mAmazonAd.destroy();
	}

    @Override
    public void onPause() {
        // STOP LOADING THE AD IF THEY BACK OUT
        if (mInterstitial != null) {
            mInterstitial.stopLoading();
        }
    }

    private static AdRequest getDefaultAdRequest(){
        AdRequest adRequest = new AdRequest();
        adRequest.setGender(AdRequest.Gender.MALE);

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) - getAge());
        adRequest.setBirthday(c);

        // Set up a list of common keywords
        Set<String> keywords = new HashSet<String>();
        keywords.add("justin.tv");
        keywords.add("twitch.tv");
        keywords.add("streaming");
        keywords.add("media");
        adRequest.addKeywords(keywords);

        return adRequest;
    }

    private static int getAge(){
        // Birthday - random
        Random rand = new Random();
        return rand.nextInt(OLDEST - YOUNGEST + 1) + YOUNGEST;
    }

    private static void initializeAmazonApps(Context applicationContext) {
        String appKey = "57f40f0337c243adaafd818aed36030a";
        // ENABLE DEBUGGING
        //AdRegistration.enableLogging(applicationContext, true);
        // ENABLE TESTING
        //AdRegistration.enableTesting(applicationContext, true);
        // ENABLE ADS
        AdRegistration.setAppKey(appKey);
    }

    private static AdTargetingOptions getAmazonAdTargetingOptions() {
        AdTargetingOptions opt = new AdTargetingOptions().setGender(AdTargetingOptions.Gender.MALE);
        opt.setAge(getAge());
        return opt;
    }
}
