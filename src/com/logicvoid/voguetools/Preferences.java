package com.logicvoid.voguetools;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;





/**
 * @author Derek Reynolds (myn)
 * 
 */
public class Preferences {
	

	private static final String PREFS_NAME = "VogueTools";
	
	public static final Boolean DEBUG = false;  // flag to enable/disable debugging

	public static Boolean setClockSpeedPref(int freq, Context appContext) {

		// setup value to return
		Boolean returnValue = false;
		
		

		try {			
			
			// Save user preferences. We need an Editor object to
			// make changes. All objects are from android.context.Context
			
			SharedPreferences settings = appContext.getSharedPreferences(PREFS_NAME, 2 ); // 2 = MODE_WORLD_WRITEABLE
			SharedPreferences.Editor editor = settings.edit();
			editor.putInt("clockSpeedPref", freq);

			// commit edits
			editor.commit();

			// if successful, lets report that all went well
			returnValue = true;

		} catch (Exception ex) {
			Log.d("setClockSpeedPref", ex.toString());
		}

		return returnValue;

	}

	public static int getClockSpeedPref(int defaultclockspeed, Context appContext) {

		// setup value to return		
		int returnValue = defaultclockspeed;
		
		 

		try {
			
			SharedPreferences settings = appContext.getSharedPreferences( PREFS_NAME, 2);	// 2 = MODE_WORLD_WRITEABLE
			
			
			int clockSpeed = settings.getInt("clockSpeedPref", 	defaultclockspeed);

			return clockSpeed;

		} catch (Exception ex) {
			Log.d("getClockSpeedPref", ex.toString());
		}

		return returnValue;

	}

	
	
	/*
	 *  An alternative to setClockSpeedPref / getClockSpeedPref
	 *  
	 *  Elevated application security model file reading/writing
	 *  
	 *  WriteToFile()
	 *  ReadFromFile()
	 *  
	 */
	
	/**
	 * @param pathToFile : Full path to file to read (/sys/module/clock_7x00/parameters/a11)
	 * @param dataToWrite : The string data you wish to be (over)written in file
	 */
	
	public static Boolean WriteToFile(String pathToFile, String dataToWrite) {

		// Elevating application security privileges in Android:
		// http://www.anddev.org/viewtopic.php?p=33450

		// setup value to return
		Boolean returnValue = false;

		try {
			
			Process p;
			try {
				p = Runtime.getRuntime().exec("su");
				DataOutputStream os = new DataOutputStream(p.getOutputStream());
				// os.writeBytes("echo \"450\" > /sys/module/clock_7x00/parameters/a11\n");
		
				
				String outputFreq = "echo \"" + dataToWrite
						+ "\" > "+ pathToFile+"\n";
				os.writeBytes(outputFreq);

				

				os.writeBytes("exit\n");
				os.flush();

				try {
					p.waitFor();
					if (p.exitValue() != 255) {
						// success
						returnValue = true;
					} else {
						// error
						returnValue = false;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception ex) {
			Log.d("WriteToFile", ex.toString());
		}

		return returnValue;
	}
	
	
	/**
	 * @param pathToFile : Full path to file to read (/sys/module/clock_7x00/parameters/a11)
	 */
	public static String ReadFromFile(String pathToFile)
	{
		// setup value to return
		String returnValue = "";
	
		try {
			
			// Get current data trimming whitespace
			String dataRead = ReadFile(pathToFile).trim();
			
			// Unable to retrieve retrieve data via reading file
			if (dataRead.length() == 0) {				
				returnValue = "";
			}
			else
			{
				returnValue = dataRead;
			}
			
		} catch (Exception ex) {
			Log.d("ReadFromFile", ex.toString());
			returnValue = "";
		}
		
		return returnValue;		
	
	}

	
	// Private methods
	/**
	 * @param pathToFile : Full path to file to read (/sys/module/clock_7x00/parameters/a11)
	 */
		private static String ReadFile(String filePath) {
		ProcessBuilder cmd;
		String result = "";

		try {

			String[] args = { "cat", filePath };

			cmd = new ProcessBuilder(args);

			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[1024];
			while (in.read(re) != -1) {
				System.out.println(new String(re));
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			Log.d("ReadFile", ex.toString());

		}
		return result;
	}





}
