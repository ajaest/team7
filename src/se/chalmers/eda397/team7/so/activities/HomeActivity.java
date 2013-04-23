package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;

import se.chalmers.eda397.team7.so.Question;
import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/*
		 * setting buttorns
		 */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		final Button QButton = (Button) findViewById(R.id.home_question);
		final Button UButton = (Button) findViewById(R.id.home_Users);
		final Button MYButton = (Button) findViewById(R.id.home_myaccount);
		final Button TagButton = (Button) findViewById(R.id.home_tag);
		final SearchView searchView = (SearchView) findViewById(R.id.searchView1);
		
		
		
		/*
		 * Database things 
		 
		
        Context ctx = this.getApplicationContext();

        SQLiteSODatabaseHelper databaseHelper;
		try {
			databaseHelper = new SQLiteSODatabaseHelper(ctx);
			SQLiteDatabase db = databaseHelper.getWritableDatabase();
			
	        DataLayerFactory dlf = new DataLayerFactory(db);
	        PostDataLayer postDL = dlf.createPostDataLayer();
	        Post p = postDL.getPostById(22);
	        
	       //List<Comment> comments = p.getComments();
	       Toast.makeText(getApplicationContext(), p.getComments().toString(), Toast.LENGTH_LONG);
		} catch (IOException e1) {
			Toast.makeText(getApplicationContext(), "crap", Toast.LENGTH_SHORT);
			e1.printStackTrace();
		}
		*/
        
		
		searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
				Question question = null;
				SQLiteSODatabaseHelper test;
				try {
					test = new SQLiteSODatabaseHelper(getApplicationContext());
					SQLiteDatabase db = test.getWritableDatabase();
					Cursor c = db.rawQuery("SELECT * FROM posts WHERE title=" + query, new String[]{});
						while(c.moveToNext()){
							question = new Question(c.getString(15), Integer.parseInt(c.getString(17)));

						}
						if (question == null){
							Toast.makeText(getApplicationContext(), "Sorry did not find anything", Toast.LENGTH_SHORT).show();
						}
						else{
							Toast.makeText(getApplicationContext(), "Question: "+ question.getTitle() + "Answers " + question.getnAnswers(), Toast.LENGTH_LONG).show();
						}
						
						
						
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

				
				
				
		
				
					
					
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				//Toast.makeText(getApplicationContext(), newText + " newtext", Toast.LENGTH_SHORT).show();
				return false;
			}
		});

	
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
				Intent intent = new Intent(HomeActivity.this, UserActivity.class);
				startActivity(intent);
				
			}
		});
		
		TagButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "Please implement me ", Toast.LENGTH_SHORT).show();
				
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
