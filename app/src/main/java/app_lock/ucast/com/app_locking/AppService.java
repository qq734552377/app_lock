package app_lock.ucast.com.app_locking;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

public class AppService extends Service {
	
	private static final String TAG="AppService";
	
	public static boolean isSecond;
	
	public static long oldTime=-200000;
	

	
	public static synchronized boolean  getIsSecond(){
		return isSecond;
	}
	public static synchronized void  setIsSecond(boolean result){
		isSecond=result;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
        return START_STICKY;  //用于保证service被kill后重新创建
    }  
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		/**
		 * 当service运行在低内存的环境时，将会kill掉一些存在的进程。因此进程的优先级将会很重要，
		 * 可以使用startForeground API将service放到前台状态。这样在低内存时被kill的几率更低
		 */
		startForeground(0, new Notification()); 
		Timer timer = new Timer();
		
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				ActivityManager mActivityManager;
				mActivityManager = (ActivityManager) AppService.this.getSystemService(Context.ACTIVITY_SERVICE);
				ComponentName topActivity = mActivityManager.getRunningTasks(1).get(0).topActivity;
				String packageName = topActivity.getPackageName();
				String acName=topActivity.getClassName();
				
				
				if (isSecond) {
					if (!packageName.equals("com.android.settings")){
						if ((System.currentTimeMillis()-oldTime)/1000 < 20) {
//							Log.e(TAG, "档掉了");
							return;
						}
						isSecond=false;
					}
					oldTime=System.currentTimeMillis();
					return;
				}
				if (packageName.equals("com.android.settings") && !getIsSecond()&&("com.android.settings.Settings").equals(acName)) {
					Log.e(TAG, "2222 "+acName);
					killApp(mActivityManager, "com.android.settings");
					mActivityManager.killBackgroundProcesses("com.android.settings");
//					Log.e(TAG, "开始到密码界面");
					Intent intent = new Intent();
					intent.setClass(AppService.this, LoginActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
				}
			}
		};
	
		timer.schedule(task, 0, 10);//从现在开始，每隔100毫秒运行一次run方法

	}
	
	@Override
	public void onDestroy() {
		stopForeground(true);
		Intent localIntent = new Intent();  
		localIntent.setClass(this, AppService.class); 
		this.startService(localIntent);    //销毁时重新启动Service
	}

	public void killApp(ActivityManager mActivityManager,String packageName){
//		ActivityManager mActivityManager;
		try{
//		mActivityManager = (ActivityManager) AppService.this.getSystemService(Context.ACTIVITY_SERVICE);
		Method forceStopPackage = mActivityManager.getClass().getDeclaredMethod("forceStopPackage", String.class);  
		forceStopPackage.setAccessible(true);  
		forceStopPackage.invoke(mActivityManager, packageName);  
		}catch (Exception e){
			
		}
	}




}
