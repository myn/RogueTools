package com.logicvoid.voguetools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;


public  class OnBootReceiver extends  BroadcastReceiver {

    

    @Override
    public void onReceive(Context context, Intent intent) {
         if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
              Log.d("VogueTools", "Got the Boot Event>>>");
              // do your stuff for example, start a background service directly
              // here              
              Toast.makeText(context, "DEBUG: VogueTools_BOOT_COMPLETED", Toast.LENGTH_LONG).show();

              
                                   
              
              String actionText = "";	
              
              
              // Set Clock Speed
              try {
  				
            	  // delay the setting of the clock speed so user has time to change it if it's too high ;)            	 
            	  //  Toast.makeText(context, "DEBUG: Started Sleep", Toast.LENGTH_LONG).show();
            	  //  SystemClock.sleep(60000);            	  
            	  //  Toast.makeText(context, "DEBUG: Ended Sleep", Toast.LENGTH_LONG).show();
            	  
            	  // get clockSpeed configuration preference  
            	  String prefClockSpeed =  Preferences.ReadFromFile("/data/data/com.logicvoid.voguetools/prefClockSpeed"); 
            	  

            	  if(prefClockSpeed == "")            	  
            	  {
                	  // if no preference is found, use current clock speed as default
            		  prefClockSpeed = OverClock.getClockSpeed();            		  
            	  }
            	  
            	  int freq = Integer.valueOf(prefClockSpeed);
  				
  				
  				if (OverClock.setClockSpeed(freq) == true) {
  					actionText = "VogueTools: Changed clock frequency to "
  							+ String.valueOf(freq) + " MHz";  				
  					
  				} else {
  					actionText = "VogueTools: Unable to change clock frequency";
  				}

  			} catch (Exception e) {

  				actionText = "VougeTools Error: " + e.toString();
  			}

              
  			Toast.makeText(context, actionText, Toast.LENGTH_LONG).show();		
              

              
             
              
         }
    }
}
