package com.focus.callblock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class CallReceiver extends BroadcastReceiver{
	public PhoneListener listen = new PhoneListener();
	@Override
	public void onReceive(Context cont, Intent in) {
		TelephonyManager telephony = (TelephonyManager) cont.getSystemService(Context.TELEPHONY_SERVICE);
		listen.context = cont;
		telephony.listen(listen, PhoneStateListener.LISTEN_CALL_STATE);
	}
}
