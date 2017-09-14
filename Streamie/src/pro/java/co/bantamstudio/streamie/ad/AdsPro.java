package co.bantamstudio.streamie.ad;

import java.util.Set;

import android.app.Activity;
import android.view.ViewGroup;

// TODO: Auto-generated Javadoc
/**
 * The Class AdsProtest.
 */
public class AdsPro implements StreamieAd {

    private StreamPlayer mStreamPlayer;

    public void setActivity(Activity activity){
		// DO NOTHING
	}

    @Override
    public void prepareInterstitial(StreamPlayer player, Set<String> keywords) {
        mStreamPlayer = player;
    }

    @Override
    public void displayInterstitial() {
        mStreamPlayer.playStream();
    }

    @Override
	public boolean loadBanner(ViewGroup parent, Set<String> keywords) {
		return false;
	}

    @Override
    public void onPause() {
        // DO NOTHING
    }

    @Override
    public void onStop() {
        // DO NOTHING
    }

    @Override
	public void reloadBanner(Set<String> keywords) {
		// DO NOTHING
	}
}
