package com.js.gauges;

import java.io.IOException;
import java.util.UUID;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

//arudino inputs
//0 - Oil Pressure
//1 - Oil Temperature
//2 - Water Temperature
//3 - Voltage

public class MainActivity extends Activity {
	public static final String MESSAGE = "com.js.gauges.MESSAGE";
	public static final int MESSAGE_WRITE = 1;
	private static final int RESULT_SETTINGS = 1;
	private static final String DEVICE_ADDRESS =  "00:13:12:06:50:21";	
	
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothDevice arduino;
	private BluetoothSocket socket;
	private StreamReader reader;
	
	//Parent Layout
	private View contentView;
	
	//Gauges
	private OilPressureGauge oilPressure;
	private OilTempGauge oilTemp;
	private WaterTempGauge waterTemp;
	private VoltageGauge voltage;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);  
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        contentView = (View) findViewById(R.id.contentView);
        oilPressure = (OilPressureGauge) findViewById(R.id.oilPressureGauge);
        oilTemp = (OilTempGauge) findViewById(R.id.oilTempGauge);
        waterTemp = (WaterTempGauge) findViewById(R.id.waterTempGauge);
        voltage = (VoltageGauge) findViewById(R.id.voltageGauge);

        this.setupActionBar();
        this.showLoading();        
        new InitConnect().execute();
        
        
    }
    
    private class InitConnect extends AsyncTask<Void, Void, Boolean> {
    	 @Override
         protected Boolean doInBackground(Void... params) {
    		 try {
    	        	bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    	            
    	            if (bluetoothAdapter != null) {
    	            	if (!bluetoothAdapter.isEnabled()) {
    	                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
    	                    startActivityForResult(enableBtIntent, 1);
    	                }
    	            	arduino = bluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS);
    	            	socket = arduino.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
    	            	
    	            	IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
    	            	registerReceiver(mReceiver, filter1);
    	            	
    	            	connectTillSuccess();   	
    	            }
    	        }
    	        catch(Exception e) {
    	        	Log.w("Connect", "InitConnect error", e);
    	        	return false;
    	        }
    		 	return true;
         }
    }
    
    private void setupActionBar() {		
    	contentView.setOnClickListener(new View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				if (getActionBar().isShowing()) {
					getActionBar().hide();
				} else {
					getActionBar().show();
				}
				
			}
		});
    }
    
    private void showLoading() {
    	oilPressure.showNoData();
        oilTemp.showNoData();
        waterTemp.showNoData();
        voltage.showNoData();
    }

    private void connectTillSuccess() {
    	showLoading();
    	while(!connectToArduino()) {
    		try {
				Thread.sleep(100);
			} catch (InterruptedException e) {}
    	}
    }
        
    private boolean connectToArduino() {
    	
    	try {
        	//bluetoothAdapter.cancelDiscovery();
        	socket.connect();        	
        	reader = new StreamReader(socket, mHandler);
        	reader.start();
    	}
    	catch (IOException e) {
    		try {
                socket.close();
            } catch (IOException closeException) { }
    		return false;
    	}
    	
    	return true;
    }    
    
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_WRITE:
            	int[] values = (int[]) msg.obj;   
            	
            	if (values != null && values.length >= 3)
            	{
                    oilPressure.setVoltage(getVoltageFromArduinoScale(values[0]));
                    oilTemp.setVoltage(getVoltageFromArduinoScale(values[1]));
                    waterTemp.setVoltage(getVoltageFromArduinoScale(values[2]));                    
                    voltage.setVoltage(getVoltageFromArduinoScale(values[3]));
            	}
            	
                break;
            }
        }
    };
    
    private double getVoltageFromArduinoScale(int input)
    {
    	return input * (5.0 / 1023.0);
    }
    
  //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();           
            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
            	connectTillSuccess();
            }           
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
 
        case R.id.action_settings:
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, RESULT_SETTINGS);
            break; 
        }
 
        return true;
    }
    
}
