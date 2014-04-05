package org.ff.sunlineoaclient.util;

import java.io.IOException;
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
import org.apache.http.util.EntityUtils;
import org.ff.sunlineoaclient.OaApplication;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class LoginUtil {
    private static LoginUtil instance;

    public static LoginUtil getInstance() {
        return instance;
    }

    // 登录时需要的字段
    static String sysName = null;
    static String oprID = null;
    static String actions = null;

    public static void initlogin() {

        Document loginDoc;
        final OaApplication app = new OaApplication();
        try {
            // 请求主页面
            Connection conn = Jsoup.connect("http://oa.sunline.cn/");
            loginDoc = conn.get();
            OaApplication.getInstance().setJSESSIONID(
                    conn.response().cookie("JSESSIONID"));
            Log.i("JSESSIONID", OaApplication.getInstance().getJSESSIONID());
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

    public static String login(String userNameValue, String passwordValue) {
        String alertString = "";
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
                userNameValue);
        BasicNameValuePair userpwdPair = new BasicNameValuePair("userpwd",
                passwordValue);
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
            post.setHeader("Cookie", "JSESSIONID="
                    + OaApplication.getInstance().getJSESSIONID());
            HttpResponse response = new DefaultHttpClient().execute(post);
            List<Cookie> cookies = ((AbstractHttpClient) client)
                    .getCookieStore().getCookies();

            HttpEntity responseEntity = response.getEntity();
            //System.out.println(EntityUtils.toString(responseEntity));
            String html = EntityUtils.toString(response.getEntity());
            Document document = Jsoup.parse(html, "http://oa.sunline.cn/httpprocesserservlet");
            //Log.i("document.html()", document.html());

            //
            if (document.select("script").size() == 1) {
                Element link = document.select("script").first();//查找第一个a元素
                String linkOuterH = link.html();
                Log.i("String", "linkHref" + linkOuterH);
                if (null != linkOuterH) {
                    alertString = linkOuterH.substring(linkOuterH.indexOf("(") + 2, linkOuterH.indexOf(")") - 3);
                }
                Log.i("String", "alertString" + alertString);
            }


            Elements loginElements = document.getElementsByTag("script");
            //Element link = document.select("script").first();
            //Log.i("text", link.text());
            for (Element inputLogin : loginElements) {
                Log.i("inputlogin", inputLogin.attr("value"));
                if (inputLogin.attr("name").equals("sysName")) {
                    sysName = inputLogin.attr("value");
                } else if (inputLogin.attr("name").equals("oprID")) {
                    oprID = inputLogin.attr("value");
                } else if (inputLogin.attr("name").equals("actions")) {
                    actions = inputLogin.attr("value");
                }
            }

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
                } else {
                    for (int i = 0; i < cookies.size(); i++) {
                        // 保存cookie
                        cookie = cookies.get(i);
                        OaApplication.getInstance().setJSESSIONID(
                                cookies.get(i).getValue());
                        if ("JSESSIONID".equals(cookies.get(i).getName())) {
                            OaApplication.getInstance().setJSESSIONID(cookies.get(i).getValue());
                            Log.i("login", cookies.get(i).getValue());
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

        return alertString;
    }
}
