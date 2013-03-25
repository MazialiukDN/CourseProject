package by.bsu.courseproject.db;


import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.v4.database.DatabaseUtilsCompat;
import static by.bsu.courseproject.db.DBConstants.*;

import java.util.HashMap;

public class ProjectManagerProvider extends ContentProvider {

  private PMDBSQLiteHelper mDataBaseHelper = null;

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
  private final HashMap<String, String> personProjectionMap;
  private final HashMap<String, String> authorizationProjectionMap;

    public static final String CONTENT_NAME_PREFIX = "content://";

    //The content:// style URL for this table
    public static final Uri PROJECT_URI = Uri.parse(CONTENT_NAME_PREFIX
                                                    + AUTHORITY + "/" + Tables.PROJECT);
    public static final Uri AUTHORIZATION_URI = Uri.parse(CONTENT_NAME_PREFIX
                                                          + AUTHORITY + "/" + Tables.AUTHORIZATION);
    public static final Uri PERSON_URI = Uri.parse(CONTENT_NAME_PREFIX
                                                     + AUTHORITY + "/" + Tables.PERSON);
    public static final Uri STAGE_EMPLOYEE_URI = Uri.parse(CONTENT_NAME_PREFIX
                                                           + AUTHORITY + "/" + Tables.STAGE_EMPLOYEE);
    public static final Uri STAGE_URI = Uri.parse(CONTENT_NAME_PREFIX
                                                  + AUTHORITY + "/" + Tables.STAGE);
    public static final Uri CONTENT_URI = Uri.parse(CONTENT_NAME_PREFIX
                                                    + AUTHORITY + "/" + Tables.PROJECT);


