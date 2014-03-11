package com.js.gauges;

import android.content.Context;
import android.util.AttributeSet;

public class OilPressureGauge extends GaugeView {
	private long pressure;
	public OilPressureGauge(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public String getDisplayValue() {
    	return Long.toString(pressure);
	}
	
	public String getDisplayLabel() {
		return "Oil Pressure (PSI)";
	}
	public boolean isInRange() {
		 int min = Integer.parseInt(getPreferences().getString("minOilPressure", "15"));
	     return pressure >= min;
	}
	public void setVoltage(double voltage) {
		double pressureValue = voltage * 25 - 12.5;
    	this.pressure = Math.round(pressureValue);
    	this.refreshDisplay();
	}
}
