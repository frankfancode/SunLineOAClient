package org.ff.sunlineoaclient.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import org.ff.sunlineoaclient.R;
import org.ff.sunlineoaclient.adapter.EmployeeListAdapter;
import org.ff.sunlineoaclient.db.Employee;
import org.ff.sunlineoaclient.provider.EmployeeListProvider;
import org.ff.sunlineoaclient.util.EmployeeUtil;

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
                onUpdateEmployee("test");

            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader loader = new CursorLoader(this,
                EmployeeListProvider.CONTENT_URI, null, null, null, null);

        return loader;
    }


    public void onUpdateEmployee(String newItem) {
        ContentResolver cr = getContentResolver();

        cr.delete(EmployeeListProvider.CONTENT_URI, null, null);
        EmployeeUtil.insertEmployeeDB(getBaseContext());
        //cr.insert(EmployeeListProvider.CONTENT_URI, values);

        getLoaderManager().restartLoader(0, null, this);
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