  public ProjectManagerProvider() {
    mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    mUriMatcher.addURI(AUTHORITY, Tables.PROJECT, PROJECT);
    mUriMatcher.addURI(AUTHORITY, Tables.PROJECT + "/#", PROJECT_ID);

    mUriMatcher.addURI(AUTHORITY, Tables.PROJECT_VIEW, PROJECT);
    mUriMatcher.addURI(AUTHORITY, Tables.PROJECT_VIEW + "/#", PROJECT_ID);

    mUriMatcher.addURI(AUTHORITY, Tables.PERSON, EMPLOYEE);
    mUriMatcher.addURI(AUTHORITY, Tables.PERSON + "/#", EMPLOYEE_ID);

    mUriMatcher.addURI(AUTHORITY, Tables.STAGE_EMPLOYEE, STAGE_EMPLOYEE);
    mUriMatcher.addURI(AUTHORITY, Tables.STAGE_EMPLOYEE + "/#", STAGE_EMPLOYEE_ID);

    mUriMatcher.addURI(AUTHORITY, Tables.STAGE, STAGE);
    mUriMatcher.addURI(AUTHORITY, Tables.STAGE + "/#", STAGE_ID);

    mUriMatcher.addURI(AUTHORITY, Tables.AUTHORIZATION, AUTHORIZATION);
    mUriMatcher.addURI(AUTHORITY, Tables.AUTHORIZATION + "/#", AUTHORIZATION_ID);

    authorizationProjectionMap = new HashMap<String, String>();
    authorizationProjectionMap.put(Columns.LOGIN, Columns.LOGIN);
    authorizationProjectionMap.put(Columns.PASSWORD, Columns.PASSWORD);
    authorizationProjectionMap.put(Columns._ID, Columns._ID);
    authorizationProjectionMap.put(Columns.SALT, Columns.SALT);

    projectProjectionMap = new HashMap<String, String>();
    projectProjectionMap.put(Columns._ID, Columns._ID);
    projectProjectionMap.put(Columns.PROJECT_PROJECTNAME, Columns.PROJECT_PROJECTNAME);
    projectProjectionMap.put(Columns.PROJECT_PROJECTDUEDATE, Columns.PROJECT_PROJECTDUEDATE);
    projectProjectionMap.put(Columns.PROJECT_CATEGORY, Columns.PROJECT_CATEGORY);
    projectProjectionMap.put(Columns.PROJECT_STATUS, Columns.PROJECT_STATUS);
    projectProjectionMap.put(Columns.PROJECT_PRIORITY, Columns.PROJECT_PRIORITY);
    projectProjectionMap.put(Columns.PROJECT_INVESTOR_ID, Columns.PROJECT_INVESTOR_ID);
    projectProjectionMap.put(Columns.PROJECT_CUSTOMER_ID, Columns.PROJECT_CUSTOMER_ID);

    projectProjectionMap.put(Columns.PERSON_FIRSTNAME, Columns.PERSON_FIRSTNAME);
    projectProjectionMap.put(Columns.PERSON_MIDDLENAME, Columns.PERSON_MIDDLENAME);
    projectProjectionMap.put(Columns.PERSON_LASTNAME, Columns.PERSON_LASTNAME);

    personProjectionMap = new HashMap<String, String>();
    personProjectionMap.put(Columns._ID, Columns._ID);
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

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {

    SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
    String finalWhere;
    int count = 0;
/*
    switch (mUriMatcher.match(uri)) {
    case PROJECT:
      count = db.delete(Tables.PROJECT, selection, selectionArgs);
      break;

    case PROJECT_ID:
			*//*finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns.STAGE_EMPLOYEE_PROJECT_ID
					+ " = " + ContentUris.parseId(uri), null);

			count = db.delete(Columns.STAGE_EMPLOYEE, finalWhere, null);*//*

      finalWhere = Columns._ID + " = " + ContentUris.parseId(uri);

      count = db.delete(Tables.PROJECT, finalWhere, selectionArgs);

      break;
    case EMPLOYEE:
      count = db.delete(Columns.EMPLOYEE_TABLE_NAME, selection, selectionArgs);
      break;
    case EMPLOYEE_ID:
      //finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns.STAGE_EMPLOYEE_PROJECT_ID
      //		+ " = " + ContentUris.parseId(uri), null);

      //count = db.delete(Columns.STAGE_EMPLOYEE, finalWhere, null);

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

    default:
      throw new IllegalArgumentException("Unsupported URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);*/
    return count;
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

  @Override
  public Uri insert(Uri uri, ContentValues values) {
/*    switch (mUriMatcher.match(uri)) {
    case PROJECT:
      if (values != null) {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        long rowId = db.insert(Columns.PROJECT_TABLE_NAME, null, values);

        if (rowId > 0) {
          Uri noteUri = ContentUris.withAppendedId(Columns.PROJECT_URI, rowId);
          getContext().getContentResolver().notifyChange(noteUri, null);
          return noteUri;

        }
      }
      throw new SQLException("Failed to insert row into " + uri);
    case EMPLOYEE:
      if (values != null) {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        long rowId = db.insert(Columns.EMPLOYEE_TABLE_NAME, null, values);

        if (rowId > 0) {
          Uri noteUri = ContentUris.withAppendedId(Columns.EMPLOYEE_URI, rowId);
          getContext().getContentResolver().notifyChange(noteUri, null);
          return noteUri;

        }
      }
      throw new SQLException("Failed to insert row into " + uri);
    case AUTHORIZATION:
      if (values != null) {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        long rowId = db.insert(Columns.AUTHORIZATION_TABLE_NAME, null, values);

        if (rowId > 0) {
          Uri noteUri = ContentUris.withAppendedId(Columns.AUTHORIZATION_URI, rowId);
          getContext().getContentResolver().notifyChange(noteUri, null);
          return noteUri;

        }
      }
      throw new SQLException("Failed to insert row into " + uri);

    case STAGE_EMPLOYEE:
      if (values != null) {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        long rowId = db.insert(Columns.STAGE_EMPLOYEE_TABLE_NAME, null, values);

        if (rowId > 0) {
          Uri noteUri = ContentUris.withAppendedId(Columns.STAGE_EMPLOYEE_URI, rowId);
          getContext().getContentResolver().notifyChange(noteUri, null);
          return noteUri;

        }
      }
      throw new SQLException("Failed to insert row into " + uri);

    case CUSTOMER_INVESTOR:
      if (values != null) {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        long rowId = db.insert(Columns.CUST_INV_TABLE_NAME, null, values);

        if (rowId > 0) {
          Uri noteUri = ContentUris.withAppendedId(Columns.CUST_INV_URI, rowId);
          getContext().getContentResolver().notifyChange(noteUri, null);
          return noteUri;

        }
      }
      throw new SQLException("Failed to insert row into " + uri);

    case STAGE:
      if (values != null) {
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        long rowId = db.insert(Columns.STAGE_TABLE_NAME, null, values);

        if (rowId > 0) {
          Uri noteUri = ContentUris.withAppendedId(Columns.STAGE_URI, rowId);
          getContext().getContentResolver().notifyChange(noteUri, null);
          return noteUri;

        }
      }
      throw new SQLException("Failed to insert row into " + uri);

    default:
      throw new IllegalArgumentException("Unknown URI " + uri);
    }*/
   return null;
  }

  @Override
  public boolean onCreate() {
    mDataBaseHelper = new PMDBSQLiteHelper(getContext());
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
                      String[] selectionArgs, String sortOrder) {

    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

    /*switch (mUriMatcher.match(uri)) {
    case PROJECT:
      qb.setTables(uri.getLastPathSegment());
      qb.setProjectionMap(projectProjectionMap);
      sortOrder = Columns.SORT_ORDER_PROJECT;
      break;
    case PROJECT_ID:
      qb.setTables(uri.getPathSegments().get(uri.getPathSegments().size() - 2));
      qb.setProjectionMap(projectProjectionMap);
      qb.appendWhere(Columns._ID + "=?");
      selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
          selectionArgs, new String[]{uri.getLastPathSegment()});
      break;

    case EMPLOYEE:
      qb.setTables(Columns.EMPLOYEE_TABLE_NAME);
      qb.setProjectionMap(personProjectionMap);
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
          selectionArgs, new String[]{uri.getLastPathSegment()});
      break;


    case EMPLOYEE_ID:
      qb.setTables(Columns.EMPLOYEE_TABLE_NAME);
      qb.setProjectionMap(personProjectionMap);
      qb.appendWhere(Columns.COLUMN_NAME_ID + "=?");
      selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
          selectionArgs, new String[]{uri.getLastPathSegment()});
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
          selectionArgs, new String[]{uri.getLastPathSegment()});
      break;
    case STAGE_EMPLOYEE:
      qb.setTables(Columns.STAGE_EMPLOYEE_TABLE_NAME);
      qb.setProjectionMap(personProjectionMap);
      if (sortOrder == null) {
        sortOrder = Columns.SORT_ORDER_ASC;
      }
      break;

    case STAGE_EMPLOYEE_ID:
      qb.setTables(Columns.STAGE_EMPLOYEE_TABLE_NAME);
      qb.setProjectionMap(personProjectionMap);
      qb.appendWhere(Columns.COLUMN_NAME_ID + "=?");
      selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
          selectionArgs, new String[]{uri.getLastPathSegment()});
      break;
		*//*case PROJECT_VIEW:

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
			break;*//*
			*//*case STAGE:
			qb.setTables(Columns.STAGE);
			qb.setProjectionMap(stageProjectionMap);
			if (sortOrder == null)
				sortOrder = Columns.SORT_ORDER_ASC;
			break;

		case STAGE_ID:
			qb.setTables(Columns.STAGE);
			qb.setProjectionMap(stageProjectionMap);
			qb.appendWhere(Columns.COLUMN_NAME_ID + "=?");
			selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
					selectionArgs, new String[] {uri.getLastPathSegment()});
			break;*//*
    default:
      throw new IllegalArgumentException("Unknown URI " + uri);
    }

    SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
    Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

    cursor.setNotificationUri(getContext().getContentResolver(), uri);*/
    Cursor cursor = null;
    return cursor;

  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
                    String[] selectionArgs) {

    SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
    int count = 0;
    /*String finalWhere;

    switch (mUriMatcher.match(uri)) {
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
*/
    return count;
  }

}

