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
 * @author Derek Reynolds
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
		
		// Expose Intent (http://groups.google.com/group/android-developers/browse_thread/thread/93ab36150bb290f2)
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
		
		
		
		// Wire up Set Clock Speed button click events
		Button btnSetClockSpeed = (Button) findViewById(R.id.btnOverClock);
		btnSetClockSpeed.setOnClickListener(btnSetClockSpeed_OnClick);
		
		
		// Get current CPU Speed 
		String CPUSpeed = OverClock.getClockSpeed();

		// Unable to retrieve retrieve CPU Speed via reading file
		if (CPUSpeed.length() == 0) {
			Toast.makeText(getBaseContext(), "Unable to determine clock frequency", Toast.LENGTH_LONG).show();
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
		//int prefCurrentCPUSpeed = Preferences.getClockSpeedPref(400, getBaseContext());		
		
		String prefCurrentCPUSpeed = Preferences.ReadFromFile("/data/data/com.logicvoid.voguetools/prefClockSpeed");	
		Toast.makeText(getBaseContext(), "Current Pref: " + prefCurrentCPUSpeed, Toast.LENGTH_LONG).show();	
		
		// Preferences test end
		
			
		
	}
	
	// Create an anonymous class to act as a button click listener for "Set Clock Speed" button
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
					// TODO: confirm preferences were set correctly by checking for return value
					//Preferences.setClockSpeedPref(freq, context);							
					Preferences.WriteToFile("/data/data/com.logicvoid.voguetools/prefClockSpeed", String.valueOf(freq));
					
				} else {
					actionText = "Unable to change clock frequency";
				}

			} catch (Exception e) {

				actionText = "Error: " + e.toString();
			}

			Toast.makeText(getBaseContext(), actionText, Toast.LENGTH_LONG).show();				
			
			
		}
      };
	
	
	
	
	
}