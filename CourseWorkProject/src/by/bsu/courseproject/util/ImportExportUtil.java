package by.bsu.courseproject.util;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class ImportExportUtil {
  public final static String NULL_VALUE = "NULL";

  public static byte[] getData(String str) {
    str = str == null ? NULL_VALUE : str;
    return str.getBytes();
  }/* Checks if external storage is available for read and write */

  public static boolean isExternalStorageWritable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state)) {
      return true;
    }
    return false;
  }/* Checks if external storage is available to at least read */

  public static boolean isExternalStorageReadable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state) ||
        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
      return true;
    }
    return false;
  }

  public static File getStorageDir() {
    // Get the directory for the user's public pictures directory.
    File file = new File(Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOWNLOADS), "Portfolio");
    if (!file.mkdirs()) {
      Log.e("debug", "Directory not created");
    }
    return file;
  }
}