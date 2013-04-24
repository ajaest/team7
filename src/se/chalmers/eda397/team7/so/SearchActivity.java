package se.chalmers.eda397.team7.so;

import java.io.IOException;

import se.chalmers.eda397.team7.so.R.id;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class SearchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		final RadioGroup radrioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		final RadioButton radioQuestion = (RadioButton) findViewById(R.id.questionRadio);
		final RadioButton radioTag = (RadioButton) findViewById(R.id.tagRadion);
		final RadioButton radioUser = (RadioButton) findViewById(id.userRadio);
		final SearchView advSearch = (SearchView) findViewById(R.id.advSearchView);
		
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
		PostDataLayer postDL = dlf.createPostDataLayer();


		advSearch.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				if (radioQuestion.isChecked()) {

					searchForQuestion(query);
				}
				else if (radioTag.isChecked()) {
					searchForTags(query);	
				}
				
				else if (radioUser.isChecked()){
					searchForUser(query);
					
				}
				
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
	
				return false;
			}
		});
		

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}



	public void searchForQuestion(String query){
		Toast.makeText(getApplicationContext(),"This is a question " + query, Toast.LENGTH_LONG).show();
		
		
		
		

	}
	
	public void searchForTags(String query){
		Toast.makeText(getApplicationContext(),"This is a tag " + query , Toast.LENGTH_LONG).show();
				
		
	}

    public void searchForUser(String query){
    	
    	Toast.makeText(getApplicationContext(), "This is a user " + query, Toast.LENGTH_LONG).show();
    	
    }
	



}
