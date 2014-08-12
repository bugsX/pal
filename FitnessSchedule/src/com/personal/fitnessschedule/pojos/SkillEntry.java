package com.personal.fitnessschedule.pojos;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

public class SkillEntry {
	
	public int	   _id = 0;
	public String name = null;
	public String metricTypes = null;
	
	private static final String		TABLE_NAME	= "skill";
	private static final String		AUTHORITY	= "com.personal.fitnessschedule.skillcontentprovider";

	public static final Uri			CONTENT_URI	= Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final String[]	PROJECTION	= { "_id", "name","metric_types"};

	public SkillEntry(int _id,String name,String metric_types) {
		this._id = _id;
		this.name = name;
		this.metricTypes = metric_types;
	}

	public SkillEntry(Cursor cursor) {
		_id = cursor.getInt(0);
		name = cursor.getString(1);
		metricTypes = cursor.getString(2);
	}

	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put("name", this.name);
		values.put("metric_types", this.metricTypes);
		return values;
	}

	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
