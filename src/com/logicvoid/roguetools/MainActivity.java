package com.logicvoid.roguetools;

//import com.myn.tools.test.R;


import com.logicvoid.roguetools.R;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * @author Derek Reynolds (myn)
 * 
 */
public class MainActivity extends TabActivity {

	public static Activity me = null;

	private static final boolean DEBUG = Preferences.DEBUG;
	private static final String TAG = "MainActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Expose Intent
		// (http://groups.google.com/group/android-developers/browse_thread/thread/93ab36150bb290f2)
		me = this;

		// ========== Set up tabs ==========
		TabHost mTabHost = getTabHost();

		mTabHost.addTab(mTabHost.newTabSpec("tab_test1").setIndicator(
				"Overclocking").setContent(R.id.tabview1));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test2")
				.setIndicator("Density").setContent(R.id.tabview2));
		mTabHost.addTab(mTabHost.newTabSpec("tab_test3").setIndicator("About")
				.setContent(R.id.tabview3));

		mTabHost.setCurrentTab(0);

		/*
		 * Confirm Device Compatibility
		 */

		Device device = new Device();
		if (!device.IsDeviceCompatible()) {
			// This device is not Compatible with this program
			Alerts.DeviceNotCompatible(this);
			return;
		}

		if (DEBUG)
			Toast.makeText(getBaseContext(),
					"Current Device: " + device.getModelShortName().toString(),
					Toast.LENGTH_LONG).show();

		
		
		/*
		 * Display EULA
		 */
		Eula.show(this);

		
		
		/*
		 * OverClock Handling
		 */

		// Wire up Set Clock Speed button click events
		Button btnSetClockSpeed = (Button) findViewById(R.id.btnOverClock);
		btnSetClockSpeed.setOnClickListener(btnSetClockSpeed_OnClick);

		// Wire up Set On Boot checkbox onCheckedChange events
		CheckBox cbSetClockOnBoot = (CheckBox) findViewById(R.id.cbSetClockOnBoot);
		cbSetClockOnBoot
				.setOnCheckedChangeListener(cbSetClockOnBoot_onCheckedChangeListener);

		// Get current CPU Speed
		String CPUSpeed = OverClock.getClockSpeed();

		// Unable to retrieve retrieve CPU Speed via reading file
		if (CPUSpeed.length() == 0) {
			Toast.makeText(getBaseContext(),
					"Unable to determine clock frequency", Toast.LENGTH_LONG)
					.show();
			return;
		}

		// Set the clock freq display
		TextView tvClockSpeed = (TextView) findViewById(R.id.tvOverClockDisplay);
		tvClockSpeed.setText(CPUSpeed);

		// ============= OverClock SeekBar ===============
		SeekBar OverClockSeekBar = (SeekBar) findViewById(R.id.SeekBarOverClock);
		final TextView seekBarValue = (TextView) findViewById(R.id.tvOverClockDisplay);

		OverClockSeekBar
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						seekBarValue.setText(String.valueOf(progress) + " MHz");
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

		// set the max clockspeed threshold for the device
		OverClockSeekBar.setMax(device.ClockSpeedMaxThreshold());

		// initialize Over Clock SeekBar to current CPU clock speed
		OverClockSeekBar.setProgress(Integer.parseInt(CPUSpeed));

		// initialize Set On Boot checkbox to current setting
		cbSetClockOnBoot.setChecked(Preferences
				.getOverclockOnBootPref(getApplicationContext()));

		// Preferences test begin
		int prefCurrentCPUSpeed = Preferences.getClockSpeedPref(400,
				getApplicationContext());

		// String prefCurrentCPUSpeed =
		// Preferences.ReadFromFile("/data/data/com.logicvoid.roguetools/prefClockSpeed");
		if (DEBUG)
			Toast.makeText(getBaseContext(),
					"Current Pref: " + String.valueOf(prefCurrentCPUSpeed),
					Toast.LENGTH_LONG).show();
		// Preferences test end
		
		
		

		/*
		 * 
		 * LCD Density Handling
		 */
		// Wire up Set LCD Density button click events
		Button btnSetLCDDensity = (Button) findViewById(R.id.btnSetDensity);
		btnSetLCDDensity.setOnClickListener(btnSetLCDDensity_OnClick);

		// Get handle on LCD Density SeekBar and TextView
		SeekBar LCDDensitySeekBar = (SeekBar) findViewById(R.id.SeekBarLCDDensity);
		final TextView seekBarLCDDensityValue = (TextView) findViewById(R.id.tvLCDDensityDisplay);

		// Get current LCD Density
		int screenDensityDpi = ScreenDensity.getDensityDPI(getBaseContext());


		// LCD Density SeekBar Handling
		LCDDensitySeekBar
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						seekBarLCDDensityValue
								.setText(String.valueOf(progress) + " DPI");
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});


		// Initialize LCD Density Seekbar to current Density
		LCDDensitySeekBar.setProgress(screenDensityDpi);
		
	
	/*
	 * About Tab Handling
	 */
		    
	    
		// Display version
		TextView tvVersion = (TextView) findViewById(R.id.tvVersion);
		tvVersion.setText(Application.getAppVersionCode(getApplicationContext()));
		
		// Populate about middle content		
		TextView tvAboutMiddle = (TextView) findViewById(R.id.tvAboutMiddle);
		tvAboutMiddle.setText("Thanks so much for trying out " + getString(R.string.app_name) +"!\n\n" +
				"Author: Derek Reynolds\n" +
				"Contact: myn_2000@yahoo.com\n\n" +
				"Special Thanks to the xda-developers community\n\n");

		// wire up Paypal button with on click event		
		ImageButton btnPayPal = (ImageButton) findViewById(R.id.btnPaypal);
		btnPayPal.setOnClickListener(btnPayPal_OnClick);
		
	
	}  // end onCreate

	/*
	 * Create an anonymous class to act as a button click listener for
	 * "Set Clock Speed" button
	 */
	private OnClickListener btnSetClockSpeed_OnClick = new OnClickListener() {
		public void onClick(View v) {			

			try {
				// Get value of OverClock SeekBar
				SeekBar OverClockSeekBar = (SeekBar) findViewById(R.id.SeekBarOverClock);
				int freq = OverClockSeekBar.getProgress();

				if (OverClock.setClockSpeed(freq) == true) {
					
					Alerts.ClockSpeedSuccess(me, freq);
					

					// Save preferences
					// TODO: confirm preferences were set correctly by checking
					// for return value
					Preferences
							.setClockSpeedPref(freq, getApplicationContext());
					// Preferences.WriteToFile("/data/data/com.logicvoid.roguetools/prefClockSpeed",String.valueOf(freq));

				} else {					
					Toast.makeText(getBaseContext(), "Unable to change clock frequency", Toast.LENGTH_LONG)
					.show();
				}

			} catch (Exception e) {

				Toast.makeText(getBaseContext(), "Error: " + e.toString(), Toast.LENGTH_LONG)
				.show();				
			}

			

		}
	};

	/*
	 * OnClickListener: Create an anonymous class to act as a button click
	 * listener for "Set Density" button
	 */
	private OnClickListener btnSetLCDDensity_OnClick = new OnClickListener() {
		public void onClick(View v) {

			String actionText = "";

			try {

				// Get handle on LCD Density SeekBar
				SeekBar LCDDensitySeekBar = (SeekBar) findViewById(R.id.SeekBarLCDDensity);
				int screenDensityDpi = LCDDensitySeekBar.getProgress();

				if (ScreenDensity.setDensityDPI(screenDensityDpi)) {
					// Successfully set Density DPI
					Alerts.LCDDensitySuccess(me,screenDensityDpi);					
					actionText = "Changed Density DPI to "
							+ String.valueOf(screenDensityDpi);

				} else {
					// Unable to set Density DPI
					actionText = "Unable to change Density DPI";

				}

			} catch (Exception ex) {
				
				Toast.makeText(getBaseContext(), "Error: " + ex.toString(), Toast.LENGTH_LONG).show();
			}			

		}
	};

	/*
	 * Create an anonymous class to act as a checked change listener for
	 * "Set On Boot" checkbox
	 */
	private OnCheckedChangeListener cbSetClockOnBoot_onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {

			try {
				// Set the OverClock Boot preference based upon selection
				Preferences.setOverclockOnBootPref(isChecked,
						getApplicationContext());

				// Set the clock speed to whatever the current seekbar is
				// configured to
				try {
					// Get value of OverClock SeekBar
					SeekBar OverClockSeekBar = (SeekBar) findViewById(R.id.SeekBarOverClock);
					int freq = OverClockSeekBar.getProgress();

					if (OverClock.setClockSpeed(freq) == true) {

						// Save Clock Speed preferences
						Preferences.setClockSpeedPref(freq,
								getApplicationContext());

					} else {

						Toast.makeText(getBaseContext(),
								"Unable to change clock frequency",
								Toast.LENGTH_LONG).show();

					}
					// Catch setClockSpeed and setClockSpeedPref errors
				} catch (Exception ex) {

					Toast.makeText(getBaseContext(), "Error: " + ex.toString(),
							Toast.LENGTH_LONG).show();
					Log
							.e(
									TAG,
									"Exception while setting clock speed or boot preference",
									ex);

				}

				// Catch Preferences.setOverclockOnBootPref errors
			} catch (Exception ex) {

				Toast.makeText(getBaseContext(), "Error: " + ex.toString(),
						Toast.LENGTH_LONG).show();

				Log
						.e(
								TAG,
								"Exception while setting overclock on boot preferences",
								ex);
			}

		}
	};
	
    
    /*
	 * Create an anonymous class to act as a button click listener for
	 * "Paypal" button
	 */
	private OnClickListener btnPayPal_OnClick = new OnClickListener() {
		public void onClick(View v) {			

			try {
				
				String url = "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=DANEPPXLYXL4J";
				Intent i = new Intent(Intent.ACTION_VIEW);
				Uri u = Uri.parse(url);
				i.setData(u);
				startActivity(i);
				
					

			} catch (Exception e) {

				Toast.makeText(getBaseContext(), "Error: " + e.toString(), Toast.LENGTH_LONG)
				.show();				
			}

			

		}
	};
    

}