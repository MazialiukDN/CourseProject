package by.bsu.courseproject.datasources;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import by.bsu.courseproject.model.Customer;
import by.bsu.courseproject.model.Investor;
import by.bsu.courseproject.model.Project;
import by.bsu.courseproject.project.ProjectCategory;
import by.bsu.courseproject.project.ProjectPriority;
import by.bsu.courseproject.project.ProjectStatus;
import by.bsu.courseproject.util.DateUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static by.bsu.courseproject.db.DBConstants.Columns;
import static by.bsu.courseproject.db.DBConstants.Tables;


/**
 * User: Artyom Strok
 * Date: 17.02.13
 * Time: 19:19
 */
public class ProjectDataSource {
  private PersonDataSource personDataSource;
  private StageDataSource stageDataSource;
  private final HashMap<String, String> projectProjectionMap = new HashMap<String, String>();

  {
    projectProjectionMap.put(Columns._ID, Columns._ID);
    projectProjectionMap.put(Columns.PROJECT_PROJECTNAME, Columns.PROJECT_PROJECTNAME);
    projectProjectionMap.put(Columns.PROJECT_PROJECTDUEDATE, Columns.PROJECT_PROJECTDUEDATE);
    projectProjectionMap.put(Columns.PROJECT_CATEGORY, Columns.PROJECT_CATEGORY);
    projectProjectionMap.put(Columns.PROJECT_STATUS, Columns.PROJECT_STATUS);
    projectProjectionMap.put(Columns.PROJECT_PRIORITY, Columns.PROJECT_PRIORITY);
    projectProjectionMap.put(Columns.PROJECT_CUSTOMER_ID, Columns.PROJECT_CUSTOMER_ID);
    projectProjectionMap.put(Columns.PROJECT_INVESTOR_ID, Columns.PROJECT_INVESTOR_ID);
  }

  private SQLiteDatabase db;

  public ProjectDataSource(SQLiteDatabase db) {
    this.db = db;
  }

  private Project cursorToProject(Cursor cursor) {
    Project project = new Project();
    project.setId(cursor.getLong(cursor.getColumnIndex(Columns._ID)));
    project.setName(cursor.getString(cursor.getColumnIndex(Columns.PROJECT_PROJECTNAME)));
    String date = cursor.getString(cursor.getColumnIndex(Columns.PROJECT_PROJECTDUEDATE));
    project.setDueDate(DateUtil.stringToDate(date));
    project.setCategory(ProjectCategory.valueOf(cursor.getString(cursor.getColumnIndex(Columns.PROJECT_CATEGORY))));
    project.setStatus(ProjectStatus.valueOf(cursor.getString(cursor.getColumnIndex(Columns.PROJECT_STATUS))));
    project.setPriority(ProjectPriority.valueOf(cursor.getString(cursor.getColumnIndex(Columns.PROJECT_PRIORITY))));
    Long customerId = cursor.getLong(cursor.getColumnIndex(Columns.PROJECT_CUSTOMER_ID));
    if (customerId != null) {
      project.setCustomer((Customer) personDataSource.load(customerId));
    }
    Long investorId = cursor.getLong(cursor.getColumnIndex(Columns.PROJECT_INVESTOR_ID));
    if (investorId != null) {
      project.setInvestor((Investor) personDataSource.load(investorId));
    }
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
    values.put(Columns.PROJECT_INVESTOR_ID, project.getInvestor().getId());

    long insertId = db.insert(Tables.PROJECT, null,
                              values);
    project.setId(insertId);
  }

  public Project load(Long id) {
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
    queryBuilder.setProjectionMap(projectProjectionMap);
    queryBuilder.setTables(Tables.PROJECT);
    queryBuilder.appendWhere(Columns._ID + " = " + id);
    Cursor cursor = queryBuilder.query(db, null, null, null, null, null, null);
    cursor.moveToFirst();
    //TODO:add validation
    return cursorToProject(cursor);
  }

  public void updateProject(Project project) {
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
                             projectProjectionMap.keySet().toArray(new String[0]), null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Project Project = cursorToProject(cursor);
      Projects.add(Project);
      cursor.moveToNext();
    }
    cursor.close();
    return Projects;
  }

  public void setPersonDataSource(PersonDataSource personDataSource) {
    this.personDataSource = personDataSource;
  }

  public void setStageDataSource(StageDataSource stageDataSource) {
    this.stageDataSource = stageDataSource;
  }
}
