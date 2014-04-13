package org.ff.sunlineoaclient.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.ff.sunlineoaclient.OaApplication;
import org.ff.sunlineoaclient.db.Employee;
import org.ff.sunlineoaclient.db.EmployeeDb;
import org.ff.sunlineoaclient.provider.EmployeeListProvider;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

/* update the table Employee
 * go to the contacts in OA search all of employees' name and phone number
 * put all employee information to an array 
 * at the last,insert array to the table Employee
 * */
public class EmployeeUtil {

    // 登录时需要的字段
    static String sysName = null;
    static String oprID = null;
    static String actions = null;
    static String actions_Search = null;
    static String name = null;
    static String lxdh = null;
    static String mobile = null;
    static String email = null;
    static int pageNo = 1;
    static String sessionId = null;
    static String JSESSIONID = OaApplication.getInstance().getJSESSIONID(); // 用来保存JSESSIONID
    String usercode = null;
    String userpwd = null;

    static int totalEmployee = 0;
    static int totalPage = 1;
    static int currentPageNo = 0;

    private static Employee[] employeesAll = null;
    private static ArrayList<Employee> employeeList = new ArrayList<Employee>();

    public void updateEmployeeDb(Context context) {
        initSearch();
        searchContacts(context);
    }

    private static void initSearch() {
        Document loginDoc;
        try {
            loginDoc = Jsoup.connect("http://oa.sunline.cn/oams/pi/ggtxlnew.jsp")
                    .userAgent("Mozilla")
                    .post();
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
        System.out.println("initSearchOver");
        Log.i("some values needs", "sysName:" + sysName + " oprId：" + oprID
                + "actions_Search：" + actions_Search);
    }

    // 搜索雇员信息
    private static void searchContacts(Context context) {
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
        BasicNameValuePair lastPageNoPair;
        BasicNameValuePair currentPageNoPair;


        BasicNameValuePair emailPair = new BasicNameValuePair("email", "@");
        pairList.add(sysNamePair);
        pairList.add(oprIDPair);
        pairList.add(actionsPair);
        pairList.add(forwardPair);
        pairList.add(namePair);
        pairList.add(lxdhPair);
        pairList.add(mobilePair);
        //pairList.add(PageNoPair);
        pairList.add(emailPair);

        //pairList.set(pairList.indexOf("PageNo")-1,"9");
        // pairList.add(usercodePair);
        // pairList.add(userpwdPair);


        HttpPost post = new HttpPost("http://oa.sunline.cn/oams/pi/ggtxl.so");

        HttpParams params;//
        params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 8000); // 连接超时
        HttpConnectionParams.setSoTimeout(params, 5000); // 响应超时
        post.setParams(params);

        post.setHeader("Cookie", "JSESSIONID=" + JSESSIONID);
        // 实例化HttpClient
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;


        currentPageNoPair = new BasicNameValuePair("PageNo", 1 + "");
        for (pageNo = 1; pageNo <= totalPage; pageNo++) {
            if (pageNo > 1) {
                lastPageNoPair = currentPageNoPair;
                pairList.remove(pairList.indexOf(lastPageNoPair));
            }
            currentPageNoPair = new BasicNameValuePair("PageNo", pageNo + "");
            pairList.add(currentPageNoPair);
            try {
                HttpEntity entity = null;
                entity = new UrlEncodedFormEntity(pairList, "GBK");
                //StringEntity stringEntity = new StringEntity("sysName="+sysName+"&oprID="+oprID+"&actions="+actions_Search+"&PageNo="+9+"&email=@");//param参数，可以为"key1=value1&key2=value2"的一串字符串
                post.setEntity(entity);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            try {

                response = client.execute(post);

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

                    List<Cookie> cookies = ((AbstractHttpClient) client)
                            .getCookieStore().getCookies();
                    //Log.i("searchContacts",EntityUtils.toString(response.getEntity()));
                    String employeeHtml = EntityUtils
                            .toString(response.getEntity());

                    Document tableDoc = Jsoup.parse(employeeHtml);
                    Elements countInfo = tableDoc.select("tr.toolbar");


                    Element totalElement = countInfo.get(0).select("font").first();
                    Log.i("tr.toolbar", countInfo.outerHtml());
                    totalEmployee = Integer.parseInt(totalElement.text());
                    Element totalPageElement = countInfo.get(0).select("font").get(2);
                    totalPage = Integer.parseInt(totalPageElement.text());
                    Log.i("totalPage", totalPage + "");

                    employeeList.addAll(tableToArray(employeeHtml));
                    for (Employee e : tableToArray(employeeHtml)) {
                        System.out.println(e.getEmployeeName() + e.getEmployeePhoneNo());
                        EmployeeListProvider a = new EmployeeListProvider();

                        ContentResolver cr = context.getContentResolver();
                        //a.onCreate();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(EmployeeDb.EmployeeTB.EMPLOYEE_NAME, e.getEmployeeName());
                        contentValues.put(EmployeeDb.EmployeeTB.EMPLOYEE_PHONENO, e.getEmployeePhoneNo());
                        //a.insert(EmployeeListProvider.CONTENT_URI,contentValues);
                        cr.insert(EmployeeListProvider.CONTENT_URI, contentValues);

                    }
//				Log.i("employees",employees.toString());
                    if (cookies.isEmpty()) {
                        Log.i("TAG", "-------Cookie NONE---------");
                    } else {

                        for (int i = 0; i < cookies.size(); i++) { // 保存cookie
                            // cookie =
                            cookies.get(i);
                            Log.i("TAG", cookies.get(i).getName() + "="
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


        }


        // HttpEntity responseEntity = response.getEntity();
        // System.out.println(EntityUtils.toString(responseEntity));

        System.out.println("searchList_Over");
    }

    // 将返回的界面表格转换成数组

    public static ArrayList<Employee> tableToArray(String tableHtml) {
        System.out.println("tableToArray_start");

        Document tableDoc = Jsoup.parse(tableHtml);// 解析HTML字符串返回一个Document实现

        Elements tableElements = tableDoc.select("table#roww");
        Document employeeDoc = Jsoup.parse(tableElements.outerHtml());
        //System.out.println(employeeDoc.html());

        Elements trs = employeeDoc.select("tr");
        trs.remove(0);
        //System.out.println(trs.html());

        String employeeName = null;
        String employeePhoneNo = null;
        ArrayList<Employee> employees = new ArrayList<Employee>();
        for (Element row : trs) {
            Elements cols = row.children();
            employeeName = cols.get(2).text().trim();
            employeePhoneNo = cols.get(4).text().trim();
            Employee oneEmployee = new Employee();
            oneEmployee.setEmployeeName(employeeName);
            oneEmployee.setEmployeePhoneNo(employeePhoneNo);
            employees.add(oneEmployee);
//			for(Element child:cols){
//				System.out.println(child.html());
//				if (child.get) {
//					
//				}
//			}
        }

        for (Employee employee : employees) {
            System.out.println(employee.getEmployeeName());
        }
        System.out.println("tableToArray_over");

        return employees;

    }

    public static void insertEmployeeDB(Context context) {
        initSearch();
        searchContacts(context);
    }
}
