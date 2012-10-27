package by.bsu.courseproject.ui;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import by.bsu.courseproject.R;
import by.bsu.courseproject.db.ProjectManagerProvider.Columns;


public class NewCustomerInvestorActivity extends Activity implements View.OnClickListener{

	public static String INV ="INV";
	public static final int inv = 1;
	public static final int cust = 2;
	public static String FROM_LIST = "FROM_LIST";
	public static int ITEM = 1;
	public static int MENU = 2;
	private ContentValues oldValues;
	private boolean mIsNew;
	private int mId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_customer_investor);
		AddListeners();

		oldValues = new ContentValues();

		Intent intent = getIntent();
		if (intent != null)
		switch(getIntent().getIntExtra(INV, -1)){
		case inv:
			this.setTitle("Инвестор");
			break;
		case cust:
			this.setTitle("Заказчик");
			break;
		}
		
		if (intent != null && intent.getIntExtra(FROM_LIST, -1) == ITEM){
			completeForm(intent);
			mIsNew = false;

		} else{
			mIsNew = true;
		switch(getIntent().getIntExtra(INV, -1)){
		case inv:
			this.setTitle("Новый инвестор");
			break;
		case cust:
			this.setTitle("Новый заказчик");
			break;
		}
		}

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
	
	private void completeForm(Intent intent){
		Cursor c = managedQuery(intent.getData(), null, null, null, null);

		if (c != null && c.moveToFirst()) {

			int colIndex = c.getColumnIndex(Columns.COLUMN_NAME_ID);
			if (colIndex != -1) {
				mId = c.getInt(colIndex);

				colIndex = c.getColumnIndex(Columns.COLUMN_FIRSTNAME);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextFirstName)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_FIRSTNAME, c.getString(colIndex));
				}

				colIndex = c.getColumnIndex(Columns.COLUMN_MIDDLENAME);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextMiddleName)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_MIDDLENAME, c.getString(colIndex));
				}

				colIndex = c.getColumnIndex(Columns.COLUMN_LASTNAME);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextLastName)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_LASTNAME, c.getString(colIndex));
				}

				colIndex = c.getColumnIndex(Columns.COLUMN_EMAIL);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextEmail)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_EMAIL, c.getString(colIndex));
				}

				colIndex = c.getColumnIndex(Columns.COLUMN_SKYPE);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextSkypeAccount)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_SKYPE, c.getString(colIndex));
				}

				colIndex = c.getColumnIndex(Columns.COLUMN_PHONE);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextPhone)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_PHONE, c.getString(colIndex));
				}

				colIndex = c.getColumnIndex(Columns.COLUMN_INFO);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextInfo)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_INFO, c.getString(colIndex));
				}

				colIndex = c.getColumnIndex(Columns.COLUMN_COMPANY);
				if (colIndex != -1){
					((EditText)findViewById(R.id.editTextCompany)).setText(c.getString(colIndex));
					oldValues.put(Columns.COLUMN_COMPANY, c.getString(colIndex));

				}

			}
		}


	}

	protected void AddListeners() {
		findViewById(R.id.buttonCancel).setOnClickListener((android.view.View.OnClickListener) this); 
		findViewById(R.id.buttonSave).setOnClickListener((android.view.View.OnClickListener) this);  

	}

	public void onClick(View v) {
		switch(v.getId()){
		case R.id.buttonCancel:
			finish();
			break;
		case R.id.buttonSave:
			if (((EditText) findViewById(R.id.editTextFirstName)).getText().toString().equals("") || 
					((EditText) findViewById(R.id.editTextLastName)).getText().toString().equals(""))
				showDialog(getString(R.string.name_dialog_title));	
			else {
				if (mIsNew)
					saveToDB();
				else if (isChange())
					saveToDB();
				finish();
			}
			break;
		}

	}

	private boolean isChange(){

		if (oldValues.containsKey(Columns.COLUMN_FIRSTNAME) && !((EditText)findViewById(R.id.editTextFirstName)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_FIRSTNAME)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_MIDDLENAME) && !((EditText)findViewById(R.id.editTextMiddleName)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_MIDDLENAME)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_LASTNAME) && !((EditText)findViewById(R.id.editTextLastName)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_LASTNAME)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_EMAIL) && !((EditText)findViewById(R.id.editTextEmail)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_EMAIL)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_SKYPE) && !((EditText)findViewById(R.id.editTextSkypeAccount)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_SKYPE)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_PHONE) && !((EditText)findViewById(R.id.editTextPhone)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_PHONE)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_INFO) && !((EditText)findViewById(R.id.editTextInfo)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_INFO)))
			return true;
		if (oldValues.containsKey(Columns.COLUMN_COMPANY) && !((EditText)findViewById(R.id.editTextCompany)).getText().toString().equals(oldValues.getAsString(Columns.COLUMN_COMPANY)))
			return true;


		return false;
	}

	private void saveToDB(){
		ContentValues cv = new ContentValues();
		cv.put(Columns.COLUMN_FIRSTNAME, ((EditText) findViewById(R.id.editTextFirstName)).getText().toString());
		cv.put(Columns.COLUMN_MIDDLENAME, ((EditText) findViewById(R.id.editTextMiddleName)).getText().toString());
		cv.put(Columns.COLUMN_LASTNAME, ((EditText) findViewById(R.id.editTextLastName)).getText().toString());
		cv.put(Columns.COLUMN_EMAIL, ((EditText) findViewById(R.id.editTextEmail)).getText().toString());
		cv.put(Columns.COLUMN_SKYPE, ((EditText) findViewById(R.id.editTextSkypeAccount)).getText().toString());
		cv.put(Columns.COLUMN_PHONE, ((EditText) findViewById(R.id.editTextPhone)).getText().toString());
		cv.put(Columns.COLUMN_INFO, ((EditText) findViewById(R.id.editTextInfo)).getText().toString());
		cv.put(Columns.COLUMN_COMPANY, ((EditText) findViewById(R.id.editTextCompany)).getText().toString());


		if (mIsNew){

			switch(getIntent().getIntExtra(INV, -1)){
			case inv:
				cv.put(Columns.COLUMN_HANDLE_CUST, 0);
				cv.put(Columns.COLUMN_HANDLE_INV, 1);
				break;
			case cust:
				cv.put(Columns.COLUMN_HANDLE_CUST, 1);
				cv.put(Columns.COLUMN_HANDLE_INV, 0);
				break;
			}
			Uri uri = getContentResolver().insert(Columns.CUST_INV_URI, cv);
			if (uri == null) 
				Log.e("HLAG", "Failed to insert into " + Columns.CUST_INV_URI);	
		} else 
			getContentResolver().update(ContentUris.withAppendedId(Columns.CUST_INV_URI, mId), cv, null, null);

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



}

