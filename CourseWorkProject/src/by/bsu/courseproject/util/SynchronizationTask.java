package by.bsu.courseproject.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.view.KeyEvent;

public class SynchronizationTask extends AsyncTask<Object, String, Boolean> {

  public static final int PROGRESS_DLG_ID = 10;
  private final Context mContext;
  private ProgressDialog mProgress;
  private final boolean isExport;

  public SynchronizationTask(Context context, boolean isExport) {
    this.mContext = context;
    this.isExport = isExport;
  }

  SynchronizationTask setProgressDialog(final ProgressDialog mProgress) {
    this.mProgress = mProgress;
    if (mProgress != null) {
      mProgress.setCancelable(true);
      mProgress.setOnKeyListener(new OnKeyListener() {

        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
          if (dialog != null && keyCode == KeyEvent.KEYCODE_BACK) {
            cancel(true);
            dialog.dismiss();
          }
          return true;
        }
      });
    }
    return this;
  }

  @Override
  protected void onPreExecute() {
    super.onPreExecute();
//    if (mProgress != null) {
//      if (!mProgress.isShowing()) {
//        ((Activity) mContext).showDialog(PROGRESS_DLG_ID);
//      }
//    }
    if (isExport) {
      mProgress = ProgressDialog.show(mContext, "Ожидайте", "Сохранение данных ...");
    } else {
      mProgress = ProgressDialog.show(mContext, "Ожидайте", "Загрузка данных ...");
    }
  }

  @Override
  protected Boolean doInBackground(Object... params) {
    return isExport ? ExportData.exportData(mContext) : ImportData.importData(mContext);
  }


  @Override
  protected void onPostExecute(Boolean result) {
    if (mProgress != null) {
      mProgress.dismiss();
    }
  }

}
