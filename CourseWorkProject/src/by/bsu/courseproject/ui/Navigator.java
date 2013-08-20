package by.bsu.courseproject.ui;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import by.bsu.courseproject.R;
import by.bsu.courseproject.util.SynchronizationTask;


public class Navigator extends Activity implements View.OnClickListener {


  private static final int LOAD_DATA = 0;
  private static final int SAVE_DATA = 1;
  private ProgressDialog mProgress;

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.navigator);

    findViewById(R.id.imageView_catalogue).setOnClickListener(this);
    findViewById(R.id.imageView_projects).setOnClickListener(this);
  }

  @Override
  protected Dialog onCreateDialog(int id) {
    if (id == SynchronizationTask.PROGRESS_DLG_ID && mProgress != null) {
      mProgress.setCancelable(true);
      mProgress.setIcon(0);
      mProgress.setMessage(getResources().getString(R.string.label_saving_data));
      return mProgress;
    }

    return null;
  }

  public void onClick(View v) {
    Intent intent = new Intent();
    switch (v.getId()) {
    case R.id.imageView_catalogue:
      intent.setClass(Navigator.this, CatalogueList.class);
      break;
    case R.id.imageView_projects:
      intent.setClass(Navigator.this, Projects.class);
      break;
    default:
      break;
    }
    startActivity(intent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(Menu.NONE, LOAD_DATA, Menu.NONE, R.string.label_load_data);
    menu.add(Menu.NONE, SAVE_DATA, Menu.NONE, R.string.label_save_data);
    return super.onCreateOptionsMenu(menu);
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case SAVE_DATA:
      mProgress = new ProgressDialog(this);
      new SynchronizationTask(this,
                              mProgress, true).execute();
      break;
    case LOAD_DATA:
      mProgress = new ProgressDialog(this);
      new SynchronizationTask(this,
                              mProgress, false).execute();
      break;
    default:
      return false;
    }

    return true;
  }

}
