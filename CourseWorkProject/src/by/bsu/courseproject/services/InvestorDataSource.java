package by.bsu.courseproject.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import by.bsu.courseproject.model.Inverstor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static by.bsu.courseproject.db.DBConstants.Columns;
import static by.bsu.courseproject.db.DBConstants.Tables;

/**
 * User: Artyom Strok
 * Date: 24.03.13
 * Time: 12:40
 */
public class InvestorDataSource {

  private SQLiteDatabase db;
  private HashMap<String, String> inverstorProjectionMap = new HashMap<String, String>();

  public InvestorDataSource(SQLiteDatabase db) {
    this.db = db;
  }

  private Inverstor cursorToInverstor(Cursor cursor) {
    Inverstor inverstor = new Inverstor();
    return inverstor;
  }

  public void persistInverstor(Inverstor inverstor) {
    ContentValues values = new ContentValues();
    long insertId = db.insert(Tables.PERSON, null,
                              values);
    inverstor.setId(insertId);
  }

  public Inverstor load(Long id) {
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
    queryBuilder.setProjectionMap(inverstorProjectionMap);
    queryBuilder.setTables(Tables.PERSON);
    queryBuilder.appendWhere(Columns._ID + " = " + id);
    Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
    cursor.moveToFirst();
    //TODO:add validation
    return cursorToInverstor(cursor);
  }

  public void updateInverstor(Inverstor inverstor) {
    ContentValues values = new ContentValues();
    long id = inverstor.getId();
    db.update(Tables.PERSON, values, Columns._ID + " = " + id, null);
  }

  public void deleteInverstor(Inverstor inverstor) {
    long id = inverstor.getId();
    System.out.println("Inverstor deleted with id: " + id);
    db.delete(Tables.PERSON, Columns._ID + " = " + id, null);
  }

  public List<Inverstor> getAllInverstors() {
    List<Inverstor> inverstors = new ArrayList<Inverstor>();

    Cursor cursor = db.query(Tables.PERSON,
                             inverstorProjectionMap.keySet().toArray(new String[0]), null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Inverstor inverstor = cursorToInverstor(cursor);
      inverstors.add(inverstor);
      cursor.moveToNext();
    }
    cursor.close();
    return inverstors;
  }
}
