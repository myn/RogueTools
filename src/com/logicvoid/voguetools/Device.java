package com.logicvoid.voguetools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.graphics.SweepGradient;
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

		Boolean returnValue = true;

		if (modelShortName == ModelShortName.UNKNOWN) {
			returnValue = false;
		}

		return returnValue;
	}

	
	
	
	// Private helper methods
	
	
	/**
	 * Returns Device Model
	 * 
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
