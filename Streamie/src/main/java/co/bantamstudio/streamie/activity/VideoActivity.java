//package co.bantamstudio.streamie.activity;
//
//import io.vov.vitamio.LibsChecker;
//import io.vov.vitamio.MediaPlayer;
//import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
//import io.vov.vitamio.MediaPlayer.OnCompletionListener;
//import io.vov.vitamio.MediaPlayer.OnPreparedListener;
//import io.vov.vitamio.MediaPlayer.OnVideoSizeChangedListener;
//import io.vov.vitamio.widget.MediaController;
//import io.vov.vitamio.widget.VideoView;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.URI;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.http.HttpException;
//import org.json.JSONArray;
//import org.json.JSONException;
//import co.bantamstudio.streamie.auth.twitch.model.TwitchStreamMeta;
//
//import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
//import com.octo.android.robospice.SpiceManager;
//import com.octo.android.robospice.persistence.exception.SpiceException;
//import com.octo.android.robospice.request.listener.RequestListener;
//
////import co.bantamstudio.streamie.StreamieApplication;
//import co.bantamstudio.streamie.twitch.api.TwitchSWFRequest;
//import co.bantamstudio.streamie.twitch.api.TwitchStreamsForChannelRequest;
//
//import android.media.AudioManager;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.SurfaceHolder;
//import android.view.SurfaceView;
//import android.widget.Toast;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.res.AssetManager;
//import android.content.res.Configuration;
//import android.graphics.PixelFormat;
//
//public class VideoActivity extends Activity implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {
//
//	private static final String TAG = "MediaPlayerDemo";
//	private int mVideoWidth;
//	private int mVideoHeight;
//	private MediaPlayer mMediaPlayer;
//	private SurfaceView mPreview;
//	private SurfaceHolder holder;
//	private static final String MEDIA = "media";
//	private static final int LOCAL_AUDIO = 1;
//	private static final int STREAM_AUDIO = 2;
//	private static final int RESOURCES_AUDIO = 3;
//	private static final int LOCAL_VIDEO = 4;
//	private static final int STREAM_VIDEO = 5;
//	private boolean mIsVideoSizeKnown = false;
//	private boolean mIsVideoReadyToBePlayed = false;
//	private String path = "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
//	private VideoView mVideoView;
//	protected SpiceManager spiceManager = new SpiceManager( JacksonSpringAndroidSpiceService.class );
//	
//	@Override
//	protected void onStart() {
//		super.onStart();
//		spiceManager.start(this);
//	}
//	
//	@Override
//	protected void onStop() {
//		spiceManager.shouldStop();
//		super.onStop();
//	}
//
//	/**
//	 * 
//	 * Called when the activity is first created.
//	 */
//	@Override
//	public void onCreate(Bundle icicle) {
//		super.onCreate(icicle);
//		if (!LibsChecker.checkVitamioLibs(this))
//			return;
//		setContentView(R.layout.videoview);
//		mPreview = (SurfaceView) findViewById(R.id.surface);
//		holder = mPreview.getHolder();
//		holder.addCallback(this);
//		holder.setFormat(PixelFormat.RGBA_8888); 
//	}
//
//	private void playVideo() {
//		doCleanUp();
//		
//		TwitchSWFRequest request = new TwitchSWFRequest("riotgames");
//		spiceManager.execute(request, new RequestListener<URI>() {
//
//			@Override
//			public void onRequestFailure(SpiceException spiceException) {
//				// TODO Auto-generated method stub
//			}
//
//			@Override
//			public void onRequestSuccess(URI result) {
//				TwitchStreamsForChannelRequest streamsForChannelRequest = new TwitchStreamsForChannelRequest("riotgames", result.toString());
//				spiceManager.execute(streamsForChannelRequest, new RequestListener<TwitchStreamMeta[]>() {
//
//					@Override
//					public void onRequestFailure(SpiceException spiceException) {
//						// TODO Auto-generated method stub
//						
//					}
//
//					@Override
//					public void onRequestSuccess(TwitchStreamMeta[] metas) {
//						TwitchStreamMeta meta = metas[0];
//						HashMap<String, String> options = new HashMap<String, String>();
//						options.put("rtmp_playpath", meta.getPlay() + "?j=" + meta.getToken());
//						options.put("rtmp_swfverify", meta.getSwfUrl());
//						options.put("rtmp_live", "1");
//						
//						try {
//
//							// Create a new media player and set the listeners
//							mMediaPlayer = new MediaPlayer(VideoActivity.this);
////							mMediaPlayer.
////							mMediaPlayer.setDataSource(meta.getConnect(), options);
//							
//							HashMap<String, String> optionss = new HashMap<String, String>();
////							optionss.put("rtmp_playpath", "test/Wildlife2.flv");
////							optionss.put("rtmp_swfurl", "http://www.cdn-br.com/swf/player.swf");
////							optionss.put("rtmp_live", "1"); 
////							optionss.put("rtmp_pageurl", "http://www.cdn-br.com/mastertv/FSB.htm"); 
////							mMediaPlayer.setDataSource("rtmp://flash.oit.duke.edu/vod/_definst_", optionss);
//							
//							optionss.put("j", meta.getToken());
//							optionss.put("swfUrl", meta.getSwfUrl());
//							optionss.put("swfVfy", "1");
//							optionss.put("live", "1"); 
//							options.put("playpath", meta.getPlayPath());
////							mMediaPlayer.setDataSource(meta.getConnect() + "/" + meta.getPlay(), optionss);
//							mMediaPlayer.setDataSource(meta.getRtmpPath());
//							
//							//mMediaPlayer.setDataSource(path);
//							mMediaPlayer.setDisplay(holder);
//							mMediaPlayer.prepare();
//							mMediaPlayer.setOnBufferingUpdateListener(VideoActivity.this);
//							mMediaPlayer.setOnCompletionListener(VideoActivity.this);
//							mMediaPlayer.setOnPreparedListener(VideoActivity.this);
//							mMediaPlayer.setOnVideoSizeChangedListener(VideoActivity.this);
//							mMediaPlayer.getMetadata();
//							setVolumeControlStream(AudioManager.STREAM_MUSIC);
//
//						} catch (Exception e) {
//							Log.e(TAG, "error: " + e.getMessage(), e);
//						}
//					}
//				});
//			}
//		});
//	}
//
//	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
//		Log.d(TAG, "onBufferingUpdate percent:" + percent);
//
//	}
//
//	public void onCompletion(MediaPlayer arg0) {
//		Log.d(TAG, "onCompletion called");
//	}
//
//	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
//		Log.v(TAG, "onVideoSizeChanged called");
//		if (width == 0 || height == 0) {
//			Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
//			return;
//		}
//		mIsVideoSizeKnown = true;
//		mVideoWidth = width;
//		mVideoHeight = height;
//		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
//			startVideoPlayback();
//		}
//	}
//
//	public void onPrepared(MediaPlayer mediaplayer) {
//		Log.d(TAG, "onPrepared called");
//		mIsVideoReadyToBePlayed = true;
//		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
//			startVideoPlayback();
//		}
//	}
//
//	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
//		Log.d(TAG, "surfaceChanged called");
//
//	}
//
//	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
//		Log.d(TAG, "surfaceDestroyed called");
//	}
//
//	public void surfaceCreated(SurfaceHolder holder) {
//		Log.d(TAG, "surfaceCreated called");
//		playVideo();
//
//	}
//
//	@Override
//	protected void onPause() {
//		super.onPause();
//		releaseMediaPlayer();
//		doCleanUp();
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		releaseMediaPlayer();
//		doCleanUp();
//	}
//
//	private void releaseMediaPlayer() {
//		if (mMediaPlayer != null) {
//			mMediaPlayer.release();
//			mMediaPlayer = null;
//		}
//	}
//
//	private void doCleanUp() {
//		mVideoWidth = 0;
//		mVideoHeight = 0;
//		mIsVideoReadyToBePlayed = false;
//		mIsVideoSizeKnown = false;
//	}
//
//	private void startVideoPlayback() {
//		Log.v(TAG, "startVideoPlayback");
//		holder.setFixedSize(mVideoWidth, mVideoHeight);
//		mMediaPlayer.start();
//	}
//}
//
//	
//
//	
//
//
//	
////	@Override
////	public void finish() {
////		//mApplication.closeStream();
////		super.finish();
////	}
//	
////	public class LoadVideoData extends AsyncTask<Integer, Integer, String>{
////
////		@Override
////		protected String doInBackground(Integer... params) {
////			RestClient restClient = JTVApi.getRMTPFind(mChannel);
////			List<StreamRTMP> streamData;
////			try {
////				streamData = JTVStream.parseStreamRTMP((JSONArray) mApplication.getJSON(restClient,restClient.bypassCache()));
////				if (streamData != null && !streamData.isEmpty()){
////					StreamRTMP stream = streamData.get(0);
////					String swf = JTVApi.urlResolveSWF();
////					RestClient http = JTVApi.getStreamRTMP(mApplication.getStreamPort(), stream.getConnect(), stream.getPlay(), stream.getToken(), swf);
////					return http.getUrlWithParamsForRTMP();
////				}
////			} catch (JSONException e1) {
////				// TODO Auto-generated catch block
////				e1.printStackTrace();
////			} catch (HttpException e1) {
////				// TODO Auto-generated catch block
////				e1.printStackTrace();
////			} catch (Exception e1) {
////				// TODO Auto-generated catch block
////				e1.printStackTrace();
////			}
////			
////			return null;
////			
////		}
////		
////		@Override
////		protected void onPostExecute(String result) {
////			//mVideoView.setVideoURI(Uri.parse(http.getUrlWithParamsForRTMP()));
////			if (result != null && !result.equalsIgnoreCase("")){
////				mVideoView.setVideoPath(result);
////				//mVideoView.setVideoPath("http://www.pocketjourney.com/downloads/pj/video/famous.3gp");
////				mVideoView.setMediaController(new MediaController(VideoActivity.this));
////				mVideoView.requestFocus();
////				mVideoView.start();
////
////
////			}
////			super.onPostExecute(result);
////		}
////			
////	}
//	
////	@Override
////	protected void onStop() {
////		mApplication.closeStream();
////		super.onStop();
////	}
//
