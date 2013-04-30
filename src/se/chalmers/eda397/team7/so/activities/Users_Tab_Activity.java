package se.chalmers.eda397.team7.so.activities;

import se.chalmers.eda397.team7.so.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Users_Tab_Activity extends TabActivity{
	
	private static final String Alphabeth = "A-Z";
	private static final String Reputation = "Reputation";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_sort_main);
        
        TabHost tabHost = getTabHost();
        
        // Newer Tab
        TabSpec alphabeth = tabHost.newTabSpec(Alphabeth);
        // Tab Icon
        alphabeth.setIndicator(Alphabeth);
        Intent inboxIntent = new Intent(this, UsersSortByAlphabeth.class);
        // Tab Content
        alphabeth.setContent(inboxIntent);
        
        // Number of answers Tab
        TabSpec reputation = tabHost.newTabSpec(Reputation);
        reputation.setIndicator(Reputation);
        Intent outboxIntent = new Intent(this, UsersSortByReputation.class);
        reputation.setContent(outboxIntent);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(alphabeth); // Adding Newer tab
        tabHost.addTab(reputation); // Adding NoA tab
    }

}
