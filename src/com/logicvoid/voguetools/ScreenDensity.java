package com.logicvoid.voguetools;

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

	public static int getDensityDPI(Context context) {

		int returnValue = Integer.MIN_VALUE;

		try {

			Display display = ((WindowManager) context
					.getSystemService(android.content.Context.WINDOW_SERVICE))
					.getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);

			int screenDensityDpi = metrics.densityDpi;

			returnValue = screenDensityDpi;

		} catch (Exception ex) {
			Log.d("getDensityDPI", ex.toString());
		}

		return returnValue;

	}

	public static Boolean setDensityDPI(int density) {

		Boolean returnValue = false;

		try {

			// TODO add set density code here
			
		} catch (Exception ex) {
			Log.d("setDensityDPI", ex.toString());
		}

		return returnValue;
	}
}