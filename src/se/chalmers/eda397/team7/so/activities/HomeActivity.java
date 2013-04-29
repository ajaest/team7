package se.chalmers.eda397.team7.so.activities;


import java.io.IOException;
import java.util.List;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.SearchActivity;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.Comment;
import se.chalmers.eda397.team7.so.data.entity.Post;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

	private ActionBar actionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/*
		 * setting buttorns
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

		// database stuff
		Context ctx = this.getApplicationContext();
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



		// search listener
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				Integer i = null;
				try {
					i = Integer.parseInt(query);

				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),"Not a number", Toast.LENGTH_SHORT).show();
					//System.out.println(e.getMessage());
					return false;
				}

				Post p = postDL.getPostById(i);
				if (p != null){
					Toast.makeText(getApplicationContext(), p.getBody(), Toast.LENGTH_SHORT).show();	
					List<Comment> comments = p.getComments();
				}
				else{
					Toast.makeText(getApplicationContext(), "ID not in database", Toast.LENGTH_SHORT).show();	
				}
				return false;


			}

			@Override
			public boolean onQueryTextChange(String newText) {
				//Toast.makeText(getApplicationContext(), newText + " newtext", Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// button on click listers
		QButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent =  new Intent(HomeActivity.this, QuestionsActivity.class);
				startActivity(intent);

			}
		});

		UButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, UserListActivity.class);
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
				startActivity(intent);

			}
		});
		// text on click
		advSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
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
