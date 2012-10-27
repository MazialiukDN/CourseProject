package by.bsu.courseproject.db;



import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.v4.database.DatabaseUtilsCompat;

public class ProjectManagerProvider extends ContentProvider{

	private DataBaseHelper mDataBaseHelper = null;

	public static final String AUTHORITY = "by.bsu.courseproject.ProjectManagerProvider";

	private static final int PROJECT = 1;
	private static final int PROJECT_ID = 2;

	private static final int EMPLOYEE = 3;
	private static final int EMPLOYEE_ID = 4;

	private static final int STAGE_EMPLOYEE = 5;
	private static final int STAGE_EMPLOYEE_ID = 6;

	private static final int STAGE = 7;
	private static final int STAGE_ID = 8;

	private static final int CUSTOMER_INVESTOR = 9;
	private static final int CUSTOMER_INVESTOR_ID = 10;
	
	private static final int VIEW_NAME = 11;
	private static final int VIEW_NAME_ID = 12;
	
	private static final int AUTHORIZATION = 14;
	private static final int AUTHORIZATION_ID = 15;

	// Uri matcher to decode incoming URIs.
	private final UriMatcher mUriMatcher;

	private final HashMap<String, String> projectProjectionMap;
	private final HashMap<String, String> employeeProjectionMap;
	private final HashMap<String, String> customerInvProjectionMap;
	private final HashMap<String, String> authorizationProjectionMap;


	static class DataBaseHelper extends SQLiteOpenHelper{

		private static final String DATABASE_NAME = "project_manager.db";
		private static final int DATABASE_VERSION = 3;


		private final String[] sqlDump = {

				"INSERT INTO \"employee\" VALUES(1,'Соколов', 'Михаил', 'Юрьевич', 'sokolov@gmail.com', 'mishail.sokolov', '(8-029) 234-89-23','23-01-1991','', '', 'Инженер-программист');",
				"INSERT INTO \"employee\" VALUES(2,'Антоневич', 'Дмитрий', 'Александрович', 'dmitry@yandex.ru', 'misha.ivanov', '(8-025) 988-45-34','25-01-1991','', '', 'Инженер-программист');",
				"INSERT INTO \"employee\" VALUES(3,'Лямцев','Сергей', 'Андреевич', 'misha@yandex.ru', 'misha.ivanov', '(8-025) 453-45-34','26-01-1991','', '', 'Инженер-программист');",
				"INSERT INTO \"employee\" VALUES(4,'Климчук', 'Александр','Иванович', 'misha@yandex.ru', 'misha.ivanov', '(8-044) 534-43-23','24-01-1991','', '', 'Инженер-программист');",
				"INSERT INTO \"employee\" VALUES(5,'Новиков','Алексей','Николаевич', 'misha@yandex.ru', 'misha.ivanov', '(8-029) 432-54-45','24-01-1991','', '', 'Инженер-программист');",
				"INSERT INTO \"employee\" VALUES(6,'Борисов','Николай', 'Алексеевич', 'misha@yandex.ru', 'misha.ivanov', '(8-025) 234-43-44','22-01-1991','', '', 'Инженер-программист');",
				"INSERT INTO \"employee\" VALUES(7,'Троцкий', 'Николай','Юрьевич', 'misha@yandex.ru', 'misha.ivanov', '(8-029) 454-62-55','20-01-1991','', '', 'Инженер-программист');",
				"INSERT INTO \"employee\" VALUES(8,'Поляков', 'Михаил','Борисович', 'misha@yandex.ru', 'misha.ivanov', '(8-029) 948-50-05','24-01-1991','', '', 'Инженер-программист');",

				"INSERT INTO \"customer_investor\" VALUES(1,1,1,'Соколов', 'Михаил', 'Юрьевич', 'sokolov@gmail.com', 'mishail.sokolov', '(8-029) 234-89-23','IBA', 'Инженер-программист');",
				"INSERT INTO \"customer_investor\" VALUES(2,0,1,'Новиков', 'Дмитрий', 'Николаевич', 'sokolov@gmail.com', 'mishail.sokolov', '(8-044) 678-89-23', 'IBA', 'Инженер-программист');",
				"INSERT INTO \"customer_investor\" VALUES(3,1,0,'Троцкий', 'Сергей','Иванович', 'sokolov@gmail.com', 'mishail.sokolov', '(8-029) 676-80-23','IBA', 'Инженер-программист');",
				"INSERT INTO \"customer_investor\" VALUES(4,0,1,'Поляков', 'Дмитрий', 'Александрович', 'sokolov@gmail.com', 'mishail.sokolov', '(8-029) 435-54-23','IBA', 'Инженер-программист');",
				"INSERT INTO \"customer_investor\" VALUES(5,1,1,'Климчук', 'Николай', 'Борисович', 'sokolov@gmail.com', 'mishail.sokolov', '(8-025) 234-45-23','IBA', 'Инженер-программист');",
				"INSERT INTO \"customer_investor\" VALUES(6,0,1,'Лямцев', 'Александр','Андреевич', 'sokolov@gmail.com', 'mishail.sokolov', '(8-029) 342-12-32', 'IBA', 'Инженер-программист');",

				"INSERT INTO \"project\" VALUES(1,'Новый мир','20120522','Строительство', 'Предлагаемый', 5, 1, 4);",
				"INSERT INTO \"project\" VALUES(2,'БГУ','20120622','Образование', 'Предлагаемый', 4, 3, 5);",
				"INSERT INTO \"project\" VALUES(3,'Юридеческая контора','20120506','Консультационные услуги', 'Предлагаемый', 3, 3, 6);",
				"INSERT INTO \"project\" VALUES(4,'Минсктранс','20120423','Транспорт', 'Предлагаемый', 4, 1, 4);",
				"INSERT INTO \"project\" VALUES(5,'Надёжный вклад','20120711','Финансы', 'Предлагаемый', 1, 1, 5);",

		};

