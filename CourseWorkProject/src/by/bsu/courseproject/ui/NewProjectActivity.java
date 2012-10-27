package by.bsu.courseproject.ui;

import java.util.Date;

import by.bsu.courseproject.R;
import by.bsu.courseproject.db.ProjectManagerProvider;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

public class NewProjectActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_project);
		/*Spinner projectCategory = (Spinner) findViewById(R.id.spinnerProjectCategory);
		final String[] categories = {ProjectCategory.HOTEL_AND_TOURISM_BUSINESS.getIdName(),
				ProjectCategory.GOVERNMENTAL_ORGANIZATION.getIdName(), ProjectCategory.SHOW_BUSINESS.getIdName(), ProjectCategory.INFORMATION_SERVICE.getIdName()};
		ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(getApplicationContext(), R.id.spinnerProjectCategory,categories);
		categoriesAdapter.setDropDownViewResource(R.id.spinnerProjectCategory);
		projectCategory.setAdapter(categoriesAdapter);*/
		final Button cancelButton = (Button) findViewById(R.id.buttonCancelProject);
		cancelButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		
		final Button saveButton = (Button) findViewById(R.id.buttonSaveProject);
		saveButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				ContentValues values = new ContentValues();
				EditText projectName = (EditText) findViewById(R.id.editTextProjectName);
				values.put(ProjectManagerProvider.Columns.PROJECT_COLUMN_PROJECTNAME, projectName.getText().toString());
				
				Spinner projectCategory = (Spinner) findViewById(R.id.spinnerProjectCategory);
				DatePicker finalDate = (DatePicker) findViewById(R.id.datePickerProjectFinalDate);
				//values.put(ProjectManagerProvider.Columns.PROJECT_COLUMN_PROJECTDUEDATE,new Date(finalDate.getYear() -1900, finalDate.getMonth(), finalDate.getDayOfMonth()).getTime());
				Spinner projectStatus = (Spinner) findViewById(R.id.spinnerProjectStatus);
				Spinner projectPriority = (Spinner) findViewById(R.id.spinnerProjectPriority);
				ContentResolver resolver = getContentResolver();
				resolver.insert(ProjectManagerProvider.Columns.PROJECT_URI, values);
				//changeActivity(CourseProjectActivity.class);
				finish();
			}
		});
		
		
	}

	/*private void changeActivity(Class activityClass) {
		Intent cancelIntent = new Intent();
		cancelIntent.setClass(getApplicationContext(),
				activityClass);
		startActivity(cancelIntent);
	}*/

}
