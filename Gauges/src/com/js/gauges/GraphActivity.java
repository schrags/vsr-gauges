package com.js.gauges;

import java.util.HashMap;

import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;

public class GraphActivity extends Activity {
	
	private GraphViewSeries oilPressureSeries;
	private GraphViewSeries oilTempSeries;
	private GraphViewSeries waterTempSeries;
	private double counter = 3.0;
	private long refreshRate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph_view);
		refreshRate = Long.parseLong(PreferenceManager.getDefaultSharedPreferences(this).getString("graphRefreshRate", "500"));
		IntentFilter filter1 = new IntentFilter(MainActivity.GAUGE_VALUE_MESSAGE);
    	registerReceiver(mReceiver, filter1);

    	oilPressureSeries = new GraphViewSeries(new GraphViewData[] {});
    	oilPressureSeries.getStyle().color = Color.BLACK;
  
    	oilTempSeries = new GraphViewSeries(new GraphViewData[] {});
    	oilTempSeries.getStyle().color = Color.BLACK;
    	
    	waterTempSeries = new GraphViewSeries(new GraphViewData[] {});
    	waterTempSeries.getStyle().color = Color.BLACK;

    	LineGraphView oilPressureGraph = new LineGraphView(this, "Oil Pressure");
		oilPressureGraph.addSeries(oilPressureSeries);
		oilPressureGraph.setViewPort(1, 50);
		oilPressureGraph.setManualYAxisBounds(100, 0);
		oilPressureGraph.getGraphViewStyle().setNumVerticalLabels(5);
		oilPressureGraph.setScalable(true);
		oilPressureGraph.setHorizontalLabels(new String[]{});
		oilPressureGraph.setDrawBackground(true);
		oilPressureGraph.setBackgroundColor(Color.GRAY);
//		oilPressureGraph.getGraphViewStyle().setGridColor(Color.BLACK);
//		oilPressureGraph.getGraphViewStyle().setHorizontalLabelsColor(Color.WHITE);
//		oilPressureGraph.getGraphViewStyle().setVerticalLabelsColor(Color.WHITE);
		LinearLayout layout = (LinearLayout) findViewById(R.id.oilPressureGraph);
//		layout.setBackgroundColor(Color.BLACK);
		layout.addView(oilPressureGraph);
		
		
		
		LineGraphView oilTempGraph = new LineGraphView(this, "Oil Temperature");
		oilTempGraph.addSeries(oilTempSeries);
		oilTempGraph.setViewPort(1, 50);
		oilTempGraph.setManualYAxisBounds(280, 100);
		oilTempGraph.getGraphViewStyle().setNumVerticalLabels(5);
		oilTempGraph.setScalable(true);
		oilTempGraph.setHorizontalLabels(new String[]{});		
		oilTempGraph.setDrawBackground(true);
		oilTempGraph.setBackgroundColor(Color.GRAY);
		LinearLayout oilTempLayout = (LinearLayout) findViewById(R.id.oilTempGraph);
		oilTempLayout.addView(oilTempGraph);
		
		LineGraphView waterTempGraph = new LineGraphView(this, "Water Temperature");
		waterTempGraph.addSeries(waterTempSeries);
		waterTempGraph.setViewPort(1, 50);
		waterTempGraph.setManualYAxisBounds(240, 100);
		waterTempGraph.getGraphViewStyle().setNumVerticalLabels(5);
		waterTempGraph.setScalable(true);
		waterTempGraph.setHorizontalLabels(new String[]{});
		waterTempGraph.setDrawBackground(true);
		waterTempGraph.setBackgroundColor(Color.GRAY);
		LinearLayout waterTempLayout = (LinearLayout) findViewById(R.id.waterTempGraph);
		waterTempLayout.addView(waterTempGraph);
	}

	public class GraphViewData implements GraphViewDataInterface {
		private double x, y;

		public GraphViewData(double x, double y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public double getX() {
			return this.x;
		}

		@Override
		public double getY() {
			return this.y;
		}
	}

	private long lastRefresh = System.currentTimeMillis();
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			long current = System.currentTimeMillis();
			if (current - lastRefresh >= refreshRate)
			{	
				HashMap<String, Double> data = (HashMap<String, Double>) intent.getExtras().get("data");

				GraphViewData oilPressure = new GraphViewData(counter, data.get("oilPressure"));
				oilPressureSeries.appendData(oilPressure, true, 100);
				
				GraphViewData oilTemp = new GraphViewData(counter, data.get("oilTemp"));
				oilTempSeries.appendData(oilTemp, true, 100);
				
				GraphViewData waterTemp = new GraphViewData(counter, data.get("waterTemp"));
				waterTempSeries.appendData(waterTemp, true, 100);
				
				counter++;
				lastRefresh = current;
			}
		}
	};
	
	@Override
	protected void onPause() {
	    super.onPause();

	    unregisterReceiver(mReceiver);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		IntentFilter filter1 = new IntentFilter(MainActivity.GAUGE_VALUE_MESSAGE);
    	registerReceiver(mReceiver, filter1);
		
	}
	
}
