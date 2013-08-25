package by.bsu.courseproject.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import by.bsu.courseproject.R;


public class DescriptionEditor extends Activity 
	implements View.OnClickListener {    
    
    public static final String EXTRA_DESCRIPTION = "TORO_DESCRIPTION";

    private EditText mText;
    private String mOriginalContent = null;

    public static class LinedEditText extends EditText {
        private Rect mRect;
        private Paint mPaint;

        public LinedEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
            
            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(0x80000000);
        }
        
        @Override
        protected void onDraw(Canvas canvas) {
            int count = getLineCount();
            Rect r = mRect;
            Paint paint = mPaint;

            for (int i = 0; i < count; i++) {
                int baseline = getLineBounds(i, r);

                canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
            }

            super.onDraw(canvas);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        if (savedInstanceState == null)
        	mOriginalContent = intent.getStringExtra(EXTRA_DESCRIPTION);        	
        
        overridePendingTransition(R.anim.pull_in, 0);
       
        setContentView(R.layout.description_editor);
        
        mText = (EditText) findViewById(R.id.description);
        
        findViewById(R.id.buttonSave).setOnClickListener((android.view.View.OnClickListener) this); 
		findViewById(R.id.buttonCancel).setOnClickListener((android.view.View.OnClickListener) this);        
    }
    
    public void onClick(View v) {    	
		Intent intent = new Intent();
		
		switch(v.getId()) {
			case R.id.buttonSave:
				String text = mText.getText().toString();			
		        intent.putExtra(EXTRA_DESCRIPTION, text);
		        setResult(RESULT_OK, intent);	        
		        finish();
		        break;
			case R.id.buttonCancel:
				setResult(RESULT_CANCELED, intent);		
		        finish();
		        break;				
			default:
		}				
	}

    @Override
    protected void onResume() {
        super.onResume();
        
        if (mOriginalContent != null)
        	mText.setTextKeepState(mOriginalContent);      
    } 
    
    @Override
    protected void onPause() {
    	if(isFinishing()) {
    		overridePendingTransition(0, R.anim.pull_out);
    	}    	
    	super.onPause();
    }    
}
    

