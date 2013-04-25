package se.chalmers.eda397.team7.so;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import se.chalmers.eda397.team7.so.R.id;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.Post;
import se.chalmers.eda397.team7.so.data.entity.User;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer.PostIndexInformation;
import se.chalmers.eda397.team7.so.datalayer.UserDataLayer;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.provider.UserDictionary.Words;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
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
		final PostDataLayer postDL = dlf.createPostDataLayer();
		final UserDataLayer userDL = dlf.createUserDataLayer();


		advSearch.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {

				if (radioQuestion.isChecked()) {

					searchForQuestion(query,postDL);
				}
				else if (radioTag.isChecked()) {
					searchForTags(query);	
				}

				else if (radioUser.isChecked()){
					searchForUser(query,userDL);

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



	public void searchForQuestion(String query, PostDataLayer postDL){
		Set<String> wordSet = new HashSet<String>();
		List<Post> foundPosts = new ArrayList<Post>();
		String[] parts = query.split(" ");
		for (String word : parts) {
			wordSet.add(word);
		}
		foundPosts.addAll(postDL.pagedFullText(wordSet, 50, 1));

		// testing to print to textview
		final TextView testTextView = (TextView) findViewById(R.id.textView1);
		testTextView.setText("test");
		String tempString = "";
		for (Post p : foundPosts){
			if(p.getTitle() != "null"){
				tempString = tempString + p.getTitle() + System.getProperty("line.separator");
			}
		}

		testTextView.setText(tempString);





	}

	public void searchForTags(String query){
		Toast.makeText(getApplicationContext(),"This is a tag " + query , Toast.LENGTH_LONG).show();



	}

	public void searchForUser(String query, UserDataLayer userDL){
		ArrayList<User> userList = new ArrayList<User>();
		userList.addAll(userDL.searchForUser(query));
		
		// testing to print to textview
		final TextView testTextView = (TextView) findViewById(R.id.textView1);
		testTextView.setText("test");
		String tempString = "";
		for (User u : userList){
			tempString = tempString + u.getDisplay_name() + System.getProperty("line.separator");

		}

		testTextView.setText(tempString);

	}




}
