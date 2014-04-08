package org.ff.sunlineoaclient.db;

import org.ff.sunlineoaclient.db.EmployeeDb.EmployeeTB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EmployeeDbHelper extends SQLiteOpenHelper {
	// If you change the database schema, you must increment the database
	// version.
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "EmployeeList.db";

	public EmployeeDbHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

	private static final String TEXT_TYPE = " TEXT ";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_EMPLOYEE = "CREATE TABLE "
			+ EmployeeTB.TABLE_NAME + " (" + EmployeeTB._ID
			+ " INTEGER PRIMARY KEY," + EmployeeTB.EMPLOYEE_NAME + TEXT_TYPE
			+ COMMA_SEP + EmployeeTB.EMPLOYEE_PHONENO + TEXT_TYPE
			+ " )";
	private static final String SQL_DELETE_EMPLOYEE = "DROP TABLE IF EXISTS "
			+ EmployeeTB.TABLE_NAME;

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_EMPLOYEE);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// This database is only a cache for online data, so its upgrade policy
		// is
		// to simply to discard the data and start over
		db.execSQL(SQL_DELETE_EMPLOYEE);
		onCreate(db);
	}

	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
}