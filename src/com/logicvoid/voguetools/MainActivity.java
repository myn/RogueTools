package com.logicvoid.voguetools;

//import com.myn.tools.test.R;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Derek Reynolds (myn)
 * 
 */
public class MainActivity extends TabActivity {
	public static Activity me = null;

	Context context = this;

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
		 * 
		 * OverClock Handling
		 */

		// Wire up Set Clock Speed button click events
		Button btnSetClockSpeed = (Button) findViewById(R.id.btnOverClock);
		btnSetClockSpeed.setOnClickListener(btnSetClockSpeed_OnClick);

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
						seekBarValue.setText(String.valueOf(progress));
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

		// initialize Over Clock SeekBar to current CPU clock speed
		OverClockSeekBar.setProgress(Integer.parseInt(CPUSpeed));

		// Preferences test begin
		// int prefCurrentCPUSpeed = Preferences.getClockSpeedPref(400, getBaseContext());


		String prefCurrentCPUSpeed = Preferences
				.ReadFromFile("/data/data/com.logicvoid.voguetools/prefClockSpeed");
		Toast.makeText(getBaseContext(),
				"Current Pref: " + prefCurrentCPUSpeed, Toast.LENGTH_LONG)
				.show();
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
		
		// Initialize LCD Density Seekbar to current Density
		LCDDensitySeekBar.setProgress(screenDensityDpi);		

		// Display initialized value of LCD Density SeekBar
		seekBarLCDDensityValue.setText(String.valueOf(screenDensityDpi));

		// LCD Density SeekBar Handling
		LCDDensitySeekBar
				.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						seekBarLCDDensityValue
								.setText(String.valueOf(progress));
					}

					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

	}

	/*
	 * Create an anonymous class to act as a button click listener for
	 * "Set Clock Speed" button
	 */
	private OnClickListener btnSetClockSpeed_OnClick = new OnClickListener() {
		public void onClick(View v) {
			String actionText = "";

			try {
				// Get value of OverClock SeekBar
				SeekBar OverClockSeekBar = (SeekBar) findViewById(R.id.SeekBarOverClock);
				int freq = OverClockSeekBar.getProgress();

				if (OverClock.setClockSpeed(freq) == true) {
					actionText = "Changed clock frequency to "
							+ String.valueOf(freq) + " MHz";

					// Save preferences
					// TODO: confirm preferences were set correctly by checking
					// for return value
					// Preferences.setClockSpeedPref(freq, context);
					Preferences
							.WriteToFile(
									"/data/data/com.logicvoid.voguetools/prefClockSpeed",
									String.valueOf(freq));

				} else {
					actionText = "Unable to change clock frequency";
				}

			} catch (Exception e) {

				actionText = "Error: " + e.toString();
			}

			Toast.makeText(getBaseContext(), actionText, Toast.LENGTH_LONG)
					.show();

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
				
				if(ScreenDensity.setDensityDPI(screenDensityDpi))
				{
					// Successfully set Density DPI
					actionText = "Changed Density DPI to "
						+ String.valueOf(screenDensityDpi);
				
				}
				else
				{
					// We unable to set Density DPI
					actionText = "Unable to change Density DPI";
				
				}
				
			} catch (Exception ex) {
				actionText = "Error: " + ex.toString();
			}
			
			Toast.makeText(getBaseContext(), actionText, Toast.LENGTH_LONG).show();
			
			
			
		}
	};

}