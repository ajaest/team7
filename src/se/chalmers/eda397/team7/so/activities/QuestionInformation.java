package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.EntityUtils;
import se.chalmers.eda397.team7.so.data.entity.Post;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import so.chalmers.eda397.so.data.entity.AnswerListAdapter;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class QuestionInformation extends Activity{
	
	//UI reference
	private TextView questionTitleTextView;
	private TextView questionBodyTextView;
	private TextView ownerTextView;
	private TextView tagListTextView;
	private TextView dateQuestionTextView;
	private TextView nViewsTextView;
	private Button SeeAllAnswers;
	private ListView answersListView;
	
	private Bundle bundle;
	private Integer idQuestion;
	private Post question;
	private PostDataLayer postDataLayer;
	private ArrayList<Post> answerList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_info);

		bundle = getIntent().getExtras();
		idQuestion = bundle.getInt("idQuestion");
		
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			SQLiteDatabase db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);
			postDataLayer= factory.createPostDataLayer();
			question = postDataLayer.getPostById(idQuestion);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		questionTitleTextView = (TextView) findViewById(R.id.textViewTitleQuestion);
		questionBodyTextView = (TextView) findViewById(R.id.textViewBodyQuestion);
		ownerTextView = (TextView) findViewById(R.id.textViewOwnerQuestion);
		dateQuestionTextView = (TextView) findViewById(R.id.textViewDateQuestion);
		nViewsTextView = (TextView) findViewById(R.id.textViewNumberViews);
		tagListTextView = (TextView) findViewById(R.id.textViewTagList);
		SeeAllAnswers = (Button) findViewById(R.id.buttonSeeAnswers);
		answersListView = (ListView) findViewById(R.id.listViewAnswersOfQuestion);
		
		questionTitleTextView.setText(question.getTitle());
		questionBodyTextView.setText(EntityUtils.extractText(question.getBody()),BufferType.SPANNABLE);
		ownerTextView.setText(postDataLayer.getOwnerQuestion(idQuestion).getDisplay_name());
		
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
		dateQuestionTextView.setText(df.format(question.getCreation_date()));
		if(question.getView_count()==0)
			nViewsTextView.setText("");
		else
			nViewsTextView.setText(question.getView_count().toString());
		//GET TAGS!!!
		if (question.getAnswer_count()==0) 
			SeeAllAnswers.setVisibility(View.GONE);
		else 
			SeeAllAnswers.setText("See " + question.getAnswer_count().toString() + " answers");
		
		

	}
	
	//Callback method for the button seeAnswers
	public void showAnswers(View view){
		
		//To solve the problem of having the ListView inside a ScrollView
		answersListView.setOnTouchListener(new ListView.OnTouchListener() {
		        @Override
		        public boolean onTouch(View v, MotionEvent event) {
		            int action = event.getAction();
		            switch (action) {
		            case MotionEvent.ACTION_DOWN:
		                // Disallow ScrollView to intercept touch events.
		                v.getParent().requestDisallowInterceptTouchEvent(true);
		                break;

		            case MotionEvent.ACTION_UP:
		                // Allow ScrollView to intercept touch events.
		                v.getParent().requestDisallowInterceptTouchEvent(false);
		                break;
		            }

		            // Handle ListView touch events.
		            v.onTouchEvent(event);
		            return true;
		        }
		    });
		answerList = (ArrayList<Post>)postDataLayer.getAnswersByPostId(idQuestion);

		answersListView.setAdapter(new AnswerListAdapter(this, answerList, R.layout.answer_item));
	}
	
	

}
