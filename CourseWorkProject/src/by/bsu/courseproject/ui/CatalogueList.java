package by.bsu.courseproject.ui;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import by.bsu.courseproject.R;
import by.bsu.courseproject.db.ProjectManagerProvider.Columns;

public class CatalogueList extends ListActivity{

	public static final int PROJECT = 0;
	public static final int CUSTOMER = 1;
	public static final int INVESTOR = 2;
	public static final int EMPLOYEE = 3;

	private final int SIZE = 4;

	private String[] mCategories;
	


	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		mCategories = new String[SIZE];
		mCategories[PROJECT] = this.getString(R.string.label_projects);
		mCategories[CUSTOMER] = this.getString(R.string.label_customers);
		mCategories[INVESTOR] = this.getString(R.string.label_investors);
		mCategories[EMPLOYEE] = this.getString(R.string.label_employees);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, mCategories));

	}

	public static final String PROJECT_COLUMN_PROJECTNAME = "PROJECTNAME";
	public static final String PROJECT_COLUMN_PROJECTDUEDATE = "PROJECTDUEDATE";
	public static final String PROJECT_COLUMN_CATEGORY = "CATEGORY";
	public static final String PROJECT_COLUMN_STATUS = "STATUS";
	public static final String PROJECT_COLUMN_PRIORITY = "PRIORITY";
	public static final String PROJECT_COLUMN_PROJECTDESCRIPTION = "PROJECTDESCRIPTION";



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
	protected void onListItemClick(ListView l, View v, int position, long id) {

		super.onListItemClick(l, v, position, id);

		Intent intent = new Intent();
		intent.setClass(CatalogueList.this, CatalogueFragmentActivity.class);	
		String[] projection = null;
		intent.putExtra(CatalogueFragmentActivity.FROM_LIST, position);
		
		switch(position) {
		case PROJECT:	
			projection = new String[] {
					Columns.COLUMN_NAME_ID,
					Columns.PROJECT_COLUMN_PROJECTNAME,
					Columns.PROJECT_COLUMN_CATEGORY,
					Columns.PROJECT_COLUMN_PRIORITY,
					Columns.PROJECT_COLUMN_STATUS,
					Columns.PROJECT_COLUMN_PROJECTDUEDATE,
					Columns.COLUMN_INV_FIRSTNAME,
					Columns.COLUMN_INV_MIDDLENAME,
					Columns.COLUMN_INV_LASTNAME,
					Columns.COLUMN_CUST_FIRSTNAME,
					Columns.COLUMN_CUST_MIDDLENAME,
					Columns.COLUMN_CUST_LASTNAME
			}; 
				
			intent.setData(Columns.CONTENT_URI);
			intent.putExtra(CatalogueListFragment.ARG_FILTER_COLUMNS,
					new String[] {Columns.PROJECT_COLUMN_PROJECTNAME, 
					Columns.PROJECT_COLUMN_CATEGORY,
					Columns.PROJECT_COLUMN_PRIORITY,
					Columns.PROJECT_COLUMN_STATUS,
					Columns.PROJECT_COLUMN_PROJECTDUEDATE,
					Columns.COLUMN_INV_FIRSTNAME,
					Columns.COLUMN_INV_MIDDLENAME,
					Columns.COLUMN_INV_LASTNAME,
					Columns.COLUMN_CUST_FIRSTNAME,
					Columns.COLUMN_CUST_MIDDLENAME,
					Columns.COLUMN_CUST_LASTNAME});
			intent.putExtra(CatalogueListFragment.ARG_ADAPTER_FROM,
			
					new String[] {Columns.PROJECT_COLUMN_PROJECTNAME, Columns.PROJECT_COLUMN_CATEGORY, Columns.PROJECT_COLUMN_PROJECTDUEDATE});			
		
			intent.putExtra(CatalogueListFragment.ARG_ADAPTER_TO,
					new int[] { android.R.id.text1, android.R.id.text2 });			break;
		case EMPLOYEE:
			projection = new String[] {
					Columns.COLUMN_NAME_ID,
					Columns.COLUMN_FIRSTNAME,
					Columns.COLUMN_MIDDLENAME,
					Columns.COLUMN_LASTNAME,
					Columns.COLUMN_EMAIL,
					Columns.COLUMN_SKYPE,
					Columns.COLUMN_PHONE,
					Columns.COLUMN_BIRTHDAY
			}; 

			intent.setData(Columns.EMPLOYEE_URI);
			intent.putExtra(CatalogueListFragment.ARG_FILTER_COLUMNS,
					new String[] {Columns.COLUMN_FIRSTNAME, Columns.COLUMN_MIDDLENAME,
					Columns.COLUMN_LASTNAME,
					Columns.COLUMN_EMAIL,
					Columns.COLUMN_SKYPE,
					Columns.COLUMN_PHONE,Columns.COLUMN_BIRTHDAY});
			intent.putExtra(CatalogueListFragment.ARG_ADAPTER_FROM,
					new String[] {Columns.COLUMN_LASTNAME, Columns.COLUMN_FIRSTNAME, Columns.COLUMN_MIDDLENAME,
					
					Columns.COLUMN_PHONE});	
			intent.putExtra(CatalogueListFragment.ARG_ADAPTER_TO,
					new int[] { R.id.fName, R.id.mName, R.id.lName, R.id.phone });
			break;
		case CUSTOMER:	
			projection = new String[] {
					Columns.COLUMN_NAME_ID,
					Columns.COLUMN_FIRSTNAME,
					Columns.COLUMN_MIDDLENAME,
					Columns.COLUMN_LASTNAME,
					Columns.COLUMN_EMAIL,
					Columns.COLUMN_SKYPE,
					Columns.COLUMN_PHONE,
					Columns.COLUMN_HANDLE_CUST,
					Columns.COLUMN_HANDLE_INV
			}; 

			intent.setData(Columns.CUST_INV_URI);
			intent.putExtra(CatalogueListFragment.ARG_FILTER_COLUMNS,
					new String[] {Columns.COLUMN_FIRSTNAME, Columns.COLUMN_MIDDLENAME,
					Columns.COLUMN_LASTNAME,
					Columns.COLUMN_EMAIL,
					Columns.COLUMN_SKYPE,
					Columns.COLUMN_PHONE});
			intent.putExtra(CatalogueListFragment.ARG_ADAPTER_FROM,
					new String[] {Columns.COLUMN_LASTNAME, Columns.COLUMN_FIRSTNAME, Columns.COLUMN_MIDDLENAME,
					
					Columns.COLUMN_PHONE});	
			intent.putExtra(CatalogueListFragment.ARG_ADAPTER_TO,
					new int[] { R.id.fName, R.id.mName, R.id.lName, R.id.phone });
			break;
		case INVESTOR: 
			projection = new String[] {
					Columns.COLUMN_NAME_ID,
					Columns.COLUMN_FIRSTNAME,
					Columns.COLUMN_MIDDLENAME,
					Columns.COLUMN_LASTNAME,
					Columns.COLUMN_EMAIL,
					Columns.COLUMN_SKYPE,
					Columns.COLUMN_PHONE,
					Columns.COLUMN_HANDLE_CUST,
					Columns.COLUMN_HANDLE_INV
			}; 

			intent.setData(Columns.CUST_INV_URI);
			intent.putExtra(CatalogueListFragment.ARG_FILTER_COLUMNS,
					new String[] {Columns.COLUMN_FIRSTNAME, Columns.COLUMN_MIDDLENAME,
					Columns.COLUMN_LASTNAME,
					Columns.COLUMN_EMAIL,
					Columns.COLUMN_SKYPE,
					Columns.COLUMN_PHONE});
			intent.putExtra(CatalogueListFragment.ARG_ADAPTER_FROM,
					new String[] {Columns.COLUMN_LASTNAME, Columns.COLUMN_FIRSTNAME, Columns.COLUMN_MIDDLENAME,
					
					Columns.COLUMN_PHONE});	
			intent.putExtra(CatalogueListFragment.ARG_ADAPTER_TO,
					new int[] { R.id.fName, R.id.mName, R.id.lName, R.id.phone });
			break;
		default:
			return;
		}


		intent.putExtra(CatalogueListFragment.ARG_PROJECTION, projection);
		startActivity(intent);
	}
	
	

}


