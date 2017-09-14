package co.bantamstudio.streamie.twitch;

import android.net.Uri;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.GrantType;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.auth.model.Oauth2Common;
import co.bantamstudio.streamie.auth.twitch.connect.TwitchConnectionFactory;
import co.bantamstudio.streamie.fragment.AbstractAuthFragment;
import co.bantamstudio.streamie.twitch.api.TwitchApi;
import co.bantamstudio.streamie.twitch.api.TwitchConnectionRequest;

public class TwitchAuthFragment extends AbstractAuthFragment {
	private OAuth2ConnectionFactory<?> mFactory;
	
	@Override
	public void onStart() {
		super.onStart();
		mFactory = mApplication.getTwitchConnectionFactory();
		getWebView().setWebViewClient(new TwitchAuthWebViewClient());
		getWebView().loadUrl(getAuthorizeUrl());
	}
	
	class TwitchAuthWebViewClient extends WebViewClient {	
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Uri uri = Uri.parse(url);
			
			if (uri.toString().contains(TwitchApi.TWITCH_AUTH_REDIRECT_URL)) {
				final AccessGrant accessGrant = createAccessGrantFromUriFragment(uri.getFragment());
				
				if (accessGrant != null) {
					// FRAGMENT CONTAINED ACCESS TOKEN
					TwitchConnectionRequest twitchConnectionRequest = new TwitchConnectionRequest(accessGrant, (TwitchConnectionFactory) mFactory, mConnectionRepository);
					mSpiceManager.execute(twitchConnectionRequest, new RequestListener<Void>() {
						@Override
						public void onRequestFailure(SpiceException spiceException) {
							// TODO DO NOTHING FOR NOW
							Toast.makeText(TwitchAuthFragment.this.getActivity(), spiceException.getMessage(), Toast.LENGTH_LONG).show();
						}

						@Override
						public void onRequestSuccess(Void result) {
							onAuthenticated(accessGrant.getAccessToken());
						}
					});
				} else {
					// CANCEL EVENT
					TwitchAuthFragment.this.onCanceled();
				}
				return true;
			} else {
				return super.shouldOverrideUrlLoading(view, url);
			}
		}		
		
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			getActivity().getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_OFF);
		}
		
		private AccessGrant createAccessGrantFromUriFragment(String uriFragment) {
			// confirm we have the fragment, and it has an access_token parameter
			if (uriFragment != null && uriFragment.contains("access_token=")) {

				try {
					// split to get the two different parameters
					String[] params = uriFragment.split("&");
					
					Map<String, String> map = new HashMap<String, String>();
					
					for (String param : params) {
						String[] paramBaby = param.split("=");
						map.put(paramBaby[0], paramBaby[1]);
					}

					// GET AT LEAST THE ACCESS TOKEN
					String accessToken = map.get(Oauth2Common.ACCESS_TOKEN);
					Assert.hasText(accessToken);
					
					String scope = map.get(Oauth2Common.SCOPE);
					String refreshToken = map.get(Oauth2Common.REFRESH_TOKEN);
					Integer expiresIn = Integer.getInteger(map.get(Oauth2Common.EXPIRES_IN));
					
					// create the connection and persist it to the repository
					return new AccessGrant(accessToken, scope, refreshToken, expiresIn);
				} catch (Exception e) {
					// DO NOTHING IF PARAMS AREN'T AS EXPECTED
				}
			}
			return null;
		}
	}

	String getAuthorizeUrl() {
		
		OAuth2Parameters params = new OAuth2Parameters();
		params.setRedirectUri(TwitchApi.TWITCH_AUTH_REDIRECT_URL);
		params.setScope(TwitchApi.TWITCH_AUTH_SCOPE);
		
		OAuth2Operations oAuth2Operations = mFactory.getOAuthOperations();
		return oAuth2Operations.buildAuthorizeUrl(GrantType.IMPLICIT_GRANT, params);
	}

	@Override
	protected String getAuthSuccessful() {
		return mApplication.getResources().getString(R.string.twitch_auth_successful);
	}

	@Override
	protected String getAuthUnsuccessful() {
		return mApplication.getResources().getString(R.string.twitch_auth_unsuccessful);
	}
}
