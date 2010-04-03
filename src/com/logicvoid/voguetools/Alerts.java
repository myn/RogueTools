package com.logicvoid.voguetools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class Alerts {

	/*
	 * YesNoDialog not implemented but keeping as great 
	 * example for future use
	 */
	public static void YesNoDialog(final Activity activity) {

		AlertDialog.Builder alt_bld = new AlertDialog.Builder(activity);
		alt_bld.setMessage("Do you want to close this window ?").setCancelable(
				false).setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						// Action for 'Yes' Button

					}
				}).setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// Action for 'NO' Button

						activity.finish();
						// dialog.cancel();
					}
				});

		AlertDialog alert = alt_bld.create();
		// Title for AlertDialog
		alert.setTitle("Title");
		// Icon for AlertDialog
		alert.setIcon(R.drawable.icon);
		alert.show();

	}

	public static void DeviceNotCompatible(final Activity activity) {

		AlertDialog.Builder alt_bld = new AlertDialog.Builder(activity);
		alt_bld
				.setMessage(
						"Unfortunately this application is not compatible with your device.")
				.setCancelable(false).setPositiveButton("Close",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id) {
								// Action for 'Close' Button
								// Close application
								activity.finish();

							}
						});
		AlertDialog alert = alt_bld.create();
		// Title for AlertDialog
		alert.setTitle("Device not compatible");
		// Icon for AlertDialog
		alert.setIcon(R.drawable.icon);
		alert.show();

	}
	
	


	public static void LCDDensitySuccess(final Activity activity, int density) {

		AlertDialog.Builder alt_bld = new AlertDialog.Builder(activity);
		alt_bld
				.setMessage(
						"Your Density DPI has been changed to: " + String.valueOf(density) + "\n\nPlease reboot for this change to take effect." )
				.setCancelable(false).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id) {
								// Action for 'OK' Button
								// Close application
								
								
								

							}
						});
		AlertDialog alert = alt_bld.create();
		// Title for AlertDialog
		alert.setTitle("Density DPI Changed");
		// Icon for AlertDialog
		alert.setIcon(R.drawable.icon);
		alert.show();

	}



	public static void ClockSpeedSuccess(final Activity activity, int freq) {

		AlertDialog.Builder alt_bld = new AlertDialog.Builder(activity);
		alt_bld
				.setMessage(
						"Your clock speed has been changed to: " + String.valueOf(freq) + "MHz\n\nThis change will take effect upon your phone sleeping." )
				.setCancelable(false).setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog, int id) {
								// Action for 'OK' Button
								// Close application
								
							}
						});
		AlertDialog alert = alt_bld.create();
		// Title for AlertDialog
		alert.setTitle("Clock Speed Changed");
		// Icon for AlertDialog
		alert.setIcon(R.drawable.icon);
		alert.show();

	}
	

}
