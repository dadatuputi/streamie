package co.bantamstudio.streamie.ad;

import android.app.Activity;

public class AdLoader {
	
	private static final String BASE_PACKAGE = "co.bantamstudio.streamie.ad.";

	@SuppressWarnings("unchecked")
	public static StreamieAd initialize(Activity activity){
		Class<? extends StreamieAd> adClass = null;
		try {
			try {
				// TRY TO LOAD THE PRO CLASS (NO ADS)			
				adClass = (Class<? extends StreamieAd>) Class.forName(BASE_PACKAGE + "AdsPro");
			} catch (ClassNotFoundException e){
				// TRY TO LOAD THE FREE CLASS (ADS)
				try {
					adClass = (Class<? extends StreamieAd>) Class.forName(BASE_PACKAGE + "AdsFree");
				} catch (ClassNotFoundException e1) {
					// NO ADS CLASS - KILL PROGRAM
					System.exit(0);
				}
			}
		} catch (ClassCastException e) {
			//maybe this is the way to do it.
		}
		
		try {
			StreamieAd ad = adClass.newInstance();
			ad.setActivity(activity);
			return ad;
		} catch (InstantiationException e) {
			// NO ADS CLASS - KILL PROGRAM
			System.exit(0);
		} catch (IllegalAccessException e) {
			// NO ADS CLASS - KILL PROGRAM
			System.exit(0);
		}		
		return null;
	}
}
