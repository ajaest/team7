package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.Comment;
import se.chalmers.eda397.team7.so.data.entity.EntityUtils;
import se.chalmers.eda397.team7.so.data.entity.Post;
import se.chalmers.eda397.team7.so.datalayer.CommentDataLayer;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import so.chalmers.eda397.so.data.entity.CommentListAdapter;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
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
	private ListView commentsListView;
	private ImageButton starButton;
	private Bundle bundle;
	private Integer idQuestion;
	private Post question;
	private PostDataLayer postDataLayer;
	private CommentDataLayer commentDataLayer;
	private ArrayList<Comment> commentList;
	private Set<String> tagSet;
	private SQLiteDatabase db;
	boolean favoriteBool;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_info);

		bundle = getIntent().getExtras();
		idQuestion = bundle.getInt("idQuestion");
		
		
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);
			postDataLayer= factory.createPostDataLayer();
			commentDataLayer = factory.createCommentDataLayer();
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
		commentsListView = (ListView) findViewById(R.id.listViewCommentsOfQuestion);
		starButton = (ImageButton) findViewById(R.id.imageButtonStarQuestion);
		questionTitleTextView.setText(question.getTitle());
		questionBodyTextView.setText(EntityUtils.extractText(question.getBody()),BufferType.SPANNABLE);
		ownerTextView.setText(postDataLayer.getOwnerQuestion(idQuestion).getDisplay_name());
		favoriteBool = false;
		starButton.setBackgroundResource(R.drawable.star);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
		dateQuestionTextView.setText(df.format(question.getCreation_date()));
		if(question.getView_count()==0)
			nViewsTextView.setText("");
		else
			nViewsTextView.setText(question.getView_count().toString());
		
		showTags();
		
		if (question.getComment_count()==0) 
			SeeAllAnswers.setVisibility(View.GONE);
		else 
			SeeAllAnswers.setText("See " + question.getComment_count().toString() + " comments");
		
		// StarImageButtonEventHandeler
		starButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Change the picture and later add to the table of favorites
			    if (favoriteBool){
			    	starButton.setBackgroundResource(R.drawable.yellowstar);
			    }
			    else{
			    	starButton.setBackgroundResource(R.drawable.star);
			    
			    }
			    favoriteBool = !favoriteBool;
				
			
				
			}
		});

	}
	
	//Callback method for the button seeAnswers
	public void showComments(View view){
		
		//To solve the problem of having the ListView inside a ScrollView
		commentsListView.setOnTouchListener(new ListView.OnTouchListener() {
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
		commentList = (ArrayList<Comment>)commentDataLayer.getCommentsByPostId(idQuestion);

		commentsListView.setAdapter(new CommentListAdapter(this, commentList, R.layout.comment_item, db));
	}
	

	
	
	
	//We show the list of tags in the view
	private void showTags(){
		tagSet = question.getTags();
		StringBuilder sbBuilder = new StringBuilder("Tags: ");
		for (String tag : tagSet) {
			sbBuilder.append(tag + " ");
		}
		tagListTextView.setText(sbBuilder.toString());
	}


}
