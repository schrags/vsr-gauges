package com.js.gauges;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class GaugeView extends LinearLayout {
	private SharedPreferences preferences;
	private double value;
	private double peakMax = Double.MIN_VALUE;
	private double peakMin = Double.MAX_VALUE;
	public boolean showPeakMin = false;
	public boolean showPeakMax = false;
	
	//Gauge views
	private LinearLayout sensorLayout;
	private TextView sensorValue;
	private TextView minMaxValue;
	private TextView sensorType;
	
	
	public GaugeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,R.styleable.GaugeView, 0, 0);
		try {
			showPeakMin = a.getBoolean(R.styleable.GaugeView_showPeakMin, false);
			showPeakMax = a.getBoolean(R.styleable.GaugeView_showPeakMax, false);
		} finally {
			a.recycle();
		}		
		inflate(context, R.layout.gauge_view, this);		
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		
		sensorLayout = (LinearLayout) findViewById(R.id.sensorLayout);
		sensorValue = (TextView) findViewById(R.id.sensorValue);
		minMaxValue = (TextView) findViewById(R.id.sensorMinMax);
		sensorType = (TextView) findViewById(R.id.sensorType);
		sensorType.setText(this.getDisplayLabel());
		
	}
	
	public SharedPreferences getPreferences() {
		return preferences;
	}

	private void setInRange(boolean inRange) {
		int color = inRange ? R.color.green : R.color.red;
		sensorLayout.setBackgroundColor(getResources().getColor(color));
	}

	private void refreshDisplay() {
		if(!sensorValue.getText().equals(this.getDisplayValue()))
		{
			sensorValue.setText(this.getDisplayValue());
			 
			 String minMax = "";
			 if (showPeakMin)
			 {
				 minMax += "Min: " + valueFormatter(peakMin);
			 }
			 if (showPeakMax)
			 {
				 if (!minMax.equals(""))
					 minMax += " / ";
				 minMax += "Max: " + valueFormatter(peakMax);
			 }
			 minMaxValue.setText(minMax);

			 setInRange(isInRange());
		}
		 
	}
	
	public void showNoData() {
		sensorValue.setText("--");
		setInRange(false);
	}
	
	public double getPeakMax() {
		return peakMax;
	}
	
	public double getPeakMin() {
		return peakMin;
	}
	
	public void setVoltage(double voltage) {
		value = this.voltageToValue(voltage);
		if (value > peakMax)
			peakMax = value;
		if (value < peakMin)
			peakMin = value;
		this.refreshDisplay();
	}
	
	public void resetPeakValues() {
		peakMax = Double.MIN_VALUE;
		peakMin = Double.MAX_VALUE;
	}
	
	public double getValue() {
		return value;
	}
	
	public String getDisplayValue() {
		return valueFormatter(value);
	}
	
	public String valueFormatter(double value) {
		return String.format("%.1f", value);
	}
	
	public abstract double voltageToValue(double voltage);

	public abstract String getDisplayLabel();

	public abstract boolean isInRange();
}
