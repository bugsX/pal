package com.personal.fitnessschedule.pojos;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;

public class WorkoutEntry {
	
	public int	   _id = 0;
	public String name = null;
	public int		workoutTypeID = 0;
	public String		workout = null;
	
	public static final String				WORKOUT_TYPE_ID						= "workout_type_id";
	public static final String				_WORKOUT								= "workout";
	
	private static final String		TABLE_NAME							= "workout";
	private static final String		AUTHORITY							= "com.personal.fitnessschedule.workoutcontentprovider";

	public static final Uri			CONTENT_URI	= Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);
	public static final String[]	PROJECTION	= { "_id", "name","workout_type_id","workout"};

	public WorkoutEntry(int _id,String name,int workoutTypeID,String workout) {
		this._id = _id;
		this.name = name;
		this.workoutTypeID = workoutTypeID;
		this.workout = workout;
	}

	public WorkoutEntry(Cursor cursor) {
		_id = cursor.getInt(0);
		name = cursor.getString(1);
		workoutTypeID = cursor.getInt(2);
		workout = cursor.getString(3);
	}

	public ContentValues getContentValues() {
		ContentValues values = new ContentValues();
		values.put("name", this.name);
		values.put("workout_type_id", this.workoutTypeID);
		values.put("workout", this.workout);
		return values;
	}

	public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}
