package com.logicvoid.voguetools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class Application {

	/*
	 * Retrieve the versionCode from the applications Manifest
	 */
	
	public static String getAppVersionCode(Context context) {

		String returnValue = "";

		try {

			PackageManager pm = context.getPackageManager();

			PackageInfo pi = pm.getPackageInfo("com.logicvoid.voguetools", 0);

			returnValue = pi.versionName;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnValue;

	}

}
