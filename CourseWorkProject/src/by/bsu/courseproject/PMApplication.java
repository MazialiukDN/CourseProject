package by.bsu.courseproject;

import android.app.Application;
import by.bsu.courseproject.db.PMDB;

/**
 * User: Artyom Strok
 * Date: 25.03.13
 * Time: 16:32
 */
public class PMApplication extends Application{

  private static PMDB pmdb;

  @Override
  public void onCreate() {
    pmdb = new PMDB(this);
    super.onCreate();
  }

  public static PMDB getPMDB(){
    return pmdb;
  }

}
