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

	@Override
	public void onReceive(Context context, Intent intent) {
		if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
			Log.d("VogueTools", "Got the Boot Event>>>");
			// Do your stuff for example, start a background service directly
			// here
			Toast.makeText(context, "DEBUG: VogueTools_BOOT_COMPLETED",
					Toast.LENGTH_LONG).show();			

			String actionText = "";

			// Set Clock Speed
			try {

				/*
				 * TODO: Delay writing of Clock Speed from preferences on start
				 * to allow users enough time to adjust clock speed if it was
				 * set to high
				 */

				// get clockSpeed configuration preference
				Log.d("VogueTools", "Getting prefClockSpeed preferences");
				String prefClockSpeed = Preferences
						.ReadFromFile("/data/data/com.logicvoid.voguetools/prefClockSpeed");

				Log.d("VogueTools", "Determining if prefClockSpeed  is empty");			
				if (prefClockSpeed == "") {
					// if no preference is found, use current clock speed as
					// default
				
					Log.d("VogueTools", "No prefClockSpeed found. use current clock speed as default");
					Toast.makeText(context, "DEBUG: No prefClockSpeed found. use current clock speed as default",
							Toast.LENGTH_LONG).show();
					prefClockSpeed = OverClock.getClockSpeed();
				}
				
				Log.d("VogueTools", "Casting prefClockSpeed to int");
				int freq = Integer.valueOf(prefClockSpeed);

				Log.d("VogueTools", "setClockSpeed(" + String.valueOf(freq) +")");				
				if (OverClock.setClockSpeed(freq) == true) {
					Log.d("VogueTools", "Changed clock frequency");	
					
					actionText = "VogueTools: Changed clock frequency to "
							+ String.valueOf(freq) + " MHz";

				} else {
					Log.d("VogueTools", "Unable to change clock frequency");	
					actionText = "VogueTools: Unable to change clock frequency";
				}

			} catch (Exception e) {
				Log.d("VogueTools", "VougeTools Error: " + e.toString());
				actionText = "VougeTools Error: " + e.toString();
			}
			Log.d("VogueTools", "Finished with OnBootReceiver");
			Toast.makeText(context, actionText, Toast.LENGTH_LONG).show();

		}
	}

}
