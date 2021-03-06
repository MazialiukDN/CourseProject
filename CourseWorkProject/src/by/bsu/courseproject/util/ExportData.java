package by.bsu.courseproject.util;

import android.content.Context;
import android.database.Cursor;
import android.util.Base64;
import android.widget.Toast;
import by.bsu.courseproject.db.DBConstants.Columns;
import by.bsu.courseproject.db.ProjectManagerProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static by.bsu.courseproject.util.ImportExportUtil.*;

class ExportData {

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
                    writePersons(out, cursor);
                    out.write("\r\n".getBytes());

                    out.write("Projects\r\n".getBytes());
                    cursor = context.getContentResolver().query(ProjectManagerProvider.PROJECT_URI, null, null, null, Columns._ID + " ASC");
                    writeProjects(out, cursor);
                    out.write("\r\n".getBytes());

                    out.write("Stages\r\n".getBytes());
                    cursor = context.getContentResolver().query(ProjectManagerProvider.STAGE_URI, null, null, null, Columns._ID + " ASC");
                    writeStages(out, cursor);
                    out.write("\r\n".getBytes());

                    out.write("Stage-Person\r\n".getBytes());
                    cursor = context.getContentResolver().query(ProjectManagerProvider.STAGE_EMPLOYEE_URI, null, null, null, Columns._ID + " ASC");
                    writeStageEmployee(out, cursor);
                    out.write("\r\n".getBytes());

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

    private static void writeStages(FileOutputStream out, Cursor cursor) {
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    out.write((cursor.getLong(cursor.getColumnIndex(Columns._ID)) + "").getBytes());
                    out.write(" | ".getBytes());
                    out.write((cursor.getLong(cursor.getColumnIndex(Columns.STAGE_TYPE)) + "").getBytes());
                    out.write(" | ".getBytes());
                    out.write((cursor.getLong(cursor.getColumnIndex(Columns.STAGE_PROJECT_ID)) + "").getBytes());
                    out.write(" | ".getBytes());
                    out.write((cursor.getLong(cursor.getColumnIndex(Columns.STAGE_MANAGER)) + "").getBytes());
                    out.write("\r\n".getBytes());
                } while (cursor.moveToNext());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeStageEmployee(FileOutputStream out, Cursor cursor) {
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    out.write((cursor.getLong(cursor.getColumnIndex(Columns._ID)) + "").getBytes());
                    out.write(" | ".getBytes());
                    out.write((cursor.getLong(cursor.getColumnIndex(Columns.STAGE_ID)) + "").getBytes());
                    out.write(" | ".getBytes());
                    out.write((cursor.getLong(cursor.getColumnIndex(Columns.EMPLOYEE_ID)) + "").getBytes());
                    out.write("\r\n".getBytes());
                } while (cursor.moveToNext());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeProjects(FileOutputStream out, Cursor cursor) {
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    out.write((cursor.getLong(cursor.getColumnIndex(Columns._ID)) + "").getBytes());
                    out.write(" | ".getBytes());
                    out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PROJECT_PROJECTNAME))));
                    out.write(" | ".getBytes());
                    String projectDescription = cursor.getString(cursor.getColumnIndex(Columns.PROJECT_DESCRIPTION));
                    byte[] bytes;
                    if (projectDescription != null && !projectDescription.isEmpty()){
                        bytes = Base64.encode(projectDescription.getBytes(),Base64.NO_WRAP);
                    }else {
                        bytes = NULL_VALUE.getBytes();
                    }
                    out.write(bytes);
                    out.write(" | ".getBytes());
                    out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PROJECT_PROJECTDUEDATE))));
                    out.write(" | ".getBytes());
                    out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PROJECT_CATEGORY))));
                    out.write(" | ".getBytes());
                    out.write((cursor.getInt(cursor.getColumnIndex(Columns.PROJECT_STATUS)) + "").getBytes());
                    out.write(" | ".getBytes());

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

    private static void writePersons(FileOutputStream out, Cursor cursor) {
        try {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    out.write((cursor.getLong(cursor.getColumnIndex(Columns._ID)) + "").getBytes());
                    out.write(" | ".getBytes());
                    out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_DISCRIMINATOR))));
                    out.write(" | ".getBytes());
                    out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_LASTNAME))));
                    out.write(" | ".getBytes());
                    out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_FIRSTNAME))));
                    out.write(" | ".getBytes());
                    out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_MIDDLENAME))));
                    out.write(" | ".getBytes());
                    out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_EMAIL))));
                    out.write(" | ".getBytes());
                    out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_SKYPE))));
                    out.write(" | ".getBytes());
                    out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_PHONE))));
                    out.write(" | ".getBytes());
                    out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.PERSON_BIRTHDAY))));
                    out.write(" | ".getBytes());
                    String personInfo = cursor.getString(cursor.getColumnIndex(Columns.PERSON_INFO));
                    byte[] bytes;
                    if (personInfo != null && !personInfo.isEmpty()){
                        bytes = Base64.encode(personInfo.getBytes(),Base64.NO_WRAP);
                    }else {
                        bytes = NULL_VALUE.getBytes();
                    }
                    out.write(bytes);
                    out.write(" | ".getBytes());
                    out.write(getData(cursor.getString(cursor.getColumnIndex(Columns.CUSTOMER_INVESTOR_COMPANY))));
                    out.write(" | ".getBytes());
                    String experience = cursor.getString(cursor.getColumnIndex(Columns.EMPLOYEE_EXPERIENCE));
                    byte[] expBytes;
                    if (experience != null && !experience.isEmpty()){
                        expBytes = Base64.encode(experience.getBytes(),Base64.NO_WRAP);
                    }else {
                        expBytes = NULL_VALUE.getBytes();
                    }
                    out.write(expBytes);
                    out.write(" | ".getBytes());
                    String education = cursor.getString(cursor.getColumnIndex(Columns.EMPLOYEE_EDUCATION));
                    byte[] edBytes;
                    if (education != null && !education.isEmpty()){
                        edBytes = Base64.encode(education.getBytes(),Base64.NO_WRAP);
                    }else {
                        edBytes = NULL_VALUE.getBytes();
                    }
                    out.write(edBytes);
                    out.write("\r\n".getBytes());
                } while (cursor.moveToNext());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
