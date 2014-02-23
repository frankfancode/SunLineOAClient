package org.ff.sunshineoaclient;

import org.ff.sunshineoaclient.util.EmployeeUtil;
import org.ff.sunshineoaclient.util.LoginUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText userName, password;
	private String userNameValue, passwordValue;
	private CheckBox rem_pw, auto_login;
	private Button btn_login;
	private ImageButton btnQuit;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除标题
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		sp = this.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
		userName = (EditText) findViewById(R.id.et_userName);
		password = (EditText) findViewById(R.id.et_password);
		rem_pw = (CheckBox) findViewById(R.id.cb_password);
		auto_login = (CheckBox) findViewById(R.id.cb_auto);
		btn_login = (Button) findViewById(R.id.btn_login);
		userName.setText("********");
		password.setText("********");
		// 判断记住密码多选框的状态
		if (sp.getBoolean("ISCHECK", false)) {
			// 设置默认是记录密码状态
			rem_pw.setChecked(true);
			userName.setText(sp.getString("USERNAME", ""));
			password.setText(sp.getString("PASSWORD", ""));
			// 判断自动登陆多选框状态
			if (sp.getBoolean("AUTO_ISCHECK", false)) {
				// 设置默认是自动登录状态
				auto_login.setChecked(true);
				// 跳转界面
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				LoginActivity.this.startActivity(intent);

			}
		}
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userNameValue = userName.getText().toString();
				passwordValue = password.getText().toString();

				if (true) {
					// 登录成功和记住密码框为选中状态才保存用户信息
					if (rem_pw.isChecked()) {
						// 记住用户名、密码、
						Editor editor = sp.edit();
						editor.putString("USER_NAME", userNameValue);
						editor.putString("PASSWORD", passwordValue);
						editor.commit();
					}

					LoginUtil.initlogin();
					String alertString = LoginUtil.login(userNameValue,
							passwordValue);
					Toast.makeText(LoginActivity.this, alertString,
							Toast.LENGTH_LONG).show();
					
					EmployeeUtil employeeUtil=new EmployeeUtil();
					employeeUtil.updateEmployeeDb();
					
					// 跳转界面
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					LoginActivity.this.startActivity(intent);
					// finish();
			
//					//更新雇员信息
//					EmployeeDbHelper mDbHelper = new EmployeeDbHelper(
//							getApplicationContext());
//					// Gets the data repository in write mode
//					SQLiteDatabase db = mDbHelper.getWritableDatabase();
//					// Create a new map of values, where column names are the
//					// keys
//					ContentValues values = new ContentValues();
//					//values.put(Employee._ID);
//					values.put(Employee.EMPLOYEE_NAME, "name");
//					values.put(Employee.EMPLOYEE_PHONENO, "123456");
//
//					// Insert the new row, returning the primary key value of
//					// the new row
//					long newRowId;
//					newRowId = db.insert(Employee.TABLE_NAME, null, values);

				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
