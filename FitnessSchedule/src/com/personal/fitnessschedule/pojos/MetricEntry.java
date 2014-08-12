package com.personal.fitnessschedule.pojos;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

public class MetricEntry {
	
	public int	   _id = 0;
	public String metricType = null;
	
	private static final String		TABLE_NAME	= "metric";
	private static final String		AUTHORITY						= "com.personal.fitnessschedule.metriccontentprovider";

	public static final Uri			CONTENT_URI	= Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final String[]	PROJECTION	= { "_id", "metric_type"};

	public MetricEntry(String metrciType) {
		this(0,metrciType);
	}
	
	public MetricEntry(int _id,String metrciType) {
		this._id = _id;
		this.metricType = metrciType;
	}

	public MetricEntry(Cursor cursor) {
		_id = cursor.getInt(0);
		metricType = cursor.getString(1);
	}

	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put("metric_type", this.metricType);
		return values;
	}

	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
