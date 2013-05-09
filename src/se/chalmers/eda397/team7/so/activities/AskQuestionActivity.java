package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.R.layout;
import se.chalmers.eda397.team7.so.R.menu;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.Question;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AskQuestionActivity extends Activity {

	private EditText editTextTitle;
	private EditText editTextTags;
	private EditText editTextBody;
	private Bundle bundle;
	private PostDataLayer postDataLayer;
	private Integer userId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_question);
		
		editTextBody = (EditText) findViewById(R.id.editTextBodyQuestion);
		editTextTags = (EditText) findViewById(R.id.editTextTagsQuestion);
		editTextTitle = (EditText) findViewById(R.id.editTextTitleQuestion);
		
		bundle = getIntent().getExtras();
		userId = bundle.getInt("UserID");
//		if (bundle.getInt("is Edition") == 1){
//			editTextBody.setText("....");
//			editTextTags.setText(":...");
//			editTextTitle.setText(".....");
//		}
		
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			SQLiteDatabase db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);
			postDataLayer = factory.createPostDataLayer();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ask_question, menu);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowTitleEnabled(true);
		}
		return true;
	}
	
	public boolean checkConditions(){
		return (!(editTextBody.getText().toString().isEmpty()|| editTextTitle.getText().toString().isEmpty()));
	}
	
	public boolean checkTags(){
		String tagsString = editTextTags.getText().toString();
		String []tags = tagsString.split(" ");
		if (!(tagsString.isEmpty()) && ( (tags.length>=1) && (tags.length<=5) ))
			return true;
		return false;
	}
	
	public String formatTags(){
		String tagsString = editTextTags.getText().toString();
		String []tags = tagsString.split(" ");
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < tags.length; i++) {
			sBuilder.append("<" + tags[i] + ">");
		}
		return sBuilder.toString();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.menu_create_question:
			if(checkConditions()){
				if(!checkTags())
					Toast.makeText(this, "Wrong number of tags", Toast.LENGTH_SHORT).show();
				else{
					
					Question question = postDataLayer.createQuestion(
							(Integer)(postDataLayer.getMaxId() +1)      ,
							(Integer)1             					 ,
							(Integer)null                             ,
							(Integer)null                             ,
							(new Date())           ,
							(Integer)0                    			 ,
							(Integer)0               				 ,
							editTextBody.getText().toString(),
							userId         					 ,
							(Integer)null                             ,
							(String)null 							 ,
							(Date)null           					 ,
							(new Date())	         ,
							(Date)null    					     ,
							(Date)null           				     ,
							editTextTitle.getText().toString(),
							formatTags()                     ,
							(Integer)0             ,
							(Integer)0            ,
							(Integer)0 		
						);
					Toast.makeText(this, "Question posted", Toast.LENGTH_SHORT).show();
					intent = new Intent(this, HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("UserID", userId);
					startActivity(intent);
				}
			}else
				Toast.makeText(this, "Missing field", Toast.LENGTH_SHORT).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
}
