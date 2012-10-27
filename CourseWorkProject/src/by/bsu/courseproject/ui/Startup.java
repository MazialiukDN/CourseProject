package by.bsu.courseproject.ui;

import java.util.Arrays;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import by.bsu.courseproject.R;
import by.bsu.courseproject.db.ProjectManagerProvider.Columns;


public class Startup extends Activity {

	private static final int DIALOG_LOGIN_WRONG_USER = 1; 
	private static final int DIALOG_LOGIN_WRONG_PASSWORD = 2;
	
	public static final String EXTRA_INTENT = "INTENT";	

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				String login = ((EditText) findViewById(R.id.editLogin)).getText().toString();
				String password = ((EditText) findViewById(R.id.editPwd)).getText().toString();

				if (login.isEmpty() || password.isEmpty()) {
					Animation shake = AnimationUtils.loadAnimation(Startup.this, R.anim.shake);
					findViewById(login.isEmpty() ? R.id.editLogin : R.id.editPwd).startAnimation(shake);
					return;
				} else {
					Cursor cursor = getContentResolver().query(Columns.AUTHORIZATION_URI, null, null, null, null);
					if (cursor != null){

						if (cursor.moveToFirst()) {

							int colIndex = cursor.getColumnIndex(Columns.SALT);
							if (colIndex != -1){
								String salt = cursor.getString(colIndex);

								colIndex = cursor.getColumnIndex(Columns.LOGIN);
								if (colIndex != -1){
									if (Arrays.equals(Tools.getCipher(login, salt), cursor.getBlob(colIndex))){
										colIndex = cursor.getColumnIndex(Columns.PASSWORD);
										if (colIndex != -1){
											if(Arrays.equals(Tools.getCipher(password, salt), cursor.getBlob(colIndex))){
												cursor.close();
												welcome(Startup.this);
											} else showDialog(DIALOG_LOGIN_WRONG_PASSWORD);
										}
									} else showDialog(DIALOG_LOGIN_WRONG_USER);
								}

							}

						} else {
							ContentValues cv = new ContentValues();

							String salt = UUID.randomUUID().toString();
							cv.put(Columns.SALT, salt);
							cv.put(Columns.LOGIN, Tools.getCipher(login, salt));
							cv.put(Columns.PASSWORD, Tools.getCipher(password, salt));
							
							getContentResolver().insert(Columns.AUTHORIZATION_URI, cv);
							welcome(Startup.this);
						}

					}
				}



			}
		});


	}



	/*public void welcome() {
		Intent intent = new Intent();
		intent.setClass(this, NavigatorActivity.class);
		startActivity(intent);
		finish();
	}
	*/
	public void welcome(Context context) {
    	Intent intent = getIntent();   
    	if (intent.getAction() != null && intent.getAction().equals(intent.ACTION_MAIN)) {
    		intent = new Intent();
    		intent.setClass(this, NavigatorActivity.class);
    		startActivity(intent);
    		finish();
    	} else {
    		context.startActivity((Intent)(intent.getExtras().getParcelable(EXTRA_INTENT)));
    	}
    }

	@Override
	protected void onPause() {		
		super.onPause();		
		Tools.setTime(this);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_LOGIN_WRONG_USER:
		case DIALOG_LOGIN_WRONG_PASSWORD:
			return new AlertDialog.Builder(Startup.this)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("Ошибка авторизации")
			.setMessage(id == DIALOG_LOGIN_WRONG_USER ? "Неверный логин" : "Неверный пароль")
			.setCancelable(false)
			.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();

				}
			})
			.create();        

		}

		return null;
	}



	

}
