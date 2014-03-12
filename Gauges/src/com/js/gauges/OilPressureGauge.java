package com.js.gauges;

import android.content.Context;
import android.util.AttributeSet;

public class OilPressureGauge extends GaugeView {
	public OilPressureGauge(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public String valueFormatter(double value) {
		return Long.toString(Math.round(value));
	}
	
	public String getDisplayLabel() {
		return "Oil Pressure (PSI)";
	}
	
	public boolean isInRange() {
		 int min = Integer.parseInt(getPreferences().getString("minOilPressure", "15"));
	     return getValue() >= min;
	}
	
	public double voltageToValue(double voltage) {
		return voltage * 25 - 12.5;
	}
}
