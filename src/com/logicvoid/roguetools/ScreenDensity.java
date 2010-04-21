package com.logicvoid.roguetools;

import java.io.DataOutputStream;
import java.io.IOException;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.content.Context;

/**
 * @author Derek Reynolds (myn)
 * 
 */
public class ScreenDensity {

	private static final String TAG = "ScreenDensity";

	public static int getDensityDPI(Context context) {

		int returnValue = 0;

		try {

			Display display = ((WindowManager) context
					.getSystemService(android.content.Context.WINDOW_SERVICE))
					.getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);

			int screenDensityDpi = metrics.densityDpi;

			returnValue = screenDensityDpi;

		} catch (Exception ex) {
			Log.e(TAG, "Exception in getDensityDPI", ex);
		}

		return returnValue;

	}

	
	/**
	 * @param density
	 *            : LCD Density you wish to set
	 * 
	 */
	public static Boolean setDensityDPI(int density) {

		// Elevating application security privileges in Android:
		// http://www.anddev.org/viewtopic.php?p=33450

		// setup value to return
		Boolean returnValue = false;

		try {
		

			Process p;
			try {
				p = Runtime.getRuntime().exec("su");
				
				DataOutputStream os = new DataOutputStream(p.getOutputStream());
				
				// density setting
				String strDensity = "ro.sf.lcd_density = " + String.valueOf(density);
			
				// mount system as read/write so we can access the /system/build.prop	
				os.writeBytes("mount -o remount,rw /system\n");					

				//os.writeBytes("echo \"text\"|cat - /system/blah.txt > /tmp/out && mv /tmp/out /system/blah.txt\n");	
				
				// Prepend new density setting at very top of build.prop because setting
				// is evaluated at first occurrence starting from top of file
				os.writeBytes("echo \"" + strDensity + "\"|cat - /system/build.prop > /tmp/out && mv /tmp/out /system/build.prop\n");
				
				
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
			Log.e(TAG, "Exception in setDensityDPI", ex);
		}

		return returnValue;
	}

}
