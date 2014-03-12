package com.js.gauges;

import android.content.Context;
import android.util.AttributeSet;

public class WaterTempGauge extends GaugeView {
	private static final double WATER_RSENSE = 46.5;	//senseR for 100-240F water thermistor
	
	public WaterTempGauge(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public String valueFormatter(double value) {
		return Long.toString(Math.round(value));
	}
	
	public String getDisplayLabel() {
		return "Water Temperature (F)";
	}
	public boolean isInRange() {
		int max = Integer.parseInt(getPreferences().getString("maxWaterTemperature", "210"));
    	return getValue() <= max;    	
	}
	
	public double voltageToValue(double voltage) {
		double resistance = 5 * WATER_RSENSE / (5 - voltage) -  (5 - voltage) / WATER_RSENSE - WATER_RSENSE;
    	return -71.89 * Math.log(resistance) + 491.79;
	}
}
