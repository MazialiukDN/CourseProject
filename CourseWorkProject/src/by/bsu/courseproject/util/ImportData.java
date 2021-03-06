package by.bsu.courseproject.util;

import android.content.ContentValues;
import android.content.Context;
import android.util.Base64;
import android.widget.Toast;
import by.bsu.courseproject.db.ProjectManagerProvider;

import java.io.*;
import java.util.StringTokenizer;

import static by.bsu.courseproject.db.DBConstants.Columns;
import static by.bsu.courseproject.util.ImportExportUtil.*;

/**
 * User: Artyom Strok
 * Date: 08.07.13
 * Time: 12:23
 */
class ImportData {

  private static final String DELIMITER = "|";

  public static boolean importData(Context context) {
    if (isExternalStorageReadable()) {
      File dirPath = getStorageDir();
      if (dirPath != null) {
        File file = new File(dirPath, "Portfolio.txt");
        if (file.exists()) {
          try {
            FileReader fileReader = new FileReader(file);
            BufferedReader reader = new BufferedReader(fileReader);
            deleteAllData(context);
            writePersons(context, reader);
            writeProjects(context, reader);
            writeStages(context, reader);
            writeStagePerson(context, reader);
            reader.close();
            fileReader.close();
            return true;
          } catch (FileNotFoundException e) {
            Toast.makeText(context, "File is not found", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }

        }
      }
    }
    return false;
  }

  private static void deleteAllData(Context context) {
    context.getContentResolver().delete(ProjectManagerProvider.PERSON_URI, null, null);
    context.getContentResolver().delete(ProjectManagerProvider.PROJECT_URI, null, null);
    context.getContentResolver().delete(ProjectManagerProvider.STAGE_URI, null, null);
    context.getContentResolver().delete(ProjectManagerProvider.STAGE_EMPLOYEE_URI, null, null);
  }

  private static ContentValues stringToPerson(String line) {
    StringTokenizer tokenizer = new StringTokenizer(line, DELIMITER);
    ContentValues values = new ContentValues();
    values.put(Columns._ID, Long.valueOf(tokenizer.nextToken().trim()));
    values.put(Columns.PERSON_DISCRIMINATOR, tokenizer.nextToken().trim());
    values.put(Columns.PERSON_LASTNAME, tokenizer.nextToken().trim());
    values.put(Columns.PERSON_FIRSTNAME, tokenizer.nextToken().trim());
    values.put(Columns.PERSON_MIDDLENAME, tokenizer.nextToken().trim());
    values.put(Columns.PERSON_EMAIL, tokenizer.nextToken().trim());
    values.put(Columns.PERSON_SKYPE, tokenizer.nextToken().trim());
    values.put(Columns.PERSON_PHONE, tokenizer.nextToken().trim());
    values.put(Columns.PERSON_BIRTHDAY, tokenizer.nextToken().trim());
      String personInfo = tokenizer.nextToken().trim();
      if (!personInfo.equals(NULL_VALUE)){
          personInfo = new String(Base64.decode(personInfo.getBytes(), Base64.NO_WRAP));
          values.put(Columns.PERSON_INFO, personInfo);
      }
    values.put(Columns.CUSTOMER_INVESTOR_COMPANY, tokenizer.nextToken().trim());
      String experience = tokenizer.nextToken().trim();
      if (!experience.equals(NULL_VALUE)){
          experience = new String(Base64.decode(experience.getBytes(), Base64.NO_WRAP));
          values.put(Columns.EMPLOYEE_EXPERIENCE, experience);
      }
      String education = tokenizer.nextToken().trim();
      if (!education.equals(NULL_VALUE)){
          education = new String(Base64.decode(education.getBytes(), Base64.NO_WRAP));
          values.put(Columns.EMPLOYEE_EDUCATION, education);
      }
    return values;
  }

  private static void writePersons(Context context, BufferedReader reader) {
    String line;
    try {
      while (!(line = reader.readLine()).isEmpty()) {
        if (line.equals("Persons")) {
          continue;
        }
        ContentValues values = stringToPerson(line);
        context.getContentResolver().insert(ProjectManagerProvider.PERSON_URI, values);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private static ContentValues stringToProject(String line) {
    StringTokenizer tokenizer = new StringTokenizer(line, DELIMITER);
    ContentValues values = new ContentValues();
    values.put(Columns._ID, Long.valueOf(tokenizer.nextToken().trim()));
    values.put(Columns.PROJECT_PROJECTNAME, tokenizer.nextToken().trim());
    String projectDescription = tokenizer.nextToken().trim();
    if (!projectDescription.equals(NULL_VALUE)){
      projectDescription = new String(Base64.decode(projectDescription.getBytes(), Base64.NO_WRAP));
      values.put(Columns.PROJECT_DESCRIPTION, projectDescription);
    }
    values.put(Columns.PROJECT_PROJECTDUEDATE, tokenizer.nextToken().trim());
    values.put(Columns.PROJECT_CATEGORY, tokenizer.nextToken().trim());
    values.put(Columns.PROJECT_STATUS, Integer.valueOf(tokenizer.nextToken().trim()));
    values.put(Columns.PROJECT_PRIORITY, Integer.valueOf(tokenizer.nextToken().trim()));
    String customer = tokenizer.nextToken().trim();
    if (!customer.equals(NULL_VALUE)) {
      values.put(Columns.PROJECT_CUSTOMER_ID, Long.valueOf(customer));
    }
    String investor = tokenizer.nextToken().trim();
    if (!investor.equals(NULL_VALUE)) {
      values.put(Columns.PROJECT_INVESTOR_ID, Long.valueOf(investor));
    }
    return values;
  }

  private static void writeProjects(Context context, BufferedReader reader) {
    String line;
    try {
      while (!(line = reader.readLine()).isEmpty()) {
        if (line.equals("Projects")) {
          continue;
        }
        ContentValues values = stringToProject(line);
        context.getContentResolver().insert(ProjectManagerProvider.PROJECT_URI, values);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static ContentValues stringToStage(String line) {
    StringTokenizer tokenizer = new StringTokenizer(line, DELIMITER);
    ContentValues values = new ContentValues();
    values.put(Columns._ID, Long.valueOf(tokenizer.nextToken().trim()));
    values.put(Columns.STAGE_TYPE, Integer.valueOf(tokenizer.nextToken().trim()));
    values.put(Columns.STAGE_PROJECT_ID, Long.valueOf(tokenizer.nextToken().trim()));
    values.put(Columns.STAGE_MANAGER, Long.valueOf(tokenizer.nextToken().trim()));
    return values;
  }


  private static void writeStages(Context context, BufferedReader reader) {
    String line;
    try {
      while (!(line = reader.readLine()).isEmpty()) {
        if (line.equals("Stages")) {
          continue;
        }
        ContentValues values = stringToStage(line);
        context.getContentResolver().insert(ProjectManagerProvider.STAGE_URI, values);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static ContentValues stringToStageEmployee(String line) {
    StringTokenizer tokenizer = new StringTokenizer(line, DELIMITER);
    ContentValues values = new ContentValues();
    values.put(Columns._ID, Long.valueOf(tokenizer.nextToken().trim()));
    values.put(Columns.STAGE_ID, Long.valueOf(tokenizer.nextToken().trim()));
    values.put(Columns.EMPLOYEE_ID, Long.valueOf(tokenizer.nextToken().trim()));
    return values;
  }

  private static void writeStagePerson(Context context, BufferedReader reader) {
    String line;
    try {
      while (!(line = reader.readLine()).isEmpty()) {
        if (line.equals("Stage-Person")) {
          continue;
        }
        ContentValues values = stringToStageEmployee(line);
        context.getContentResolver().insert(ProjectManagerProvider.STAGE_EMPLOYEE_URI, values);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
