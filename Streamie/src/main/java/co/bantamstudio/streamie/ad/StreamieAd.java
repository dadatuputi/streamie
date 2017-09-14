package co.bantamstudio.streamie.ad;

import android.app.Activity;
import android.view.ViewGroup;

import java.util.Set;

public interface StreamieAd {
	
	public void setActivity(Activity activity);
	public void prepareInterstitial(StreamPlayer player, Set<String> keywords);
    public void displayInterstitial();
	public boolean loadBanner(ViewGroup parent, Set<String> keywords);
	public void onStop();
    public void onPause();
	public void reloadBanner(Set<String> keywords);
	
	public interface AdKeyword {
		public Set<String> getKeywords();
	}
	
	public static interface StreamPlayer {
		public void playStream();
	}
}
