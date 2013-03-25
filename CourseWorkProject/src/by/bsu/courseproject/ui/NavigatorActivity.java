package by.bsu.courseproject.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import by.bsu.courseproject.R;


public class NavigatorActivity extends Activity implements View.OnClickListener {


  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.navigator);

    findViewById(R.id.imageView_catalogue).setOnClickListener((android.view.View.OnClickListener) this);
    findViewById(R.id.imageView_projects).setOnClickListener((android.view.View.OnClickListener) this);
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

  public void onClick(View v) {
    Intent intent = new Intent();
    switch (v.getId()) {
    case R.id.imageView_catalogue:
      intent.setClass(NavigatorActivity.this, CatalogueList.class);
      break;
    case R.id.imageView_projects:
      intent.setClass(NavigatorActivity.this, ProjectsActivity.class);
      break;
    default:
      break;
    }

    startActivity(intent);
    return;

  }


}
