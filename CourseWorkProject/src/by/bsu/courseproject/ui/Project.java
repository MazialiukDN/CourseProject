package by.bsu.courseproject.ui;

import static by.bsu.courseproject.project.ProjectPriority.NORMAL;
import static by.bsu.courseproject.project.ProjectStatus.PROPOSED;
import static by.bsu.courseproject.project.ProjectStatus.values;

import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import by.bsu.courseproject.PMApplication;
import by.bsu.courseproject.R;
import by.bsu.courseproject.db.DBConstants.Columns;
import by.bsu.courseproject.db.ProjectManagerProvider;
import by.bsu.courseproject.project.ProjectPriority;
import by.bsu.courseproject.project.ProjectStatus;
import by.bsu.courseproject.stage.StageType;
import by.bsu.courseproject.util.DateUtil;

public class Project extends Activity implements DatePickerDialog.OnDateSetListener, View.OnClickListener {

  private static final String FROM_LIST = "FROM_LIST";
  private static final int ITEM = 1;
  private ContentValues oldValues;

  private static final int DATE_PICKER_DIALOG = 1;
  private static final int DIALOG_PRIORITY = 2;
  private static final int DIALOG_STATUS = 3;

  private boolean mIsNew;
  private int mId = -1;

  private int mInvOldId = -1;
  private int mCustOldId = -1;
  private int mInvId = -1;
  private int mCustId = -1;

