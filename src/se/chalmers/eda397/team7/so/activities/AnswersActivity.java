package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.util.List;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.Answer;
import se.chalmers.eda397.team7.so.data.entity.Question;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import so.chalmers.eda397.team7.so.widget.AnswerListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class AnswersActivity extends Activity {

	private ListView answersListView;
	private List<Answer> answersList;
	private Bundle bundle;
	private Integer idQuestion;
	private SQLiteDatabase db;
	private PostDataLayer postDataLayer;
	private Question question;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answers);
		
		bundle = getIntent().getExtras();
		idQuestion = bundle.getInt("idQuestion");
		
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);
			postDataLayer= factory.createPostDataLayer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		question = postDataLayer.getQuestionById(idQuestion);
		answersList = question.getAnswers();
		answersListView = (ListView) findViewById(R.id.listViewAnswers);
		answersListView.setAdapter(new AnswerListAdapter(this, answersList, R.layout.answer_item));
		answersListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){

				Integer ide = answersList.get(position).getId();
				Intent intent = new Intent(view.getContext(), CommentsActivity.class);
				intent.putExtra("idPost", ide);
				startActivity(intent);
			}
		});        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.answers, menu);
		return true;
	}

}
