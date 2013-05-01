package by.bsu.courseproject.ui;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

public class Tools {

  public static final String LOGIN_PREFERENCES = "LOGIN_PREFERENCES";
  public static final String TIME = "TIME";

  public static void setTime(Context context) {
    SharedPreferences settings = context.getSharedPreferences(LOGIN_PREFERENCES, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putLong(TIME, (new Date(Calendar.getInstance().getTimeInMillis())).getTime());
    editor.commit();
  }

  public static boolean checkTimeLoggedOn(Context context) {
    SharedPreferences settings = context.getSharedPreferences(LOGIN_PREFERENCES, 0);
    if ((new Date(Calendar.getInstance().getTimeInMillis())).getTime() - settings.getLong(TIME, -1) > 30 * 60 * 1000) {
      SharedPreferences.Editor editor = settings.edit();
      editor.commit();
      return false;
    } else {
      return true;
    }
  }

  public static byte[] getCipher(String info, String salt) {

    String finalInfo = info.concat(salt);
    byte[] bInfo = null;
    try {
      bInfo = finalInfo.getBytes("UTF-8");
      MessageDigest messageDigest;
      try {
        messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.update(bInfo);
        return messageDigest.digest();

      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      }

    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    return null;
  }
}
