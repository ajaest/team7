package se.chalmers.eda397.team7.so.activities;

import se.chalmers.eda397.team7.so.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ProfileActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
	        
		TabHost tabHost = getTabHost();
        
        // Info Tab
        TabSpec info = tabHost.newTabSpec("INFO");
        info.setIndicator("INFO");
        Intent intent1 = new Intent(this, UserActivity.class);
        intent1.putExtra("idUser", 13);
        //TODO put correct bundle
        info.setContent(intent1);
        
        // MyPosts Tab
        TabSpec myPosts = tabHost.newTabSpec("MY POSTS");
        myPosts.setIndicator("MY POSTS");
        Intent intent2 = new Intent(this, Questions_Tab_Activity.class);
        intent2.putExtra("tagPressed", " ");
        intent2.putExtra("idQuestioner", 13);
        myPosts.setContent(intent2);
        
        // FavPosts Tab
        TabSpec favPosts = tabHost.newTabSpec("FAVORITES");
        favPosts.setIndicator("FAVORITES");
        Intent intent3 = new Intent(this, Questions_Tab_Activity.class);
        intent3.putExtra("tagPressed", " ");
        favPosts.setContent(intent3);
        
        // myTags Tab
        TabSpec myTags = tabHost.newTabSpec("MY TAGS");
        myTags.setIndicator("MY TAGS");
        Intent intent4 = new Intent(this, Questions_Tab_Activity.class);
        intent4.putExtra("tagPressed", " ");
        myTags.setContent(intent4);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(info); // Adding info tab
        tabHost.addTab(myPosts); // Adding myPosts tab
        tabHost.addTab(favPosts); // Adding favPosts tab
        tabHost.addTab(myTags); // Adding myTags tab
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
