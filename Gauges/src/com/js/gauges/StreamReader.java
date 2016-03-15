package com.js.gauges;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

public class StreamReader extends Thread {
	public static final String MESSAGE = "com.js.gauges.MESSAGE";
	public static final char END_FLAG = '}';
	private final int STREAM_WAIT_INTERVAL = 100;
	
	private final BluetoothSocket socket;
	private final InputStream inputStream;
	private final Handler mHandler;	
	private StringBuffer forwardBuffer = new StringBuffer();
	 
    public StreamReader(BluetoothSocket socket, Handler mHandler) {
        this.socket = socket;
        InputStream tmpIn = null;
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) { }
 
        inputStream = tmpIn;
        this.mHandler = mHandler;
    }
        
    public void run() {
        byte[] buffer = new byte[512];
        int bytes;
       
        while (!this.isInterrupted()) {
            try {
                bytes = inputStream.read(buffer);
                String msg = new String(buffer, 0, (bytes != -1) ? bytes : 0 );                
                forwardData(msg);
            } catch (IOException e) {
            	e.printStackTrace();
            }  
        }
    }
    
    private void forwardData(String data){
		char c;
		for (int i=0;i<data.length();i++){
			c = data.charAt(i);
			if (c == END_FLAG) {
				if (forwardBuffer.toString().startsWith("{"))
				{
					int[] parsedData = parseData(forwardBuffer.toString().substring(1,forwardBuffer.toString().length()));
					mHandler.obtainMessage(MainActivity.MESSAGE_WRITE, -1, -1, parsedData).sendToTarget();
				}				
								
				forwardBuffer = new StringBuffer();
			}
			else {
				forwardBuffer.append(c);
			}
		}
	}
    
    private int[] parseData(String data)
    {
    	String[] values = data.split(Pattern.quote("|"));
    	int[] results = new int[0];
    	
    	try
    	{
        	if (values.length > 0)
        	{
        		results = new int[values.length];
        		for(int i = 0; i < values.length; i++)
        		{
        			results[i] = Integer.parseInt(values[i]);
        		}
        	}
    	}
    	catch(NumberFormatException e){
    		System.out.println("wtf");
    	}
    	
    	return results;    	
    }
    
    public void cancel() {
        try {
        	this.interrupt();
            socket.close();
            inputStream.close();            
        } catch (IOException e) { }
    }
	 
}
