package app_lock.ucast.com.app_locking.tools;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/6/12.
 */
public class SavePasswd {
	
	public static long oldTime=0;
	public final static String  OLDPWD="YL230220";

    private static SavePasswd savePasswd=new SavePasswd();
    private SavePasswd(){
        save("原始密码",OLDPWD);
        if(get("新密码").equals("ucast_test")){
            save("新密码",OLDPWD);
        }
    };
    public static SavePasswd getInstace(){
        return savePasswd;
    }

    public void save (String name ,String passwd){
        SharedPreferences sp=ExceptionApplication
                .getInstance().getSharedPreferences("passwd", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        
        String md_Pwd=getMD5(passwd);
        
        editor.putString(name,md_Pwd);
        editor.commit();
    }
    public String get(String name){
        SharedPreferences sp=ExceptionApplication
                .getInstance().getSharedPreferences("passwd", Context.MODE_PRIVATE);

        return sp.getString(name,"ucast_test");
    }
    
    public String getMD5(String info)
    {
      try
      {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(info.getBytes("UTF-8"));
        byte[] encryption = md5.digest();
          
        StringBuffer strBuf = new StringBuffer();
        for (int i = 0; i < encryption.length; i++)
        {
          if (Integer.toHexString(0xff & encryption[i]).length() == 1)
          {
            strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
          }
          else
          {
            strBuf.append(Integer.toHexString(0xff & encryption[i]));
          }
        }
          
        return strBuf.toString();
      }
      catch (NoSuchAlgorithmException e)
      {
        return "";
      }
      catch (UnsupportedEncodingException e)
      {
        return "";
      }
    }
    
}
