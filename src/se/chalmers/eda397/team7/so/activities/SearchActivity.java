package se.chalmers.eda397.team7.so.activities;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.R.id;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
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
		final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		final RadioButton radioQuestion = (RadioButton) findViewById(R.id.questionRadio);
		final RadioButton radioTag = (RadioButton) findViewById(R.id.tagRadion);
		final RadioButton radioUser = (RadioButton) findViewById(id.userRadio);
		final SearchView advSearch = (SearchView) findViewById(R.id.advSearchView);



		advSearch.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				if (radioQuestion.isChecked()) {
					Intent intent = new Intent(SearchActivity.this, QuestionsActivity.class);
					intent.putExtra("typeSearch", 1);
					intent.putExtra("query", query);
					startActivity(intent);
				}
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




}
