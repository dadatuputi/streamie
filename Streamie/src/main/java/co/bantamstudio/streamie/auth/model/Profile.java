package co.bantamstudio.streamie.auth.model;

public interface Profile {
	public String getUsername();
	public String getName();
	public String getPicture();
	public long getId();
	public String getProfileUrl();
	public String getPrimaryTagline();
	public String getSecondaryTagline();
}
