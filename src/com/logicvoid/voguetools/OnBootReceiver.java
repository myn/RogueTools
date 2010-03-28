package com.logicvoid.voguetools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Derek Reynolds (myn)
 * 
 */

public class OnBootReceiver extends BroadcastReceiver {
	private static final String TAG = "OnBootReceiver";

	private static final boolean DEBUG = Preferences.DEBUG;

	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			if (DEBUG)
				Log.d("VogueTools", "Got the Boot Event>>>");
			// Do your stuff for example, start a background service directly
			// here
			if (DEBUG)
				Toast.makeText(context, "DEBUG: VogueTools_BOOT_COMPLETED",
						Toast.LENGTH_LONG).show();

			// If the user has configured to set Clock Speed on boot
			if (Preferences.getOverclockOnBootPref(context)) {
				String actionText = "";

				// Set Clock Speed
				try {

					/*
					 * TODO: Delay writing of Clock Speed from preferences on
					 * start to allow users enough time to adjust clock speed if
					 * it was set to high
					 * 
					 * Use a service: xma's post on Sun Jan 31, 2010 11:50 PM
					 * http
					 * ://www.anddev.org/creating_a_background_application-t10676
					 * .html
					 */

					// String prefClockSpeed =
					// Preferences.ReadFromFile("/data/data/com.logicvoid.voguetools/prefClockSpeed");
					// get current clockSpeed
					if (DEBUG)
						Log.d("VogueTools", "Getting current clockspeed");
					String CurrentClockSpeed = OverClock.getClockSpeed();

					// get clockSpeed configuration preference using current
					// clock speed as default if no preferences have been set
					if (DEBUG)
						Log.d("VogueTools",
								"Getting prefClockSpeed preferences");
					int prefClockSpeed = Preferences.getClockSpeedPref(Integer
							.valueOf(CurrentClockSpeed), context);

					if (DEBUG)
						Log.d("VogueTools", "Casting prefClockSpeed to int");
					int freq = Integer.valueOf(prefClockSpeed);

					if (DEBUG)
						Log.d("VogueTools", "setClockSpeed("
								+ String.valueOf(freq) + ")");
					if (OverClock.setClockSpeed(freq) == true) {
						if (DEBUG)
							Log.d("VogueTools", "Changed clock frequency");

						actionText = "VogueTools: Changed clock freq to "
								+ String.valueOf(freq) + " MHz";

					} else {
						if (DEBUG)
							Log.d("VogueTools",
									"Unable to change clock frequency");
						actionText = "VogueTools: Unable to change clock frequency";
					}

				} catch (Exception e) {
					Log.e(TAG, "Exception in OnBootReceiver" ,e);
					actionText = "VougeTools Error: " + e.toString();
				}
				Toast.makeText(context, actionText, Toast.LENGTH_LONG).show();
				if (DEBUG)
					Log.d("VogueTools", "Finished with OnBootReceiver");

			}

		}
	}

}
