package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.Question;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class QuestionItemTab extends TabActivity {

	private static final String INFO = "Info";
	private static final String ANSWERS = "Answers";
	private Bundle bundle;
	private Integer userId;
	private Integer questionId;
	private PostDataLayer postDataLayer;
	private Question question;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_item_tab);
		
		bundle = getIntent().getExtras();
		userId = bundle.getInt("UserID");
		questionId = bundle.getInt("idQuestion");
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			SQLiteDatabase db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);

			postDataLayer= factory.createPostDataLayer();
			question = postDataLayer.getQuestionById(questionId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setTitle(question.getTitle());
		TabHost tabHost = getTabHost();
		 // Newer Tab
        TabSpec Info = tabHost.newTabSpec(INFO);
        // Tab Icon
        Info.setIndicator(INFO);
        Intent infoIntent = new Intent(this, QuestionInformation.class);
        
        infoIntent.putExtras(bundle);
        infoIntent.putExtra("UserID", userId);
        Info.setContent(infoIntent);
       
        
        // Newer Tab
        TabSpec Answers = tabHost.newTabSpec(ANSWERS);
        // Tab Icon
        Answers.setIndicator(ANSWERS);
        Intent answersIntent = new Intent(this, AnswersActivity.class);
        answersIntent.putExtras(bundle);
        answersIntent.putExtra("UserID", userId);
        Answers.setContent(answersIntent);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(Info); // Adding Newer tab
        tabHost.addTab(Answers); // Adding NoA tab
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question_item_tab, menu);
		MenuItem editMenuItem = menu.findItem(R.id.menu_edit_question);  
		MenuItem removeMenuItem = menu.findItem(R.id.menu_remove_question);   
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (ownerQuestion()) {
				editMenuItem.setVisible(true);
				removeMenuItem.setVisible(true);
			}
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowTitleEnabled(true);
			
		}
		return true;
	}
	
	private boolean ownerQuestion(){
		Question question = null;
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			SQLiteDatabase db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);

			postDataLayer= factory.createPostDataLayer();
			question = postDataLayer.getQuestionById(questionId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (question.getOwnerUser().getId().equals(userId));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;

		case R.id.menu_edit_question:
			Intent intent = new Intent(QuestionItemTab.this, AskQuestionActivity.class);
			intent.putExtra("UserID", userId);
			intent.putExtra("isEdition", true);
			intent.putExtra("idQuestion",questionId);
			startActivity(intent);
			return true;
			
		case R.id.menu_remove_question:
			createDialog();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void createDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure?");
		builder.setTitle("Remove question");
		// Add the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   postDataLayer.deletePost(questionId);
		               finish();
		           }
		       });
		builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               //Do nothing, just cancel
		           }
		       });

		// Create the AlertDialog
		AlertDialog dialog = builder.create();
		dialog.show();
	}

}
