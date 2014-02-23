package org.ff.sunshineoaclient;

import android.app.Application;

public class OaApplication extends Application {
	private static OaApplication instance;

	public static OaApplication getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
	}

	private String JSESSIONID;

	/**
	 * @return the jSESSIONID
	 */
	public String getJSESSIONID() {
		return JSESSIONID;
	}

	/**
	 * @param jSESSIONID
	 *            the jSESSIONID to set
	 */
	public void setJSESSIONID(String jSESSIONID) {
		JSESSIONID = jSESSIONID;
	}
}
