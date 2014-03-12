package com.js.gauges;

import android.content.Context;
import android.util.AttributeSet;

public class OilTempGauge extends GaugeView {
	private static final double OIL_RSENSE = 32.35;	//senseR for 100-320F temp thermistor
	
	public OilTempGauge(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public String getDisplayValue() {
    	return Long.toString(Math.round(getValue()));
	}
	
	public String getDisplayLabel() {
		return "Oil Temperature (F)";
	}
	public boolean isInRange() {
		int max = Integer.parseInt(getPreferences().getString("maxOilTemperature", "250"));
    	return getValue() <= max;
	}
	public double voltageToValue(double voltage) {
		double resistance = 5 * OIL_RSENSE / (5 - voltage) -  (5 - voltage) / OIL_RSENSE - OIL_RSENSE;    	
    	return -65.3233 * Math.log(resistance) + 498.2194;
	}
}
