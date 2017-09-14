package co.bantamstudio.streamie.justintv.api;

import android.content.Context;
import android.widget.ImageView;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.auth.justintv.model.JtvCategory;

// TODO: Auto-generated Javadoc
/**
 * The Class JtvApi.
 */
public class JustinTVApi {

	// API Methods
	public static enum APIMethod {
		STREAM_LIST, CATEGORY_LIST, CATEGORY
	}
	
	// JUSTIN.TV CONSTANTS - from https://github.com/chrippa/livestreamer/blob/master/src/livestreamer/plugins/justintv.py
	/** The Constant JTV_API_BASE_URL. */
	private static final String JTV_BASE_URL_USHER = "http://usher.justin.tv";
	
	/** The Constant JTV_STREAM_INFO_URL. */
	protected static final String JTV_STREAM_INFO_URL = JTV_BASE_URL_USHER + "/find/";
	
	/** The Constant JTV_METADATA_URL. */
	protected static final String JTV_METADATA_URL = "http://www.justin.tv/meta/";
	
	/** The Constant JTV_METADATA_URL_END. */
	protected static final String JTV_METADATA_URL_END = "?on_site=true";
	
	/** The Constant JTV_SWF_URL. */
	private static final String JTV_SWF_URL = "http://www.justin.tv/widgets/live_embed_player.swf";
    
    /** The Constant HLSStreamTokenKey. */
    protected static final String HLSStreamTokenKey = "ENTER TOKEN KEY";
    
    /** The Constant HLSStreamTokenURL. */
    protected static final String HLSStreamTokenURL = JTV_BASE_URL_USHER + "/stream/iphone_token/";
    
    /** The Constant HLSSPlaylistURL. */
    protected static final String HLSSPlaylistURL = JTV_BASE_URL_USHER + "/stream/multi_playlist/";

	// STREAM API CONSTANTS
	/** The Constant STREAM_CSS. */
	private static final String STREAM_CSS = "<style type=\"text/css\">body, html {margin: 0;padding: 0;background-color: #000000;}</style>";
	
	/** The Constant STREAM_WIDTH_REGEX_PATTERN. */
	public static final String STREAM_WIDTH_REGEX_PATTERN = "width=\\\"([0-9]{1,4})\\\"";
	
	/** The Constant STREAM_WIDTH_REGEX_REPLACEMENT. */
	public static final String STREAM_WIDTH_REGEX_REPLACEMENT = "width=\\\"100%\\\"";
	
	/** The Constant STREAM_HEIGHT_REGEX_PATTERN. */
	public static final String STREAM_HEIGHT_REGEX_PATTERN = "height=\\\"([0-9]{1,4})\\\"";
	
	/** The Constant STREAM_HEIGHT_REGEX_REPLACEMENT. */
	public static final String STREAM_HEIGHT_REGEX_REPLACEMENT = "height=\\\"100%\\\"";
	public static final String STREAM_HEIGHT_REGEX_REPLACEMENT_50 = "height=\\\"50%\\\"";
	
	/** The Constant STREAM_AUTOPLAY_REGEX_PATTERN. */
	public static final String STREAM_AUTOPLAY_REGEX_PATTERN = "auto_play=false";
	
	/** The Constant STREAM_AUTOPLAY_REGEX_REPLACEMENT. */
	public static final String STREAM_AUTOPLAY_REGEX_REPLACEMENT = "auto_play=true";
	
	/** The Constant STREAM_VOLUME_REGEX_PATTERN. */
	public static final String STREAM_VOLUME_REGEX_PATTERN = "start_volume=([0-9]{1,4})";
	
	/** The Constant STREAM_VOLUME_REGEX_REPLACEMENT. */
	public static final String STREAM_VOLUME_REGEX_REPLACEMENT = "start_volume=100";
	
	/** The Constant STREAM_HTML_OPEN. */
	public static final String STREAM_HTML_OPEN = "<html><head>" + STREAM_CSS
			+ "</head><body>";
	
	/** The Constant STREAM_HTML_CLOSE. */
	public static final String STREAM_HTML_CLOSE = "</body></html>";
	// CHAT API CONSTANTS
	/** The Constant USER_AGENT_DESKTOP. */
	public static final String USER_AGENT_DESKTOP = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";
	public static final String CHAT_WIDTH_REGEX_PATTERN = "width:([0-9]{1,6})px;";
	public static final String CHAT_WIDTH_REGEX_REPLACEMENT = "width:100%;";
	public static final String CHAT_HEIGHT_REGEX_PATTERN = "height:([0-9]{1,6})px;";
	public static final String CHAT_HEIGHT_REGEX_REPLACEMENT = "height:100%;";
	/** The Constant CHAT_CSS. */
	public static final String CHAT_CSS_START = "<style type=\"text/css\">";
	public static final String CHAT_CSS_END = "</style>";

	public static final String JTV_BASE_URL_CDN = "http://www-cdn.jtvnw.net/";
	private static final String JTV_BASE_URL = "http://api.justin.tv/api/";

