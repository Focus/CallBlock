package com.focus.callblock;

import java.util.ArrayList;

import android.content.Context;
import android.media.AudioManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class PhoneListener extends PhoneStateListener{
	private boolean intercept = false;
	private int mode;
	
	public Context context;
	public void onCallStateChanged(int state, String number){
		switch(state){
		case TelephonyManager.CALL_STATE_IDLE:
			if(intercept)
				switchSilent();
			break;
		case TelephonyManager.CALL_STATE_RINGING:
			DBHandle db = new DBHandle(context);
			ArrayList<String> nums = db.getActiveFilters();
			
			for(int i = 0; i < nums.size(); i++){
				Log.v("PhoneListener", nums.get(i));
				if( compareNumbers(number, nums.get(i)) && !intercept){
					switchSilent();	
					break;
				}
			}
			
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			break;
		}
	}
	
	public void switchSilent(){
		AudioManager aud = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		if(!intercept){
			mode = aud.getRingerMode();
			aud.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			intercept = true;
		}
		else{
			aud.setRingerMode(mode);
			intercept = false;
		}
	}
	
	public boolean compareNumbers(String in, String numbers){
		if(numbers.length() <= 0)
			return true;
		if( numbers.startsWith("*") && numbers.endsWith("*") ){
			if(in.indexOf( numbers.substring(1, numbers.length()-1) ) == -1)
				return false;
			else
				return true;
		}
		else if( numbers.startsWith("*") ){
			if(in.indexOf( numbers.substring(1) ) == -1)
				return false;
			else
				return true;
		}
		else if( numbers.endsWith("*") ){
			if(in.indexOf( numbers.substring(0, numbers.length()-1) ) == -1)
				return false;
			else
				return true;
		}
		return in.equals(numbers);
	}
}