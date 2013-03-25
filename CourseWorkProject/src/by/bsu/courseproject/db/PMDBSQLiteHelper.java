package by.bsu.courseproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static by.bsu.courseproject.db.DBConstants.Columns;
import static by.bsu.courseproject.db.DBConstants.Tables;

/**
 * User: Artyom Strok
 * Date: 23.03.13
 * Time: 11:54
 */
public class PMDBSQLiteHelper extends SQLiteOpenHelper {
  private static final String DATABASE_NAME = "project_manager.db";
  private static final int DATABASE_VERSION = 5;


  private final String[] sqlDump = {

      "INSERT INTO \"person\" VALUES(1,'E',  'Соколов',   'Михаил',   'Юрьевич',       'sokolov@gmail.com', 'mishail.sokolov', '(8-029) 234-89-23','23-01-1991','', '',  '','Инженер-программист');",
      "INSERT INTO \"person\" VALUES(2,'E',  'Антоневич', 'Дмитрий',  'Александрович', 'dmitry@yandex.ru',  'misha.ivanov',    '(8-025) 988-45-34','25-01-1991','', '',  '','Инженер-программист');",
      "INSERT INTO \"person\" VALUES(3,'E',  'Лямцев',    'Сергей',   'Андреевич',     'misha@yandex.ru',   'misha.ivanov',    '(8-025) 453-45-34','26-01-1991','', '',  '','Инженер-программист');",
      "INSERT INTO \"person\" VALUES(4,'E',  'Климчук',   'Александр','Иванович',      'misha@yandex.ru',   'misha.ivanov',    '(8-044) 534-43-23','24-01-1991','', '',  '','Инженер-программист');",
      "INSERT INTO \"person\" VALUES(5,'E',  'Новиков',   'Алексей',  'Николаевич',    'misha@yandex.ru',   'misha.ivanov',    '(8-029) 432-54-45','24-01-1991','', '',  '','Инженер-программист');",
      "INSERT INTO \"person\" VALUES(6,'E',  'Борисов',   'Николай',  'Алексеевич',    'misha@yandex.ru',   'misha.ivanov',    '(8-025) 234-43-44','22-01-1991','', '',  '','Инженер-программист');",
      "INSERT INTO \"person\" VALUES(7,'E',  'Троцкий',   'Николай',  'Юрьевич',       'misha@yandex.ru',   'misha.ivanov',    '(8-029) 454-62-55','20-01-1991','', '',  '','Инженер-программист');",
      "INSERT INTO \"person\" VALUES(8,'E',  'Поляков',   'Михаил',   'Борисович',     'misha@yandex.ru',   'misha.ivanov',    '(8-029) 948-50-05','24-01-1991','', '',  '','Инженер-программист');",
      "INSERT INTO \"person\" VALUES(9,'C', 'Соколов',   'Михаил',   'Юрьевич',       'sokolov@gmail.com', 'mishail.sokolov', '(8-029) 234-89-23','23-01-1991','','IBA','','');",
      "INSERT INTO \"person\" VALUES(10,'I', 'Новиков',   'Дмитрий',  'Николаевич',    'sokolov@gmail.com', 'mishail.sokolov', '(8-044) 678-89-23','23-01-1991','','IBA','','');",
      "INSERT INTO \"person\" VALUES(11,'C', 'Троцкий',   'Сергей',   'Иванович',      'sokolov@gmail.com', 'mishail.sokolov', '(8-029) 676-80-23','23-01-1991','','IBA','','');",
      "INSERT INTO \"person\" VALUES(12,'I', 'Поляков',   'Дмитрий',  'Александрович', 'sokolov@gmail.com', 'mishail.sokolov', '(8-029) 435-54-23','23-01-1991','','IBA','','');",
      "INSERT INTO \"person\" VALUES(13,'C','Климчук',   'Николай',  'Борисович',     'sokolov@gmail.com', 'mishail.sokolov', '(8-025) 234-45-23','23-01-1991','','IBA','','');",
      "INSERT INTO \"person\" VALUES(14,'I', 'Лямцев',    'Александр','Андреевич',     'sokolov@gmail.com', 'mishail.sokolov', '(8-029) 342-12-32','23-01-1991','','IBA','','');",

      "INSERT INTO \"project\" VALUES(1,'Новый мир','20120522','Строительство', 'Предлагаемый', 5, 1, 4)",
      "INSERT INTO \"project\" VALUES(2,'БГУ','20120622','Образование', 'Предлагаемый', 4, 3, 5)",
      "INSERT INTO \"project\" VALUES(3,'Юридеческая контора','20120506','Консультационные услуги', 'Предлагаемый', 3, 3, 6);",
      "INSERT INTO \"project\" VALUES(4,'Минсктранс','20120423','Транспорт', 'Предлагаемый', 4, 1, 4)",
      "INSERT INTO \"project\" VALUES(5,'Надёжный вклад','20120711','Финансы', 'Предлагаемый', 1, 1, 5)"

  };