	// JTV REST API DOCUMENTATION:
	// http://apiwiki.justin.tv/mediawiki/index.php/REST_API_Documentation
	/** The Constant format. */
	private static final String REQUEST_FORMAT = "json";
	
	public static HttpHeaders getHeaders() {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		return requestHeaders;
	}

	/*
	 * STREAM/LIST: http://apiwiki.justin.tv/mediawiki/index.php/Stream/list
	 * returns stream information for all live channels. The results are sorted
	 * by the current number of subText, with the most popular channels first.
	 */
	public static final String STREAMLIST = JTV_BASE_URL + "stream/list."
			+ REQUEST_FORMAT;
	
	/*
	 * STREAM/SUMMARY:
	 * http://apiwiki.justin.tv/mediawiki/index.php/Stream/summary Returns
	 * aggregate stream information for all live channels (possibly scoped by
	 * category).
	 */
	// private static final String streamSummary =
	// "http://api.justin.tv/api/streams/summary.";

	/*
	 * STREAM/SEARCH: http://apiwiki.justin.tv/mediawiki/index.php/Stream/search
	 * Returns stream information for the live channels that match a search
	 * query. The results are sorted by the current number of subText, with the
	 * most popular channels first.
	 */
	/** The Constant streamSearch. */
	public static final String SEARCH = JTV_BASE_URL + "stream/search/{query}." + REQUEST_FORMAT;

	/*
	 * USER/SHOW: http://api.justin.tv/api/user/show/ Returns extended
	 * information for a user, specified by user id or login.
	 */
	// private static final String userShow =
	// "http://api.justin.tv/api/user/show/";

	/*
	 * USER/UPDATE: http://apiwiki.justin.tv/mediawiki/index.php/User/update
	 * Updates an existing user. This call requires OAuth authentication for the
	 * target user.
	 */
	// private static final String userUpdate =
	// "http://api.justin.tv/api/user/update.";

	/*
	 * USER/FAVORITES:
	 * http://apiwiki.justin.tv/mediawiki/index.php/User/favorites Get all
	 * channels who are favorites of a user.
	 */
	// private static final String userFavorites =
	// "http://api.justin.tv/api/user/favorites/";

	/*
	 * CHANNEL/SHOW: http://apiwiki.justin.tv/mediawiki/index.php/Channel/show
	 * Returns extended information for a channel, specified by channel id or
	 * login. Requires OAuth authentication for private channels.
	 */
	/** The Constant channelShow. */
	private static final String channelShow = JTV_BASE_URL + "channel/show/";

	/*
	 * CHANNEL/EMBED: http://apiwiki.justin.tv/mediawiki/index.php/Channel/embed
	 * Get embed code for a channel.
	 */
	// private static final String channelEmbed =
	// "http://api.justin.tv/api/channel/embed/";

	/*
	 * FAN/CREATE: http://apiwiki.justin.tv/mediawiki/index.php/Fan/create
	 * Creates a new fan. This call requires OAuth authentication for the
	 * initiating user. (Favorites)
	 */
	// private static final String fanCreate =
	// "http://api.justin.tv/api/fan/create/";

	/*
	 * FAN/DESTROY: http://apiwiki.justin.tv/mediawiki/index.php/Fan/destroy
	 * Destroys a fan relationship. This call requires OAuth authentication for
	 * the initiating user.
	 */
	// private static final String fanDestroy =
	// "http://api.justin.tv/api/fan/destroy/";

	/*
	 * CATEGORY/LIST: http://apiwiki.justin.tv/mediawiki/index.php/Category/list
	 * Returns a list of categories.
	 */
	/** The Constant categoryList. */
	public static final String CATEGORIES = JTV_BASE_URL + "category/list." + REQUEST_FORMAT;

	/*
	 * EMBED/FROMURL:
	 * http://apiwiki.justin.tv/mediawiki/index.php/Embed/from_url Returns the
	 * embed code for the content on a given page.
	 */
	// private static final String embedFromURL =
	// "http://api.justin.tv/api/embed/from_url.";

	/*
	 * CHANNEL/CHAT EMBED:
	 * http://apiwiki.justin.tv/mediawiki/index.php/Channel/chat_embed Get chat
	 * embed code for a channel.
	 */
	/** The Constant channelChatEmbed. */
	private static final String channelChatEmbed = JTV_BASE_URL + "channel/chat_embed/";
	

	/**
	 * Gets the channel chat embed.
	 *
	 * @param channel the channel
	 * @param height the height
	 * @param width the width
	 * @return the channel chat embed
	 */

	
//	/**
//	 * Gets the rMTP find.
//	 *
//	 * @param channel the channel
//	 * @return the rMTP find
//	 */
//	public static RestClient getRMTPFind(String channel){
//		RestClient rc = new RestClient(JTV_STREAM_INFO_URL + channel + "." + REQUEST_FORMAT);
//		rc.AddParam("type", "any");
//		rc.setRequestMethod(rtmpFindReqMethod);
//		return rc;		
//	}
	
