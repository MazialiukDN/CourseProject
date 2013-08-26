package by.bsu.courseproject.datasources;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import by.bsu.courseproject.model.Customer;
import by.bsu.courseproject.model.Employee;
import by.bsu.courseproject.model.Investor;
import by.bsu.courseproject.model.Person;
import by.bsu.courseproject.util.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static by.bsu.courseproject.db.DBConstants.Columns;
import static by.bsu.courseproject.db.DBConstants.Tables;

/**
 * User: Artyom Strok
 * Date: 24.03.13
 * Time: 12:40
 */
public class PersonDataSource {
  private static final String EMPLOYEE_DISCRIMINATOR = "E";
  private static final String CUSTOMER_DISCRIMINATOR = "C";
  private static final String INVESTOR_DISCRIMINATOR = "I";
  private final SQLiteDatabase db;
  private final HashMap<String, String> personProjectionMap = new HashMap<String, String>();

  {
    personProjectionMap.put(Columns._ID, Columns._ID);
    personProjectionMap.put(Columns.PERSON_DISCRIMINATOR, Columns.PERSON_DISCRIMINATOR);
    personProjectionMap.put(Columns.PERSON_FIRSTNAME, Columns.PERSON_FIRSTNAME);
    personProjectionMap.put(Columns.PERSON_MIDDLENAME, Columns.PERSON_MIDDLENAME);
    personProjectionMap.put(Columns.PERSON_LASTNAME, Columns.PERSON_LASTNAME);
    personProjectionMap.put(Columns.PERSON_EMAIL, Columns.PERSON_EMAIL);
    personProjectionMap.put(Columns.PERSON_SKYPE, Columns.PERSON_SKYPE);
    personProjectionMap.put(Columns.PERSON_PHONE, Columns.PERSON_PHONE);
    personProjectionMap.put(Columns.PERSON_BIRTHDAY, Columns.PERSON_BIRTHDAY);
    personProjectionMap.put(Columns.PERSON_INFO, Columns.PERSON_INFO);
    personProjectionMap.put(Columns.CUSTOMER_INVESTOR_COMPANY, Columns.CUSTOMER_INVESTOR_COMPANY);
    personProjectionMap.put(Columns.EMPLOYEE_EXPERIENCE, Columns.EMPLOYEE_EXPERIENCE);
    personProjectionMap.put(Columns.EMPLOYEE_EDUCATION, Columns.EMPLOYEE_EDUCATION);
  }

  public PersonDataSource(SQLiteDatabase db) {
    this.db = db;
  }

  private Person cursorToPerson(Cursor cursor) {
    Person person;
    String personDiscriminator = cursor.getString(cursor.getColumnIndex(Columns.PERSON_DISCRIMINATOR));
    if (personDiscriminator.equals(EMPLOYEE_DISCRIMINATOR)) {
      person = new Employee();
      ((Employee) person).setEducation(cursor.getString(cursor.getColumnIndex(Columns.EMPLOYEE_EDUCATION)));
      ((Employee) person).setExperience(cursor.getString(cursor.getColumnIndex(Columns.EMPLOYEE_EXPERIENCE)));
    } else if (personDiscriminator.equals(INVESTOR_DISCRIMINATOR)) {
      person = new Investor();
      ((Investor) person).setCompany(cursor.getString(cursor.getColumnIndex(Columns.CUSTOMER_INVESTOR_COMPANY)));
    } else {
      person = new Customer();
      ((Customer) person).setCompany(cursor.getString(cursor.getColumnIndex(Columns.CUSTOMER_INVESTOR_COMPANY)));
    }
    person.setId(cursor.getLong(cursor.getColumnIndex(Columns._ID)));
    person.setDiscriminator(personDiscriminator);
    person.setFirstName(cursor.getString(cursor.getColumnIndex(Columns.PERSON_FIRSTNAME)));
    person.setLastName(cursor.getString(cursor.getColumnIndex(Columns.PERSON_LASTNAME)));
    person.setMiddleName(cursor.getString(cursor.getColumnIndex(Columns.PERSON_MIDDLENAME)));
    person.setEmail(cursor.getString(cursor.getColumnIndex(Columns.PERSON_EMAIL)));
    person.setSkype(cursor.getString(cursor.getColumnIndex(Columns.PERSON_SKYPE)));
    person.setPhone(cursor.getString(cursor.getColumnIndex(Columns.PERSON_PHONE)));
    String date = cursor.getString(cursor.getColumnIndex(Columns.PERSON_BIRTHDAY));
    if (date != null){
      person.setBirthdate(DateUtil.stringToDate(date));
    }
    person.setInfo(cursor.getString(cursor.getColumnIndex(Columns.PERSON_INFO)));
    return person;
  }

  public void persistPerson(Person person) {
    ContentValues values = new ContentValues();
    long insertId = db.insert(Tables.PERSON, null,
                              values);
    person.setId(insertId);
  }

  public Person load(Long id) {
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
    queryBuilder.setProjectionMap(personProjectionMap);
    queryBuilder.setTables(Tables.PERSON);
    queryBuilder.appendWhere(Columns._ID + " = " + id);
    Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
    cursor.moveToFirst();
    //TODO:add validation
    return cursorToPerson(cursor);
  }

  public void updatePerson(Person person) {
    ContentValues values = new ContentValues();
    long id = person.getId();
    db.update(Tables.PERSON, values, Columns._ID + " = " + id, null);
  }

  public void deletePerson(Person person) {
    long id = person.getId();
    System.out.println("Person deleted with id: " + id);
    db.delete(Tables.PERSON, Columns._ID + " = " + id, null);
  }

  private List<? extends Person> getAllPersons(String personDiscriminator) {
    List<Person> persons = new ArrayList<Person>();
    final String where = Columns.PERSON_DISCRIMINATOR + " = \"" + personDiscriminator + "\"";
    Set<String> projection = personProjectionMap.keySet();
    Cursor cursor = db.query(Tables.PERSON,
                             projection.toArray(new String[projection.size()]), where, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Person person = cursorToPerson(cursor);
      persons.add(person);
      cursor.moveToNext();
    }
    cursor.close();
    return persons;
  }

  public List<Employee> getAllEmpoyees() {
    return (List<Employee>) getAllPersons(EMPLOYEE_DISCRIMINATOR);
  }


  public List<Customer> getAllCustomers() {
    return (List<Customer>) getAllPersons(CUSTOMER_DISCRIMINATOR);
  }


  public List<Investor> getAllInvestors() {
    return (List<Investor>) getAllPersons(INVESTOR_DISCRIMINATOR);
  }
}
