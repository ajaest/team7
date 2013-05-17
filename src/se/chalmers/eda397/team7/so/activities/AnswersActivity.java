package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.util.List;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.Answer;
import se.chalmers.eda397.team7.so.data.entity.Question;
import se.chalmers.eda397.team7.so.datalayer.AnswerDataLayer;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import so.chalmers.eda397.team7.so.widget.AnswerListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;



public class AnswersActivity extends Activity {

	private ListView answersListView;
	private List<Answer> answersList;
	private Bundle bundle;
	private Integer idQuestion;
	private SQLiteDatabase db;
	private PostDataLayer postDataLayer;
	private AnswerDataLayer answerDataLayer;
	private Question question;
	private Button answerQuestionButton;
	private Integer userId;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_answers);
		bundle = getIntent().getExtras();
		idQuestion = bundle.getInt("idQuestion");
		userId = bundle.getInt("UserID");
		inflateList();
		
	
	}

	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.context_menu_answers, menu);
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
	
	
	
	public void inflateList(){
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);
			postDataLayer= factory.createPostDataLayer();
			answerDataLayer = factory.createAnswerDataLayer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		answerQuestionButton = (Button) findViewById(R.id.buttonAnswerQuestion);
		question = postDataLayer.getQuestionById(idQuestion);
		answersList = question.getAnswers();
		answersListView = (ListView) findViewById(R.id.listViewAnswers);
		this.registerForContextMenu(answersListView);//For the floating menu
		answersListView.setAdapter(new AnswerListAdapter(this, answersList, R.layout.answer_item));
		answersListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){

				Integer ide = answersList.get(position).getId();
				
				Intent intent = new Intent(view.getContext(), CommentsActivity.class);
				intent.putExtra("idPost", ide);
				intent.putExtra("UserID",userId);
				startActivity(intent);
			}
		});        
		
		answerQuestionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent =  new Intent(AnswersActivity.this, PopupAnswerOrCommentActivity.class);
				Log.d("mytest", "id "+idQuestion.toString());
				intent.putExtra("idPost", idQuestion);
				intent.putExtra("UserID", userId);
				intent.putExtra("isComment", false);
				startActivity(intent);

			}
		});
	
	}

	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	   AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	    //info.position is the position in the list
	    Integer position = info.position;
	    Integer ide = answersList.get(position).getId();
	    

    	
	    switch (item.getItemId()) {
	        case R.id.voteUpAnswer:
	        	answerDataLayer.voteUp(ide);
	        	inflateList();
	        	return true;
	        case R.id.voteDownAnswer:
	        	answerDataLayer.voteDown(ide); 
	        	inflateList();
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	    
	    
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.answers, menu);
		return true;
	}

}
