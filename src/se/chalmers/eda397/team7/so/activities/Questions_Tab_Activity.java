package se.chalmers.eda397.team7.so.activities;

import se.chalmers.eda397.team7.so.R;
import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class Questions_Tab_Activity extends TabActivity{
	int userID;
	private static final String NEWER = "Newer";
	private static final String NoA = "Number Of Answers";
	private Bundle bundle;
	private String tagPressed;
	private TabHost tabHost;
	private TabHost.TabSpec Newer;
	private TabHost.TabSpec numberOfAnswers;
	private static boolean isHeat = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_sort_main);
        
    	bundle = getIntent().getExtras();
    	userID = bundle.getInt("UserID");
		

        ///Get the tag pressed from the tag cloud
        tagPressed = bundle.getString("tagPressed");
        
        tabHost = getTabHost();
        addTabs(isHeat);
        
        
    }
    
    private void addTabs(boolean isHeat){
    	// Newer Tab
        Newer = tabHost.newTabSpec(NEWER);
       // Tab Icon
       Newer.setIndicator(NEWER);
       Intent inboxIntent = new Intent(this, QuestionSortByDate.class);
       inboxIntent.putExtra("UserID", userID);
       inboxIntent.putExtra("isHeat", isHeat);
       if (!tagPressed.equals(" ")) { //We came from the tag cloud
       	inboxIntent.putExtra("typeSearch", 2);
           inboxIntent.putExtra("query", tagPressed);
        
		}
       	
       // Tab Content
       Newer.setContent(inboxIntent);
       
       // Number of answers Tab
        numberOfAnswers = tabHost.newTabSpec(NoA);
       numberOfAnswers.setIndicator(NoA);
       Intent outboxIntent = new Intent(this, QuestionSortByNoA.class);
       outboxIntent.putExtra("UserID", userID);
       outboxIntent.putExtra("isHeat", isHeat);
       if (!tagPressed.equals(" ")) { //We came from the tag cloud
       	outboxIntent.putExtra("query", tagPressed);
       	outboxIntent.putExtra("typeSearch", 2);
  
       }
      
       numberOfAnswers.setContent(outboxIntent);
       
       // Adding all TabSpec to TabHost
       tabHost.addTab(Newer); // Adding Newer tab
       tabHost.addTab(numberOfAnswers); // Adding NoA tab
       
    }
    
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.questions_tab, menu); 
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowTitleEnabled(true);
		}
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.menu_heat_questions:
			boolean a = !isHeat;			
			isHeat = a;
			
			tabHost.clearAllTabs();
			addTabs(isHeat);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
