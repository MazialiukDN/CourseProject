package by.bsu.courseproject.ui;

import by.bsu.courseproject.R;
import by.bsu.courseproject.db.ProjectManagerProvider.Columns;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ProjectsActivity extends FragmentActivity {
	private static final int IDM_NEW_PROJECT = 101;

	static final String[] PROJECTION = new String[] { Columns.COLUMN_NAME_ID,
			Columns.PROJECT_COLUMN_PROJECTNAME,
			Columns.PROJECT_COLUMN_CATEGORY, Columns.PROJECT_COLUMN_PRIORITY,
			Columns.PROJECT_COLUMN_PROJECTDUEDATE,
			Columns.PROJECT_COLUMN_STATUS, 

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.projects);
	}

	@Override
	protected void onResume() {
		super.onResume();
		((TableLayout) findViewById(R.id.tableProjects)).removeAllViews();

		Cursor cursor = managedQuery(Columns.CONTENT_URI, PROJECTION, null,
				null, null);

		if (cursor != null && cursor.moveToFirst()) {
			do {
				String projectName = cursor.getString(cursor
						.getColumnIndex(Columns.PROJECT_COLUMN_PROJECTNAME));
				
				addRow(projectName);

			} while (cursor.moveToNext());
		}
	};

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
			intent.setClass(getApplicationContext(), NewProjectActivity.class);
			break;
		default:
			return false;
		}
		startActivity(intent);
		return true;
	}

	private void addRow(String projectName) {
		TableLayout tableLayout = (TableLayout) findViewById(R.id.tableProjects);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		TableRow tableRow = (TableRow) inflater.inflate(R.layout.row_project,
				null);

		TextView tv = (TextView) tableRow.findViewById(R.id.projectName);
		tv.setText(projectName);

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
}