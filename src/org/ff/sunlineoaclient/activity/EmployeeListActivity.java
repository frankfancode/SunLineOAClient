package org.ff.sunlineoaclient.activity;

import android.app.*;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.ff.sunlineoaclient.OaApplication;
import org.ff.sunlineoaclient.R;
import org.ff.sunlineoaclient.adapter.EmployeeListAdapter;
import org.ff.sunlineoaclient.db.Employee;
import org.ff.sunlineoaclient.provider.EmployeeListProvider;
import org.ff.sunlineoaclient.util.EmployeeUtil;
import org.ff.sunlineoaclient.view.InsertProgressDialog;

import java.util.ArrayList;

/**
 * @author Frank Fan
 * @since 0.0.1
 */
public class EmployeeListActivity extends Activity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private String TAG = "EmployeeListActivity";
    private ArrayList<Employee> employeeList;
    private EmployeeListAdapter aa;
    private ProgressDialog m_Dialog;
    private ProgressBar pb;
    private InsertProgressDialog progressDialog;
    private Boolean yesornot = true;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        Log.i(TAG, "start");
        FragmentManager fm = getFragmentManager();

        EmployeeListFragment employeeListFragment = (EmployeeListFragment) fm
                .findFragmentById(R.id.employeeListFragment);

        employeeList = new ArrayList<Employee>();

        int resID = R.layout.employee_item;
        aa = new EmployeeListAdapter(this, resID, employeeList);

        employeeListFragment.setListAdapter(aa);

        getLoaderManager().initLoader(0, null, this);

        Button update = (Button) findViewById(R.id.update);


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!OaApplication.getInstance().isConnected()) {
                    Toast.makeText(EmployeeListActivity.this, "哎呀，没网了，连上网再更新好不好啊？", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog();
//                if (yesornot) {
//                    backgroundExecution();
//                    m_Dialog.show(EmployeeListActivity.this, "请等待...", "正在下载联系人信息，请稍后...", true);
//                }


//                m_Dialog = new ProgressDialog(EmployeeListActivity.this);
//                //.show(EmployeeListActivity.this, "请等待...", "正在下载安装文件，请稍后...", true)
//                m_Dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                m_Dialog.setMax(100);
//                m_Dialog.setProgress(100);


            }
        });

    }

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("更新联系人信息将会消耗流量，是否更新？");

        builder.setTitle("提示");

        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                backgroundExecution();
                yesornot = true;

            }
        });

        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                yesornot = false;
            }
        });

        builder.create().show();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {//定义一个Handler，用于处理下载线程与UI间通讯
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 0:
                        pb.setMax(100);
                    case 1:
                        pb.setProgress(OaApplication.progressing);
                        getLoaderManager().restartLoader(0, null, EmployeeListActivity.this);
                        break;
                    case 2:
                        Toast.makeText(EmployeeListActivity.this, "联系人信息更新完毕", 1).show();
                        getLoaderManager().restartLoader(0, null, EmployeeListActivity.this);
                        break;

                    case -1:
                        String error = msg.getData().getString("error");
                        Toast.makeText(EmployeeListActivity.this, error, 1).show();

                        break;
                }
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader loader = new CursorLoader(this,
                EmployeeListProvider.CONTENT_URI, null, null, null, null);

        return loader;
    }

    private void backgroundExecution() {

        new Thread() {
            public void run() {
                try {
                    onUpdateEmployee();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();

    }
//
//    private Runnable doBackgroundThreadProcessing = new Runnable() {
//        @Override
//        public void run() {
//            onUpdateEmployee();
//            sendMsg(0);
//            m_Dialog.dismiss();
//
//        }
//    };


    private void sendMsg(int flag) {
        Message msg = new Message();
        msg.what = flag;
        handler.sendMessage(msg);
    }

    public void onUpdateEmployee() {
        ContentResolver cr = getContentResolver();

        cr.delete(EmployeeListProvider.CONTENT_URI, null, null);
        EmployeeUtil.insertEmployeeDB(getBaseContext(), handler);
        //new UpdateAsyncTask().execute(getBaseContext());

        //cr.insert(EmployeeListProvider.CONTENT_URI, values);


    }


    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int keyEmployeeNameIndex = cursor.getColumnIndexOrThrow(EmployeeListProvider.KEY_EMPLOYEE_NAME);
        int keyEmployeePhoneNoIndex = cursor.getColumnIndexOrThrow(EmployeeListProvider.KEY_EMPLOYEE_PHONENO);

        employeeList.clear();
        while (cursor.moveToNext()) {
            Employee newItem = new Employee(cursor.getString(keyEmployeeNameIndex), cursor.getString(keyEmployeePhoneNoIndex));
            employeeList.add(newItem);
        }
        aa.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}