  public PMDBSQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {

    sqLiteDatabase.execSQL("CREATE TABLE " + Tables.PROJECT + "("
                           + Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                           + Columns.PROJECT_PROJECTNAME + " TEXT NOT NULL,"
                           + Columns.PROJECT_PROJECTDUEDATE + " TEXT,"
                           + Columns.PROJECT_CATEGORY + " TEXT,"
                           + Columns.PROJECT_STATUS + " TEXT,"
                           + Columns.PROJECT_PRIORITY + " INTEGER,"
                           + Columns.PROJECT_CUSTOMER_ID + " INTEGER,"
                           + Columns.PROJECT_INVESTOR_ID + ");");

    sqLiteDatabase.execSQL("CREATE TABLE " + Tables.AUTHORIZATION + " ("
                           + Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                           + Columns.LOGIN + " BLOB,"
                           + Columns.SALT + " TEXT,"
                           + Columns.PASSWORD + " BLOB" + ");");

    sqLiteDatabase.execSQL("CREATE TABLE " + Tables.PERSON + " ("
                           + Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                           + Columns.PERSON_DISCRIMINATOR + "TEXT,"
                           + Columns.PERSON_LASTNAME + " TEXT,"
                           + Columns.PERSON_FIRSTNAME + " TEXT,"
                           + Columns.PERSON_MIDDLENAME + " TEXT,"
                           + Columns.PERSON_EMAIL + " TEXT,"
                           + Columns.PERSON_SKYPE + " TEXT,"
                           + Columns.PERSON_PHONE + " TEXT,"
                           + Columns.PERSON_BIRTHDAY + " TEXT,"
                           + Columns.PERSON_INFO + " TEXT,"
                           + Columns.CUSTOMER_INVESTOR_COMPANY + " TEXT,"
                           + Columns.EMPLOYEE_EXPERIENCE + " TEXT,"
                           + Columns.EMPLOYEE_EDUCATION + " TEXT);");

    sqLiteDatabase.execSQL("CREATE VIEW "
                           + Tables.PROJECT_VIEW
                           + " AS SELECT "
                           + "P." + Columns._ID
                           + ",P." + Columns.PROJECT_PROJECTNAME
                           + ",P." + Columns.PROJECT_PROJECTDUEDATE
                           + ",P." + Columns.PROJECT_CATEGORY
                           + ",P." + Columns.PROJECT_STATUS
                           + ",P." + Columns.PROJECT_PRIORITY
                           + ",P. " + Columns.PROJECT_INVESTOR_ID
                           + ",P. " + Columns.PROJECT_CUSTOMER_ID
                           + ",i. " + Columns.PERSON_FIRSTNAME + " as " + Columns.INVESTOR_FIRSTNAME
                           + ",i. " + Columns.PERSON_MIDDLENAME + " as " + Columns.INVESTOR_MIDDLENAME
                           + ",i. " + Columns.PERSON_LASTNAME + " as " + Columns.INVESTOR_LASTNAME
                           + ",сust. " + Columns.PERSON_FIRSTNAME + " as " + Columns.CUSTOMER_FIRSTNAME
                           + ",сust. " + Columns.PERSON_MIDDLENAME + " as " + Columns.CUSTOMER_MIDDLENAME
                           + ",сust. " + Columns.PERSON_LASTNAME + " as " + Columns.CUSTOMER_LASTNAME
                           + " FROM  project p"
                           + " LEFT OUTER JOIN customer_investor i ON  p.INVESTOR_ID = i._id "
                           + " LEFT OUTER JOIN customer_investor сust ON  p.CUSTOMER_ID = сust._id ");

    sqLiteDatabase.execSQL("CREATE TABLE " + Tables.STAGE_EMPLOYEE + " ("
                           + Columns._ID + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                           + Columns.STAGE_ID + "STAGE_ID C,"
                           + Columns.EMPLOYEE_ID + " INTEGER );");

    sqLiteDatabase.execSQL("CREATE TABLE " + Tables.STAGE + " ("
               + Columns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
               + Columns.STAGE_TYPE + " TEXT,"
               + Columns.STAGE_PROJECT_ID + " INTEGER,"
               + Columns.STAGE_MANAGER + " INTEGER );");

    /*sqLiteDatabase.execSQL("CREATE VIEW "
               + Tables.PROJECT_VIEW
               + " AS SELECT "
               + "P." + Columns._ID
               + ",P." + Columns.PROJECT_PROJECTNAME
               + ",P." + Columns.PROJECT_PROJECTDUEDATE
               + ",P." + Columns.PROJECT_CATEGORY
               + ",P." + Columns.PROJECT_STATUS
               + ",P." + Columns.PROJECT_PRIORITY
               + ",P. " + Columns.PROJECT_COLUMN_PROJECTDESCRIPTION
               + ",P. " + Columns.STAGE1
               + ",P. " + Columns.STAGE2
               + ",P. " + Columns.STAGE3
               + ",P. " + Columns.STAGE4
               + ",P. " + Columns.STAGE5
               + ",E1." + Columns.PERSON_FIRSTNAME + " as " + Columns.EMPLOYEE_COLUMN_EMPLNAME1
               + ",E2." + Columns.PERSON_FIRSTNAME + " as " + Columns.EMPLOYEE_COLUMN_EMPLNAME2
               + ",E3." + Columns.PERSON_FIRSTNAME + " as " + Columns.EMPLOYEE_COLUMN_EMPLNAME3
               + ",E4." + Columns.PERSON_FIRSTNAME + " as " + Columns.EMPLOYEE_COLUMN_EMPLNAME4
               + ",E5." + Columns.PERSON_FIRSTNAME + " as " + Columns.EMPLOYEE_COLUMN_EMPLNAME5
               + " FROM  PROJECT P"
               + " LEFT OUTER JOIN STAGE S1 ON P.STAGE1 = S1._id "
               + " LEFT OUTER JOIN PERSON E1 ON S1.MANAGER_ID = E1._id"
               + " LEFT OUTER JOIN STAGE S2 ON P.STAGE2 = S2._id "
               + " LEFT OUTER JOIN PERSON E2 ON S2.MANAGER_ID = E2._id"
               + " LEFT OUTER JOIN STAGE S3 ON P.STAGE3 = S3._id "
               + " LEFT OUTER JOIN PERSON E3 ON S3.MANAGER_ID = E3._id"
               + " LEFT OUTER JOIN STAGE S4 ON P.STAGE4 = S4._id "
               + " LEFT OUTER JOIN PERSON E4 ON S4.MANAGER_ID = E4._id"
               + " LEFT OUTER JOIN STAGE S5 ON P.STAGE5 = S5._id "
               + " LEFT OUTER JOIN PERSON E5 ON S5.MANAGER_ID = E5._id");
    */
    for (int i = 0; i < sqlDump.length; i++) {
      sqLiteDatabase.execSQL(sqlDump[i]);
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Tables.STAGE_EMPLOYEE);
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Tables.PERSON);
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Tables.PROJECT);
    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Tables.AUTHORIZATION);
    sqLiteDatabase.execSQL("DROP VIEW IF EXISTS " + Tables.PROJECT_VIEW);
//    sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Tables.CUSTOMER_INVESTOR);
    onCreate(sqLiteDatabase);
  }


}
