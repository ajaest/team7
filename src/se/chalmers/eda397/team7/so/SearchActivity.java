package se.chalmers.eda397.team7.so;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.chalmers.eda397.team7.so.R.id;
import se.chalmers.eda397.team7.so.activities.HomeActivity;
import se.chalmers.eda397.team7.so.activities.QuestionsActivity;
import se.chalmers.eda397.team7.so.activities.Questions_Tab_Activity;
import se.chalmers.eda397.team7.so.activities.UserListActivity;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.Post;
import se.chalmers.eda397.team7.so.data.entity.User;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import se.chalmers.eda397.team7.so.datalayer.UserDataLayer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;
/*
 * TypeSearch
 * 	1 = SearchFullText Question
 * 	2 = Search Tag Question
 *  3 = Search User 
 */ 
public class SearchActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		// XML thingys
		AutoCompleteTextView singelUserSearch = (AutoCompleteTextView) findViewById(R.id.singelUserSearch);
		final MultiAutoCompleteTextView multiTagSearch = (MultiAutoCompleteTextView) findViewById(R.id.multiTagSearch);
		ImageButton imgButton = (ImageButton) findViewById(R.id.multiTagButton);
		
		// SingelSeach
		final String[] Users = new String[] {"Coincon", "eHenrik", "Murat", "Luis" };
		ArrayAdapter<String> userAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, Users);
		singelUserSearch.setAdapter(userAdapter);
		// MultiSeach
		final String[] Tags = new String[] {"Java", "XML", "HTML", "Android", "Facebook"
		};

		ArrayAdapter<String> tagAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, Tags);
		multiTagSearch.setAdapter(tagAdapter);
		multiTagSearch.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		
			
		imgButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String query = "";
				query = multiTagSearch.getText().toString();
				query = query.replaceAll(",", "");
				query=query.toLowerCase();
				Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
				
				Intent intent = new Intent(SearchActivity.this, Questions_Tab_Activity.class);
				intent.putExtra("typeSearch", 2);
				intent.putExtra("query", query);
				startActivity(intent);
				
			}
		});
		
	}








@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.search, menu);
	return true;
}




}

/*    *******Commet''''''''''''
		else if (radioTag.isChecked()) {
			Intent intent = new Intent(SearchActivity.this, QuestionsActivity.class);
			intent.putExtra("typeSearch", 2);
			intent.putExtra("query", query);
			startActivity(intent);
		}

		else if (radioUser.isChecked()){
			Intent intent = new Intent(SearchActivity.this, UserListActivity.class);
			intent.putExtra("typeSearch", 3);
			intent.putExtra("query", query);
			startActivity(intent);
 */
