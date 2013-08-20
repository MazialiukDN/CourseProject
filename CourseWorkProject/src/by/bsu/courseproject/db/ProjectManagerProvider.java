package by.bsu.courseproject.db;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.v4.database.DatabaseUtilsCompat;
import by.bsu.courseproject.PMApplication;
import by.bsu.courseproject.db.DBConstants.Columns;
import by.bsu.courseproject.db.DBConstants.Tables;

import java.util.HashMap;

public class ProjectManagerProvider extends ContentProvider {

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

  private static final int AUTHORIZATION = 14;
  private static final int AUTHORIZATION_ID = 15;

  // Uri matcher to decode incoming URIs.
  private final UriMatcher mUriMatcher;

  private final HashMap<String, String> projectProjectionMap;
  private final HashMap<String, String> personProjectionMap;
  private final HashMap<String, String> stageProjectionMap;
  private final HashMap<String, String> stageEmployeenProjectionMap;
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
    projectProjectionMap.put(Columns.PROJECT_DESCRIPTION, Columns.PROJECT_DESCRIPTION);
    projectProjectionMap.put(Columns.PROJECT_PROJECTDUEDATE, Columns.PROJECT_PROJECTDUEDATE);
    projectProjectionMap.put(Columns.PROJECT_CATEGORY, Columns.PROJECT_CATEGORY);
    projectProjectionMap.put(Columns.PROJECT_STATUS, Columns.PROJECT_STATUS);
    projectProjectionMap.put(Columns.PROJECT_PRIORITY, Columns.PROJECT_PRIORITY);
    projectProjectionMap.put(Columns.PROJECT_INVESTOR_ID, Columns.PROJECT_INVESTOR_ID);
    projectProjectionMap.put(Columns.PROJECT_CUSTOMER_ID, Columns.PROJECT_CUSTOMER_ID);

    personProjectionMap = new HashMap<String, String>();
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

    stageProjectionMap = new HashMap<String, String>();
    stageProjectionMap.put(Columns._ID, Columns._ID);
    stageProjectionMap.put(Columns.STAGE_TYPE, Columns.STAGE_TYPE);
    stageProjectionMap.put(Columns.STAGE_PROJECT_ID, Columns.STAGE_PROJECT_ID);
    stageProjectionMap.put(Columns.STAGE_MANAGER, Columns.STAGE_MANAGER);

