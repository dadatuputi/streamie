package co.bantamstudio.streamie.drawer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.octo.android.robospice.SpiceManager;

import org.springframework.util.StringUtils;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.adapter.DrawerAdapter;
import co.bantamstudio.streamie.adapter.DrawerAdapter.RowType;
import co.bantamstudio.streamie.auth.model.Profile;

public abstract class DrawerProfile extends DrawerItem {
	private final ImageLoader mImageLoader;
	Profile mProfile;
	private DisplayImageOptions mDisplayOptions;
	final SpiceManager mSpiceManager;
	private String mLoadingText1;
	private String mLoadingText2;
	final Context mContext;
	DrawerAdapter mDrawerAdapter;

	DrawerProfile(Context context, SpiceManager spiceManager, String loadingText1, String loadingText2) {
		mSpiceManager = spiceManager;
		mImageLoader = ImageLoader.getInstance();
		mLoadingText1 = loadingText1;
		mLoadingText2 = loadingText2;
		mContext = context;

		mDisplayOptions = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.bitmapConfig(Bitmap.Config.ARGB_8888).delayBeforeLoading(0)
				.displayer(new FadeInBitmapDisplayer(0)).build();
	}

	@Override
	public int getViewType() {
		return RowType.HEADER_ITEM.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view = inflater.inflate(R.layout.list_item_drawer_image_two_line, null);

		TextView text1 = (TextView) view.findViewById(R.id.text1);
		TextView text2 = (TextView) view.findViewById(R.id.text2);
		final ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
		final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);

		// IS PROFILE LOADED YET?
		if (mProfile == null) {
			// SET SPINNER VISIBLE, LOADING TEXT
			progressBar.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.INVISIBLE);
			
			text1.setText(mLoadingText1);
			
			if (StringUtils.hasText(mLoadingText2)) {
				text2.setText(mLoadingText2);
				text2.setVisibility(View.VISIBLE);
			} else {
				text2.setVisibility(View.GONE);
			}
			
			
		} else {
			// SHOW PROFILE
			if (StringUtils.hasText(mProfile.getPicture())) {
				mImageLoader.displayImage(mProfile.getPicture(), imageView,
						mDisplayOptions, new ImageLoadingListener() {

							@Override
							public void onLoadingCancelled(String arg0, View arg1) {
								progressBar.setVisibility(View.GONE);
								imageView.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingComplete(String arg0, View arg1,
									Bitmap arg2) {
								progressBar.setVisibility(View.GONE);
								imageView.setVisibility(View.VISIBLE);
							}

							@Override
							public void onLoadingFailed(String arg0, View arg1,
									FailReason arg2) {
								progressBar.setVisibility(View.GONE);
								imageView.setVisibility(View.GONE);
							}

							@Override
							public void onLoadingStarted(String arg0, View arg1) {
								progressBar.setVisibility(View.VISIBLE);
								imageView.setVisibility(View.INVISIBLE);
							}

						});
			} else {
				imageView.setVisibility(View.GONE);
				progressBar.setVisibility(View.GONE);
			}
			
			if (StringUtils.hasText(mProfile.getPrimaryTagline())){
				text1.setText(mProfile.getPrimaryTagline());
			}
			if (StringUtils.hasText(mProfile.getSecondaryTagline())){
				text2.setText(mProfile.getSecondaryTagline());
				text2.setVisibility(View.VISIBLE);
			} else {
				text2.setVisibility(View.GONE);
			}
			
		}

		return view;
	}
}