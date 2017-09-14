package co.bantamstudio.streamie.api;
//package co.bantamstudio.streamie;
//package co.bantamstudio.streamie;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class StreamRTMP {
//
//	private String mPlay;
//	private String mToken;
//	private String mConnect;
//	private String mType;
//	private String mNode;
//	private String mNeededInfo;
//	private int mVideoHeight;
//	private double mBitrate;
//	private int mBroadcastPart;
//	private String mCluster;
//	private int mBroadcastId;
//
//	public StreamRTMP(String node, String neededInfo, String play, int videoHeight, double bitrate, int broadcastPart, String cluster, String token, String connect, int broadcastId, String type){
//		mNode = node;
//		mNeededInfo = neededInfo;
//		mPlay = play;
//		mVideoHeight = videoHeight;
//		mBitrate = bitrate;
//		mBroadcastPart = broadcastPart;
//		mCluster = cluster;
//		mToken = token;
//		mConnect = connect;
//		mBroadcastId = broadcastId;
//		mType = type;
//	}
//	
//	public StreamRTMP(JSONObject json) throws JSONException{
//		if (!json.isNull("node"))
//			mNode = json.getString("node");
//		if (!json.isNull("needed_info"))
//			mNeededInfo = json.getString("needed_info");
//		if (!json.isNull("play"))
//			mPlay = json.getString("play");
//		if (!json.isNull("video_height"))
//			mVideoHeight = json.getInt("video_height");
//		if (!json.isNull("bitrate"))
//			mBitrate = json.getDouble("bitrate");
//		if (!json.isNull("broadcast_part"))
//			mBroadcastPart = json.getInt("broadcast_part");
//		if (!json.isNull("cluster"))
//			mCluster = json.getString("cluster");
//		if (!json.isNull("token"))
//			mToken = json.getString("token");
//		if (!json.isNull("connect"))
//			mConnect = json.getString("connect");
//		if (!json.isNull("broadcast_id"))
//			mBroadcastId = json.getInt("broadcast_id");
//		if (!json.isNull("type"))
//			mType = json.getString("type");
//	}
//
//	public String getConnect() {
//		return mConnect;
//	}
//
//	public String getToken() {
//		return mToken;
//	}
//
//	public String getPlay() {
//		return mPlay;
//	}
//
//	public String getType() {
//		return mType;
//	}
//}
