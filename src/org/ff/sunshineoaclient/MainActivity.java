package org.ff.sunshineoaclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.ff.sunshineoaclient.util.LoginUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	static String TAG = "Main";

	// 登录时需要的字段
	static String sysName = null;
	static String oprID = null;
	static String actions = null;
	static String actions_Search = null;
	static String name = null;
	static String lxdh = null;
	static String mobile = null;
	static String email = null;
	static String PageNo = null;
	static String sessionId = null;
	static String JSESSIONID = null; // 用来保存JSESSIONID
	String usercode = null;
	String userpwd = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new Thread(runnable).start();
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String val = data.getString("value");
			Log.i("mylog", "请求结果-->" + val);
		}
	};
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			//
			// TODO: http request.
			//
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("value", "请求结果");
			msg.setData(data);
			handler.sendMessage(msg);
			
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private static void initlogin() {
		Document loginDoc;
		try {
			// 请求主页面
			Connection conn = Jsoup.connect("http://oa.sunline.cn/");
			loginDoc = conn.get();
			JSESSIONID = conn.response().cookie("JSESSIONID");
			Log.i("JSESSIONID", JSESSIONID);
			Elements loginElements = loginDoc.getElementsByTag("input");
			// Elements links = content.getElementsByTag("a");
			for (Element inputLogin : loginElements) {
				if (inputLogin.attr("name").equals("sysName")) {
					sysName = inputLogin.attr("value");
				} else if (inputLogin.attr("name").equals("oprID")) {
					oprID = inputLogin.attr("value");
				} else if (inputLogin.attr("name").equals("actions")) {
					actions = inputLogin.attr("value");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void login() {
		// 实例化HttpClient
		HttpClient client = new DefaultHttpClient();
		// 用目标地址 实例一个POST方法
		HttpPost post = new HttpPost(
				"http://oa.sunline.cn/httpprocesserservlet");
		post.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
		// 阻止自动重定向，目的是获取第一个ResponseHeader的Cookie和Location
		post.setHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=gbk");
		HttpParams params = new BasicHttpParams();//
		params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 8000); // 连接超时
		HttpConnectionParams.setSoTimeout(params, 5000); // 响应超时
		post.setParams(params);

		ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
		// 将需要的键值对写出来
		BasicNameValuePair sysNamePair = new BasicNameValuePair("sysName",
				sysName);
		BasicNameValuePair oprIDPair = new BasicNameValuePair("oprID", oprID);
		BasicNameValuePair actionsPair = new BasicNameValuePair("actions",
				actions);
		BasicNameValuePair usercodePair = new BasicNameValuePair("usercode",
				"********");
		BasicNameValuePair userpwdPair = new BasicNameValuePair("userpwd",
				"********");
		// 给POST方法加入上述键值对
		// post.setRequestBody(new NameValuePair[] { sysNamePair, oprIDPair,
		// actionsPair, usercodePair, userpwdPair });

		pairList.add(sysNamePair);
		pairList.add(oprIDPair);
		pairList.add(actionsPair);
		pairList.add(usercodePair);
		pairList.add(userpwdPair);

		try {
			HttpEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
			post.setEntity(entity);
			post.setHeader("Cookie", "JSESSIONID=" + JSESSIONID);
			HttpResponse response = new DefaultHttpClient().execute(post);
			List<Cookie> cookies = ((AbstractHttpClient) client)
					.getCookieStore().getCookies();

			HttpEntity responseEntity = response.getEntity();
			// System.out.println(EntityUtils.toString(responseEntity));

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				System.out.println();
				String buff;
				try {
					// buff = StreamTool
					// .readInputStream(response.getEntity().getContent());
					// // 将返回的内容格式化为String存在html中
					// String html = new String(buff);
					// System.out.println(html);
					// 任务完成了，释放连接
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Cookie cookie = null;

				if (cookies.isEmpty()) {
					Log.i(TAG, "-------Cookie NONE---------");
				} else {
					for (int i = 0; i < cookies.size(); i++) {
						// 保存cookie
						cookie = cookies.get(i);
						Log.i(TAG, cookies.get(i).getName() + "="
								+ cookies.get(i).getValue());
						JSESSIONID = cookies.get(i).getValue();
						if ("JSESSIONID".equals(cookies.get(i).getName())) {
							JSESSIONID = cookies.get(i).getValue();
							Log.i("login", JSESSIONID);
							/*
							 * 
							 * Editor editor = sp . edit ( ) ; editor .
							 * putString ( "JSESSIONID" , JSESSIONID ) ; editor
							 * . commit ( ) ;
							 * 
							 * Log. i ( TAG , "JSESSIONID=" + cookies . get ( i
							 * ) . getValue ( ) ) ;
							 */
						} else {
							Log.i("login", "value is not equal JSESSIN");
						}
					}
				}

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void initSearch() {
		Document loginDoc;
		try {
			loginDoc = Jsoup
					.connect("http://oa.sunline.cn/oams/pi/ggtxlnew.jsp")
					.userAgent("Mozilla").post();
			Elements loginElements = loginDoc.getElementsByTag("input");
			// Elements links = content.getElementsByTag("a");
			for (Element inputLogin : loginElements) {
				if (inputLogin.attr("name").equals("sysName")) {
					sysName = inputLogin.attr("value");
				} else if (inputLogin.attr("name").equals("oprID")) {
					oprID = inputLogin.attr("value");
				} else if (inputLogin.attr("name").equals("actions")) {
					actions_Search = inputLogin.attr("value");
					System.out.println(actions_Search);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("initSearch");

	}

	private static void searchList() {
		System.out.println("searchList_start");

		// 将需要的键值对写出来
		ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
		// 将需要的键值对写出来
		BasicNameValuePair sysNamePair = new BasicNameValuePair("sysName",
				sysName);
		BasicNameValuePair oprIDPair = new BasicNameValuePair("oprID", oprID);
		BasicNameValuePair actionsPair = new BasicNameValuePair("actions",
				actions_Search);
		BasicNameValuePair forwardPair = new BasicNameValuePair("forward",
				"/oams/pi/ggtxlnew.jsp");
		BasicNameValuePair namePair = new BasicNameValuePair("name", "");
		BasicNameValuePair lxdhPair = new BasicNameValuePair("lxdh", "");
		BasicNameValuePair mobilePair = new BasicNameValuePair("mobile", "");
		BasicNameValuePair PageNoPair = new BasicNameValuePair("PageNo", "1");
		BasicNameValuePair usercodePair = new BasicNameValuePair("usercode",
				"********");
		BasicNameValuePair userpwdPair = new BasicNameValuePair("userpwd",
				"********");
		BasicNameValuePair emailPair = new BasicNameValuePair("email", "@");

		pairList.add(sysNamePair);
		pairList.add(oprIDPair);
		pairList.add(actionsPair);
		pairList.add(forwardPair);
		pairList.add(namePair);
		pairList.add(lxdhPair);
		pairList.add(mobilePair);
		pairList.add(PageNoPair);
		// pairList.add(usercodePair);
		// pairList.add(userpwdPair);
		pairList.add(emailPair);

		HttpPost post = new HttpPost("http://oa.sunline.cn/oams/pi/ggtxl.so");

		try {
			HttpEntity entity = null;
			entity = new UrlEncodedFormEntity(pairList, "GBK");
			post.setEntity(entity);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// HttpParams params = new BasicHttpParams();//
		// params = new BasicHttpParams();
		// HttpConnectionParams.setConnectionTimeout(params, 8000); // 连接超时
		// HttpConnectionParams.setSoTimeout(params, 5000); // 响应超时
		// post.setParams(params);

		post.setHeader("Cookie", "JSESSIONID=" + JSESSIONID);
		// 实例化HttpClient
		HttpClient client = new DefaultHttpClient();

		HttpResponse response;
		try {
			response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				List<Cookie> cookies = ((AbstractHttpClient) client)
						.getCookieStore().getCookies();

				if (cookies.isEmpty()) {
					Log.i(TAG, "-------Cookie NONE---------");
				} else {

					for (int i = 0; i < cookies.size(); i++) { // 保存cookie
																// cookie =
						cookies.get(i);
						Log.i(TAG, cookies.get(i).getName() + "="
								+ cookies.get(i).getValue());
						if ("JSESSIONID".equals(cookies.get(i).getName())) {
						}
					}

				}

			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// HttpEntity responseEntity = response.getEntity();
		// System.out.println(EntityUtils.toString(responseEntity));

		System.out.println("searchList_Over");
	}

	/**
	 * 获取网页HTML源代码
	 * 
	 * @param url
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */

	/*
	 * private static String getHtml(String url) throws ParseException,
	 * IOException { // TODO Auto-generated method stub HttpGet get = new
	 * HttpGet(url); if ("" != cookie) { get.addHeader("Cookie", cookie); }
	 * HttpResponse httpResponse = new DefaultHttpClient().execute(get);
	 * HttpEntity entity = httpResponse.getEntity(); return
	 * EntityUtils.toString(entity); }
	 */
}
