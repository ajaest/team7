package se.chalmers.eda397.team7.so.activities;


import se.chalmers.eda397.team7.so.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	int userID;

	private ActionBar actionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/*
		 * setting buttons
		 */
		
		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		final Button QButton = (Button) findViewById(R.id.home_question);
		final Button UButton = (Button) findViewById(R.id.home_Users);
		final Button MYButton = (Button) findViewById(R.id.home_myaccount);
		final Button TagButton = (Button) findViewById(R.id.home_tag);
		final SearchView searchView = (SearchView) findViewById(R.id.searchView1);
		final TextView advSearch = (TextView) findViewById(R.id.home_textview);
		// just to get text underlined
		advSearch.setPaintFlags(advSearch.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		Bundle bundle;
		
		bundle = getIntent().getExtras();

			userID = bundle.getInt("UserID");
	

		// search listener
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				
				Intent intent = new Intent(HomeActivity.this, Questions_Tab_Activity.class);
				intent.putExtra("typeSearch", 1);
				intent.putExtra("tagPressed",query);
				intent.putExtra("query", query);
				intent.putExtra("UserID", userID);

				startActivity(intent);

				return false;


			}

			@Override
			public boolean onQueryTextChange(String newText) {
				//Toast.makeText(getApplicationContext(), newText + " newtext", Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// qustionButton on click listers
		QButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent =  new Intent(HomeActivity.this, Questions_Tab_Activity.class);
				intent.putExtra("tagPressed", " ");
				intent.putExtra("UserID", userID);
				startActivity(intent);

			}
		});

		UButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, Users_Tab_Activity.class);
				intent.putExtra("UserID", userID);
				startActivity(intent);
			}
		});


		MYButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Please implement me ", Toast.LENGTH_SHORT).show();

			}
		});

		TagButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, TagCloudActivity.class);
				intent.putExtra("UserID", userID);
				startActivity(intent);

			}
		});
		// text on click
		advSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
				intent.putExtra("UserID", userID);
				startActivity(intent);
				
			}
		});
	}
	

	



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

}
