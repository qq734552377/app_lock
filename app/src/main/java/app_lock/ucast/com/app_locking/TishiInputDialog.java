package app_lock.ucast.com.app_locking;

import com.ucast.applock_service.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;


/**
 * Created by Administrator on 2016/6/14.
 */
public class TishiInputDialog extends Dialog {
    public TishiInputDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("恢复原始密码");
        setContentView(R.layout.tishi_input_dilog);
    }
}
