package com.personal.fitnessschedule.pojos;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

public class WorkoutTrailEntry {
	
	public int	   _id = 0;
	public int workoutID = 0;
	public String	trail = "";
	
	private static final String		TABLE_NAME						= "workouttrail";
	private static final String		AUTHORITY						= "com.personal.fitnessschedule.workouttrailcontentprovider";

	public static final Uri			CONTENT_URI	= Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final String[]	PROJECTION	= { "_id", "workout_id","trail"};

	public WorkoutTrailEntry(int _id,int workoutID,String trail) {
		this._id = _id;
		this.workoutID = workoutID;
		this.trail = trail;
	}

	public WorkoutTrailEntry(Cursor cursor) {
		_id = cursor.getInt(0);
		workoutID = cursor.getInt(1);
		trail = cursor.getString(2);
	}

	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put("workout_id", this.workoutID);
		values.put("trail", this.trail);
		return values;
	}

	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