	/**
	 * Gets the stream rtmp.
	 *
	 * @param port the port
	 * @param connect the connect
	 * @param play the play
	 * @param token the token
	 * @param swfUrl the swf url
	 * @return the stream rtmp
	 */
//	public static RestClient getStreamRTMP(int port, String connect, String play, String token, String swfUrl){		
//		RestClient http = new RestClient("http://" + StreamieApplication.RTMP_ADDRESS + ":" + port + "/");
//		http.AddParam("r", connect + "/" + play);
//		http.AddParam("j", token);
//		http.AddParam("v", "");
//		http.AddParam("W", swfUrl);
//		
//		//http.AddParam("z", "");
//		
//		http.setRequestMethod(RequestMethod.GET);
//		
//		return http;
//	}
	
	/**
	 * Url resolve swf.
	 *
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String urlResolveSWF() throws IOException{
		URL swfUrl;
		try {
			swfUrl = new URL(JTV_SWF_URL);
		} catch (MalformedURLException e) {
			return JTV_SWF_URL;
		}
		
		URLConnection urlc = swfUrl.openConnection();
		HttpURLConnection httpUrlC = (HttpURLConnection) urlc;
		httpUrlC.setInstanceFollowRedirects(false);
		httpUrlC.connect();
		String result = httpUrlC.getHeaderField("Location");
		if (result != null && !result.equalsIgnoreCase(""))
			return result;
		else
			return JTV_SWF_URL;
	}
	
	
	/*
	 *  Favorites / Follows
	 *  Get a list of channels the user is following
	 *  http://apiwiki.justin.tv/mediawiki/index.php/User/favorites
	 *  
	 */
	private static final String favoritesList = JTV_BASE_URL + "user/favorites/";
	
	/* Follows - create follow
	 * http://apiwiki.justin.tv/mediawiki/index.php/Fan/create
	 * 
	 * eg: http://api.justin.tv/api/fan/create/officecam.xml
	 */	
	public final static String FOLLOWS_CREATE = JTV_BASE_URL + "fan/create/{channel}." + REQUEST_FORMAT;
	
	/* Follows - destroy follow
	 * http://apiwiki.justin.tv/mediawiki/index.php/Fan/destroy
	 * 
	 * eg: http://api.justin.tv/api/fan/destroy/officecam.xml
	 */
	public final static String FOLLOWS_DESTROY = JTV_BASE_URL + "fan/destroy/{channel}." + REQUEST_FORMAT;
	
	/*
	 * WHOAMI - get user from oauth token
	 * http://apiwiki.justin.tv/mediawiki/index.php/Account/whoami
	 * 
	 * eg: http://api.justin.tv/api/account/whoami.format
	 */
	public final static String WHOAMI = JTV_BASE_URL + "account/whoami." + REQUEST_FORMAT;

	
	public static String getChatEmbed(String height, String width, String channel, boolean isAutoscroll){
		String autoScroll = isAutoscroll?"yes":"no";
		return "<iframe frameborder=\"0\" " + 
		        "scrolling=\"" + autoScroll + "\" " + 
		        "id=\"chat_embed\" " + 
		        "src=\"http://www.justin.tv/chat/embed?channel=" + channel + "&amp;popout_chat=true\" " + 
		        "height=\"" + height + "\" " + 
		        "width=\"" + width + "\"> " +
		"</iframe>";
	}
	
	// SET JUSTIN.TV CATEGORY IMAGES
	private static final Map<String, Integer> CategoryDrawableMap;
	static {
		Map<String, Integer> aMap = new HashMap<String, Integer>();
		aMap.put("animals", R.drawable.animals);
		aMap.put("social", R.drawable.social);
		aMap.put("gaming", R.drawable.gaming);
        aMap.put("poker", R.drawable.poker);
		aMap.put("other", R.drawable.other);
        aMap.put("creativity", R.drawable.creativity);
		aMap.put("news", R.drawable.news);
		aMap.put("entertainment", R.drawable.entertainment);
		aMap.put("sports", R.drawable.sports);
		aMap.put("all", R.drawable.all);
		CategoryDrawableMap = Collections.unmodifiableMap(aMap);
	}
	
	private static int getCategoryColorId(JtvCategory category) {
		for (int i = CategoryThresholds.length - 1; i >= 0; i--) {
			if (category.getViewersCount() >= CategoryThresholds[i])
				return CategoryColors[i];
		}
		return CategoryColors[0];
	}
	
	/** The Constant CategoryThresholds. */
	private static final int[] CategoryThresholds = { 1, 100, 1000, 5000,
			50000, 100000 };
	
	/** The Constant CategoryColors. */
	private static final int[] CategoryColors = { R.color.ics_light_blue,
			R.color.ics_light_blue, R.color.ics_light_purple,
			R.color.ics_light_green, R.color.ics_light_orange,
			R.color.ics_light_red };
	
	public static void setImage(JtvCategory category, ImageView image, Context context) {
        System.out.println(category.getName());
        int imageResId;
        if (CategoryDrawableMap.containsKey(category.getKey()))
            imageResId = CategoryDrawableMap.get(category.getKey());
        else
            imageResId = CategoryDrawableMap.get("other");
        image.setImageResource(imageResId);
		image.setBackgroundColor(context.getResources().getColor(getCategoryColorId(category)));
	}

}