    stageEmployeenProjectionMap = new HashMap<String, String>();
    stageEmployeenProjectionMap.put(Columns._ID, Columns._ID);
    stageEmployeenProjectionMap.put(Columns.STAGE_ID, Columns.STAGE_ID);
    stageEmployeenProjectionMap.put(Columns.EMPLOYEE_ID, Columns.EMPLOYEE_ID);

  }


  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {

    SQLiteDatabase db = PMApplication.getPMDB().getDB();
    String finalWhere;
    int count;

    switch (mUriMatcher.match(uri)) {
    case PROJECT:
      count = db.delete(Tables.PROJECT, selection, selectionArgs);
      break;

    case PROJECT_ID:

      finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns._ID
                                                        + " = " + ContentUris.parseId(uri), null);

      count = db.delete(Tables.PROJECT, finalWhere, selectionArgs);

      break;
    case EMPLOYEE:
      count = db.delete(Tables.PERSON, selection, selectionArgs);
      break;
    case EMPLOYEE_ID:
      finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns._ID
                                                        + " = " + ContentUris.parseId(uri), null);

      count = db.delete(Tables.PERSON, finalWhere, selectionArgs);

      break;
    case STAGE_EMPLOYEE:
      count = db.delete(Tables.STAGE_EMPLOYEE, selection, selectionArgs);
      break;
    case STAGE_EMPLOYEE_ID:
      finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns._ID
                                                        + " = " + ContentUris.parseId(uri), selection);
      count = db.delete(Tables.STAGE_EMPLOYEE, finalWhere, selectionArgs);
      break;
    case STAGE:
      count = db.delete(Tables.STAGE, selection, selectionArgs);
      break;
    case STAGE_ID:
      finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns._ID
                                                        + " = " + ContentUris.parseId(uri), selection);
      count = db.delete(Tables.STAGE, finalWhere, selectionArgs);
      break;

    case CUSTOMER_INVESTOR:
      count = db.delete(Tables.PERSON, selection, selectionArgs);
      break;
    case CUSTOMER_INVESTOR_ID:
      finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns._ID
                                                        + " = " + ContentUris.parseId(uri), selection);
      count = db.delete(Tables.PERSON, finalWhere, selectionArgs);
      break;

    default:
      throw new IllegalArgumentException("Unsupported URI: " + uri);
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
    SQLiteDatabase db = PMApplication.getPMDB().getDB();
    switch (mUriMatcher.match(uri)) {
    case PROJECT:
      if (values != null) {
        long rowId = db.insert(Tables.PROJECT, null, values);

        if (rowId > 0) {
          Uri noteUri = ContentUris.withAppendedId(PROJECT_URI, rowId);
          getContext().getContentResolver().notifyChange(noteUri, null);
          return noteUri;

        }
      }
      throw new SQLException("Failed to insert row into " + uri);
    case EMPLOYEE:
      if (values != null) {
        long rowId = db.insert(Tables.PERSON, null, values);

        if (rowId > 0) {
          Uri noteUri = ContentUris.withAppendedId(PERSON_URI, rowId);
          getContext().getContentResolver().notifyChange(noteUri, null);
          return noteUri;

        }
      }
      throw new SQLException("Failed to insert row into " + uri);
    case AUTHORIZATION:
      if (values != null) {
        long rowId = db.insert(Tables.AUTHORIZATION, null, values);

        if (rowId > 0) {
          Uri noteUri = ContentUris.withAppendedId(AUTHORIZATION_URI, rowId);
          getContext().getContentResolver().notifyChange(noteUri, null);
          return noteUri;

        }
      }
      throw new SQLException("Failed to insert row into " + uri);

    case STAGE_EMPLOYEE:
      if (values != null) {
        long rowId = db.insert(Tables.STAGE_EMPLOYEE, null, values);

        if (rowId > 0) {
          Uri noteUri = ContentUris.withAppendedId(STAGE_EMPLOYEE_URI, rowId);
          getContext().getContentResolver().notifyChange(noteUri, null);
          return noteUri;

        }
      }
      throw new SQLException("Failed to insert row into " + uri);

    case CUSTOMER_INVESTOR:
      if (values != null) {
        long rowId = db.insert(Tables.PERSON, null, values);

        if (rowId > 0) {
          Uri noteUri = ContentUris.withAppendedId(PERSON_URI, rowId);
          getContext().getContentResolver().notifyChange(noteUri, null);
          return noteUri;

        }
      }
      throw new SQLException("Failed to insert row into " + uri);

    case STAGE:
      if (values != null) {
        long rowId = db.insert(Tables.STAGE, null, values);

        if (rowId > 0) {
          Uri noteUri = ContentUris.withAppendedId(STAGE_URI, rowId);
          getContext().getContentResolver().notifyChange(noteUri, null);
          return noteUri;

        }
      }
      throw new SQLException("Failed to insert row into " + uri);

    default:
      throw new IllegalArgumentException("Unknown URI " + uri);
    }

  }

  @Override
  public boolean onCreate() {
    return true;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
                      String[] selectionArgs, String sortOrder) {

    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

    switch (mUriMatcher.match(uri)) {
    case PROJECT:
      qb.setTables(uri.getLastPathSegment());
      qb.setProjectionMap(projectProjectionMap);
      if (sortOrder == null) {
        sortOrder = Columns.SORT_ORDER_PROJECT;
      }
      break;
    case PROJECT_ID:
      qb.setTables(uri.getPathSegments().get(uri.getPathSegments().size() - 2));
      qb.setProjectionMap(projectProjectionMap);
      qb.appendWhere(Columns._ID + "=?");
      selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
          selectionArgs, new String[]{uri.getLastPathSegment()});
      break;

    case EMPLOYEE:
      qb.setTables(Tables.PERSON);
      qb.setProjectionMap(personProjectionMap);
      if (sortOrder == null) {
        sortOrder = Columns.SORT_ORDER_EMP;
      }
      break;

    case AUTHORIZATION:
      qb.setTables(Tables.AUTHORIZATION);
      qb.setProjectionMap(authorizationProjectionMap);
      sortOrder = Columns.SORT_ORDER_ASC;
      break;

    case AUTHORIZATION_ID:
      qb.setTables(Tables.AUTHORIZATION);
      qb.setProjectionMap(authorizationProjectionMap);
      qb.appendWhere(Columns._ID + "=?");
      selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
          selectionArgs, new String[]{uri.getLastPathSegment()});
      break;


    case EMPLOYEE_ID:
      qb.setTables(Tables.PERSON);
      qb.setProjectionMap(personProjectionMap);
      qb.appendWhere(Columns._ID + "=?");
      selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
          selectionArgs, new String[]{uri.getLastPathSegment()});
      break;

    case CUSTOMER_INVESTOR:
      qb.setTables(Tables.PERSON);
      qb.setProjectionMap(personProjectionMap);
      if (sortOrder == null) {
        sortOrder = Columns.SORT_ORDER_EMP;
      }
      break;

    case CUSTOMER_INVESTOR_ID:
      qb.setTables(Tables.PERSON);
      qb.setProjectionMap(personProjectionMap);
      qb.appendWhere(Columns._ID + "=?");
      selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
          selectionArgs, new String[]{uri.getLastPathSegment()});
      break;
    case STAGE:
      qb.setTables(Tables.STAGE);
      qb.setProjectionMap(stageProjectionMap);
      if (sortOrder == null) {
        sortOrder = Columns.SORT_ORDER_ASC;
      }
      break;

    case STAGE_ID:
      qb.setTables(Tables.STAGE);
      qb.setProjectionMap(stageProjectionMap);
      qb.appendWhere(Columns._ID + "=?");
      selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
          selectionArgs, new String[]{uri.getLastPathSegment()});
      break;
    case STAGE_EMPLOYEE:
      qb.setTables(Tables.STAGE_EMPLOYEE);
      qb.setProjectionMap(stageEmployeenProjectionMap);
      if (sortOrder == null) {
        sortOrder = Columns.SORT_ORDER_ASC;
      }
      break;

    case STAGE_EMPLOYEE_ID:
      qb.setTables(Tables.STAGE_EMPLOYEE);
      qb.setProjectionMap(stageEmployeenProjectionMap);
      qb.appendWhere(Columns._ID + "=?");
      selectionArgs = DatabaseUtilsCompat.appendSelectionArgs(
          selectionArgs, new String[]{uri.getLastPathSegment()});
      break;
    default:
      throw new IllegalArgumentException("Unknown URI " + uri);
    }

    SQLiteDatabase db = PMApplication.getPMDB().getDB();
    Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

    cursor.setNotificationUri(getContext().getContentResolver(), uri);
    return cursor;

  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
                    String[] selectionArgs) {

    SQLiteDatabase db = PMApplication.getPMDB().getDB();
    int count;
    String finalWhere;

    switch (mUriMatcher.match(uri)) {
    case PROJECT:
      count = db.update(Tables.PROJECT, values, selection, selectionArgs);
      break;
    case PROJECT_ID:
      finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns._ID
                                                        + " = " + ContentUris.parseId(uri), selection);
      count = db.update(Tables.PROJECT, values, finalWhere, selectionArgs);

      break;
    case EMPLOYEE:
      count = db.update(Tables.PERSON, values, selection, selectionArgs);
      break;
    case EMPLOYEE_ID:
      finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns._ID
                                                        + " = " + ContentUris.parseId(uri), selection);
      count = db.update(Tables.PERSON, values, finalWhere, selectionArgs);

      break;

    case CUSTOMER_INVESTOR:
      count = db.update(Tables.PERSON, values, selection, selectionArgs);
      break;
    case CUSTOMER_INVESTOR_ID:
      finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns._ID
                                                        + " = " + ContentUris.parseId(uri), selection);
      count = db.update(Tables.PERSON, values, finalWhere, selectionArgs);

      break;

    case STAGE_EMPLOYEE:
      count = db.update(Tables.STAGE_EMPLOYEE, values, selection, selectionArgs);
      break;
    case STAGE_EMPLOYEE_ID:
      finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns._ID
                                                        + " = " + ContentUris.parseId(uri), selection);
      count = db.update(Tables.STAGE_EMPLOYEE, values, finalWhere, selectionArgs);

      break;
    case STAGE:
      count = db.update(Tables.STAGE, values, selection, selectionArgs);
      break;
    case STAGE_ID:
      finalWhere = DatabaseUtilsCompat.concatenateWhere(Columns._ID
                                                        + " = " + ContentUris.parseId(uri), selection);
      count = db.update(Tables.STAGE, values, finalWhere, selectionArgs);

      break;
    default:
      throw new IllegalArgumentException("Unknown URI " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);

    return count;
  }

}