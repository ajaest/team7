package se.chalmers.eda397.team7.so.activities;

import se.chalmers.eda397.team7.so.R;
import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class Questions_Tab_Activity extends TabActivity{
	int userID;
	private static final String NEWER = "Newer";
	private static final String NoA = "Number Of Answers";
	private static final String FULL_TEXT = "Full text";
	private static final String TAG_MATCHES = "Relevance";
	private Bundle bundle;
	private String tagPressed;
	private String query;
	private Integer typeSearch;
	
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

    	typeSearch = bundle.getInt("typeSearch");

		

        ///Get the tag pressed from the tag cloud
        tagPressed = bundle.getString("tagPressed");
        query = bundle.getString("query");
        
        
        ///
        /// Relevance tab. Present at fulltext search and multi-tag search
        ///
        
        tabHost = getTabHost();
        addTabs(isHeat);
        
        
    }
    
    private void addTabs(boolean isHeat){
    	Intent inboxIntent;
        
        TabSpec currentTab =null;
        // Newer Tab
        if(typeSearch.equals(1)){
        	currentTab = tabHost.newTabSpec(FULL_TEXT);
        	currentTab.setIndicator(FULL_TEXT+ ": " + query);
        }else if(typeSearch.equals(3)){
        	currentTab = tabHost.newTabSpec(TAG_MATCHES);
        	currentTab.setIndicator(TAG_MATCHES);
        }
        // Tab Icon
        
        if(typeSearch.equals(1) || typeSearch.equals(3)){
	        inboxIntent = new Intent(this, QuestionSortByDate.class);
	        inboxIntent.putExtra("UserID", userID);
	        
	        inboxIntent.putExtra("typeSearch", typeSearch);
	        inboxIntent.putExtra("isHeat", isHeat);
	        
	        if (!tagPressed.equals(" ")) { //We came from the tag cloud
	        	
	            inboxIntent.putExtra("query", tagPressed);
			}else{
				inboxIntent.putExtra("query", query);
			}
	        
	        currentTab.setContent(inboxIntent);
	        tabHost.addTab(currentTab); // Adding Newer tab
        }
        
        if(!typeSearch.equals(1)){
	        
        	//By date
        	currentTab = tabHost.newTabSpec(NEWER);
        	currentTab.setIndicator(NEWER);
        	
        	inboxIntent = new Intent(this, QuestionSortByDate.class);
            inboxIntent.putExtra("UserID", userID);
            inboxIntent.putExtra("isHeat", isHeat);
            if (!tagPressed.equals(" ")) { //We came from the tag cloud
            	inboxIntent.putExtra("typeSearch", 2);
                inboxIntent.putExtra("query", tagPressed);
                inboxIntent.putExtra("orderby", "date");             
    		}
            
            // Tab Content
            currentTab.setContent(inboxIntent);
            tabHost.addTab(currentTab); // Adding Newer tab
	        
	        // Number of answers Tab
	        TabSpec numberOfAnswers = tabHost.newTabSpec(NoA);
	        numberOfAnswers.setIndicator(NoA);
	        Intent outboxIntent = new Intent(this, QuestionSortByNoA.class);
	        outboxIntent.putExtra("isHeat", isHeat);
	        outboxIntent.putExtra("UserID", userID);
	        if (!tagPressed.equals(" ")) { //We came from the tag cloud
	        	outboxIntent.putExtra("query", tagPressed);
	        	outboxIntent.putExtra("typeSearch", 2);
	   
	        }
	        
	        numberOfAnswers.setContent(outboxIntent);
	   
	        
	        tabHost.addTab(numberOfAnswers); // Adding NoA tab
        }
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
			
			/*tabHost.clearAllTabs();
			addTabs(isHeat);*/
			Intent intent = new Intent(this,Questions_Tab_Activity.class);
			intent.putExtras(bundle);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
