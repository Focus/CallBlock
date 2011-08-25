package com.focus.callblock;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class CallService extends Service{

	private CallReceiver call;
	private int NOTIFICATION_ID = 123;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onStart(Intent intent, int startid){
		super.onStart(intent, startid);
		call = new CallReceiver();
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor edit = pref.edit();
		edit.putBoolean("service", true);
		edit.commit();
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon, "CallBlock", System.currentTimeMillis());
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, CallBlockActivity.class), android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
		notification.setLatestEventInfo(this, "CallBlock", "CallBlock is active.", pendingIntent);
		notificationManager.notify(NOTIFICATION_ID, notification);
	}
	
	@Override
	public void onDestroy(){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor edit = pref.edit();
		edit.putBoolean("service", false);
		edit.commit();
		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(NOTIFICATION_ID);
		super.onDestroy();
	}

}
