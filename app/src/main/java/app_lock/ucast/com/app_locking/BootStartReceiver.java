package app_lock.ucast.com.app_locking;


import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class BootStartReceiver extends BroadcastReceiver {
	public static final String action_boot = "android.intent.action.BOOT_COMPLETED";
	
	
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(action_boot)) {
			//开机自动启动这个service
			Intent intentStart=new Intent(context,AppService.class);
			context.startService(intentStart);
		} 
		
		
	}

}
