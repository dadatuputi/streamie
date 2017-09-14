package co.bantamstudio.streamie.justintv;

import android.net.Uri;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.util.StringUtils;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.auth.justintv.connect.JustinTVConnectionFactory;
import co.bantamstudio.streamie.fragment.AbstractAuthFragment;
import co.bantamstudio.streamie.justintv.api.JustinTVConnectionRequest;
import co.bantamstudio.streamie.justintv.api.JustinTVConnectionTokenRequest;

public class JustinTVAuthFragment extends AbstractAuthFragment {
	// TODO MAKE THESE FINAL, GET ON INIT FROM OBFUSC LOC
	private final String mRedirectUri = "http://streamie.tv/authenticate/justin";
	private JustinTVConnectionFactory mFactory;
	private OAuthToken mRequestToken;

	@Override
	public void onStart() {
		super.onStart();
		mFactory = mApplication.getJustinTVConnectionFactory();
		getWebView().setWebViewClient(new JustinTVAuthWebViewClient());

		// GET TOKEN ASYNCHROUSLY
		mSpiceManager.execute(new JustinTVConnectionTokenRequest(mFactory,
				mRedirectUri, null), new RequestListener<OAuthToken>() {

			@Override
			public void onRequestFailure(SpiceException spiceException) {
				// TODO handle failure
				System.out.println(spiceException.getMessage());
			}

			@Override
			public void onRequestSuccess(OAuthToken token) {

				String url = mFactory.getOAuthOperations().buildAuthorizeUrl(
						token.getValue(), OAuth1Parameters.NONE);
				getWebView().loadUrl(url);
				mRequestToken = token;
			}
		});
	}

	private class JustinTVAuthWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Uri uri = Uri.parse(url);

			// TODO CHECK FOR MIS-CLICKS
			
			if (uri.toString().contains(mRedirectUri)) {
				final String oauth_token = uri.getQueryParameter("oauth_token");

				if (StringUtils.hasText(oauth_token)) {
					JustinTVConnectionRequest justinTVConnectionRequest = new JustinTVConnectionRequest(
							mRequestToken, oauth_token, mFactory,
							mConnectionRepository);
					mSpiceManager.execute(justinTVConnectionRequest,
							new RequestListener<Void>() {

								@Override
								public void onRequestFailure(
										SpiceException spiceException) {
									// TODO DO NOTHING FOR NOW
									Toast.makeText(JustinTVAuthFragment.this.getActivity(),
											spiceException.getMessage(),
											Toast.LENGTH_LONG).show();
								}

								@Override
								public void onRequestSuccess(Void result) {
									JustinTVAuthFragment.this
											.onAuthenticated(oauth_token);
								}
							});
				} else {
					// CANCEL EVENT
					JustinTVAuthFragment.this.onCanceled();
				}
				return true;
			} else {
				return super.shouldOverrideUrlLoading(view, url);
			}
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			getActivity().getWindow().setFeatureInt(
					Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_OFF);
		}

	}

	@Override
	protected String getAuthSuccessful() {
		return mApplication.getResources().getString(R.string.justin_tv_auth_successful);
	}

	@Override
	protected String getAuthUnsuccessful() {
		return mApplication.getResources().getString(R.string.justin_tv_auth_successful);
	}


}
