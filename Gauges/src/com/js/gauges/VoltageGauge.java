package com.js.gauges;

import android.content.Context;
import android.util.AttributeSet;

public class VoltageGauge extends GaugeView {

	private double voltage;

	public VoltageGauge(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public String getDisplayValue() {		
		return String.format("%.2f", voltage);
	}

	public String getDisplayLabel() {
		return "Voltage (V)";
	}

	public boolean isInRange() {
		double min = Double.parseDouble(getPreferences().getString("minVoltage","12.5"));
		return voltage >= min;
	}

	public void setVoltage(double voltage) {
		this.voltage = voltage * 4.1545;

		this.refreshDisplay();
	}
}
