package by.bsu.courseproject.ui;


import android.content.Intent;
import android.os.Bundle;


import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import by.bsu.courseproject.R;

public class CatalogueFragmentActivity extends FragmentActivity {


	private static final int IDM_NEW_PROJECT = 101;
	private static final int IDM_NEW_EMPLOYEE = 102;
	private static final int IDM_NEW_CUSTOMER = 103;
	private static final int IDM_NEW_INVESTOR = 104;
	
	private static final int IDM_ALL = 105;
	private static final int IDM_TODAY = 106;
	private static final int IDM_WEEK = 107;
	private static final int IDM_MONTH = 108;

	private int mCurrList = -1;

	public static final String FROM_LIST = "FROM_LIST";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_filter);
        
		mCurrList = getIntent().getIntExtra(FROM_LIST, -1);
		switch(mCurrList) {
		case CatalogueList.PROJECT:
			this.setTitle(R.string.label_projects);
			break;	
		case CatalogueList.EMPLOYEE:
			this.setTitle(R.string.label_employees);
			break;
		case CatalogueList.CUSTOMER:
			this.setTitle(R.string.label_customers);
			break;
		case CatalogueList.INVESTOR:
			this.setTitle(R.string.label_investors);
			break;
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		switch(mCurrList) {
		case CatalogueList.PROJECT:
			menu.add(Menu.NONE, IDM_NEW_PROJECT, Menu.NONE, R.string.label_new_project);
			menu.add(1, IDM_ALL, Menu.NONE, "Все").setIcon(android.R.drawable.ic_menu_sort_alphabetically);
			menu.add(1, IDM_TODAY, Menu.NONE, "На сегодня").setIcon(android.R.drawable.ic_menu_today);
			menu.add(1, IDM_WEEK, Menu.NONE, "На неделю").setIcon(android.R.drawable.ic_menu_week);
			menu.add(1, IDM_MONTH, Menu.NONE, "На месяц").setIcon(android.R.drawable.ic_menu_month);
			break;	
		case CatalogueList.EMPLOYEE:
			menu.add(Menu.NONE, IDM_NEW_EMPLOYEE, Menu.NONE, R.string.label_new_employee).setIcon(android.R.drawable.ic_menu_add);
			break;
		case CatalogueList.CUSTOMER:
			menu.add(Menu.NONE, IDM_NEW_CUSTOMER, Menu.NONE, R.string.label_new_customer).setIcon(android.R.drawable.ic_menu_add);
			break;
		case CatalogueList.INVESTOR:
			menu.add(Menu.NONE, IDM_NEW_INVESTOR, Menu.NONE, R.string.label_new_investor).setIcon(android.R.drawable.ic_menu_add);
			break;
		}

		return super.onCreateOptionsMenu(menu);
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		switch (item.getItemId()) {
		case IDM_NEW_PROJECT:
			intent.putExtra(NewEmployeeActivity.FROM_LIST, NewEmployeeActivity.MENU);
			intent.setClass(getApplicationContext(), NewProject.class);
			startActivity(intent);
			break;
		case IDM_ALL:
			this.setTitle("Проекты (все)");
			((CatalogueListFragment) getSupportFragmentManager().findFragmentById(R.id.CatalogueListFragment)).changeFilter(CatalogueListFragment.ALL);
			break;
		case IDM_TODAY:
			this.setTitle("Проекты (на сегодня)");
			((CatalogueListFragment) getSupportFragmentManager().findFragmentById(R.id.CatalogueListFragment)).changeFilter(CatalogueListFragment.TODAY);
			break;
		case IDM_WEEK:
			this.setTitle("Проекты (на неделю)");
			((CatalogueListFragment) getSupportFragmentManager().findFragmentById(R.id.CatalogueListFragment)).changeFilter(CatalogueListFragment.WEEK);
			break;
		case IDM_MONTH:
			this.setTitle("Проекты (на месяц)");
			((CatalogueListFragment) getSupportFragmentManager().findFragmentById(R.id.CatalogueListFragment)).changeFilter(CatalogueListFragment.MONTH);
			break;
		case IDM_NEW_EMPLOYEE:
			intent.setClass(getApplicationContext(), NewEmployeeActivity.class);
			startActivity(intent);
			break;
		case IDM_NEW_CUSTOMER:
			intent.putExtra(NewCustomerInvestorActivity.INV, NewCustomerInvestorActivity.cust);
			intent.setClass(getApplicationContext(), NewCustomerInvestorActivity.class);
			startActivity(intent);
			break;
			
		case IDM_NEW_INVESTOR:
			intent.putExtra(NewCustomerInvestorActivity.INV, NewCustomerInvestorActivity.inv);
			intent.setClass(getApplicationContext(), NewCustomerInvestorActivity.class);
			startActivity(intent);
			break;
		default:
			return false;
		}
		
		return true;
	}

}
