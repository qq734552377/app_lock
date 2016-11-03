package app_lock.ucast.com.app_locking;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		// full screen  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);  

		if (!isServiceWork(this,"com.example.apklock.AppService")) {
			Intent intentStart=new Intent(this,AppService.class);
			intentStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startService(intentStart);
		}
		finish();//没有setContentView方法，并且在启动完成service之后。立即finish该activity，以防被用户发现

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!isServiceWork(this,"com.example.apklock.AppService")) {
			Intent intentStart=new Intent(this,AppService.class);
			intentStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startService(intentStart);
		}
	}

	/**
	 * 判断某个服务是否正在运行的方法
	 * 
	 * @param mContext
	 * @param serviceName
	 *            是包名+服务的类名（例如：com.example.apklock.AppService）
	 * @return true代表正在运行，false代表服务没有正在运行
	 */
	public static boolean isServiceWork(Context mContext, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) mContext
		.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> myList = myAM.getRunningServices(Integer.MAX_VALUE);
		if (myList.size() <= 0) {
			return false;
		}
		for (int i = 0; i < myList.size(); i++) {
			String mName = myList.get(i).service.getClassName().toString();
			if (mName.equals(serviceName)) {
				isWork = true;
				break;
			}
		}
		return isWork;
	}

}
