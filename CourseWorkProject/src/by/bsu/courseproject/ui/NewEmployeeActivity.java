package by.bsu.courseproject.ui;

import java.util.Calendar;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import by.bsu.courseproject.R;
import by.bsu.courseproject.db.ProjectManagerProvider.Columns;

public class NewEmployeeActivity extends Activity implements OnDateSetListener, View.OnClickListener{

	public static String FROM_LIST = "FROM_LIST";
	public static int ITEM = 1;
	public static int MENU = 2;
	private ContentValues oldValues;
	private static final int DATE_PICKER_DIALOG = 1;
	private boolean mIsNew;
	private int mId = -1;

	private int mPendingView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_employee);
		AddListeners();

		oldValues = new ContentValues();

		mPendingView = R.id.editTextBirthDate;

		Intent intent = getIntent();
		if (intent != null && intent.getIntExtra(FROM_LIST, -1) == ITEM){
			completeForm(intent);
			mIsNew = false;
			this.setTitle("Исполнитель");
		} else
			mIsNew = true;

	}

	@Override
	protected void onResume() {	
		super.onResume();
		if (!Tools.checkTimeLoggedOn(this)) {			
			Intent intent = new Intent();
			intent.setClass(this, Startup.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);						
			intent.putExtra(Startup.EXTRA_INTENT, getIntent());
			startActivity(intent);
			finish();
		}			
	}	

	@Override
	protected void onPause() {		
		super.onPause();		
		Tools.setTime(this);
	}
	
	@Override
	protected Dialog onCreateDialog(int dialogId){
		switch (dialogId) {
		case DATE_PICKER_DIALOG:
			return new DatePickerDialog(this, this, 1900, 0, 1);
			}
		return null;
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {		
		super.onPrepareDialog(id, dialog);
		switch (id) {
		case DATE_PICKER_DIALOG:
			String val = ((EditText)findViewById(mPendingView)).getText().toString();
			if (!val.equals("")) {
				StringTokenizer strtok = new StringTokenizer(val, "-");
				int day = Integer.parseInt(strtok.nextToken());
				int month = Integer.parseInt(strtok.nextToken());
				int year = Integer.parseInt(strtok.nextToken());
				((DatePickerDialog)dialog).updateDate(year, month - 1, day);
			}
			else {
				final Calendar c = Calendar.getInstance();
				((DatePickerDialog)dialog).updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
			}
			break;
		}
	}


	private void completeForm(Intent intent){
		Cursor c = managedQuery(intent.getData(), null, null, null, null);

		if (c != null && c.moveToFirst()) {

			int colIndex = c.getColumnIndex(Columns.COLUMN_NAME_ID);
			if (colIndex != -1) {
				mId = c.getInt(colIndex);

				colIndex = c.getColumnIndex(Columns.COLUMN_FIRSTNAME);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextEmployeeFirstName)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_FIRSTNAME, c.getString(colIndex));
				}

				colIndex = c.getColumnIndex(Columns.COLUMN_MIDDLENAME);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextEmployeeMiddleName)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_MIDDLENAME, c.getString(colIndex));
				}

				colIndex = c.getColumnIndex(Columns.COLUMN_LASTNAME);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextEmployeeLastName)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_LASTNAME, c.getString(colIndex));
				}

				colIndex = c.getColumnIndex(Columns.COLUMN_EMAIL);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextEmployeeEmail)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_EMAIL, c.getString(colIndex));
				}

				colIndex = c.getColumnIndex(Columns.COLUMN_SKYPE);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextEmployeeSkypeAccount)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_SKYPE, c.getString(colIndex));
				}

				colIndex = c.getColumnIndex(Columns.COLUMN_PHONE);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextEmployeePhone)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_PHONE, c.getString(colIndex));
				}

				colIndex = c.getColumnIndex(Columns.COLUMN_INFO);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextInfo)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_INFO, c.getString(colIndex));
				}

				colIndex = c.getColumnIndex(Columns.COLUMN_EDUCATION);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextEducation)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_EDUCATION, c.getString(colIndex));
				}

				colIndex = c.getColumnIndex(Columns.COLUMN_EXPERIENCE);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextExperience)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_EXPERIENCE, c.getString(colIndex));
				}
				
				colIndex = c.getColumnIndex(Columns.COLUMN_BIRTHDAY);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextBirthDate)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_BIRTHDAY, c.getString(colIndex));

				}

			}
		}


	}

	protected void AddListeners() {
		findViewById(R.id.buttonCancel).setOnClickListener((android.view.View.OnClickListener) this); 
		findViewById(R.id.buttonSave).setOnClickListener((android.view.View.OnClickListener) this);  
		findViewById(R.id.editTextBirthDate).setOnClickListener((android.view.View.OnClickListener) this);

	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.buttonCancel:
			finish();
			break;
		case R.id.buttonSave:
			if (((EditText) findViewById(R.id.editTextEmployeeFirstName)).getText().toString().equals("") || 
					((EditText) findViewById(R.id.editTextEmployeeLastName)).getText().toString().equals(""))
				showDialog(getString(R.string.name_dialog_title));	
			else {
				if (mIsNew)
					saveToDB();
				else if (isChange())
					saveToDB();
				finish();
			}
			break;
		case R.id.editTextBirthDate: 
			 showDialog(DATE_PICKER_DIALOG);
			break;
		}

	}

	private boolean isChange(){

		if (oldValues.containsKey(Columns.COLUMN_FIRSTNAME) && !((EditText)findViewById(R.id.editTextEmployeeFirstName)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_FIRSTNAME)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_MIDDLENAME) && !((EditText)findViewById(R.id.editTextEmployeeMiddleName)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_MIDDLENAME)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_LASTNAME) && !((EditText)findViewById(R.id.editTextEmployeeLastName)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_LASTNAME)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_EMAIL) && !((EditText)findViewById(R.id.editTextEmployeeEmail)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_EMAIL)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_SKYPE) && !((EditText)findViewById(R.id.editTextEmployeeSkypeAccount)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_SKYPE)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_PHONE) && !((EditText)findViewById(R.id.editTextEmployeePhone)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_PHONE)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_INFO) && !((EditText)findViewById(R.id.editTextInfo)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_INFO)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_EDUCATION) && !((EditText)findViewById(R.id.editTextEducation)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_EDUCATION)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_EXPERIENCE) && !((EditText)findViewById(R.id.editTextExperience)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_EXPERIENCE)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_BIRTHDAY) && !((EditText)findViewById(R.id.editTextBirthDate)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_BIRTHDAY)))
			return true;


		return false;
	}

	private void saveToDB(){
		ContentValues cv = new ContentValues();
		cv.put(Columns.COLUMN_FIRSTNAME, ((EditText) findViewById(R.id.editTextEmployeeFirstName)).getText().toString());
		cv.put(Columns.COLUMN_MIDDLENAME, ((EditText) findViewById(R.id.editTextEmployeeMiddleName)).getText().toString());
		cv.put(Columns.COLUMN_LASTNAME, ((EditText) findViewById(R.id.editTextEmployeeLastName)).getText().toString());
		cv.put(Columns.COLUMN_EMAIL, ((EditText) findViewById(R.id.editTextEmployeeEmail)).getText().toString());
		cv.put(Columns.COLUMN_SKYPE, ((EditText) findViewById(R.id.editTextEmployeeSkypeAccount)).getText().toString());
		cv.put(Columns.COLUMN_PHONE, ((EditText) findViewById(R.id.editTextEmployeePhone)).getText().toString());
		cv.put(Columns.COLUMN_BIRTHDAY, ((EditText) findViewById(R.id.editTextBirthDate)).getText().toString());
		cv.put(Columns.COLUMN_INFO, ((EditText) findViewById(R.id.editTextInfo)).getText().toString());
		cv.put(Columns.COLUMN_EDUCATION, ((EditText) findViewById(R.id.editTextEducation)).getText().toString());
		cv.put(Columns.COLUMN_EXPERIENCE, ((EditText) findViewById(R.id.editTextExperience)).getText().toString());

		if (mIsNew){
			Uri uri = getContentResolver().insert(Columns.EMPLOYEE_URI, cv);
			if (uri == null) 
				Log.e("HLAG", "Failed to insert into " + Columns.EMPLOYEE_URI);	
		} else 
			getContentResolver().update(ContentUris.withAppendedId(Columns.EMPLOYEE_URI, mId), cv, null, null);

	}

	private void showDialog(String title){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setCancelable(true)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setCancelable(true)
		.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		String val = String.format("%02d-%02d-%d", dayOfMonth, monthOfYear + 1, year);
		((EditText)findViewById(mPendingView)).setText(val);
		
	}
	


}