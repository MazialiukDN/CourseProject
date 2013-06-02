package by.bsu.courseproject.ui;

import java.util.List;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import by.bsu.courseproject.PMApplication;
import by.bsu.courseproject.R;
import by.bsu.courseproject.db.DBConstants;
import by.bsu.courseproject.db.ProjectManagerProvider;
import by.bsu.courseproject.stage.StageType;

public class Projects extends FragmentActivity implements View.OnClickListener {
  private static final int IDM_NEW_PROJECT = 101;
  private static final int PROJECT_INFO_ID = 200;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.projects);
  }

  @Override
  protected void onResume() {
    super.onResume();
    ((TableLayout) findViewById(R.id.tableProjects)).removeAllViews();
    addHeader();
    List<by.bsu.courseproject.model.Project> projects = PMApplication.getPMDB().getProjectDataSource().getAllProjects();
    for (by.bsu.courseproject.model.Project project : projects) {
      addRow(project);
    }
  }

  ;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(Menu.NONE, IDM_NEW_PROJECT, Menu.NONE,
             R.string.label_new_project);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Intent intent = new Intent();
    switch (item.getItemId()) {
    case IDM_NEW_PROJECT:
      intent.setClass(getApplicationContext(), Project.class);
      break;
    default:
      return false;
    }
    startActivity(intent);
    return true;
  }

  private void addHeader() {
	   TableLayout tableLayout = (TableLayout) findViewById(R.id.tableProjects);
	    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	    TableRow tableRow = (TableRow) inflater.inflate(R.layout.project_row,
	                                                    null);

	   LinearLayout stageInitiationLayout = (LinearLayout) tableRow.findViewById(R.id.stage1);
	   LinearLayout stageLayout = (LinearLayout) stageInitiationLayout.findViewById(R.id.infoStages);
	   stageLayout.setVisibility(View.GONE);
	   TextView title = (TextView) stageInitiationLayout.findViewById(R.id.titleStage);
	   title.setVisibility(View.VISIBLE);
	   title.setText(getResources().getString(R.string.label_initiating));
	   
	   LinearLayout stagePlanningLayout = (LinearLayout) tableRow.findViewById(R.id.stage2);
	   stageLayout = (LinearLayout) stagePlanningLayout.findViewById(R.id.infoStages);
	   stageLayout.setVisibility(View.GONE);
	   title = (TextView) stagePlanningLayout.findViewById(R.id.titleStage);
	   title.setVisibility(View.VISIBLE);
	   title.setText(getResources().getString(R.string.label_planning));
	   
	    LinearLayout stageExecutingLayout = (LinearLayout) tableRow.findViewById(R.id.stage3);
	    stageLayout = (LinearLayout) stageExecutingLayout.findViewById(R.id.infoStages);
		   stageLayout.setVisibility(View.GONE);
		   title = (TextView) stageExecutingLayout.findViewById(R.id.titleStage);
		   title.setVisibility(View.VISIBLE);
		   title.setText(getResources().getString(R.string.label_executing));
		   
	    LinearLayout stageMonitoringLayout = (LinearLayout) tableRow.findViewById(R.id.stage4);
	    stageLayout = (LinearLayout) stageMonitoringLayout.findViewById(R.id.infoStages);
		   stageLayout.setVisibility(View.GONE);
		   title = (TextView) stageMonitoringLayout.findViewById(R.id.titleStage);
		   title.setVisibility(View.VISIBLE);
		   title.setText(getResources().getString(R.string.label_controlling));
		   
	    LinearLayout stageClosingLayout = (LinearLayout) tableRow.findViewById(R.id.stage5);
	    stageLayout = (LinearLayout) stageClosingLayout.findViewById(R.id.infoStages);
		   stageLayout.setVisibility(View.GONE);
		   title = (TextView) stageClosingLayout.findViewById(R.id.titleStage);
		   title.setVisibility(View.VISIBLE);
		   title.setText(getResources().getString(R.string.label_closing));


	    View projectInfo = tableRow.findViewById(R.id.projectInfo);
	    projectInfo.setVisibility(View.GONE);

	    View titleProject = tableRow.findViewById(R.id.titleProjects);
	    titleProject.setVisibility(View.VISIBLE);
	    
	    tableLayout.addView(tableRow);
	    
	    View v = new View(this);
	    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1);
	    v.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
	    v.setLayoutParams(params);
	    tableLayout.addView(v);
  }
 
  private void addRow(by.bsu.courseproject.model.Project project) {
    TableLayout tableLayout = (TableLayout) findViewById(R.id.tableProjects);
    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    TableRow tableRow = (TableRow) inflater.inflate(R.layout.project_row,
                                                    null);

    
    

    
    
    LinearLayout stageInitiationLayout = (LinearLayout) tableRow.findViewById(R.id.stage1);
    prepareStageCell(StageType.INITIATION, project, stageInitiationLayout);
    //LayoutParams params = (LinearLayout.LayoutParams)stageInitiationLayout.getLayoutParams();
    //params.height = height;
   // stageInitiationLayout.setLayoutParams(params);
    
    LinearLayout stagePlanningLayout = (LinearLayout) tableRow.findViewById(R.id.stage2);
    prepareStageCell(StageType.PLANNING_AND_DESIGN, project, stagePlanningLayout);
   // stagePlanningLayout.setLayoutParams(params);
    LinearLayout stageExecutingLayout = (LinearLayout) tableRow.findViewById(R.id.stage3);
    prepareStageCell(StageType.EXECUTING, project, stageExecutingLayout);
    //stageExecutingLayout.setLayoutParams(params);
    LinearLayout stageMonitoringLayout = (LinearLayout) tableRow.findViewById(R.id.stage4);
    prepareStageCell(StageType.MONITORING_AND_CONTROLLING, project, stageMonitoringLayout);
   // stageMonitoringLayout.setLayoutParams(params);
    LinearLayout stageClosingLayout = (LinearLayout) tableRow.findViewById(R.id.stage5);
    prepareStageCell(StageType.CLOSING, project, stageClosingLayout);
  // stageClosingLayout.setLayoutParams(params);

    /*TableRow.LayoutParams paramst = new TableRow.LayoutParams(1, height);
    paramst.height = height;
    View v = tableRow.findViewById(R.id.sep1);
    v.setLayoutParams(paramst);
    v = tableRow.findViewById(R.id.sep2);
    v.setLayoutParams(paramst);
    v = tableRow.findViewById(R.id.sep3);
    v.setLayoutParams(paramst);
    v = tableRow.findViewById(R.id.sep4);
    v.setLayoutParams(paramst);
    v = tableRow.findViewById(R.id.sep5);
    v.setLayoutParams(paramst);*/
    View projectInfo = tableRow.findViewById(R.id.projectInfo);
    projectInfo.setVisibility(View.VISIBLE);

    View titleProject = tableRow.findViewById(R.id.titleProjects);
    titleProject.setVisibility(View.GONE);
    
    projectInfo.setId(PROJECT_INFO_ID);
    projectInfo.setTag(project.getId());
    TextView projectName = (TextView) tableRow.findViewById(R.id.projectName);
    projectName.setText(project.getName());

    
    TextView projectCategory = (TextView) tableRow.findViewById(R.id.projectCategory);
    projectCategory.setText("( " + project.getCategory().getIdName() + " )");
    if (project.getCustomer() != null) {
      TextView projectCustomerFirstName = (TextView) tableRow.findViewById(R.id.customerFirstName);
      projectCustomerFirstName.setText(project.getCustomer().getFirstName());

      TextView projectCustomerLastName = (TextView) tableRow.findViewById(R.id.customerLastName);
      projectCustomerLastName.setText(project.getCustomer().getLastName());
    }
    tableLayout.addView(tableRow);
    
    View v = new View(this);
    TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 1);
    v.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    v.setLayoutParams(params);
    tableLayout.addView(v);

  }

  private void prepareStageCell(StageType stageType, by.bsu.courseproject.model.Project project, LinearLayout stageLayout) {
    stageLayout.setId(stageType.ordinal());
    stageLayout.setTag(project.getId());
    stageLayout.setTag(R.id.projectName, project.getName());
    by.bsu.courseproject.model.Stage stageData = PMApplication.getPMDB().getStageDataSource().load(project.getId(), stageType.ordinal());
    setStageData(stageLayout, stageData);
  }

  private void setStageData(LinearLayout stageLayout, by.bsu.courseproject.model.Stage stage) {
    if (stage.getManager() != null) {
      TextView managerFirstName = (TextView) stageLayout.findViewById(R.id.ManagerFirstName);
      managerFirstName.setText(stage.getManager().getFirstName());
      TextView managerLastName = (TextView) stageLayout.findViewById(R.id.ManagerLastName);
      managerLastName.setText(stage.getManager().getLastName());
      TextView managerPatronymic = (TextView) stageLayout.findViewById(R.id.ManagerPatronymic);
      managerPatronymic.setText(stage.getManager().getMiddleName());
    }
    /*TextView stageStatus = (TextView)stageLayout.findViewById(R.id.StageStatus);
    stageStatus.setText(stage);*/
  }

  public void onClick(View view) {
    Intent intent = new Intent();
    intent.putExtra(DBConstants.Columns.STAGE_PROJECT_ID, (Long) view.getTag());
    intent.putExtra(DBConstants.Columns.PROJECT_PROJECTNAME, (String) view.getTag(R.id.projectName));
    switch (view.getId()) {
    case PROJECT_INFO_ID: {
      Uri uri = ContentUris.withAppendedId(ProjectManagerProvider.PROJECT_URI, (Long) view.getTag());
      intent.setData(uri);
      intent.setClass(this, Project.class);
      intent.putExtra(Employee.FROM_LIST, 1);
      break;
    }
    case 0: {
      prepareStageIntent(intent, StageType.INITIATION);
      break;
    }
    case 1: {
      prepareStageIntent(intent, StageType.PLANNING_AND_DESIGN);
      break;
    }
    case 2: {
      prepareStageIntent(intent, StageType.EXECUTING);
      break;
    }
    case 3: {
      prepareStageIntent(intent, StageType.MONITORING_AND_CONTROLLING);
      break;
    }
    case 4: {
      prepareStageIntent(intent, StageType.CLOSING);
      break;
    }
    }
    startActivity(intent);
  }

  private void prepareStageIntent(Intent intent, StageType stageType) {
    intent.setClass(getApplicationContext(), Stage.class);
    intent.putExtra(DBConstants.Columns.STAGE_TYPE, stageType.ordinal());
  }


}