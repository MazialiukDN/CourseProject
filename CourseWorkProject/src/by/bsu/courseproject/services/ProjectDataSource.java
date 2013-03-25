package by.bsu.courseproject.services;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import by.bsu.courseproject.db.PMDBSQLiteHelper;
import by.bsu.courseproject.model.Project;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static by.bsu.courseproject.db.DBConstants.Columns;
import static by.bsu.courseproject.db.DBConstants.Tables;


/**
 * User: Artyom Strok
 * Date: 17.02.13
 * Time: 19:19
 */
public class ProjectDataSource {
  private SQLiteDatabase database;
  private PMDBSQLiteHelper dbHelper;
  private String[] allColumns = {Columns._ID, Columns.PROJECT_PROJECTNAME, Columns.PROJECT_PROJECTDUEDATE, Columns.PROJECT_CATEGORY,
                                 Columns.PROJECT_STATUS, Columns.PROJECT_PRIORITY, Columns.PROJECT_CUSTOMER_ID,
                                 Columns.PROJECT_INVESTOR_ID};

  public ProjectDataSource(Context context) {
    dbHelper = new PMDBSQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  private Project cursorToProject(Cursor cursor){
    Project project = new Project();
/*    project.setId();
    project.setName(cursor.getString());
    project.setDueDate();
    project.setCategory();
    project.setStatus();
    project.setPriority();
    project.setCustomer();
    project.setInverstor();*/
    return project;
  }

  public void persistProject(Project project) {
    ContentValues values = new ContentValues();
    values.put(Columns.PROJECT_PROJECTNAME, project.getName());
    long insertId = database.insert(Tables.PROJECT, null,
                                    values);
    project.setId(insertId);
  }

  public void updateProject(Project project){
    ContentValues values = new ContentValues();
    long id = project.getId();
   database.update(Tables.PROJECT,values,Columns._ID + " = " + id, null);
  }

  public void deleteProject(Project project) {
    long id = project.getId();
    System.out.println("Project deleted with id: " + id);
    //TODO: delete stages and stage_employee
    database.delete(Tables.PROJECT, Columns._ID + " = " + id, null);
  }

  public List<Project> getAllProjects() {
    List<Project> Projects = new ArrayList<Project>();

    Cursor cursor = database.query(Tables.PROJECT,
                                   allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Project Project = cursorToProject(cursor);
      Projects.add(Project);
      cursor.moveToNext();
    }
    cursor.close();
    return Projects;
  }

}
