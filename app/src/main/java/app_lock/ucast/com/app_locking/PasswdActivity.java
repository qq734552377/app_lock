package app_lock.ucast.com.app_locking;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ucast.applock_service.R;

import app_lock.ucast.com.app_locking.tools.SavePasswd;


public class PasswdActivity extends Activity implements View.OnClickListener{

    private EditText et1;
    private EditText et2;
    private EditText et3;
    private Button bt1;
    private Button bt2;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	setFinishOnTouchOutside(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwd);
        
        init();
    }

    private void init(){
        et1=(EditText)findViewById(R.id.passwd_et_old);
        et2=(EditText)findViewById(R.id.passwd_et_new1);
        et3=(EditText)findViewById(R.id.passwd_et_new2);
        bt1=(Button)findViewById(R.id.passwd_bt1);
        bt2=(Button)findViewById(R.id.passwd_bt2);
        
        bt1.setOnClickListener(this);
        bt2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.passwd_bt1:
                PasswdActivity.this.finish();
                break;
            
            case R.id.passwd_bt2:
                String old=et1.getText().toString().trim();
                String mdOld= SavePasswd.getInstace().getMD5(old);
                String new1=et2.getText().toString().trim();
                String new2=et3.getText().toString().trim();

                if (SavePasswd.getInstace().get("新密码").equals(mdOld)){
                    if (new1.equals(new2)){
                        if (new1.length()>=8){
                            SavePasswd.getInstace().save("新密码",new1);
                            PasswdActivity.this.finish();
                        }else{
                            showToast("新密码最少为八位，请重新输入");
                        }
                    }else{
                        et2.setText("");
                        et3.setText("");
                        showToast("两次输入的新密码不一致，请重新输入");
                    }
                }else {
                    et1.setText("");
                    showToast("原始密码输入错误，请重新输入");
                }
                break;
        }
    }

    public void showToast(String str){
        Toast.makeText(PasswdActivity.this,str,Toast.LENGTH_SHORT).show();
    }
}
