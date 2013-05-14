package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import se.chalmers.eda397.team7.so.datalayer.UserDataLayer;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

/*
 * TypeSearch
 * 	1 = SearchFullText Question
 * 	2 = Search Tag Question
 *  3 = Search User 
 *  
 */ 


@SuppressLint("DefaultLocale")
public class SearchActivity extends Activity {
	
	private  Context ctx;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		// XML thingys
		final AutoCompleteTextView singelUserSearch = (AutoCompleteTextView) findViewById(R.id.singelUserSearch);
		final MultiAutoCompleteTextView multiTagSearch = (MultiAutoCompleteTextView) findViewById(R.id.multiTagSearch);
		ImageButton imgButton = (ImageButton) findViewById(R.id.multiTagButton);
		ctx = this.getApplicationContext();
		SQLiteSODatabaseHelper databaseHelper = null;
		try {
			databaseHelper = new SQLiteSODatabaseHelper(ctx);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SQLiteDatabase db = databaseHelper.getWritableDatabase();
		DataLayerFactory dlf = new DataLayerFactory(db);
		final PostDataLayer postDL = dlf.createPostDataLayer();
		final UserDataLayer userDL = dlf.createUserDataLayer();
		Bundle bundle;
		final int userID;
		bundle = getIntent().getExtras();
    	userID = bundle.getInt("UserID");
    	
		
		
		
		// SingelSeach
		
		ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, userDL.getDistinctListOfUsers());
		singelUserSearch.setAdapter(userAdapter);
		
		singelUserSearch.setOnItemClickListener(new OnItemClickListener() {


			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(SearchActivity.this, UserListActivity.class);
				intent.putExtra("typeSearch", 3);
				intent.putExtra("UserID", userID);
				intent.putExtra("query", singelUserSearch.getText().toString());
				startActivity(intent);
			
				
			}
		});
		// MultiSeach
	


		ArrayAdapter<String> tagAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, postDL.getListOfTags());
		multiTagSearch.setAdapter(tagAdapter);
		multiTagSearch.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		
			
		imgButton.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View v) {
				String query = "";
				query = multiTagSearch.getText().toString();
				
				query = query.replaceAll(",", "");
				query=query.toLowerCase();
				
				String tags[] = query.split(" ");
				if (tags.length>4)
					Toast.makeText(ctx, "Too many tags", Toast.LENGTH_SHORT).show();
				else{
					Intent intent = new Intent(SearchActivity.this, TagCloudActivity.class);
					intent.putExtra("UserID", userID);
					if (tags[0].equals("")){ //We don't search for anything --> we show all the cloud
						intent.putExtra("See_my_tags", false);
						intent.putExtra("multitag", false);
					}
					else{
						intent.putExtra("See_my_tags", false);
						intent.putExtra("query", query);						
						intent.putExtra("isMultitag",true);
					}
					startActivity(intent);
				}
				
				
			}
		});
		
		
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowTitleEnabled(true);
		}
		return true;
	}




}

