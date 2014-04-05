package org.ff.sunlineoaclient.db;

import android.provider.BaseColumns;

public class EmployeeDb {
	public EmployeeDb() {
	}
	/* Inner class that defines the table contents */
	public static abstract class Employee implements BaseColumns {
		public static final String TABLE_NAME = "employee";
		public static final String EMPLOYEE_NAME = "employee_name";
		public static final String EMPLOYEE_PHONENO = "phoneno";
	}

	
}
