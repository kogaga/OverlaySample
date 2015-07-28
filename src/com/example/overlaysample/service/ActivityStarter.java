package com.example.overlaysample.service;

import com.example.overlaysample.MainActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ActivityStarter extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
}
