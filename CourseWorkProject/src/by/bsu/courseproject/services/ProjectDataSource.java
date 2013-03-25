package by.bsu.courseproject.services;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import by.bsu.courseproject.DateUtil;
import by.bsu.courseproject.model.Project;
import by.bsu.courseproject.project.ProjectCategory;
import by.bsu.courseproject.project.ProjectPriority;
import by.bsu.courseproject.project.ProjectStatus;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static by.bsu.courseproject.db.DBConstants.Columns;
import static by.bsu.courseproject.db.DBConstants.Tables;


/**
 * User: Artyom Strok
 * Date: 17.02.13
 * Time: 19:19
 */
public class ProjectDataSource {
  private String[] allColumns = {Columns._ID, Columns.PROJECT_PROJECTNAME, Columns.PROJECT_PROJECTDUEDATE, Columns.PROJECT_CATEGORY,
                                 Columns.PROJECT_STATUS, Columns.PROJECT_PRIORITY, Columns.PROJECT_CUSTOMER_ID,
                                 Columns.PROJECT_INVESTOR_ID};
  private SQLiteDatabase db;

  public ProjectDataSource(SQLiteDatabase db) {
    this.db = db;
  }

  private Project cursorToProject(Cursor cursor){
    Project project = new Project();
    project.setId(cursor.getLong(cursor.getColumnIndex(Columns._ID)));
    project.setName(cursor.getString(cursor.getColumnIndex(Columns.PROJECT_PROJECTNAME)));
    String date = cursor.getString(cursor.getColumnIndex(Columns.PROJECT_PROJECTDUEDATE));
    project.setDueDate(DateUtil.stringToDate(date));
    project.setCategory(ProjectCategory.valueOf(cursor.getString(cursor.getColumnIndex(Columns.PROJECT_CATEGORY))));
    project.setStatus(ProjectStatus.valueOf(cursor.getString(cursor.getColumnIndex(Columns.PROJECT_STATUS))));
    project.setPriority(ProjectPriority.valueOf(cursor.getString(cursor.getColumnIndex(Columns.PROJECT_PRIORITY))));
    //TODO:use investor and customer datasource for load investor and customer
    /*project.setCustomer();
    project.setInverstor();*/
    return project;
  }

  public void persistProject(Project project) {
    ContentValues values = new ContentValues();
    values.put(Columns.PROJECT_PROJECTNAME, project.getName());
    values.put(Columns.PROJECT_PROJECTDUEDATE, project.getDueDate().toString());
    values.put(Columns.PROJECT_CATEGORY, project.getStatus().toString());
    values.put(Columns.PROJECT_STATUS, project.getStatus().toString());
    values.put(Columns.PROJECT_PRIORITY, project.getPriority().toString());
    values.put(Columns.PROJECT_CUSTOMER_ID, project.getCustomer().getId());
    values.put(Columns.PROJECT_INVESTOR_ID, project.getInverstor().getId());

    long insertId = db.insert(Tables.PROJECT, null,
                              values);
    project.setId(insertId);
  }

  public void updateProject(Project project){
    ContentValues values = new ContentValues();
    long id = project.getId();
    db.update(Tables.PROJECT, values, Columns._ID + " = " + id, null);
  }

  public void deleteProject(Project project) {
    long id = project.getId();
    System.out.println("Project deleted with id: " + id);
    //TODO: delete stages and stage_employee
    db.delete(Tables.PROJECT, Columns._ID + " = " + id, null);
  }

  public List<Project> getAllProjects() {
    List<Project> Projects = new ArrayList<Project>();

    Cursor cursor = db.query(Tables.PROJECT,
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
