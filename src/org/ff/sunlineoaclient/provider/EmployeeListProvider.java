/*
 * Copyright 2012-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ff.sunlineoaclient.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import org.ff.sunlineoaclient.db.EmployeeDbHelper;

/**
 * @author Frank Fan
 * @since 0.0.1
 */
public class EmployeeListProvider extends ContentProvider {
    public static final Uri CONTENT_URI =
            Uri.parse("content://oorg.ff.sunlineoaclient.provider/addressitems");

    public static final String KEY_ID = "_id";
    public static final String KEY_TASK = "task";
    public static final String KEY_CREATION_DATE = "creation_date";

    private EmployeeDbHelper employeeDbHelper;


    @Override
    public boolean onCreate() {
        employeeDbHelper = new EmployeeDbHelper(getContext(),
                EmployeeDbHelper.DATABASE_NAME, null,
                EmployeeDbHelper.DATABASE_VERSION);

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
