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

public class WorkouttrailContentProvider extends ContentProvider {

	private DataHelper						dbHelper;
	private static HashMap<String, String>	WORKOUTTRAIL_PROJECTION_MAP;
	private static final String				TABLE_NAME						= "workouttrail";
	private static final String				AUTHORITY						= "com.personal.fitnessschedule.workouttrailcontentprovider";

	public static final Uri					CONTENT_URI						= Uri.parse("content://" + AUTHORITY + "/"
																					+ TABLE_NAME);
	public static final Uri					_ID_FIELD_CONTENT_URI			= Uri.parse("content://" + AUTHORITY + "/"
																					+ TABLE_NAME.toLowerCase());
	public static final Uri					WORKOUT_ID_FIELD_CONTENT_URI	= Uri.parse("content://" + AUTHORITY + "/"
																					+ TABLE_NAME.toLowerCase()
																					+ "/workout_id");
	public static final Uri					TRAIL_FIELD_CONTENT_URI			= Uri.parse("content://" + AUTHORITY + "/"
																					+ TABLE_NAME.toLowerCase()
																					+ "/trail");

	public static final String				DEFAULT_SORT_ORDER				= "_id ASC";

	private static final UriMatcher			URL_MATCHER;

	private static final int				WORKOUTTRAIL					= 1;
	private static final int				WORKOUTTRAIL__ID				= 2;
	private static final int				WORKOUTTRAIL_WORKOUT_ID			= 3;
	private static final int				WORKOUTTRAIL_TRAIL				= 4;

	// Content values keys (using column names)
	public static final String				_ID								= "_id";
	public static final String				WORKOUT_ID						= "workout_id";
	public static final String				TRAIL							= "trail";

	public boolean onCreate() {
		dbHelper = new DataHelper(getContext(), true);
		return (dbHelper == null) ? false : true;
	}

	public Cursor query(Uri url, String[] projection, String selection, String[] selectionArgs, String sort) {
		SQLiteDatabase mDB = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		switch (URL_MATCHER.match(url)) {
			case WORKOUTTRAIL:
				qb.setTables(TABLE_NAME);
				qb.setProjectionMap(WORKOUTTRAIL_PROJECTION_MAP);
				break;
			case WORKOUTTRAIL__ID:
				qb.setTables(TABLE_NAME);
				qb.appendWhere("_id=" + url.getPathSegments().get(1));
				break;
			case WORKOUTTRAIL_WORKOUT_ID:
				qb.setTables(TABLE_NAME);
				qb.appendWhere("workout_id='" + url.getPathSegments().get(2) + "'");
				break;
			case WORKOUTTRAIL_TRAIL:
				qb.setTables(TABLE_NAME);
				qb.appendWhere("trail='" + url.getPathSegments().get(2) + "'");
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
			case WORKOUTTRAIL:
				return "vnd.android.cursor.dir/vnd.com.personal.fitnessschedule.workouttrail";
			case WORKOUTTRAIL__ID:
				return "vnd.android.cursor.item/vnd.com.personal.fitnessschedule.workouttrail";
			case WORKOUTTRAIL_WORKOUT_ID:
				return "vnd.android.cursor.item/vnd.com.personal.fitnessschedule.workouttrail";
			case WORKOUTTRAIL_TRAIL:
				return "vnd.android.cursor.item/vnd.com.personal.fitnessschedule.workouttrail";

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
		if (URL_MATCHER.match(url) != WORKOUTTRAIL) {
			throw new IllegalArgumentException("Unknown URL " + url);
		}

		rowID = mDB.insert("workouttrail", "workouttrail", values);
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
			case WORKOUTTRAIL:
				count = mDB.delete(TABLE_NAME, where, whereArgs);
				break;
			case WORKOUTTRAIL__ID:
				segment = url.getPathSegments().get(1);
				count = mDB.delete(TABLE_NAME, "_id=" + segment
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
				break;
			case WORKOUTTRAIL_WORKOUT_ID:
				segment = "'" + url.getPathSegments().get(2) + "'";
				count = mDB.delete(TABLE_NAME, "workout_id=" + segment
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
				break;
			case WORKOUTTRAIL_TRAIL:
				segment = "'" + url.getPathSegments().get(2) + "'";
				count = mDB.delete(TABLE_NAME, "trail=" + segment
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
			case WORKOUTTRAIL:
				count = mDB.update(TABLE_NAME, values, where, whereArgs);
				break;
			case WORKOUTTRAIL__ID:
				segment = url.getPathSegments().get(1);
				count = mDB.update(TABLE_NAME, values, "_id=" + segment
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
				break;
			case WORKOUTTRAIL_WORKOUT_ID:
				segment = "'" + url.getPathSegments().get(2) + "'";
				count = mDB.update(TABLE_NAME, values, "workout_id=" + segment
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""), whereArgs);
				break;
			case WORKOUTTRAIL_TRAIL:
				segment = "'" + url.getPathSegments().get(2) + "'";
				count = mDB.update(TABLE_NAME, values, "trail=" + segment
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
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase(), WORKOUTTRAIL);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/#", WORKOUTTRAIL__ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/workout_id" + "/*", WORKOUTTRAIL_WORKOUT_ID);
		URL_MATCHER.addURI(AUTHORITY, TABLE_NAME.toLowerCase() + "/trail" + "/*", WORKOUTTRAIL_TRAIL);

		WORKOUTTRAIL_PROJECTION_MAP = new HashMap<String, String>();
		WORKOUTTRAIL_PROJECTION_MAP.put(_ID, "_id");
		WORKOUTTRAIL_PROJECTION_MAP.put(WORKOUT_ID, "workout_id");
		WORKOUTTRAIL_PROJECTION_MAP.put(TRAIL, "trail");

	}
}
