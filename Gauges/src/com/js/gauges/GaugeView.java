package com.js.gauges;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class GaugeView extends LinearLayout {
	private SharedPreferences preferences;
	public GaugeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.gauge_view, this);		
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		TextView sensorValue = (TextView) findViewById(R.id.sensorType);
		sensorValue.setText(this.getDisplayLabel());
	}
	
	public SharedPreferences getPreferences() {
		return preferences;
	}

	private void setInRange(boolean inRange, LinearLayout grid) {
		int color = inRange ? R.color.green : R.color.red;
		grid.setBackgroundColor(getResources().getColor(color));
	}

	public void refreshDisplay() {
		 TextView sensorValue = (TextView) findViewById(R.id.sensorValue);
		 sensorValue.setText(this.getDisplayValue());

		 setInRange(isInRange(), (LinearLayout) findViewById(R.id.sensorLayout));
	}
	
	public void showNoData() {
		TextView sensorValue = (TextView) findViewById(R.id.sensorValue);
		sensorValue.setText("--");
		setInRange(false, (LinearLayout) findViewById(R.id.sensorLayout));
	}

	public abstract String getDisplayValue();

	public abstract String getDisplayLabel();

	public abstract boolean isInRange();
}
