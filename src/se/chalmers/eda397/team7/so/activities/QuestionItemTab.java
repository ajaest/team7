package se.chalmers.eda397.team7.so.activities;

import se.chalmers.eda397.team7.so.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class QuestionItemTab extends TabActivity {

	private static final String INFO = "Info";
	private static final String ANSWERS = "Answers";
	private Bundle bundle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_item_tab);
		
		bundle = getIntent().getExtras();
		TabHost tabHost = getTabHost();
		 // Newer Tab
        TabSpec Info = tabHost.newTabSpec(INFO);
        // Tab Icon
        Info.setIndicator(INFO);
        Intent infoIntent = new Intent(this, QuestionInformation.class);
        infoIntent.putExtras(bundle);
        Info.setContent(infoIntent);
        
        
        // Newer Tab
        TabSpec Answers = tabHost.newTabSpec(ANSWERS);
        // Tab Icon
        Answers.setIndicator(ANSWERS);
        Intent answersIntent = new Intent(this, AnswersActivity.class);
        answersIntent.putExtras(bundle);
        Answers.setContent(answersIntent);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(Info); // Adding Newer tab
        tabHost.addTab(Answers); // Adding NoA tab
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question_item_tab, menu);
		return true;
	}

}
