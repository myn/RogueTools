package com.logicvoid.voguetools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.util.Log;

/**
 * @author Derek Reynolds (myn)
 * 
 */
public class Device {
	private static final String TAG = "Device";

	// Enums
	public enum ModelShortName {
		Vogue, Kaiser, Polaris, Diamond, Raphael, Blackstone, Topaz, Rhodium, UNKNOWN
	}

	// Constructors
	public Device() {
		this.modelFullName = retrieveModelFullName();
		this.modelShortName = retrieveModelShortName();

	}

	// Private fields
	private String modelFullName = "";
	private ModelShortName modelShortName = ModelShortName.UNKNOWN;

	// Public methods
	public String getModelFullName() {
		return modelFullName;
	}

	public ModelShortName getModelShortName() {
		return modelShortName;
	}

	public Boolean IsDeviceCompatible() {

		Boolean returnValue = false;

		if (modelShortName == ModelShortName.Vogue) {
			returnValue = true;
		}
		else if(modelShortName == ModelShortName.Kaiser) {
			returnValue = true;
		}
		else if(modelShortName == ModelShortName.Polaris) {
			returnValue = true;
		}

		return returnValue;
	}

	/*
	 * @return The maximum clock threshold one should overclock to per device
	 */

	public int ClockSpeedMaxThreshold() {

		switch (modelShortName) {
		case Vogue:
			return 600;
		case Kaiser:
			return 600;
		case Polaris:
			return 600;
		case Diamond:
			return 600;
		case Raphael:
			// (makkonen) Some people have reported stability on raphael up to
			// 720, I think. I think mine would reboot within 30 seconds at 650,
			// though.
			return 750;
		case Blackstone:
			return 600;
		case Topaz:
			return 600;
		case Rhodium:
			return 600;
		default:
			return 0;

		}
	}

	// Private helper methods

	/*
	 * @return The model full name of the device or NULL if not found
	 */
	private String retrieveModelFullName() {

		String[] command = { "cat", "/proc/cpuinfo" };
		ProcessBuilder pb = new ProcessBuilder(command);

		BufferedReader br = null;
		String line;
		try {
			Process p = pb.start();
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			line = br.readLine();
			while (line != null) {
				if (line.startsWith("Hardware")) {
					return line.substring(line.indexOf("Hardware\t\t: ") + 12)
							.trim();

				}
				line = br.readLine();
			}
		} catch (IOException ex) {
			Log.e(TAG, "Unable to read /proc/cpuinfo", ex);
			return null;
		} finally {
			// cleanly close the BufferedReader InputStream
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					Log.e(TAG, "Exception while closing InputStream", e);
				}
			}

		}

		return line;
	}

	private ModelShortName retrieveModelShortName() {
		if (modelFullName.toLowerCase().contains("vogue")) {
			return ModelShortName.Vogue;
		} else if (modelFullName.toLowerCase().contains("kaiser")) {
			return ModelShortName.Kaiser;
		} else if (modelFullName.toLowerCase().contains("polaris")) {
			return ModelShortName.Kaiser;
		} else if (modelFullName.toLowerCase().contains("diamond")) {
			return ModelShortName.Diamond;
		} else if (modelFullName.toLowerCase().contains("raphael")) {
			return ModelShortName.Raphael;
		} else if (modelFullName.toLowerCase().contains("blackstone")) {
			return ModelShortName.Kaiser;
		} else if (modelFullName.toLowerCase().contains("topaz")) {
			return ModelShortName.Topaz;
		} else if (modelFullName.toLowerCase().contains("rhodium")) {
			return ModelShortName.Rhodium;
		} else {
			return ModelShortName.UNKNOWN;
		}
	}

}