		public DataBaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL("CREATE TABLE project ("
					+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "PROJECTNAME TEXT NOT NULL,"
					+ "PROJECTDUEDATE TEXT,"
					+ "CATEGORY TEXT,"
					+ "STATUS TEXT,"
					+ "PRIORITY INTEGER,"
					+ "INVESTOR_ID INTEGER,"
					+ "CUSTOMER_ID INTEGER" + ");");
			
			db.execSQL("CREATE TABLE authorization ("
					+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "LOGIN BLOB,"
					+ "SALT TEXT,"
					+ "PASSWORD BLOB" + ");");

			db.execSQL("CREATE TABLE employee ("
					+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "LASTNAME TEXT,"
					+ "FIRSTNAME TEXT,"
					+ "MIDDLENAME TEXT,"
					+ "EMAIL TEXT,"
					+ "SKYPE TEXT,"
					+ "PHONE TEXT,"
					+ "BIRTHDAY TEXT,"
					+ "experience TEXT,"
					+ "education TEXT,"
					+ "INFO TEXT" + ");");


			db.execSQL("CREATE TABLE customer_investor ("
					+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "HANDLE_INV INTEGER,"
					+ "HANDLE_CUST INTEGER,"
					+ "LASTNAME TEXT,"
					+ "FIRSTNAME TEXT,"
					+ "MIDDLENAME TEXT,"
					+ "EMAIL TEXT,"
					+ "SKYPE TEXT,"
					+ "PHONE TEXT,"
					+ "COMPANY TEXT,"
					+ "INFO TEXT" + ");");
	
			
			db.execSQL("CREATE VIEW "
					+ Columns.VIEW_NAME
					+ " AS SELECT "
					+ "P." 		+ Columns.COLUMN_NAME_ID
					+ ",P." 	+ Columns.PROJECT_COLUMN_PROJECTNAME
					+ ",P." 	+ Columns.PROJECT_COLUMN_PROJECTDUEDATE
					+ ",P." 	+ Columns.PROJECT_COLUMN_CATEGORY
					+ ",P." 	+ Columns.PROJECT_COLUMN_STATUS					
					+ ",P."		+ Columns.PROJECT_COLUMN_PRIORITY
					+ ",P. "	+ Columns.PROJECT_INVESTOR_ID
					+ ",P. "	+ Columns.PROJECT_CUSTOMER_ID
					+ ",i. "	+ Columns.COLUMN_FIRSTNAME + " as " + Columns.COLUMN_INV_FIRSTNAME
					+ ",i. "	+ Columns.COLUMN_MIDDLENAME + " as " + Columns.COLUMN_INV_MIDDLENAME
					+ ",i. "	+ Columns.COLUMN_LASTNAME + " as " + Columns.COLUMN_INV_LASTNAME
					+ ",сust. "	+ Columns.COLUMN_FIRSTNAME + " as " + Columns.COLUMN_CUST_FIRSTNAME
					+ ",сust. "	+ Columns.COLUMN_MIDDLENAME + " as " + Columns.COLUMN_CUST_MIDDLENAME
					+ ",сust. "	+ Columns.COLUMN_LASTNAME + " as " + Columns.COLUMN_CUST_LASTNAME
					+ " FROM  project p" 
					+ " LEFT OUTER JOIN customer_investor i ON  p.INVESTOR_ID = i._id "
					+ " LEFT OUTER JOIN customer_investor сust ON  p.CUSTOMER_ID = сust._id ");
			/*db.execSQL("CREATE TABLE stage_employee ("
					+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "STAGE_ID C,"
					+ "EMPL_ID INTEGER"  + ");");

			db.execSQL("CREATE TABLE stage ("
					+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
					+ "MANAGER_ID C"  + ");");

			db.execSQL("CREATE VIEW "
					+ Columns.VIEW_NAME
					+ " AS SELECT "
					+ "P." 		+ Columns.COLUMN_NAME_ID
					+ ",P." 	+ Columns.PROJECT_COLUMN_PROJECTNAME
					+ ",P." 	+ Columns.PROJECT_COLUMN_PROJECTDUEDATE
					+ ",P." 	+ Columns.PROJECT_COLUMN_CATEGORY
					+ ",P." 	+ Columns.PROJECT_COLUMN_STATUS					
					+ ",P."		+ Columns.PROJECT_COLUMN_PRIORITY
					+ ",P. "	+ Columns.PROJECT_COLUMN_PROJECTDESCRIPTION
					+ ",P. "	+ Columns.STAGE1
					+ ",P. "	+ Columns.STAGE2
					+ ",P. "	+ Columns.STAGE3
					+ ",P. "	+ Columns.STAGE4
					+ ",P. "	+ Columns.STAGE5
					+ ",E1." 	+ Columns.COLUMN_FIRSTNAME + " as " + Columns.EMPLOYEE_COLUMN_EMPLNAME1
					+ ",E2." 	+ Columns.COLUMN_FIRSTNAME + " as " + Columns.EMPLOYEE_COLUMN_EMPLNAME2
					+ ",E3." 	+ Columns.COLUMN_FIRSTNAME + " as " + Columns.EMPLOYEE_COLUMN_EMPLNAME3
					+ ",E4." 	+ Columns.COLUMN_FIRSTNAME + " as " + Columns.EMPLOYEE_COLUMN_EMPLNAME4
					+ ",E5." 	+ Columns.COLUMN_FIRSTNAME + " as " + Columns.EMPLOYEE_COLUMN_EMPLNAME5
					+ " FROM  PROJECT P" 
					+ " LEFT OUTER JOIN STAGE S1 ON P.STAGE1 = S1._id " 
					+ " LEFT OUTER JOIN EMPLOYEE E1 ON S1.MANAGER_ID = E1._id" 
					+ " LEFT OUTER JOIN STAGE S2 ON P.STAGE2 = S2._id "
					+ " LEFT OUTER JOIN EMPLOYEE E2 ON S2.MANAGER_ID = E2._id" 
					+ " LEFT OUTER JOIN STAGE S3 ON P.STAGE3 = S3._id "
					+ " LEFT OUTER JOIN EMPLOYEE E3 ON S3.MANAGER_ID = E3._id" 
					+ " LEFT OUTER JOIN STAGE S4 ON P.STAGE4 = S4._id "
					+ " LEFT OUTER JOIN EMPLOYEE E4 ON S4.MANAGER_ID = E4._id"  
					+ " LEFT OUTER JOIN STAGE S5 ON P.STAGE5 = S5._id "
					+ " LEFT OUTER JOIN EMPLOYEE E5 ON S5.MANAGER_ID = E5._id");
			 */
			for (int i = 0; i < sqlDump.length; i++){
				db.execSQL(sqlDump[i]);
			}

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS stage_employee");
			db.execSQL("DROP TABLE IF EXISTS employee");
			db.execSQL("DROP TABLE IF EXISTS project");
			db.execSQL("DROP TABLE IF EXISTS authorization");
			db.execSQL("DROP VIEW IF EXISTS project_view");
			db.execSQL("DROP TABLE IF EXISTS customer_investor");
			onCreate(db);

		}
	}

	public static final class Columns implements BaseColumns{

		private Columns(){
			super();
		}

		//The table name offered by this provider
		public static final String PROJECT_TABLE_NAME = "project";
		public static final String EMPLOYEE_TABLE_NAME = "employee";
		public static final String STAGE_EMPLOYEE_TABLE_NAME = "stage_employee";
		public static final String STAGE_TABLE_NAME = "stage";
		public static final String CUST_INV_TABLE_NAME = "customer_investor";
		public static final String AUTHORIZATION_TABLE_NAME = "authorization";
		
		
		public static final String VIEW_NAME = "project_view";

		public static final String CONTENT_NAME_PREFIX = "content://";

		//The content:// style URL for this table
		public static final Uri PROJECT_URI = Uri.parse(CONTENT_NAME_PREFIX
				+ AUTHORITY + "/" + PROJECT_TABLE_NAME);
		public static final Uri AUTHORIZATION_URI = Uri.parse(CONTENT_NAME_PREFIX
				+ AUTHORITY + "/" + AUTHORIZATION_TABLE_NAME);
		public static final Uri EMPLOYEE_URI = Uri.parse(CONTENT_NAME_PREFIX
				+ AUTHORITY + "/" + EMPLOYEE_TABLE_NAME);
		public static final Uri STAGE_EMPLOYEE_URI = Uri.parse(CONTENT_NAME_PREFIX
				+ AUTHORITY + "/" + STAGE_EMPLOYEE_TABLE_NAME);
		public static final Uri STAGE_URI = Uri.parse(CONTENT_NAME_PREFIX
				+ AUTHORITY + "/" + STAGE_TABLE_NAME);
		public static final Uri CUST_INV_URI = Uri.parse(CONTENT_NAME_PREFIX
				+ AUTHORITY + "/" + CUST_INV_TABLE_NAME);
		public static final Uri CONTENT_URI = Uri.parse(CONTENT_NAME_PREFIX
				+ AUTHORITY + "/" + VIEW_NAME);


		public static final String COLUMN_NAME_ID = "_id";

		public static final String PROJECT_COLUMN_PROJECTNAME = "PROJECTNAME";
		public static final String PROJECT_COLUMN_PROJECTDUEDATE = "PROJECTDUEDATE";
		public static final String PROJECT_COLUMN_CATEGORY = "CATEGORY";
		public static final String PROJECT_COLUMN_STATUS = "STATUS";
		public static final String PROJECT_COLUMN_PRIORITY = "PRIORITY";
		public static final String PROJECT_INVESTOR_ID = "INVESTOR_ID";
		public static final String PROJECT_CUSTOMER_ID = "CUSTOMER_ID";
		
		public static final String LOGIN = "LOGIN";
		public static final String PASSWORD = "PASSWORD";
		public static final String SALT = "SALT";

		public static final String COLUMN_FIRSTNAME = "FIRSTNAME";
		public static final String COLUMN_MIDDLENAME = "MIDDLENAME";
		public static final String COLUMN_LASTNAME = "LASTNAME";
		public static final String COLUMN_EMAIL = "EMAIL";
		public static final String COLUMN_SKYPE = "SKYPE";
		public static final String COLUMN_PHONE = "PHONE";
		public static final String COLUMN_BIRTHDAY = "BIRTHDAY";
		public static final String COLUMN_INFO = "INFO";
		public static final String COLUMN_EXPERIENCE = "experience";
		public static final String COLUMN_EDUCATION= "education";

		public static final String COLUMN_COMPANY = "COMPANY";
		public static final String COLUMN_HANDLE_INV = "HANDLE_INV";
		public static final String COLUMN_HANDLE_CUST = "HANDLE_CUST";

		public static final String STAGE_COLUMN_MANAGER_ID = "MANAGER_ID";

		public static final String SORT_ORDER_ASC = "_id ASC";
		public static final String SORT_ORDER_EMP = "LASTNAME ASC";
		public static final String SORT_ORDER_DESC = "_id DESC";
		public static final String SORT_ORDER_PROJECT = "PRIORITY ASC";
		
		public static final String COLUMN_INV_FIRSTNAME = "COLUMN_INV_FIRSTNAME";
		public static final String COLUMN_INV_MIDDLENAME = "COLUMN_INV_MIDDLENAME";
		public static final String COLUMN_INV_LASTNAME = "COLUMN_INV_LASTNAME";
		
		public static final String COLUMN_CUST_FIRSTNAME = "COLUMN_CUST_FIRSTNAME";
		public static final String COLUMN_CUST_MIDDLENAME = "COLUMN_CUST_MIDDLENAME";
		public static final String COLUMN_CUST_LASTNAME = "COLUMN_CUST_LASTNAME";

	}


	public ProjectManagerProvider(){
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		mUriMatcher.addURI(AUTHORITY, Columns.PROJECT_TABLE_NAME, PROJECT);
		mUriMatcher.addURI(AUTHORITY, Columns.PROJECT_TABLE_NAME + "/#", PROJECT_ID);

		mUriMatcher.addURI(AUTHORITY, Columns.VIEW_NAME, PROJECT);
		mUriMatcher.addURI(AUTHORITY, Columns.VIEW_NAME + "/#", PROJECT_ID);

		mUriMatcher.addURI(AUTHORITY, Columns.EMPLOYEE_TABLE_NAME, EMPLOYEE);
		mUriMatcher.addURI(AUTHORITY, Columns.EMPLOYEE_TABLE_NAME + "/#", EMPLOYEE_ID);

		mUriMatcher.addURI(AUTHORITY, Columns.STAGE_EMPLOYEE_TABLE_NAME, STAGE_EMPLOYEE);
		mUriMatcher.addURI(AUTHORITY, Columns.STAGE_EMPLOYEE_TABLE_NAME + "/#", STAGE_EMPLOYEE_ID);

		mUriMatcher.addURI(AUTHORITY, Columns.STAGE_TABLE_NAME, STAGE);
		mUriMatcher.addURI(AUTHORITY, Columns.STAGE_TABLE_NAME + "/#", STAGE_ID);

		mUriMatcher.addURI(AUTHORITY, Columns.CUST_INV_TABLE_NAME, CUSTOMER_INVESTOR);
		mUriMatcher.addURI(AUTHORITY, Columns.CUST_INV_TABLE_NAME + "/#", CUSTOMER_INVESTOR_ID);
		
		mUriMatcher.addURI(AUTHORITY, Columns.AUTHORIZATION_TABLE_NAME, AUTHORIZATION);
		mUriMatcher.addURI(AUTHORITY, Columns.AUTHORIZATION_TABLE_NAME + "/#", AUTHORIZATION_ID);
		
		authorizationProjectionMap = new HashMap<String, String>();
		authorizationProjectionMap.put(Columns.LOGIN, Columns.LOGIN);
		authorizationProjectionMap.put(Columns.PASSWORD, Columns.PASSWORD);
		authorizationProjectionMap.put(Columns.COLUMN_NAME_ID, Columns.COLUMN_NAME_ID);
		authorizationProjectionMap.put(Columns.SALT, Columns.SALT);

		projectProjectionMap = new HashMap<String, String>();
		projectProjectionMap.put(Columns.COLUMN_NAME_ID, Columns.COLUMN_NAME_ID);
		projectProjectionMap.put(Columns.PROJECT_COLUMN_PROJECTNAME, Columns.PROJECT_COLUMN_PROJECTNAME);
		projectProjectionMap.put(Columns.PROJECT_COLUMN_PROJECTDUEDATE, Columns.PROJECT_COLUMN_PROJECTDUEDATE);
		projectProjectionMap.put(Columns.PROJECT_COLUMN_CATEGORY, Columns.PROJECT_COLUMN_CATEGORY);
		projectProjectionMap.put(Columns.PROJECT_COLUMN_STATUS, Columns.PROJECT_COLUMN_STATUS);
		projectProjectionMap.put(Columns.PROJECT_COLUMN_PRIORITY, Columns.PROJECT_COLUMN_PRIORITY);
		projectProjectionMap.put(Columns.PROJECT_INVESTOR_ID, Columns.PROJECT_INVESTOR_ID);
		projectProjectionMap.put(Columns.PROJECT_CUSTOMER_ID, Columns.PROJECT_CUSTOMER_ID);
		
		projectProjectionMap.put(Columns.COLUMN_INV_FIRSTNAME, Columns.COLUMN_INV_FIRSTNAME);
		projectProjectionMap.put(Columns.COLUMN_INV_MIDDLENAME, Columns.COLUMN_INV_MIDDLENAME);
		projectProjectionMap.put(Columns.COLUMN_INV_LASTNAME, Columns.COLUMN_INV_LASTNAME);
		
		projectProjectionMap.put(Columns.COLUMN_CUST_FIRSTNAME, Columns.COLUMN_CUST_FIRSTNAME);
		projectProjectionMap.put(Columns.COLUMN_CUST_MIDDLENAME, Columns.COLUMN_CUST_MIDDLENAME);
		projectProjectionMap.put(Columns.COLUMN_CUST_LASTNAME, Columns.COLUMN_CUST_LASTNAME);

		employeeProjectionMap = new HashMap<String, String>();
		employeeProjectionMap.put(Columns.COLUMN_NAME_ID, Columns.COLUMN_NAME_ID);
		employeeProjectionMap.put(Columns.COLUMN_FIRSTNAME, Columns.COLUMN_FIRSTNAME);
		employeeProjectionMap.put(Columns.COLUMN_MIDDLENAME, Columns.COLUMN_MIDDLENAME);
		employeeProjectionMap.put(Columns.COLUMN_LASTNAME, Columns.COLUMN_LASTNAME);
		employeeProjectionMap.put(Columns.COLUMN_EMAIL, Columns.COLUMN_EMAIL);

		employeeProjectionMap.put(Columns.COLUMN_SKYPE, Columns.COLUMN_SKYPE);
		employeeProjectionMap.put(Columns.COLUMN_PHONE, Columns.COLUMN_PHONE);
		employeeProjectionMap.put(Columns.COLUMN_BIRTHDAY, Columns.COLUMN_BIRTHDAY);
		employeeProjectionMap.put(Columns.COLUMN_INFO, Columns.COLUMN_INFO);
		employeeProjectionMap.put(Columns.COLUMN_EXPERIENCE, Columns.COLUMN_EXPERIENCE);
		employeeProjectionMap.put(Columns.COLUMN_EDUCATION, Columns.COLUMN_EDUCATION);


		customerInvProjectionMap = new HashMap<String, String>();
		customerInvProjectionMap.put(Columns.COLUMN_NAME_ID, Columns.COLUMN_NAME_ID);
		customerInvProjectionMap.put(Columns.COLUMN_FIRSTNAME, Columns.COLUMN_FIRSTNAME);
		customerInvProjectionMap.put(Columns.COLUMN_MIDDLENAME, Columns.COLUMN_MIDDLENAME);
		customerInvProjectionMap.put(Columns.COLUMN_LASTNAME, Columns.COLUMN_LASTNAME);
		customerInvProjectionMap.put(Columns.COLUMN_EMAIL, Columns.COLUMN_EMAIL);
		customerInvProjectionMap.put(Columns.COLUMN_HANDLE_INV, Columns.COLUMN_HANDLE_INV);
		customerInvProjectionMap.put(Columns.COLUMN_HANDLE_CUST, Columns.COLUMN_HANDLE_CUST);
		customerInvProjectionMap.put(Columns.COLUMN_SKYPE, Columns.COLUMN_SKYPE);
		customerInvProjectionMap.put(Columns.COLUMN_PHONE, Columns.COLUMN_PHONE);
		customerInvProjectionMap.put(Columns.COLUMN_COMPANY, Columns.COLUMN_COMPANY);
		customerInvProjectionMap.put(Columns.COLUMN_INFO, Columns.COLUMN_INFO);

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {

		SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
		String finalWhere;
		int count;

		switch (mUriMatcher.match(uri)) {
		case PROJECT: 
			count = db.delete(Columns.PROJECT_TABLE_NAME, selection, selectionArgs);
			break;

		case PROJECT_ID: 
			/*finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns.STAGE_EMPLOYEE_COLUMN_PROJECT_ID
					+ " = " + ContentUris.parseId(uri), null);

			count = db.delete(Columns.STAGE_EMPLOYEE_TABLE_NAME, finalWhere, null);*/

			finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns.COLUMN_NAME_ID
					+ " = " + ContentUris.parseId(uri), null);

			count = db.delete(Columns.PROJECT_TABLE_NAME, finalWhere, selectionArgs); 

			break;
		case EMPLOYEE: 
			count = db.delete(Columns.EMPLOYEE_TABLE_NAME, selection, selectionArgs);
			break;
		case EMPLOYEE_ID: 
			//finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns.STAGE_EMPLOYEE_COLUMN_PROJECT_ID
			//		+ " = " + ContentUris.parseId(uri), null);

			//count = db.delete(Columns.STAGE_EMPLOYEE_TABLE_NAME, finalWhere, null);

			finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns.COLUMN_NAME_ID
					+ " = " + ContentUris.parseId(uri), null);

			count = db.delete(Columns.EMPLOYEE_TABLE_NAME, finalWhere, selectionArgs); 

			break;
		case STAGE_EMPLOYEE: 
			count = db.delete(Columns.STAGE_EMPLOYEE_TABLE_NAME, selection, selectionArgs);
			break;
		case STAGE_EMPLOYEE_ID: 
			finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns.COLUMN_NAME_ID
					+ " = " + ContentUris.parseId(uri), selection);
			count = db.delete(Columns.STAGE_EMPLOYEE_TABLE_NAME, finalWhere, selectionArgs);
			break;
		case STAGE: 
			count = db.delete(Columns.STAGE_TABLE_NAME, selection, selectionArgs);
			break;
		case STAGE_ID: 
			finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns.COLUMN_NAME_ID
					+ " = " + ContentUris.parseId(uri), selection);
			count = db.delete(Columns.STAGE_TABLE_NAME, finalWhere, selectionArgs);
			break;

		case CUSTOMER_INVESTOR: 
			count = db.delete(Columns.CUST_INV_TABLE_NAME, selection, selectionArgs);
			break;
		case CUSTOMER_INVESTOR_ID: 
			finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns.COLUMN_NAME_ID
					+ " = " + ContentUris.parseId(uri), selection);
			count = db.delete(Columns.CUST_INV_TABLE_NAME, finalWhere, selectionArgs);
			break;

		default: throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch (mUriMatcher.match(uri)) {
		case PROJECT: 
			if (values != null){
				SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
				long rowId = db.insert(Columns.PROJECT_TABLE_NAME, null, values);

				if (rowId > 0){
					Uri noteUri = ContentUris.withAppendedId(Columns.PROJECT_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;

				}
			}
			throw new SQLException("Failed to insert row into " + uri);	
		case EMPLOYEE: 
			if (values != null){
				SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
				long rowId = db.insert(Columns.EMPLOYEE_TABLE_NAME, null, values);

				if (rowId > 0){
					Uri noteUri = ContentUris.withAppendedId(Columns.EMPLOYEE_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;

				}
			}
			throw new SQLException("Failed to insert row into " + uri);	
		case AUTHORIZATION:
			if (values != null){
				SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
				long rowId = db.insert(Columns.AUTHORIZATION_TABLE_NAME, null, values);

				if (rowId > 0){
					Uri noteUri = ContentUris.withAppendedId(Columns.AUTHORIZATION_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;

				}
			}
			throw new SQLException("Failed to insert row into " + uri);	
			
		case STAGE_EMPLOYEE: 
			if (values != null){
				SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
				long rowId = db.insert(Columns.STAGE_EMPLOYEE_TABLE_NAME, null, values);

				if (rowId > 0){
					Uri noteUri = ContentUris.withAppendedId(Columns.STAGE_EMPLOYEE_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;

				}
			}
			throw new SQLException("Failed to insert row into " + uri);	

		case CUSTOMER_INVESTOR: 
			if (values != null){
				SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
				long rowId = db.insert(Columns.CUST_INV_TABLE_NAME, null, values);

				if (rowId > 0){
					Uri noteUri = ContentUris.withAppendedId(Columns.CUST_INV_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;

				}
			}
			throw new SQLException("Failed to insert row into " + uri);	

		case STAGE: 
			if (values != null){
				SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
				long rowId = db.insert(Columns.STAGE_TABLE_NAME, null, values);

				if (rowId > 0){
					Uri noteUri = ContentUris.withAppendedId(Columns.STAGE_URI, rowId);
					getContext().getContentResolver().notifyChange(noteUri, null);
					return noteUri;

				}
			}
			throw new SQLException("Failed to insert row into " + uri);	

		default: throw new IllegalArgumentException("Unknown URI " + uri);			
		}	

	}

	@Override
	public boolean onCreate() {

		mDataBaseHelper = new DataBaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch(mUriMatcher.match(uri)){
		case PROJECT: 
			qb.setTables(uri.getLastPathSegment());
			qb.setProjectionMap(projectProjectionMap);
			sortOrder = Columns.SORT_ORDER_PROJECT;
			break;
		case PROJECT_ID:
			qb.setTables(uri.getPathSegments().get(uri.getPathSegments().size()-2));
			qb.setProjectionMap(projectProjectionMap);
			qb.appendWhere(Columns.COLUMN_NAME_ID + "=?");
			selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
					selectionArgs, new String[] {uri.getLastPathSegment()});
			break;

		case EMPLOYEE:
			qb.setTables(Columns.EMPLOYEE_TABLE_NAME);
			qb.setProjectionMap(employeeProjectionMap);
			sortOrder = Columns.SORT_ORDER_EMP;
			break;
			
		case AUTHORIZATION:
			qb.setTables(Columns.AUTHORIZATION_TABLE_NAME);
			qb.setProjectionMap(authorizationProjectionMap);
			sortOrder = Columns.SORT_ORDER_ASC;
			break;
			
		case AUTHORIZATION_ID: 
			qb.setTables(Columns.AUTHORIZATION_TABLE_NAME);
			qb.setProjectionMap(authorizationProjectionMap);
			qb.appendWhere(Columns.COLUMN_NAME_ID + "=?");
			selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
					selectionArgs, new String[] {uri.getLastPathSegment()});
			break;
			

		case EMPLOYEE_ID: 
			qb.setTables(Columns.EMPLOYEE_TABLE_NAME);
			qb.setProjectionMap(employeeProjectionMap);
			qb.appendWhere(Columns.COLUMN_NAME_ID + "=?");
			selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
					selectionArgs, new String[] {uri.getLastPathSegment()});
			break;

		case CUSTOMER_INVESTOR:
			qb.setTables(Columns.CUST_INV_TABLE_NAME);
			qb.setProjectionMap(customerInvProjectionMap);
			sortOrder = Columns.SORT_ORDER_EMP;
			break;

		case CUSTOMER_INVESTOR_ID: 
			qb.setTables(Columns.CUST_INV_TABLE_NAME);
			qb.setProjectionMap(customerInvProjectionMap);
			qb.appendWhere(Columns.COLUMN_NAME_ID + "=?");
			selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
					selectionArgs, new String[] {uri.getLastPathSegment()});
			break;
		case STAGE_EMPLOYEE:
			qb.setTables(Columns.STAGE_EMPLOYEE_TABLE_NAME);
			qb.setProjectionMap(employeeProjectionMap);
			if (sortOrder == null)
				sortOrder = Columns.SORT_ORDER_ASC;
			break;

		case STAGE_EMPLOYEE_ID: 
			qb.setTables(Columns.STAGE_EMPLOYEE_TABLE_NAME);
			qb.setProjectionMap(employeeProjectionMap);
			qb.appendWhere(Columns.COLUMN_NAME_ID + "=?");
			selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
					selectionArgs, new String[] {uri.getLastPathSegment()});
			break;
		/*case VIEW_NAME:
			
			qb.setTables(uri.getLastPathSegment());
			sortOrder = Columns.SORT_ORDER_PROJECT;
			break;
		case VIEW_NAME_ID:
			// The incoming URI is for a single row.
			qb.setTables(uri.getPathSegments().get(uri.getPathSegments().size()-2));
			//qb.setProjectionMap(columnsProjectionMap);
			qb.appendWhere(Columns.COLUMN_NAME_ID + "=?");
			selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
					selectionArgs, new String[] { uri.getLastPathSegment() });
			break;*/
			/*case STAGE:
			qb.setTables(Columns.STAGE_TABLE_NAME);
			qb.setProjectionMap(stageProjectionMap);
			if (sortOrder == null)
				sortOrder = Columns.SORT_ORDER_ASC;
			break;

		case STAGE_ID: 
			qb.setTables(Columns.STAGE_TABLE_NAME);
			qb.setProjectionMap(stageProjectionMap);
			qb.appendWhere(Columns.COLUMN_NAME_ID + "=?");
			selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
					selectionArgs, new String[] {uri.getLastPathSegment()});
			break;*/
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
		Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
		int count;
		String finalWhere;

		switch(mUriMatcher.match(uri)){
		case PROJECT:
			count = db.update(Columns.PROJECT_TABLE_NAME, values, selection, selectionArgs);
			break;
		case PROJECT_ID:
			finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns.COLUMN_NAME_ID
					+ " = " + ContentUris.parseId(uri), selection);
			count = db.update(Columns.PROJECT_TABLE_NAME, values, finalWhere, selectionArgs);

			break;
		case EMPLOYEE:
			count = db.update(Columns.EMPLOYEE_TABLE_NAME, values, selection, selectionArgs);
			break;
		case EMPLOYEE_ID:
			finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns.COLUMN_NAME_ID
					+ " = " + ContentUris.parseId(uri), selection);
			count = db.update(Columns.EMPLOYEE_TABLE_NAME, values, finalWhere, selectionArgs);

			break;

		case CUSTOMER_INVESTOR:
			count = db.update(Columns.CUST_INV_TABLE_NAME, values, selection, selectionArgs);
			break;
		case CUSTOMER_INVESTOR_ID:
			finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns.COLUMN_NAME_ID
					+ " = " + ContentUris.parseId(uri), selection);
			count = db.update(Columns.CUST_INV_TABLE_NAME, values, finalWhere, selectionArgs);

			break;

		case STAGE_EMPLOYEE:
			count = db.update(Columns.STAGE_EMPLOYEE_TABLE_NAME, values, selection, selectionArgs);
			break;
		case STAGE_EMPLOYEE_ID:
			finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns.COLUMN_NAME_ID
					+ " = " + ContentUris.parseId(uri), selection);
			count = db.update(Columns.STAGE_EMPLOYEE_TABLE_NAME, values, finalWhere, selectionArgs);

			break;
		case STAGE:
			count = db.update(Columns.STAGE_TABLE_NAME, values, selection, selectionArgs);
			break;
		case STAGE_ID:
			finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns.COLUMN_NAME_ID
					+ " = " + ContentUris.parseId(uri), selection);
			count = db.update(Columns.STAGE_TABLE_NAME, values, finalWhere, selectionArgs);

			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);

		return count;
	}

}

