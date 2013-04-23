package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.util.ArrayList;

import se.chalmers.eda397.team7.so.R;
import so.chalmers.eda397.so.data.entity.Post;
import so.chalmers.eda397.so.data.entity.PostListAdapter;
import so.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import so.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import so.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import android.os.Bundle;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.widget.ListView;

public class QuestionsActivity extends Activity {

	private ListView questionListView;
	private ArrayList<Post> questionList= new ArrayList<Post>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions);
		
		questionList = retrieveList();
		questionListView = (ListView)findViewById(R.id.listViewQuestions);
		questionListView.setAdapter(new PostListAdapter(this, questionList, R.layout.question_item));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.questions, menu);
		return true;
	}
	
	private ArrayList<Post> retrieveList(){
		ArrayList<Post> questions = new ArrayList<Post>();
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			SQLiteDatabase db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);
			PostDataLayer postDataLayer = factory.createPostDataLayer();
			questions = postDataLayer.getQuestionList();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return questions;
	}

}
