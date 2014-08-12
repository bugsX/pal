package com.personal.fitnessschedule.pojos;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

public class WorkoutTypeEntry {
	
	public int	   _id = 0;
	public String name = null;
	public String workoutTemplate = null;
	
	private static final String		TABLE_NAME							= "workouttypes";
	private static final String		AUTHORITY							= "com.personal.fitnessschedule.workouttypescontentprovider";

	public static final Uri			CONTENT_URI	= Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final String[]	PROJECTION	= { "_id", "name","workout_template"};

	public WorkoutTypeEntry(int _id,String name,String workoutTemplate) {
		this._id = _id;
		this.name = name;
		this.workoutTemplate = workoutTemplate;
	}

	public WorkoutTypeEntry(Cursor cursor) {
		_id = cursor.getInt(0);
		name = cursor.getString(1);
		workoutTemplate = cursor.getString(2);
	}

	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put("name", this.name);
		values.put("workout_template", this.workoutTemplate);
		return values;
	}

	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
