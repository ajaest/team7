package se.chalmers.eda397.team7.so.activities;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.R.layout;
import se.chalmers.eda397.team7.so.R.menu;
import android.os.Build;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AskQuestionActivity extends Activity {

	private EditText editTextTitle;
	private EditText editTextTags;
	private EditText editTextBody;
	private Bundle bundle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask_question);
		
		editTextBody = (EditText) findViewById(R.id.editTextBodyQuestion);
		editTextTags = (EditText) findViewById(R.id.editTextTagsQuestion);
		editTextTitle = (EditText) findViewById(R.id.editTextTitleQuestion);
		
//		bundle = getIntent().getExtras();
//		if (bundle.getInt("is Edition") == 1){
//			editTextBody.setText("....");
//			editTextTags.setText(":...");
//			editTextTitle.setText(".....");
//		}
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
					Toast.makeText(this, "Question posted", Toast.LENGTH_SHORT).show();
					intent = new Intent(this, HomeActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
