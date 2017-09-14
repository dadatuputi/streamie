package co.bantamstudio.streamie.twitch;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.Preference;
import android.util.AttributeSet;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.util.Assert;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.activity.GenericFragmentActivity;
import co.bantamstudio.streamie.auth.twitch.api.Twitch;

public class TwitchAuthPreference extends Preference {
	
	private final ConnectionRepository mConnectionRepository;
	private final StreamieApplication mApplication;
	private final Context mContext;

	public TwitchAuthPreference(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		mApplication = (StreamieApplication) context.getApplicationContext();
        Assert.notNull(mApplication);
		mConnectionRepository = mApplication.getConnectionRepository();
		mContext = context;
		
		setTitle();
		
		this.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				if (mConnectionRepository.findPrimaryConnection(Twitch.class) != null) {
					// USER IS CONNECTED - DISCONNECT
					// SHOW DIALOG PROMPTING IF USER IS SURE
					DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case DialogInterface.BUTTON_POSITIVE:
								// DISCONNECT
								mConnectionRepository.removeConnections("twitch");
								// SWITCH BUTTON TEXT
								setTitle(R.string.pref_twitch_log_in_title);
							case DialogInterface.BUTTON_NEGATIVE:
								// DISMISS
								dialog.dismiss();
								break;
							}
						}
					};
					
					AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
					builder.setMessage(mContext.getResources().getString(R.string.pref_twitch_log_out_confirmation)).setPositiveButton(mContext.getResources().getString(R.string.yes), dialogClickListener)
					    .setNegativeButton(mContext.getResources().getString(R.string.no), dialogClickListener).show();
				} else {
					// USER IS NOT CONNECTED - START ACTIVITY
	    			Intent intent = new Intent(mContext.getApplicationContext(), GenericFragmentActivity.class);
	    			intent.putExtra(StreamieApplication.FRAGMENT, TwitchAuthFragment.class.getName());
	    			mContext.startActivity(intent);
				}
				return true;
			}
		});
	}
	
	public void setTitle() {
		if (mConnectionRepository.findPrimaryConnection(Twitch.class) != null) {
			// USER IS CONNECTED
			this.setTitle(R.string.pref_twitch_log_out_title);
		} else {
			this.setTitle(R.string.pref_twitch_log_in_title);
		}
	}

}
