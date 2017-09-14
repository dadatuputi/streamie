package co.bantamstudio.streamie.twitch.api;
import co.bantamstudio.streamie.R;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import co.bantamstudio.streamie.auth.twitch.model.TwitchGame;
import co.bantamstudio.streamie.auth.twitch.model.TwitchGameWrapper;
import org.springframework.util.Assert;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import co.bantamstudio.streamie.StreamieApplication;

public class TwitchApi {
	/*
	 * https://github.com/justintv/Twitch-API
	 */
    public final static String TWITCH_AUTH_SCOPE = "user_read user_follows_edit chat_login";
    public final static String TWITCH_AUTH_REDIRECT_URL = "http://streamie.tv/authenticate/twitch";
    private final static String TWITCH_BASE_URL = "https://api.twitch.tv/kraken";

	private static final String STREAM_CSS = "<style type=\"text/css\">body, html {margin: 0;padding: 0;}</style>";
	public static final String STREAM_HTML_OPEN = "<html><head>" + STREAM_CSS
			+ "</head><body>";
	public static final String STREAM_HTML_CLOSE = "</body></html>";
	public final static String TWITCH_SWF = "http://www.justin.tv/widgets/live_embed_player.swf?channel={channel}";
	public final static String TWITCH_API = "http://usher.justin.tv/find/{channel}.json?type=any&group=&channel_subscription=";
	
	public static HttpHeaders getHeaders() {
		HttpHeaders requestHeaders = new HttpHeaders();
		MediaType twitchMediaType = MediaType.valueOf("application/vnd.twitchtv.v2+json");
		List<MediaType> list = new ArrayList<MediaType>(1);
		list.add(twitchMediaType);
		requestHeaders.setAccept(list);
		return requestHeaders;
	}

	/*
	 * Games - Get the top games
	 * https://github.com/justintv/Twitch-API/blob/master/resources/games.md
	 */
	public final static String GAMES = TWITCH_BASE_URL + "/games/top";

	/*
	 * Streams - Get list of streams
	 * https://github.com/justintv/Twitch-API/blob/master/v2_resources/streams.md#get-streams
	 */
	public final static String STREAMS = TWITCH_BASE_URL + "/streams";
	
	/*
	 * Featured Streams - get top streams
	 * https://github.com/justintv/Twitch-API/blob/master/v2_resources/streams.md#get-streamsfeatured
	 */
	public final static String STREAMS_FEATURED = TWITCH_BASE_URL + "/streams/featured";

	/*
	 * Search
	 * https://github.com/justintv/Twitch-API/blob/master/v2_resources/search.md
	 */
	public final static String SEARCH_STREAMS = TWITCH_BASE_URL + "/search/streams";
	public final static String SEARCH_GAMES = TWITCH_BASE_URL + "/search/games";

	/*
	 * Follows
	 * https://github.com/justintv/Twitch-API/blob/master/v2_resources/users
	 * .md#get-streamsfollowed
	 */
	public final static String FOLLOWS = TWITCH_BASE_URL + "/streams/followed";

	/*
	 * Follow / Delete a channel
	 * https://github.com/justintv/Twitch-API/blob/master/v2_resources/follows.md#put-usersuserfollowschannelstarget
	 */
	public final static String FOLLOWS_UPDATE = TWITCH_BASE_URL + "/users/{user}/follows/channels/{channel}";
	
	/*
	 * Get Video Embed Using Justin.tv API because I can't figure it out through
	 * Twitch.tv API http://apiwiki.justin.tv/mediawiki/index.php/Channel/embed
	 */
	// private final static String EMBED = JtvApi.JTV_BASE_URL +
	// "channel/embed";
	public static String getEmbed(String height, String width, String volume,
			String channel) {
		return "<object type=\"application/x-shockwave-flash\" "
				+ "height=\""
				+ height
				+ "\" "
				+ "width=\""
				+ width
				+ "\" "
				+ "id=\"live_embed_player_flash\" "
				+ "data=\"http://www.twitch.tv/widgets/live_embed_player.swf?channel="
				+ channel
				+ "\" "
				+ "bgcolor=\"#000000\"> "
				+ "<param name=\"allowFullScreen\" value=\"true\" /> "
				+ "<param name=\"allowScriptAccess\" value=\"always\" /> "
				+ "<param name=\"allowNetworking\" value=\"all\" /> "
				+ "<param name=\"movie\" value=\"http://www.twitch.tv/widgets/live_embed_player.swf\" /> "
				+ "<param name=\"flashvars\" value=\"hostname=www.twitch.tv&channel="
				+ channel + "&auto_play=true&start_volume=" + volume + "\" /> "
				+ "</object>";
	}

	public static String getChatEmbed(String height, String width,
			String channel, boolean isAutoscroll) {
		String autoScroll = isAutoscroll ? "yes" : "no";
		return "<iframe frameborder=\"0\" " + "scrolling=\"" + autoScroll
				+ "\" " + "id=\"chat_embed\" "
				+ "src=\"http://twitch.tv/chat/embed?channel=" + channel
				+ "&amp;popout_chat=false\" " + "height=\"" + height + "\" "
				+ "width=\"" + width + "\"> " + "</iframe>";
	}

	/*
	 * Favorites / Follows Get a list of channels the user is following
	 * https://github
	 * .com/justintv/Twitch-API/blob/master/v2_resources/follows.md
	 * #get-usersuserfollowschannels
	 */
	private static final String favoritesList = TWITCH_BASE_URL + "/users/";


	/*
	 * STREAM/LIST: https://github.com/justintv/Twitch-API/wiki/Streams-Resource
	 * returns stream information for all live channels. The results are sorted
	 * by the current number of subText, with the most popular channels first.
	 */
	public static final String streamList = TWITCH_BASE_URL + "/streams";

	public static String ParseTwitchAccessToken(String url) {
		Uri uri = Uri.parse(url);
		String fragment = uri.getFragment();
        int accessTokenStart = 0;

        if (fragment != null) {
            accessTokenStart = fragment.indexOf("access_token=") + 13;
        }
        int accessTokenEnd = (fragment.indexOf("&") > 0) ? fragment
				.indexOf("&") : fragment.length() - 1;
		return fragment.substring(accessTokenStart,
				accessTokenEnd);
	}

	public static final String USER_SELF = TWITCH_BASE_URL + "/user";

	public static void setImage(TwitchGameWrapper game, ImageView image,
			Context context) {

        Assert.notNull(game);
        Assert.notNull(game.getGame());

        if (game.getGame().getBox() != null && game.getGame().getBox().getMedium() != null) {
            image.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            StreamieApplication.getImageLoader().displayImage(game.getGame().getBox().getMedium(), image, StreamieApplication.getDisplayImageOptions());
        } else if (game.getName().equalsIgnoreCase(TwitchGame.ALL_TWITCH_STREAMS)) {
            image.setImageDrawable(context.getResources().getDrawable(R.drawable.gaming));
            image.setBackgroundColor(context.getResources().getColor(R.color.ics_light_blue));
        }
	}
}
