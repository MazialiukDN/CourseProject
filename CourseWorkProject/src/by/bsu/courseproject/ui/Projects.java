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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import by.bsu.courseproject.PMApplication;
import by.bsu.courseproject.R;
import by.bsu.courseproject.db.ProjectManagerProvider;

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

  private void addRow(by.bsu.courseproject.model.Project project) {
    TableLayout tableLayout = (TableLayout) findViewById(R.id.tableProjects);
    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    TableRow tableRow = (TableRow) inflater.inflate(R.layout.project_row,
                                                    null);

    View projectInfo = tableRow.findViewById(R.id.projectInfo);
    projectInfo.setId(PROJECT_INFO_ID);
    projectInfo.setTag(project.getId());
    TextView projectName = (TextView) tableRow.findViewById(R.id.projectName);
    projectName.setText(project.getName());

    TextView projectCategory = (TextView) tableRow.findViewById(R.id.projectCategory);
    projectCategory.setText(project.getCategory().getIdName());

    TextView projectCustomerFirstName = (TextView) tableRow.findViewById(R.id.customerFirstName);
    projectCustomerFirstName.setText(project.getCustomer().getLastName());

    TextView projectCustomerLastName = (TextView) tableRow.findViewById(R.id.customerLastName);
    projectCustomerLastName.setText(project.getCustomer().getLastName());
    /*tv = (TextView) tableRow.findViewById(R.id.managerNameI);
    tv.setText(employeeName1);
		tv = (TextView) tableRow.findViewById(R.id.managerNameP);
		tv.setText(employeeName2);

		tv = (TextView) tableRow.findViewById(R.id.managerNameE);
		tv.setText(employeeName3);

		tv = (TextView) tableRow.findViewById(R.id.managerNameC);
		tv.setText(employeeName4);

		tv = (TextView) tableRow.findViewById(R.id.managerNameCl);
		tv.setText(employeeName5);*/

    tableLayout.addView(tableRow);

  }

  public void onClick(View view) {
    if (PROJECT_INFO_ID == view.getId()) {
      Uri uri = ContentUris.withAppendedId(ProjectManagerProvider.PROJECT_URI, (Long) view.getTag());
      Intent intent = new Intent();
      intent.setData(uri);
      intent.setClass(getApplicationContext(), Project.class);
      intent.putExtra(Employee.FROM_LIST, 1);
      startActivity(intent);
    }
  }

  /*
  private void addRow(Long projectID, String projectName, String employeeName1,
			String employeeName2, String employeeName3, String employeeName4,
			String employeeName5) {
		TableLayout tableLayout = (TableLayout) findViewById(R.id.tableProjects);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		TableRow tableRow = (TableRow) inflater.inflate(R.layout.row_project,
				null);
		TextView tv = (TextView) tableRow.findViewById(R.id.projectName);
		tv.setText(projectName);
		tv.setTag(projectID);
		tv.setOnClickListener(this);

		tv = (TextView) tableRow.findViewById(R.id.managerNameI);
		tv.setText(employeeName1);
		tv.setTag(projectID);
		tv.setOnClickListener(this);

		tv = (TextView) tableRow.findViewById(R.id.managerNameP);
		tv.setText(employeeName2);
		tv.setTag(projectID);
		tv.setOnClickListener(this);

		tv = (TextView) tableRow.findViewById(R.id.managerNameE);
		tv.setText(employeeName3);
		tv.setTag(projectID);
		tv.setOnClickListener(this);

		tv = (TextView) tableRow.findViewById(R.id.managerNameC);
		tv.setText(employeeName4);
		tv.setTag(projectID);
		tv.setOnClickListener(this);

		tv = (TextView) tableRow.findViewById(R.id.managerNameCl);
		tv.setText(employeeName5);
		tv.setTag(projectID);
		tv.setOnClickListener(this);

		tableLayout.addView(tableRow);

	}
  * */


}