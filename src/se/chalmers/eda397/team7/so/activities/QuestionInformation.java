package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.EntityUtils;
import se.chalmers.eda397.team7.so.data.entity.Post;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer.OrderCriteria;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
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
	private ImageButton starButton;
	private Bundle bundle;
	private Integer idQuestion;
	private Post question;
	private PostDataLayer postDataLayer;
	private Set<String> tagSet;
	private SQLiteDatabase db;
	private Integer userID;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_info);
		
		
		bundle = getIntent().getExtras();
		idQuestion = bundle.getInt("idQuestion");
		userID = bundle.getInt("UserID");
		inflateList();
		

	}
	@Override
	protected void onResume(){
		inflateList();
		super.onResume();
	}

	
	@Override
	protected void onRestart() {
		   inflateList(); 
		super.onRestart();
	}
	public void inflateList(){
		
		
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);

			postDataLayer= factory.createPostDataLayer();
			question = postDataLayer.getQuestionById(idQuestion);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		questionTitleTextView = (TextView) findViewById(R.id.textViewTitleQuestion);
		questionBodyTextView = (TextView) findViewById(R.id.textViewBodyQuestion);
		ownerTextView = (TextView) findViewById(R.id.textViewOwnerQuestion);
		dateQuestionTextView = (TextView) findViewById(R.id.textViewDateQuestion);
		nViewsTextView = (TextView) findViewById(R.id.textViewNumberViews);
		tagListTextView = (TextView) findViewById(R.id.textViewTagList);
		SeeAllAnswers = (Button) findViewById(R.id.buttonSeeAnswers);
		starButton = (ImageButton) findViewById(R.id.imageButtonStarQuestion);
		questionTitleTextView.setText(question.getTitle());
		questionBodyTextView.setText(EntityUtils.extractText(question.getBody()),BufferType.SPANNABLE);
		ownerTextView.setText(question.getOwnerUser().getDisplay_name());
		
	    if (postDataLayer.isFavourite(question.getId(), userID)){
	    	starButton.setBackgroundResource(R.drawable.yellowstar);

	    }
	    else{
	    	starButton.setBackgroundResource(R.drawable.star);


	    			    
	    }
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		dateQuestionTextView.setText(df.format(question.getCreation_date()));
		if(question.getView_count()==0)
			nViewsTextView.setText("");
		else
			nViewsTextView.setText(question.getView_count().toString());
		
		showTags();
		
		if (question.getComments().size()==0) 
			SeeAllAnswers.setText("Add a comment");
		
		else
		
			SeeAllAnswers.setText("See " + question.getComments().size() + " comments");
		
		// StarImageButtonEventHandeler
		starButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Change the picture and later add to the table of favorites
				
			    if (postDataLayer.isFavourite(question.getId(), userID)){
			    	postDataLayer.removeFavourite(question.getId(), userID);
			    	starButton.setBackgroundResource(R.drawable.star);
			    	
			    }
			    else{
			    
			    	postDataLayer.addFavourite(question.getId(), userID);
			    	starButton.setBackgroundResource(R.drawable.yellowstar);
			        
			    }
			    
				
			
				
			}
		});
		

		
	}
	//Callback method for the button seeAnswers
	public void showComments(View view){
		Intent intent = new Intent(this, CommentsActivity.class);
		intent.putExtra("idPost", idQuestion);
		intent.putExtra("UserID", userID);
		startActivity(intent);
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
