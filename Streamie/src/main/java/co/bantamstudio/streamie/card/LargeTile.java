package co.bantamstudio.streamie.card;
import co.bantamstudio.streamie.HelperUtils;
import co.bantamstudio.streamie.R;

import co.bantamstudio.streamie.auth.justintv.model.JustinTVStream;
import co.bantamstudio.streamie.auth.model.Stream;
import co.bantamstudio.streamie.auth.twitch.model.TwitchStream;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import co.bantamstudio.streamie.adapter.CardAdapter.TileType;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class LargeTile extends Tile {

	public LargeTile(Stream stream, HelperUtils.FragmentManager pusher) {
		super(stream, pusher);
	}

	@Override
	public int getViewType() {
		return TileType.LARGE_TILE.ordinal();
	}

    private class ViewHolder {
        public TextView title;
        public TextView game;
        public TextView viewers;
        public TextView description;
        public TextView watch;
        public ImageView screenshot;
        public ImageView logo;
        public LinearLayout expando;
    }

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view = convertView;
        final ViewHolder holder;

		if (convertView == null || convertView.getTag() == null) {
			view = inflater.inflate(R.layout.card_large, null);
            Assert.notNull(view);
            holder = new ViewHolder();

            holder.title = (TextView) view.findViewById(R.id.textView1);
            holder.game = (TextView) view.findViewById(R.id.textView2);
            holder.viewers = (TextView) view.findViewById(R.id.textView3);
            holder.description = (TextView) view.findViewById(R.id.textView4);
            holder.watch = (TextView) view.findViewById(R.id.textView5);
            holder.screenshot = (ImageView) view.findViewById(R.id.imageView1);
            holder.logo = (ImageView)view.findViewById(R.id.imageView3);
            holder.expando = (LinearLayout) view.findViewById(R.id.linearLayout2);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
		}

		holder.title.setText(stream.getTitle());
		holder.game.setText(stream.getGame());
		holder.viewers.setText(String.valueOf(stream.getViewers()));
		
		// SHOW DESCRIPTION
		if (StringUtils.hasText(stream.getDescription())) {
			holder.description.setText(Html.fromHtml(stream.getDescription()));
			holder.description.setVisibility(View.VISIBLE);
		} else {
			holder.description.setVisibility(View.GONE);
		}
		
		// SHOW / HIDE EXPAND LIST
		
		if (isExpanded) {
			holder.expando.setVisibility(View.VISIBLE);
		}
		else {
			holder.expando.setVisibility(View.GONE);
		}
		
		holder.watch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// LAUNCH STREAM
				stream.loadStream(v.getContext(), pusher);
			}
		});
			
		mImageLoader.displayImage(stream.getScreenshot(), holder.screenshot,
				mDisplayOptions, new ImageLoadingListener() {

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {
					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
                        if (stream instanceof JustinTVStream)
                            holder.logo.setImageResource(R.drawable.jtv_logo_white);
                    }

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {
					}

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
                        if (stream instanceof TwitchStream)
                            holder.logo.setImageResource(R.drawable.twitch_logo);
                        else if (stream instanceof JustinTVStream)
                            holder.logo.setImageResource(R.drawable.jtv_logo_gray);					}
				});
		
		return view;
	}

	public void openCard(View view) {
		
		view.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				System.out.println(arg1);
			}
		});
		
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.linearLayout1);
		LinearLayout ll2 = (LinearLayout) view.findViewById(R.id.linearLayout2);
		
		RelativeLayout.LayoutParams params = (LayoutParams) ll.getLayoutParams();
        Assert.notNull(params);
		params.height = LayoutParams.MATCH_PARENT;
		ll.setLayoutParams(params);
	
		ll2.setVisibility(View.VISIBLE);
		
		setExpanded(true);
	}
	
	public void closeCard(View view) { 
		LinearLayout ll = (LinearLayout) view.findViewById(R.id.linearLayout1);
		LinearLayout ll2 = (LinearLayout) view.findViewById(R.id.linearLayout2);
		
		RelativeLayout.LayoutParams params = (LayoutParams) ll.getLayoutParams();
        Assert.notNull(params);
		params.height = LayoutParams.WRAP_CONTENT;
		ll.setLayoutParams(params);
		
		ll2.setVisibility(View.GONE);
		setExpanded(false);
	}
	
}
