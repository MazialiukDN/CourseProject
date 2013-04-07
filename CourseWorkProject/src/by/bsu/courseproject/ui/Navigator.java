package by.bsu.courseproject.ui;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import by.bsu.courseproject.R;


public class Navigator extends Activity implements View.OnClickListener {


  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.navigator);

    findViewById(R.id.imageView_catalogue).setOnClickListener((android.view.View.OnClickListener) this);
    findViewById(R.id.imageView_projects).setOnClickListener((android.view.View.OnClickListener) this);
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
    return;

  }


}
