//package co.bantamstudio.streamie.fragment;
//import co.bantamstudio.streamie.R;
//
//import jerklib.ConnectionManager;
//import jerklib.Profile;
//import jerklib.Session;
//import jerklib.events.IRCEvent;
//import jerklib.events.IRCEvent.Type;
//import jerklib.events.JoinCompleteEvent;
//import jerklib.events.MessageEvent;
//import jerklib.events.MotdEvent;
//import jerklib.events.TopicEvent;
//import jerklib.listeners.IRCEventListener;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.FrameLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import co.bantamstudio.streamie.StreamieApplication;
//import com.actionbarsherlock.app.SherlockFragment;
//
//public class NewChatFragment extends SherlockFragment implements IRCEventListener {
//
//	private String mChannelName;
//	private StreamieApplication mApplication;
//	private FrameLayout mContainer;
//	private boolean mIsPortrait;
//	private String mChatEmbed;
//	private float mAlpha;
//	private ConnectionManager manager;
//	private ListView mChatHistory;
//	private ArrayAdapter<String> mAdapter;
//
//	private enum DIRECTION {
//		LEFT,
//		RIGHT
//	}
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//
//		// ASSUMING WE'RE PUTTING THIS FRAGMENT IN A FRAMELAYOUT
//		return (RelativeLayout) inflater.inflate(R.layout.frame_layout_ad_chat, null);
//	}
//
//	@Override
//	public void onStart() {
//		super.onStart();
//
//		mApplication = (StreamieApplication) getActivity().getApplication();
//
//		// LOAD ADS
////		StreamieAd mAds = AdLoader.initialize(getActivity());
////		mAds.loadBanner((ViewGroup) getView().findViewById(R.id.ad_layout),null);
////
////		mChannelName = getArguments().getString(StreamieApplication.KEY);
////		mIsPortrait = getArguments().getBoolean(StreamieApplication.PORTRAIT, false);
////		mChatEmbed = getArguments().getString(StreamieApplication.CHAT_EMBED);
////
////		if (mChannelName == null || mChannelName.equalsIgnoreCase(""))
////			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
////
////		// SET ALPHA
////		int alphaInt = Integer.parseInt(mApplication.getChatAlpha());
////		String alphaCss = (alphaInt != 0)?"opacity: " + (float) alphaInt/100 + ";":"";
//
//		// CHAT WIDTH
////		DisplayMetrics dm = new DisplayMetrics();
////		dm = getActivity().getBaseContext().getResources().getDisplayMetrics();
////		int widthInt = Integer.parseInt(mApplication.getChatWidth());
//
//
////		String css = JtvApi.CHAT_CSS_START + "iframe {border: none; width: " + widthInt + "% }" + JtvApi.CHAT_CSS_END;
////		mChatEmbed = css + mChatEmbed;
//
//		mContainer = (FrameLayout) getView().findViewById(R.id.container);
//		mChatHistory = (ListView) getView().findViewById(R.id.chatHistory);
//		mAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_chat, R.id.listItemText);
//		mChatHistory.setAdapter(mAdapter);
//
//		manager = new ConnectionManager(new Profile("scripy"));
//
//		Session session = manager.requestConnection("irc.freenode.net");
//
//		session.addIRCEventListener(this);
//	}
//
//
//	@Override
//	public void receiveEvent(IRCEvent e) {
//		if (e.getType() == Type.CONNECT_COMPLETE)
//		{
//			e.getSession().join("#jerklib");
//
//		}
//		else if (e.getType() == Type.CHANNEL_MESSAGE)
//		{
//			MessageEvent me = (MessageEvent) e;
//			System.out.println("<" + me.getNick() + ">"+ ":" + me.getMessage());
//		}
//		else if (e.getType() == Type.JOIN_COMPLETE)
//		{
//			JoinCompleteEvent jce = (JoinCompleteEvent) e;
//
//			/* say hello and version number */
//			jce.getChannel().say("Hello from Jerklib");
//		}
//		else if (e.getType() == Type.MOTD) {
//			MotdEvent m = (MotdEvent) e;
//			mAdapter.add(m.getMotdLine());
//			mAdapter.notifyDataSetChanged();
//		}
//		else if (e.getType() == Type.TOPIC) {
//			TopicEvent m = (TopicEvent) e;
//			mAdapter.add(m.getTopic());
//			mAdapter.notifyDataSetChanged();
//		}
//		else
//		{
//			System.out.println(e.getType() + " " + e.getRawEventData());
//		}
//	}
//}
