package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.Question;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer.OrderCriteria;
import so.chalmers.eda397.team7.so.widget.PostListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class QuestionSortByDate extends Activity{

	private ListView questionListView;
	private List<Question> questionList;
	private Bundle bundle;
	private String query="";
	private int userID;
	private int typeSearch = 0;
	private PostListAdapter listAdapter;
	private boolean isHeat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions);
		bundle = getIntent().getExtras();
		if (bundle != null){
			typeSearch = bundle.getInt("typeSearch");
			query = bundle.getString("query");
			query= query==null?"":query;
			userID = bundle.getInt("UserID");
			isHeat = bundle.getBoolean("isHeat");
		}
		
		inflateList();
		
	}
	
	
	@Override
	protected void onRestart() {
		inflateList();      
		super.onRestart();
	}
	@Override
	protected void onResume() {
		inflateList();
		super.onResume();
	}


	/**
	 * We load the list of questions from the db according to the type of search we are doing
	 */
	public void inflateList(){ 
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
	    if (typeSearch == 1){ // questions from search
			questionList = retriveSearchList(postDataLayer, query);
		}
	    else if(typeSearch == 11){
			questionList = retrieveList(postDataLayer);
		}
		else if (typeSearch == 2){ //show questions by tags
			questionList = retriveQuestionListByTagOrderByDate(postDataLayer, query);
			
		}
		else if (typeSearch == 3){ //show questions by tags
			questionList = retriveQuestionListByTag(postDataLayer, query);
			
		}
		else if (typeSearch == 4){//get questions of a specific user
			questionList = postDataLayer.getQuestionsOfUser(userID, OrderCriteria.CREATION_DATE);
		}
		else if (typeSearch == 5){//get favorite questions of a user
			questionList = postDataLayer.getAllFavourite(userID, OrderCriteria.CREATION_DATE);
		}
		else {
			questionList = retrieveList(postDataLayer);
		}		

		questionListView = (ListView)findViewById(R.id.listViewQuestions);
		listAdapter = new PostListAdapter(this, questionList, R.layout.question_item, isHeat);
		questionListView.setAdapter(listAdapter);
		questionListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){

				Integer ide = questionList.get(position).getId();
				Intent intent = new Intent(view.getContext(), QuestionItemTab.class);
				intent.putExtra("idQuestion", ide);
				intent.putExtra("UserID", userID);
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

	private List<Question> retrieveList(PostDataLayer postDataLayer){
		List<Question> questions ;

		questions = postDataLayer.getQuestionSortedBy("creation_date");
		return questions;
	}

	private List<Question> retriveSearchList(PostDataLayer postDataLayer, String query){
		ArrayList<Question> questions = new ArrayList<Question>();
		Set<String> wordSet = new HashSet<String>();
		String[] parts = query.split(" ");
		for (String word : parts) {
			wordSet.add(word);
		}
		questions.addAll(postDataLayer.pagedFullTextSearch(wordSet, 50, 0));


		return questions; 
	}
	
	private List<Question> retriveQuestionListByTagOrderByDate(PostDataLayer postDataLayer, String query){
		List<Question> questions = new ArrayList<Question>();
		Set<String> wordSet = new HashSet<String>();
		String[] parts = query.split(" ");
		for (String word : parts) {
			wordSet.add(word);
		}
		questions = postDataLayer.pagedTagSearch(wordSet, OrderCriteria.CREATION_DATE, 50, 0);
		return questions;
	}
	
	private List<Question> retriveQuestionListByTag(PostDataLayer postDataLayer, String query){
		List<Question> questions = new ArrayList<Question>();
		Set<String> wordSet = new HashSet<String>();
		String[] parts = query.split(" ");
		for (String word : parts) {
			wordSet.add(word);
		}
		questions = postDataLayer.pagedTagSearch(wordSet,  50, 0);
		return questions;
	}
}
