package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.util.Date;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.R.layout;
import se.chalmers.eda397.team7.so.R.menu;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.datalayer.AnswerDataLayer;
import se.chalmers.eda397.team7.so.datalayer.CommentDataLayer;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.test.data.CommentDataLayerTest;
import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


public class PopupAnswerOrCommentActivity extends Activity {

	private ImageButton buttonAnswerQuestion;
	private Bundle bundle;
	Integer idPost;
	private Integer userID;
	boolean isComment;
	private EditText editTextBody;
	private CommentDataLayer commentDataLayer;
	private AnswerDataLayer answerDataLayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popup_answer_comment);
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			SQLiteDatabase db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);
			commentDataLayer = factory.createCommentDataLayer();
			answerDataLayer = factory.createAnswerDataLayer();
			
	

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bundle = getIntent().getExtras();
		idPost = bundle.getInt("idPost");
		userID = bundle.getInt("UserID");
		
		isComment = bundle.getBoolean("isComment");
		buttonAnswerQuestion = (ImageButton) findViewById(R.id.imageButtonAnswerQuestion);
		editTextBody = (EditText) findViewById(R.id.editTextBodyAnswerQuestion);
	
		if(isComment){
			this.setTitle("Add a comment");
			
			buttonAnswerQuestion.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					//Toast.makeText(getApplicationContext(),commentDataLayer.(getMaxId()+1).toString() , Toast.LENGTH_LONG).show();
					int id;
					id = commentDataLayer.getMaxId()+1;
					
					commentDataLayer.createComment(editTextBody.getText().toString(), idPost, userID, id);
					finish();

				}
			});
			
		
		}
		else{
			this.setTitle("Answer to question ");
			buttonAnswerQuestion.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					Integer id;
					id = answerDataLayer.getMaxId()+1;
					
					answerDataLayer.createAnswer(
							id     ,
							(Integer)2             					 ,
							(Integer)idPost                             ,
							(Integer)null                            ,
							(new Date())           ,
							(Integer)0                    			 ,
							(Integer)0               				 ,
							editTextBody.getText().toString(),
							userID         					 ,
							(Integer)null                             ,
							(String)null 							 ,
							(Date)null           					 ,
							(new Date())	         ,
							(Date)null    					     ,
							(Date)null           				     ,
							(String)null,
							(String)null                     ,
							(Integer)0             ,
							(Integer)0            ,
							(Integer)0 		
						);
					finish();

				}
			});
		}
			
		

	}

	

}
