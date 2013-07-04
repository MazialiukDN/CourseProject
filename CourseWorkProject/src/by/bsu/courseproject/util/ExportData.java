package by.bsu.courseproject.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import by.bsu.courseproject.db.DBConstants.Columns;
import by.bsu.courseproject.db.ProjectManagerProvider;

public class ExportData {
	
	private final static String NULL_VALUE = "NULL";
	
	public static boolean exportData(Context context) {
		if (isExternalStorageReadable()) {
			File dirPath = getStorageDir();
			if (dirPath != null) {
				File file = new File(dirPath, "Portfolio.txt");
				if (file.exists()) {
					file.delete();
				}
			}
		}
		if (isExternalStorageWritable()) {
			File dirPath = getStorageDir();
			if (dirPath != null) {
				File file = new File(dirPath, "Portfolio.txt");
				FileOutputStream out;
				try {
					out = new FileOutputStream(file);

					out.write("Persons\r\n".getBytes());
					Cursor cursor = context.getContentResolver().query(ProjectManagerProvider.PERSON_URI, null, null, null, Columns._ID + " ASC");
					writePersons(out, cursor, 13);
					out.write("\r\n".getBytes());
					
					out.write("Projects\r\n".getBytes());
					cursor = context.getContentResolver().query(ProjectManagerProvider.PROJECT_URI, null, null, null, Columns._ID + " ASC");
					writeProjects(out, cursor, 8);
					out.write("\r\n".getBytes());
					
					out.write("Stages\r\n".getBytes());
					cursor = context.getContentResolver().query(ProjectManagerProvider.STAGE_URI, null, null, null, Columns._ID + " ASC");
					writeStages(out, cursor, 4);
					out.write("\r\n".getBytes());
					
					out.write("Stage-Person\r\n".getBytes());
					cursor = context.getContentResolver().query(ProjectManagerProvider.STAGE_EMPLOYEE_URI, null, null, null, Columns._ID + " ASC");
					writeStageEmployee(out, cursor, 3);
					
					out.flush();
					out.close();
					return true;
				} catch (FileNotFoundException e) {
					Toast.makeText(context, "File is not found", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Toast.makeText(context, "Directory not created", Toast.LENGTH_SHORT).show();	
			} 
		} else {
			Toast.makeText(context, "External storage is not available", Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	private static void writeStages(FileOutputStream out, Cursor cursor, int count) {
		try {
			if (cursor != null && cursor.moveToFirst()) {
				do {
					  out.write((cursor.getLong(cursor.getColumnIndex(Columns._ID)) + "").getBytes());out.write(" | ".getBytes());
					  out.write((cursor.getLong(cursor.getColumnIndex(Columns.STAGE_TYPE)) + "").getBytes());out.write(" | ".getBytes());
					  out.write((cursor.getLong(cursor.getColumnIndex(Columns.STAGE_PROJECT_ID)) + "").getBytes());out.write(" | ".getBytes());
					  out.write((cursor.getLong(cursor.getColumnIndex(Columns.STAGE_MANAGER)) + "").getBytes());
					  out.write("\r\n".getBytes());
				} while (cursor.moveToNext());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void writeStageEmployee(FileOutputStream out, Cursor cursor, int count) {
		try {
			if (cursor != null && cursor.moveToFirst()) {
				do {
					  out.write((cursor.getLong(cursor.getColumnIndex(Columns._ID)) + "").getBytes());out.write(" | ".getBytes());
					  out.write((cursor.getLong(cursor.getColumnIndex(Columns.STAGE_ID)) + "").getBytes());out.write(" | ".getBytes());
					  out.write((cursor.getLong(cursor.getColumnIndex(Columns.EMPLOYEE_ID)) + "").getBytes());
					  out.write("\r\n".getBytes());
				} while (cursor.moveToNext());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeProjects(FileOutputStream out, Cursor cursor, int count) {
		try {
			if (cursor != null && cursor.moveToFirst()) {
				do {
					  out.write((cursor.getLong(cursor.getColumnIndex(Columns._ID)) + "").getBytes());out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PROJECT_PROJECTNAME))));out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PROJECT_PROJECTDUEDATE))));out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PROJECT_CATEGORY))));out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PROJECT_STATUS))));out.write(" | ".getBytes());
					  
					  if (cursor.isNull(cursor.getColumnIndex(Columns.PROJECT_PRIORITY))) {
						  out.write(NULL_VALUE.getBytes());
					  } else {
						  out.write((cursor.getLong(cursor.getColumnIndex(Columns.PROJECT_PRIORITY)) + "").getBytes());
					  }
					  out.write(" | ".getBytes());
					  
					  if (cursor.isNull(cursor.getColumnIndex(Columns.PROJECT_CUSTOMER_ID))) {
						  out.write(NULL_VALUE.getBytes());
					  } else {
						  out.write((cursor.getLong(cursor.getColumnIndex(Columns.PROJECT_CUSTOMER_ID)) + "").getBytes());
					  }
					  out.write(" | ".getBytes());
					  
					  if (cursor.isNull(cursor.getColumnIndex(Columns.PROJECT_INVESTOR_ID))) {
						  out.write(NULL_VALUE.getBytes());
					  } else {
						  out.write((cursor.getLong(cursor.getColumnIndex(Columns.PROJECT_INVESTOR_ID)) + "").getBytes());
					  }
					  out.write("\r\n".getBytes());
				} while (cursor.moveToNext());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writePersons(FileOutputStream out, Cursor cursor, int count) {
		try {
			if (cursor != null && cursor.moveToFirst()) {
				do {
					  out.write((cursor.getLong(cursor.getColumnIndex(Columns._ID)) + "").getBytes());out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_DISCRIMINATOR))));out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_LASTNAME))));out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_FIRSTNAME))));out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_MIDDLENAME))));out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_EMAIL))));out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_SKYPE))));out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_PHONE))));out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_BIRTHDAY))));out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_INFO))));out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.CUSTOMER_INVESTOR_COMPANY))));out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.EMPLOYEE_EXPERIENCE))));out.write(" | ".getBytes());
					  out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.EMPLOYEE_EDUCATION))));
					  out.write("\r\n".getBytes());
				} while (cursor.moveToNext());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static byte[] getData(String str) {
		 str = str == null ? NULL_VALUE : str;
		 return str.getBytes();
	}
	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	public static File getStorageDir() {
	    // Get the directory for the user's public pictures directory. 
	    File file = new File(Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_DOWNLOADS), "Portfolio");
	    if (!file.mkdirs()) {
	        Log.e("debug", "Directory not created");
	    }
	    return file;
	}
	
}
