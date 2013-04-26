package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.Post;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import so.chalmers.eda397.so.data.entity.PostListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class QuestionSortByNoA extends Activity{

	private ListView questionListView;
	private List<Post> questionList= new ArrayList<Post>();
	private Bundle bundle;
	String query="";
	int typeSearch = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions);
		PostDataLayer postDataLayer = null;
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			SQLiteDatabase db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);
			postDataLayer = factory.createPostDataLayer();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bundle = getIntent().getExtras();
		if (bundle != null){
			typeSearch = bundle.getInt("typeSearch");
			query = bundle.getString("query");
		}
		if (typeSearch == 1){ // It questions
			questionList = retriveSearchList(postDataLayer, query);
		}
		
		else if (typeSearch == 2){
			questionList = retriveQuestionListByTag(postDataLayer, query);
			
		}
		else {
			questionList = retrieveList(postDataLayer);
		}

		questionListView = (ListView)findViewById(R.id.listViewQuestions);
		questionListView.setAdapter(new PostListAdapter(this, questionList, R.layout.question_item));
		questionListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){

				Integer ide = questionList.get(position).getId();
				Intent intent = new Intent(view.getContext(), QuestionInformation.class);
				intent.putExtra("idQuestion", ide);
				startActivity(intent);
			}
		});        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.questions, menu);
		return true;
	}

	private ArrayList<Post> retrieveList(PostDataLayer postDataLayer){
		ArrayList<Post> questions = new ArrayList<Post>();

		//questions = new ArrayList<Post>(postDataLayer.getQuestionList());
		questions = new ArrayList<Post>(postDataLayer.getQuestionSortedBy("answer_count"));
		return questions;
	}

	private ArrayList<Post> retriveSearchList(PostDataLayer postDataLayer, String query){
		ArrayList<Post> questions = new ArrayList<Post>();
		Set<String> wordSet = new HashSet<String>();
		String[] parts = query.split(" ");
		for (String word : parts) {
			wordSet.add(word);
		}
		questions.addAll(postDataLayer.pagedFullTextSearch(wordSet, 50, 1));


		return questions; 
	}
	
	private List<Post> retriveQuestionListByTag(PostDataLayer postDataLayer, String query){
		List<Post> questions = new ArrayList<Post>();
		Set<String> wordSet = new HashSet<String>();
		String[] parts = query.split(" ");
		for (String word : parts) {
			wordSet.add(word);
		}
		questions = postDataLayer.pagedTagSearch(wordSet, 50, 1);
		return questions;
	}
}
