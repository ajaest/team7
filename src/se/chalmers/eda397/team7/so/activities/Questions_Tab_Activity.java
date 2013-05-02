package se.chalmers.eda397.team7.so.activities;

import se.chalmers.eda397.team7.so.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class Questions_Tab_Activity extends TabActivity{

	private static final String NEWER = "Newer";
	private static final String NoA = "Number Of Answers";
	private Bundle bundle;
	private String tagPressed;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_sort_main);
        
        bundle = getIntent().getExtras();
        ///Get the tag pressed from the tag cloud
        tagPressed = bundle.getString("tagPressed");
        Log.d("testing","tag pressed tab activity:"+ tagPressed);
        TabHost tabHost = getTabHost();
        
        // Newer Tab
        TabSpec Newer = tabHost.newTabSpec(NEWER);
        // Tab Icon
        Newer.setIndicator(NEWER);
        Intent inboxIntent = new Intent(this, QuestionSortByDate.class);
        if (!tagPressed.equals(" ")) { //We came from the tag cloud
        	inboxIntent.putExtra("typeSearch", 2);
            inboxIntent.putExtra("query", tagPressed);
		}
        	
        // Tab Content
        Newer.setContent(inboxIntent);
        
        // Number of answers Tab
        TabSpec numberOfAnswers = tabHost.newTabSpec(NoA);
        numberOfAnswers.setIndicator(NoA);
        Intent outboxIntent = new Intent(this, QuestionSortByNoA.class);
        if (!tagPressed.equals(" ")) { //We came from the tag cloud
        	outboxIntent.putExtra("query", tagPressed);
        	outboxIntent.putExtra("typeSearch", 2);
        }
       
        numberOfAnswers.setContent(outboxIntent);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(Newer); // Adding Newer tab
        tabHost.addTab(numberOfAnswers); // Adding NoA tab
    }
}
