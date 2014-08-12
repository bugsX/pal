/*
 * *****************************************************************************
 * Parts of this code sample are licensed under Apache License, Version 2.0 *
 * Copyright (c) 2009, Android Open Handset Alliance. All rights reserved. * * *
 * Except as noted, this code sample is offered under a modified BSD license. *
 * Copyright (C) 2010, Motorola Mobility, Inc. All rights reserved. * * For more
 * details, see MOTODEV_Studio_for_Android_LicenseNotices.pdf * in your
 * installation folder. *
 * *****************************************************************************
 */

package com.personal.fitnessschedule.providers;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class WorkoutContentProvider extends ContentProvider {

	private DataHelper						dbHelper;
	private static HashMap<String, String>	WORKOUT_PROJECTION_MAP;
	private static final String				TABLE_NAME							= "workout";
	private static final String				AUTHORITY							= "com.personal.fitnessschedule.workoutcontentprovider";

	public static final Uri					CONTENT_URI							= Uri.parse("content://" + AUTHORITY
																						+ "/" + TABLE_NAME);
	public static final Uri					_ID_FIELD_CONTENT_URI				= Uri.parse("content://" + AUTHORITY
																						+ "/"
																						+ TABLE_NAME.toLowerCase());
	public static final Uri					NAME_FIELD_CONTENT_URI				= Uri.parse("content://" + AUTHORITY
																						+ "/"
																						+ TABLE_NAME.toLowerCase()
																						+ "/name");
	public static final Uri					WORKOUT_TYPE_ID_FIELD_CONTENT_URI	= Uri.parse("content://" + AUTHORITY
																						+ "/"
																						+ TABLE_NAME.toLowerCase()
																						+ "/workout_type_id");
	public static final Uri					WORKOUT_FIELD_CONTENT_URI			= Uri.parse("content://" + AUTHORITY
																						+ "/"
																						+ TABLE_NAME.toLowerCase()
																						+ "/workout");

	public static final String				DEFAULT_SORT_ORDER					= "_id ASC";

	private static final UriMatcher			URL_MATCHER;

	private static final int				WORKOUT								= 1;
	private static final int				WORKOUT__ID							= 2;
	private static final int				WORKOUT_NAME						= 3;
	private static final int				WORKOUT_WORKOUT_TYPE_ID				= 4;
	private static final int				WORKOUT_WORKOUT						= 5;

	// Content values keys (using column names)
	public static final String				_ID									= "_id";
	public static final String				NAME								= "name";
	public static final String				WORKOUT_TYPE_ID						= "workout_type_id";
	public static final String				_WORKOUT								= "workout";

	public boolean onCreate() {
		dbHelper = new DataHelper(getContext(), true);
		return (dbHelper == null) ? false : true;
	}

	public Cursor query(Uri url, String[] projection, String selection, String[] selectionArgs, String sort) {
		SQLiteDatabase mDB = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (URL_MATCHER.match(url)) {
			case WORKOUT:
				qb.setTables(TABLE_NAME);
				qb.setProjectionMap(WORKOUT_PROJECTION_MAP);
				break;
			case WORKOUT__ID:
				qb.setTables(TABLE_NAME);
				qb.appendWhere("_id=" + url.getPathSegments().get(1));
				break;
			case WORKOUT_NAME:
				qb.setTables(TABLE_NAME);
				qb.appendWhere("name='" + url.getPathSegments().get(2) + "'");
				break;
			case WORKOUT_WORKOUT_TYPE_ID:
				qb.setTables(TABLE_NAME);
				qb.appendWhere("workout_type_id='" + url.getPathSegments().get(2) + "'");
				break;
			case WORKOUT_WORKOUT:
				qb.setTables(TABLE_NAME);
				qb.appendWhere("workout='" + url.getPathSegments().get(2) + "'");
				break;

			default:
				throw new IllegalArgumentException("Unknown URL " + url);
		}
		String orderBy = "";
		if (TextUtils.isEmpty(sort)) {
			orderBy = DEFAULT_SORT_ORDER;
		} else {
			orderBy = sort;
		}
		Cursor c = qb.query(mDB, projection, selection, selectionArgs, null, null, orderBy);
		c.setNotificationUri(getContext().getContentResolver(), url);
		return c;
	}

	public String getType(Uri url) {
		switch (URL_MATCHER.match(url)) {
			case WORKOUT:
				return "vnd.android.cursor.dir/vnd.com.personal.fitnessschedule.workout";
			case WORKOUT__ID:
				return "vnd.android.cursor.item/vnd.com.personal.fitnessschedule.workout";
			case WORKOUT_NAME:
				return "vnd.android.cursor.item/vnd.com.personal.fitnessschedule.workout";
			case WORKOUT_WORKOUT_TYPE_ID:
				return "vnd.android.cursor.item/vnd.com.personal.fitnessschedule.workout";
			case WORKOUT_WORKOUT:
				return "vnd.android.cursor.item/vnd.com.personal.fitnessschedule.workout";

			default:
				throw new IllegalArgumentException("Unknown URL " + url);
		}
	}

	public Uri insert(Uri url, ContentValues initialValues) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		long rowID;
		ContentValues values;
		if (initialValues != null) {
			values = new ContentValues(initialValues);
		} else {
			values = new ContentValues();
		}
		if (URL_MATCHER.match(url) != WORKOUT) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("workout", "workout", values);
		if (rowID > 0) {
			Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(uri, null);
			return uri;
		}
		throw new SQLException("Failed to insert row into " + url);
	}

	public int delete(Uri url, String where, String[] whereArgs) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		int count;
		String segment = "";
		switch (URL_MATCHER.match(url)) {
			case WORKOUT:
				count = mDB.delete(TABLE_NAME, where, whereArgs);
				break;
			case WORKOUT__ID:
				segment = url.getPathSegments().get(1);
				count = mDB.delete(TABLE_NAME, "_id=" + segment
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
				break;
			case WORKOUT_NAME:
				segment = "'" + url.getPathSegments().get(2) + "'";
				count = mDB.delete(TABLE_NAME, "name=" + segment
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
				break;
			case WORKOUT_WORKOUT_TYPE_ID:
				segment = "'" + url.getPathSegments().get(2) + "'";
				count = mDB.delete(TABLE_NAME, "workout_type_id=" + segment
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
				break;
			case WORKOUT_WORKOUT:
				segment = "'" + url.getPathSegments().get(2) + "'";
				count = mDB.delete(TABLE_NAME, "workout=" + segment
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
				break;

			default:
				throw new IllegalArgumentException("Unknown URL " + url);
		}
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	public int update(Uri url, ContentValues values, String where, String[] whereArgs) {
		SQLiteDatabase mDB = dbHelper.getWritableDatabase();
		int count;
		String segment = "";
		switch (URL_MATCHER.match(url)) {
			case WORKOUT:
				count = mDB.update(TABLE_NAME, values, where, whereArgs);
				break;
			case WORKOUT__ID:
				segment = url.getPathSegments().get(1);
				count = mDB.update(TABLE_NAME, values, "_id=" + segment
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
				break;
			case WORKOUT_NAME:
				segment = "'" + url.getPathSegments().get(2) + "'";
				count = mDB.update(TABLE_NAME, values, "name=" + segment
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
				break;
			case WORKOUT_WORKOUT_TYPE_ID:
				segment = "'" + url.getPathSegments().get(2) + "'";
				count = mDB.update(TABLE_NAME, values, "workout_type_id=" + segment
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
				break;
			case WORKOUT_WORKOUT:
				segment = "'" + url.getPathSegments().get(2) + "'";
				count = mDB.update(TABLE_NAME, values, "workout=" + segment
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
				break;

			default:
				throw new IllegalArgumentException("Unknown URL " + url);
		}
		getContext().getContentResolver().notifyChange(url, null);
		return count;
	}

	static {
		URL_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), WORKOUT);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/#", WORKOUT__ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/name" + "/*", WORKOUT_NAME);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/workout_type_id" + "/*", WORKOUT_WORKOUT_TYPE_ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/workout" + "/*", WORKOUT_WORKOUT);

		WORKOUT_PROJECTION_MAP = new HashMap<String, String>();
		WORKOUT_PROJECTION_MAP.put(_ID, "_id");
		WORKOUT_PROJECTION_MAP.put(NAME, "name");
		WORKOUT_PROJECTION_MAP.put(WORKOUT_TYPE_ID, "workout_type_id");
		WORKOUT_PROJECTION_MAP.put(_WORKOUT, "workout");

	}
}
