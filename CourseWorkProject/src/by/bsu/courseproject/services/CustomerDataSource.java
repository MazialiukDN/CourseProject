package by.bsu.courseproject.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import by.bsu.courseproject.model.Customer;

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
public class CustomerDataSource {

  private SQLiteDatabase db;
  private HashMap<String, String> customerProjectionMap = new HashMap<String, String>();

  {
    customerProjectionMap.put(Columns._ID, Columns._ID);
  }

  public CustomerDataSource(SQLiteDatabase db) {
    this.db = db;
  }

  private Customer cursorToCustomer(Cursor cursor) {
    Customer customer = new Customer();
    return customer;
  }

  public void persistCustomer(Customer customer) {
    ContentValues values = new ContentValues();
    long insertId = db.insert(Tables.PERSON, null,
                              values);
    customer.setId(insertId);
  }

  public Customer load(Long id) {
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
    queryBuilder.setProjectionMap(customerProjectionMap);
    queryBuilder.setTables(Tables.PERSON);
    queryBuilder.appendWhere(Columns._ID + " = " + id);
    Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
    cursor.moveToFirst();
    //TODO:add validation
    return cursorToCustomer(cursor);
  }

  public void updateCustomer(Customer customer) {
    ContentValues values = new ContentValues();
    long id = customer.getId();
    db.update(Tables.PERSON, values, Columns._ID + " = " + id, null);
  }

  public void deleteCustomer(Customer customer) {
    long id = customer.getId();
    System.out.println("Customer deleted with id: " + id);
    db.delete(Tables.PERSON, Columns._ID + " = " + id, null);
  }

  public List<Customer> getAllCustomers() {
    List<Customer> customers = new ArrayList<Customer>();

    Cursor cursor = db.query(Tables.PERSON,
                             customerProjectionMap.keySet().toArray(new String[0]), null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Customer customer = cursorToCustomer(cursor);
      customers.add(customer);
      cursor.moveToNext();
    }
    cursor.close();
    return customers;
  }
}
