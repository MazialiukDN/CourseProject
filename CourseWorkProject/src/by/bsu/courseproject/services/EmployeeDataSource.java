package by.bsu.courseproject.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import by.bsu.courseproject.model.Employee;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static by.bsu.courseproject.db.DBConstants.Columns;
import static by.bsu.courseproject.db.DBConstants.Tables;


/**
 * User: Artyom Strok
 * Date: 24.03.13
 * Time: 12:39
 */
public class EmployeeDataSource {

  private SQLiteDatabase db;
  private HashMap<String, String> employeeProjectionMap = new HashMap<String, String>();

  public EmployeeDataSource(SQLiteDatabase db) {
    this.db = db;
  }

  private Employee cursorToEmployee(Cursor cursor) {
    Employee employee = new Employee();
    return employee;
  }

  public void persistEmployee(Employee employee) {
    ContentValues values = new ContentValues();
    long insertId = db.insert(Tables.PERSON, null,
                              values);
    employee.setId(insertId);
  }

  public Employee load(Long id) {
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
    queryBuilder.setProjectionMap(employeeProjectionMap);
    queryBuilder.setTables(Tables.PERSON);
    queryBuilder.appendWhere(Columns._ID + " = " + id);
    Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
    cursor.moveToFirst();
    //TODO:add validation
    return cursorToEmployee(cursor);
  }

  public void updateEmployee(Employee employee) {
    ContentValues values = new ContentValues();
    long id = employee.getId();
    db.update(Tables.PERSON, values, Columns._ID + " = " + id, null);
  }

  public void deleteEmployee(Employee employee) {
    long id = employee.getId();
    System.out.println("Employee deleted with id: " + id);
    db.delete(Tables.PERSON, Columns._ID + " = " + id, null);
  }

  public List<Employee> getAllEmployees() {
    List<Employee> employees = new ArrayList<Employee>();

    Cursor cursor = db.query(Tables.PERSON,
                             employeeProjectionMap.keySet().toArray(new String[0]), null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Employee employee = cursorToEmployee(cursor);
      employees.add(employee);
      cursor.moveToNext();
    }
    cursor.close();
    return employees;
  }

}
