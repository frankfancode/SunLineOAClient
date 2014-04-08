package org.ff.sunlineoaclient.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import org.ff.sunlineoaclient.R;
import org.ff.sunlineoaclient.adapter.EmployeeListAdapter;
import org.ff.sunlineoaclient.db.Employee;
import org.ff.sunlineoaclient.provider.EmployeeListProvider;

import java.util.ArrayList;

/**
 * @author Frank Fan
 * @since 0.0.1
 */
public class AddressListActivity extends Activity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private String TAG = "AddressListActivity";
    private ArrayList<Employee> employeeList;
    private EmployeeListAdapter aa;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        Log.i(TAG, "start");
        FragmentManager fm = getFragmentManager();
        EmployeeListFragment employeeListFragment = (EmployeeListFragment) fm
                .findFragmentById(R.id.AddressListFragment);

        employeeList = new ArrayList<Employee>();

        int resID = R.layout.address_item;
        aa = new EmployeeListAdapter(this, resID, employeeList);

        employeeListFragment.setListAdapter(aa);

        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader loader = new CursorLoader(this,
                EmployeeListProvider.CONTENT_URI, null, null, null, null);

        return loader;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}