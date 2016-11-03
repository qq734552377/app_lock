package app_lock.ucast.com.app_locking;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ucast.applock_service.R;

import app_lock.ucast.com.app_locking.tools.ExceptionApplication;
import app_lock.ucast.com.app_locking.tools.SavePasswd;

public class LoginActivity extends Activity implements OnClickListener{
	  private Button bt_login;
	    private Button bt_changPassward;
	    private Button bt_recoveryPassward;
	    private EditText et_passward;
	    private TishiInputDialog tishiDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setFinishOnTouchOutside(false);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
		
		Settings.System.putInt(getContentResolver(),Settings.System.ACCELEROMETER_ROTATION, 0);
//		//得到是否开启
		int flag = Settings.System.getInt(getContentResolver(),
					Settings.System.ACCELEROMETER_ROTATION, 0);
//		showToast(flag+"");
		Log.e(TAG, "onCreate ");
		getMetaData();
	}
	
	
	
	

    public void init(){
        bt_login=(Button)findViewById(R.id.bt_login);
        bt_changPassward=(Button)findViewById(R.id.bt_changPassward);
        bt_recoveryPassward=(Button)findViewById(R.id.bt_recoveryPassward);
        et_passward=(EditText)findViewById(R.id.et_passward);
        tishiDialog=new TishiInputDialog(this);

        bt_login.setOnClickListener(this);
        bt_changPassward.setOnClickListener(this);
        bt_recoveryPassward.setOnClickListener(this);

    }
	
    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
    	 switch (v.getId()){
         case R.id.bt_login:
             String et_passwd=et_passward.getText().toString().trim();
             String md_et_passwd= SavePasswd.getInstace().getMD5(et_passwd);
             if (SavePasswd.getInstace().get("新密码").equals(md_et_passwd)){
                 doWork();
             }else {
                 showToast("密码错误，请重新输入");
             }
             break;

         case R.id.bt_changPassward:
             Intent intent2=new Intent(LoginActivity.this,PasswdActivity.class);
             intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
             startActivity(intent2);
             break;

         case R.id.bt_recoveryPassward:
             tishiDialog.show();
             final EditText tishi_et= (EditText) tishiDialog.findViewById(R.id.tishi_et);
             
             final String str=SavePasswd.getInstace().get("原始密码");

             tishiDialog.findViewById(R.id.bt1).setOnClickListener(new OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     tishiDialog.cancel();
                 }
             });
             tishiDialog.findViewById(R.id.bt2).setOnClickListener(new OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     String etStr=tishi_et.getText().toString().trim();
                     final String md_etStr=SavePasswd.getInstace().getMD5(etStr);
                     if (md_etStr.equals(str)) {
                         SavePasswd.getInstace().save("新密码",SavePasswd.OLDPWD);
                         showToast("已恢复为初始密码");
                         tishiDialog.cancel();
                     }else {
                         showToast("密码有误,请重新输入!");
                     }
                 }
             });

             break;

     }
	}
    
    public void showToast(String str){
        Toast.makeText(ExceptionApplication.getInstance(),str,Toast.LENGTH_SHORT).show();
    }
    
    
	
	
	//使用泛型，让T继承自View，并且$方法返回的是T类型
	//这样做的好处是，使用findViewById不用向下转型了
	public <T extends View> T $(int id) {
		return (T) findViewById(id);
	}
	
	 
	
	
	//这里重写onBackPressed方法，由于是用Intent.FLAG_ACTIVITY_NEW_TASK这种方式启动的activity，
	//所以如果这里实现默认的super.onBackPressed()的话，很快就会被用户发现
	@Override
	public void onBackPressed() {
		long oldTime=SavePasswd.oldTime;
//		if(System.currentTimeMillis()-oldTime > 5000){
//			showToast("请双击退出");
//		}
		SavePasswd.oldTime=System.currentTimeMillis();
		LoginActivity.this.finish();
		return;
	}


	
	public void doWork(){
		AppService.setIsSecond(true);
		Log.e("LoginActivity", "isSecond="+AppService.isSecond);
		AppService.oldTime=System.currentTimeMillis();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startApp("com.android.settings","com.android.settings.Settings");
		LoginActivity.this.finish();
		
	}
	
	public void startApp(String packageName,String activity){
		Intent intent = new Intent();
		// 组件名称，第一个参数是包名，也是主配置文件Manifest里设置好的包名 第二个是类名，要带上包名
		intent.setComponent(new ComponentName(packageName, activity));
		intent.setAction(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Log.i("time", "setAction start " + System.currentTimeMillis() + "");
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		
		super.onPause();
	}



	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private static final String TAG = "LoginActivity";



	private void getMetaData() {
		try {
			ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(),
					PackageManager.GET_META_DATA);
			if (ai == null || ai.metaData == null) return;
			int mTopLevelHeaderId = ai.metaData.getInt(META_DATA_KEY_HEADER_ID);
			String mFragmentClass = ai.metaData.getString(META_DATA_KEY_FRAGMENT_CLASS);

			Log.e(TAG, "111111 :"+mTopLevelHeaderId+"     "+mFragmentClass);

			// Check if it has a parent specified and create a Header object
			final int parentHeaderTitleRes = ai.metaData.getInt(META_DATA_KEY_PARENT_TITLE);
			String parentFragmentClass = ai.metaData.getString(META_DATA_KEY_PARENT_FRAGMENT_CLASS);

			Log.e(TAG, "222222 :"+parentHeaderTitleRes+"---"+parentFragmentClass);

		} catch (Exception nnfe) {
			// No recovery

			Log.e(TAG, "getMetaData Exception");
		}
	}
	private static final String META_DATA_KEY_HEADER_ID =
			"com.android.settings.TOP_LEVEL_HEADER_ID";
	private static final String META_DATA_KEY_FRAGMENT_CLASS =
			"com.android.settings.FRAGMENT_CLASS";
	private static final String META_DATA_KEY_PARENT_TITLE =
			"com.android.settings.PARENT_FRAGMENT_TITLE";
	private static final String META_DATA_KEY_PARENT_FRAGMENT_CLASS =
			"com.android.settings.PARENT_FRAGMENT_CLASS";
	
}
