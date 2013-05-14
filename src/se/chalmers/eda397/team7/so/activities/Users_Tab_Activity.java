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
public class Users_Tab_Activity extends TabActivity{
	
	private static final String Alphabet = "A-Z";
	private static final String Reputation = "Reputation";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_sort_main);
        
		TabHost tabHost = getTabHost();
        
        // Newer Tab
        TabSpec alphabet = tabHost.newTabSpec(Alphabet);
        // Tab Icon
        alphabet.setIndicator(Alphabet);
        Intent inboxIntent = new Intent(this, UsersSortByAlphabeth.class);
        // Tab Content
        alphabet.setContent(inboxIntent);
        
        // Number of answers Tab
        TabSpec reputation = tabHost.newTabSpec(Reputation);
        reputation.setIndicator(Reputation);
        Intent outboxIntent = new Intent(this, UsersSortByReputation.class);
        reputation.setContent(outboxIntent);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(alphabet); // Adding Alphabet tab
        tabHost.addTab(reputation); // Adding reputation tab
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

