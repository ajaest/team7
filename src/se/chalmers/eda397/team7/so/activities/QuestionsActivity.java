package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.util.ArrayList;

import se.chalmers.eda397.team7.so.Question;
import se.chalmers.eda397.team7.so.QuestionListAdapter;
import se.chalmers.eda397.team7.so.R;
import so.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.widget.ListView;

public class QuestionsActivity extends Activity {

	private ListView questionListView;
	private ArrayList<Question> questionList= new ArrayList<Question>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions);
		
		questionList = retrieveList();
		questionListView = (ListView)findViewById(R.id.listViewQuestions);
		questionListView.setAdapter(new QuestionListAdapter(this, questionList, R.layout.question_item));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.questions, menu);
		return true;
	}
	
	private ArrayList<Question> retrieveList(){
		ArrayList<Question> questions = new ArrayList<Question>();
		Question question;
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			SQLiteDatabase db = test.getWritableDatabase();
			Cursor c = db.rawQuery("SELECT title, COALESCE(answer_count, 0) FROM posts where post_type_id=1 ORDER BY creation_date LIMIT 40", new String[]{});
				while(c.moveToNext()){
					question = new Question(c.getString(0), Integer.parseInt(c.getString(1)));
					questions.add(question);
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return questions;
	}

}