  private int mPendingView;
  private int mPriority = 2;
  private int mStatus = -1;
  private final int REQUEST_CUSTOMER = 101;
  private final int REQUEST_INVESTOR = 102;
  private final int REQUEST_DESCRIPTION_CODE = 103;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.new_project_catalogue);
    AddListeners();
    oldValues = new ContentValues();

    mPendingView = R.id.editDate;

    Intent intent = getIntent();
    if (intent != null && intent.getIntExtra(FROM_LIST, -1) == ITEM) {
      completeForm(intent);
      mIsNew = false;
      this.setTitle("Проект");
    } else {
      mIsNew = true;
      this.setTitle("Новый проект");

      ((EditText) findViewById(R.id.editTextPriority)).setText(NORMAL.getIdName());
      ((EditText) findViewById(R.id.editTextStatus)).setText(PROPOSED.getIdName());
      mPriority = NORMAL.ordinal();
      mStatus = PROPOSED.ordinal();
      Date date = DateUtil.getCurrentDateWithDefaultOffset();
      String convertedDate = DateUtil.dateToString(date);
      ((EditText) findViewById(R.id.editDate)).setText(convertedDate);
    }

  }


  @Override
  protected void onResume() {
    super.onResume();
    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    //InputMethodManager imm = (InputMethodManager)getSystemService(
    	   //   Context.INPUT_METHOD_SERVICE);
    	//imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
  }

  @Override
  protected Dialog onCreateDialog(int dialogId) {
    switch (dialogId) {
    case DATE_PICKER_DIALOG:
      return new DatePickerDialog(this, this, 1900, 0, 1);
    case DIALOG_PRIORITY:
      return new AlertDialog.Builder(Project.this)
          .setTitle("Приоритет")
          .setSingleChoiceItems(R.array.select_priority, mPriority, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
              ((EditText) findViewById(R.id.editTextPriority)).setText(ProjectPriority.values()[whichButton].getIdName());
              mPriority = whichButton;
            }
          })
          .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
          })
          .create();
    case DIALOG_STATUS:
      return new AlertDialog.Builder(Project.this)
          .setTitle("Статус")
          .setSingleChoiceItems(R.array.select_status, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
              ((EditText) findViewById(R.id.editTextStatus)).setText(values()[whichButton].getIdName());
              mStatus = whichButton;


            }
          })
          .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
          })
          .create();
    }
    return null;
  }

  @Override
  protected void onPrepareDialog(int id, Dialog dialog) {
    super.onPrepareDialog(id, dialog);
    switch (id) {
    case DATE_PICKER_DIALOG:
      String date = ((EditText) findViewById(mPendingView)).getText().toString();
      if (!date.isEmpty()) {
        StringTokenizer strtok = new StringTokenizer(date, "-");
        int day = Integer.parseInt(strtok.nextToken());
        int month = Integer.parseInt(strtok.nextToken());
        int year = Integer.parseInt(strtok.nextToken());
        ((DatePickerDialog) dialog).updateDate(year, month - 1, day);
      } else {
        final Calendar c = Calendar.getInstance();
        ((DatePickerDialog) dialog).updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
      }
      break;
    case DIALOG_PRIORITY:
      ((AlertDialog) dialog).getListView().setItemChecked(mPriority, true);
      break;
    case DIALOG_STATUS:
      ((AlertDialog) dialog).getListView().setItemChecked(mStatus, true);
      break;
    }
  }


  private void completeForm(Intent intent) {
    Cursor c = managedQuery(intent.getData(), new String[]{Columns._ID,
                                                           Columns.PROJECT_PROJECTNAME,
                                                           Columns.PROJECT_DESCRIPTION,
                                                           Columns.PROJECT_CATEGORY,
                                                           Columns.PROJECT_STATUS,
                                                           Columns.PROJECT_PRIORITY,
                                                           Columns.PROJECT_PROJECTDUEDATE,
                                                           Columns.PROJECT_INVESTOR_ID,
                                                           Columns.PROJECT_CUSTOMER_ID}, null, null, null);

    if (c != null && c.moveToFirst()) {

      int colIndex = c.getColumnIndex(Columns._ID);
      if (colIndex != -1) {
        mId = c.getInt(colIndex);

        colIndex = c.getColumnIndex(Columns.PROJECT_PROJECTNAME);
        if (colIndex != -1) {
          ((EditText) findViewById(R.id.editTextProjectName)).setText(c.getString(colIndex));
          oldValues.put(Columns.PROJECT_PROJECTNAME, c.getString(colIndex));
        }
        colIndex = c.getColumnIndex(Columns.PROJECT_DESCRIPTION);
        if (colIndex != -1) {
          ((EditText) findViewById(R.id.projectDescription)).setText(c.getString(colIndex));
          oldValues.put(Columns.PROJECT_DESCRIPTION, c.getString(colIndex));
        }

        colIndex = c.getColumnIndex(Columns.PROJECT_CATEGORY);
        if (colIndex != -1) {
          ((EditText) findViewById(R.id.editTextCategory)).setText(c.getString(colIndex));
          oldValues.put(Columns.PROJECT_CATEGORY, c.getString(colIndex));
        }

        colIndex = c.getColumnIndex(Columns.PROJECT_STATUS);
        if (colIndex != -1) {
          mStatus = c.getInt(colIndex);
          ((EditText) findViewById(R.id.editTextStatus)).setText(ProjectStatus.values()[mStatus].getIdName());
          oldValues.put(Columns.PROJECT_STATUS, ProjectStatus.values()[mStatus].getIdName());
        }


        colIndex = c.getColumnIndex(Columns.PROJECT_PRIORITY);
        if (colIndex != -1) {
          mPriority = c.getInt(colIndex);
          ((EditText) findViewById(R.id.editTextPriority)).setText(ProjectPriority.values()[mPriority].getIdName());
          oldValues.put(Columns.PROJECT_PRIORITY, ProjectPriority.values()[mPriority].getIdName());
        }
        colIndex = c.getColumnIndex(Columns.PROJECT_PROJECTDUEDATE);

        if (colIndex != -1) {
          ((EditText) findViewById(R.id.editDate)).setText(c.getString(colIndex));
          oldValues.put(Columns.PROJECT_PROJECTDUEDATE, c.getString(colIndex));
        }


        colIndex = c.getColumnIndex(Columns.PROJECT_CUSTOMER_ID);
        if (colIndex != -1) {
          mCustId = c.getInt(colIndex);
          mCustOldId = mCustId;
          c = getContentResolver().query(ProjectManagerProvider.PERSON_URI, new String[]{Columns._ID, Columns.PERSON_FIRSTNAME, Columns.PERSON_MIDDLENAME,
                                                                                         Columns.PERSON_LASTNAME}, Columns._ID + " = \"" + mCustId + "\"",
                                         null, null);
          if (c != null && c.moveToFirst()) {
            colIndex = c.getColumnIndex(Columns.PERSON_FIRSTNAME);
            if (colIndex != -1) {
              ((EditText) findViewById(R.id.editTextCustf)).setText(c.getString(colIndex));
            }

            colIndex = c.getColumnIndex(Columns.PERSON_MIDDLENAME);
            if (colIndex != -1) {
              ((EditText) findViewById(R.id.editTextCustm)).setText(c.getString(colIndex));
            }

            colIndex = c.getColumnIndex(Columns.PERSON_LASTNAME);
            if (colIndex != -1) {
              ((EditText) findViewById(R.id.editTextCustl)).setText(c.getString(colIndex));
            }

          }
          c.close();
        }

      }

      c = managedQuery(intent.getData(), new String[]{
          Columns.PROJECT_INVESTOR_ID}, null, null, null);

      if (c != null && c.moveToFirst()) {
        colIndex = c.getColumnIndex(Columns.PROJECT_INVESTOR_ID);
        if (colIndex != -1) {
          mInvId = c.getInt(colIndex);
          mInvOldId = mInvId;
          c = getContentResolver().query(ProjectManagerProvider.PERSON_URI, new String[]{Columns._ID, Columns.PERSON_FIRSTNAME, Columns.PERSON_MIDDLENAME,
                                                                                         Columns.PERSON_LASTNAME}, Columns._ID + " = \"" + mInvId + "\"",
                                         null, null);
          if (c != null && c.moveToFirst()) {
            colIndex = c.getColumnIndex(Columns.PERSON_FIRSTNAME);
            if (colIndex != -1) {
              ((EditText) findViewById(R.id.editTextInvf)).setText(c.getString(colIndex));
            }

            colIndex = c.getColumnIndex(Columns.PERSON_MIDDLENAME);
            if (colIndex != -1) {
              ((EditText) findViewById(R.id.editTextInvm)).setText(c.getString(colIndex));
            }

            colIndex = c.getColumnIndex(Columns.PERSON_LASTNAME);
            if (colIndex != -1) {
              ((EditText) findViewById(R.id.editTextInvl)).setText(c.getString(colIndex));
            }

          }

          c.close();

        }


      }
    }


  }

  void AddListeners() {
    findViewById(R.id.buttonCancel).setOnClickListener(this);
    findViewById(R.id.buttonSave).setOnClickListener(this);
    findViewById(R.id.editDate).setOnClickListener(this);

    findViewById(R.id.editTextCustf).setOnClickListener(this);
    findViewById(R.id.editTextCustm).setOnClickListener(this);
    findViewById(R.id.editTextCustl).setOnClickListener(this);

    findViewById(R.id.editTextInvf).setOnClickListener(this);
    findViewById(R.id.editTextInvm).setOnClickListener(this);
    findViewById(R.id.editTextInvl).setOnClickListener(this);

    findViewById(R.id.editTextPriority).setOnClickListener(this);
    findViewById(R.id.editTextStatus).setOnClickListener(this);
    findViewById(R.id.projectDescription).setOnClickListener(this);
    

  }

  public void onClick(View v) {
    switch (v.getId()) {
    case R.id.buttonCancel:
      finish();
      break;
    case R.id.buttonSave:
      if (((EditText) findViewById(R.id.editTextProjectName)).getText().toString().equals("")) {
        showDialog(getResources().getString(R.string.label_name_project));
      } else {
        if (mIsNew) {
          saveToDB();
        } else if (isChange()) {
          saveToDB();
        }
        finish();
      }
      break;
    case R.id.editDate:
      showDialog(DATE_PICKER_DIALOG);
      break;
    case R.id.editTextCustf:
    case R.id.editTextCustm:
    case R.id.editTextCustl:
      callList(true);
      break;
    case R.id.editTextInvf:
    case R.id.editTextInvm:
    case R.id.editTextInvl:
      callList(false);
      break;
    case R.id.editTextPriority:
      showDialog(DIALOG_PRIORITY);
      break;
    case R.id.editTextStatus:
      showDialog(DIALOG_STATUS);
      break;
    case R.id.projectDescription:
    	Intent intent = new Intent();
    	intent.setClass(this, DescriptionEditor.class);
    	int reqCode = REQUEST_DESCRIPTION_CODE;
		String descriptionString = ((EditText) findViewById(R.id.projectDescription)).getText().toString();
		intent.putExtra(DescriptionEditor.EXTRA_DESCRIPTION,
				descriptionString == null ? "" : descriptionString);
		startActivityForResult(intent, reqCode);
		break;
    }

  }


  private void callList(boolean isCustomer) {
    Intent intent = new Intent();
    intent.setClass(Project.this, CatalogueFragment.class);

    String[] projection = new String[]{
        Columns._ID,
        Columns.PERSON_DISCRIMINATOR,
        Columns.PERSON_FIRSTNAME,
        Columns.PERSON_MIDDLENAME,
        Columns.PERSON_LASTNAME,
        Columns.PERSON_EMAIL,
        Columns.PERSON_SKYPE,
        Columns.PERSON_PHONE
    };

    intent.setData(ProjectManagerProvider.PERSON_URI);
    intent.putExtra(CatalogueListFragment.ARG_FILTER_COLUMNS,
                    new String[]{Columns.PERSON_FIRSTNAME, Columns.PERSON_MIDDLENAME,
                                 Columns.PERSON_LASTNAME,
                                 Columns.PERSON_EMAIL,
                                 Columns.PERSON_SKYPE,
                                 Columns.PERSON_PHONE});
    intent.putExtra(CatalogueListFragment.ARG_ADAPTER_FROM,
                    new String[]{Columns.PERSON_LASTNAME, Columns.PERSON_FIRSTNAME, Columns.PERSON_MIDDLENAME,

                                 Columns.PERSON_PHONE});
    intent.putExtra(CatalogueListFragment.ARG_ADAPTER_TO,
                    new int[]{R.id.fName, R.id.mName, R.id.lName, R.id.phone});

    intent.setAction(Intent.ACTION_PICK);

    int reqCode;
    if (isCustomer) {
      intent.putExtra(CatalogueFragment.FROM_LIST, CatalogueList.CUSTOMER);
      reqCode = REQUEST_CUSTOMER;
    } else {
      intent.putExtra(CatalogueFragment.FROM_LIST, CatalogueList.INVESTOR);
      reqCode = REQUEST_INVESTOR;
    }
    intent.putExtra(CatalogueListFragment.ARG_PROJECTION, projection);
    startActivityForResult(intent, reqCode);

  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
      case REQUEST_INVESTOR:
        completeInvestor(data.getData());
        break;
      case REQUEST_CUSTOMER:
        completeCustomer(data.getData());
        break;
      case REQUEST_DESCRIPTION_CODE:
    	  String descriptionString = data
			.getStringExtra(DescriptionEditor.EXTRA_DESCRIPTION);
    	  ((EditText) findViewById(R.id.projectDescription)).setText(descriptionString);
    	  break;
      default:
        break;
      }
    }
  }

  private void completeInvestor(Uri uri) {
    Cursor c = managedQuery(uri, new String[]{Columns._ID,
                                              Columns._ID,
                                              Columns.PERSON_FIRSTNAME,
                                              Columns.PERSON_MIDDLENAME,
                                              Columns.PERSON_LASTNAME}, null, null, null);

    if (c != null && c.moveToFirst()) {

      int colIndex = c.getColumnIndex(Columns._ID);
      if (colIndex != -1) {
        mInvId = c.getInt(colIndex);
      }

      colIndex = c.getColumnIndex(Columns.PERSON_FIRSTNAME);
      if (colIndex != -1) {
        ((EditText) findViewById(R.id.editTextInvf)).setText(c.getString(colIndex));
      }

      colIndex = c.getColumnIndex(Columns.PERSON_MIDDLENAME);
      if (colIndex != -1) {
        ((EditText) findViewById(R.id.editTextInvm)).setText(c.getString(colIndex));
      }

      colIndex = c.getColumnIndex(Columns.PERSON_LASTNAME);
      if (colIndex != -1) {
        ((EditText) findViewById(R.id.editTextInvl)).setText(c.getString(colIndex));
      }
    }
  }

  private void completeCustomer(Uri uri) {
    Cursor c = managedQuery(uri, new String[]{Columns._ID,
                                              Columns._ID,
                                              Columns.PERSON_FIRSTNAME,
                                              Columns.PERSON_MIDDLENAME,
                                              Columns.PERSON_LASTNAME}, null, null, null);

    if (c != null && c.moveToFirst()) {

      int colIndex = c.getColumnIndex(Columns._ID);
      if (colIndex != -1) {
        mCustId = c.getInt(colIndex);
      }

      colIndex = c.getColumnIndex(Columns.PERSON_FIRSTNAME);
      if (colIndex != -1) {
        ((EditText) findViewById(R.id.editTextCustf)).setText(c.getString(colIndex));
      }

      colIndex = c.getColumnIndex(Columns.PERSON_MIDDLENAME);
      if (colIndex != -1) {
        ((EditText) findViewById(R.id.editTextCustm)).setText(c.getString(colIndex));
      }

      colIndex = c.getColumnIndex(Columns.PERSON_LASTNAME);
      if (colIndex != -1) {
        ((EditText) findViewById(R.id.editTextCustl)).setText(c.getString(colIndex));
      }
    }
  }

  private boolean isChange() {

    if (oldValues.containsKey(Columns.PROJECT_PROJECTNAME) && !((EditText) findViewById(R.id.editTextProjectName)).getText().toString().equals(
        oldValues.getAsString(Columns.PROJECT_PROJECTNAME))) {
      return true;
    }
    if (oldValues.containsKey(Columns.PROJECT_DESCRIPTION) && !((EditText) findViewById(R.id.projectDescription)).getText().toString().equals(
        oldValues.getAsString(Columns.PROJECT_DESCRIPTION))) {
      return true;
    }
    if (oldValues.containsKey(Columns.PROJECT_CATEGORY) && !((EditText) findViewById(R.id.editTextCategory)).getText().toString().equals(
        oldValues.getAsString(Columns.PROJECT_CATEGORY))) {
      return true;
    }
    if (oldValues.containsKey(Columns.PROJECT_STATUS) && !((EditText) findViewById(R.id.editTextStatus)).getText().toString().equals(
        oldValues.getAsString(Columns.PROJECT_STATUS))) {
      return true;
    }
    if (oldValues.containsKey(Columns.PROJECT_PRIORITY) && !((EditText) findViewById(R.id.editTextPriority)).getText().toString().equals(
        oldValues.getAsString(Columns.PROJECT_PRIORITY))) {
      return true;
    }
    if (oldValues.containsKey(Columns.PROJECT_PROJECTDUEDATE) && !((EditText) findViewById(R.id.editDate)).getText().toString().equals(
        oldValues.getAsString(Columns.PROJECT_PROJECTDUEDATE))) {
      return true;
    }

    if (mInvOldId != mInvId) return true;
    if (mCustOldId != mCustId) return true;
    return false;
  }

  private void saveToDB() {
    ContentValues cv = new ContentValues();
    cv.put(Columns.PROJECT_PROJECTNAME, ((EditText) findViewById(R.id.editTextProjectName)).getText().toString());
    cv.put(Columns.PROJECT_DESCRIPTION, ((EditText) findViewById(R.id.projectDescription)).getText().toString());
    cv.put(Columns.PROJECT_CATEGORY, ((EditText) findViewById(R.id.editTextCategory)).getText().toString());
    cv.put(Columns.PROJECT_STATUS, mStatus);
    cv.put(Columns.PROJECT_PROJECTDUEDATE, ((EditText) findViewById(R.id.editDate)).getText().toString());
    cv.put(Columns.PROJECT_INVESTOR_ID, mInvId);
    cv.put(Columns.PROJECT_CUSTOMER_ID, mCustId);
    cv.put(Columns.PROJECT_PRIORITY, mPriority);

    if (mIsNew) {
      Uri uri = getContentResolver().insert(ProjectManagerProvider.PROJECT_URI, cv);
      if (uri == null) {
        Log.e("HLAG", "Failed to insert into " + ProjectManagerProvider.PROJECT_URI);
      }
      for (StageType stageType : StageType.values()) {
        by.bsu.courseproject.model.Stage stage = new by.bsu.courseproject.model.Stage();
        by.bsu.courseproject.model.Project project = new by.bsu.courseproject.model.Project();
        project.setId(ContentUris.parseId(uri));
        stage.setProject(project);
        stage.setType(stageType);
        PMApplication.getPMDB().getStageDataSource().persistStage(stage);
      }
    } else {
      getContentResolver().update(ContentUris.withAppendedId(ProjectManagerProvider.PROJECT_URI, mId), cv, null, null);
    }

  }

  private void showDialog(String title) {
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
    ((EditText) findViewById(mPendingView)).setText(val);

  }


}
