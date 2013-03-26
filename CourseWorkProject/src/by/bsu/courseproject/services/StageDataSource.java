package by.bsu.courseproject.services;

import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

/**
 * User: Artyom Strok
 * Date: 24.03.13
 * Time: 12:38
 */
public class StageDataSource {

  private SQLiteDatabase db;
  private HashMap<String, String> stageProjectionMap = new HashMap<String, String>();

  public StageDataSource(SQLiteDatabase db) {
    this.db = db;
  }


}
