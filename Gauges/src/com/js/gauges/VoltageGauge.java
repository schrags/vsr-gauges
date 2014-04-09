package com.js.gauges;

import android.content.Context;
import android.util.AttributeSet;

public class VoltageGauge extends GaugeView {

	public VoltageGauge(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public String getDisplayLabel() {
		return "Voltage (V)";
	}

	public boolean isInRange() {
		double min = Double.parseDouble(getPreferences().getString("minVoltage","13.2"));
		return getValue() >= min;
	}
	
	public double voltageToValue(double voltage) {
		return voltage * 4.1545; 
	}
}
