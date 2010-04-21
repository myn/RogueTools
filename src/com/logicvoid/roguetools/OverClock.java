/**
 * 
 */
package com.logicvoid.roguetools;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

/**
 * @author Derek Reynolds (myn)
 * 
 */
public class OverClock {
	private static final String TAG = "OverClock";

	// Public methods

	/**
	 * @param freq
	 *            : Frequency you wish to set clock speed to
	 * 
	 */
	public static Boolean setClockSpeed(int freq) {

		// Elevating application security privileges in Android:
		// http://www.anddev.org/viewtopic.php?p=33450

		// setup value to return
		Boolean returnValue = false;

		try {
			// Determine what freqTable divider we should use
			String busSpeed = (freq > 400) ? "4" : "3";

			Process p;
			try {
				p = Runtime.getRuntime().exec("su");
				DataOutputStream os = new DataOutputStream(p.getOutputStream());
				// os.writeBytes("echo \"450\" > /sys/module/clock_7x00/parameters/a11\n");

				// Change frequency
				String strFreq = String.valueOf(freq);
				String outputFreq = "echo \"" + strFreq
						+ "\" > /sys/module/clock_7x00/parameters/a11\n";
				os.writeBytes(outputFreq);

				// Change bus speed
				os.writeBytes("echo \"" + busSpeed
						+ "\" > /sys/module/clock_7x00/parameters/ahb_div\n");

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
			Log.e(TAG, "Exception in setClockSpeed", ex);
		}

		return returnValue;
	}

	public static String getClockSpeed() {
		// setup value to return
		String returnValue = "";

		try {

			// Get current CPU Speed trimming whitespace
			String CPUSpeed = ReadFile("/sys/module/clock_7x00/parameters/a11")
					.trim();

			// Unable to retrieve retrieve CPU Speed via reading file
			if (CPUSpeed.length() == 0) {
				returnValue = "";
			} else {
				returnValue = CPUSpeed;
			}

		} catch (Exception ex) {
			Log.e(TAG, "Exception in getClockSpeed", ex);

		}

		return returnValue;

	}

	// Private methods
	/**
	 * @param filePath
	 *            : Full path to file to read
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
			Log.e(TAG, "Exception in ReadFile", ex);

		}
		return result;
	}	

}
