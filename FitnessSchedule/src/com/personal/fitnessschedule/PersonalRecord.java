package com.personal.fitnessschedule;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.personal.fitnessschedule.pojos.MetricEntry;

public class PersonalRecord extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_record);
		Cursor cr = getContentResolver().query(MetricEntry.CONTENT_URI, MetricEntry.PROJECTION, "", null, null);
		
		try{
			if(cr!=null && cr.getCount() > 0){
				if(cr.moveToFirst())
					do{
						TextView tempView = new TextView(this);
						tempView.setText(cr.getInt(0)+"-"+cr.getString(1));
						((LinearLayout)findViewById(R.id.personal_record_metrics_display)).addView(tempView);
					}while(cr.moveToNext());
			}else{
				ContentValues[] tempValues = new ContentValues[3];
				MetricEntry tempEntry = new MetricEntry("pushup");
				tempValues[0] = tempEntry.getContentValues();
				tempEntry = new MetricEntry("weight");
				tempValues[1] = tempEntry.getContentValues();
				tempEntry = new MetricEntry("round");
				tempValues[2] = tempEntry.getContentValues();
				getContentResolver().bulkInsert(MetricEntry.CONTENT_URI, tempValues);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(cr!=null)
				cr.close();
		}
		
		Button metricView = (Button) findViewById(R.id.personal_record_vm);
		metricView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Cursor cr = getContentResolver().query(MetricEntry.CONTENT_URI, MetricEntry.PROJECTION, "", null, null);
				try{
					if(cr!=null && cr.getCount() > 0){
						if(cr.moveToFirst())
							do{
								TextView tempView = new TextView(PersonalRecord.this);
								tempView.setText(cr.getInt(0)+"-"+cr.getString(1));
								((LinearLayout)findViewById(R.id.personal_record_metrics_display)).addView(tempView);
							}while(cr.moveToNext());
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				finally{
					if(cr!=null)
						cr.close();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
