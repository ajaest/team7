package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.User;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.UserDataLayer;
import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ProfileActivity extends TabActivity {

	private Bundle bundle;
	private Integer userId;
	private TabHost tabHost;
	private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
	
		bundle = getIntent().getExtras();
		userId = bundle.getInt("UserID");
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			SQLiteDatabase db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);
			UserDataLayer userDataLayer= factory.createUserDataLayer();
			user = userDataLayer.getUserById(userId);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setTitle(user.getDisplay_name());
		tabHost = getTabHost();
        // Info Tab
        TabSpec info = tabHost.newTabSpec("INFO");
        info.setIndicator("INFO");
        Intent intent1 = new Intent(this, UserActivity.class);
        intent1.putExtra("idUser", userId);
        //TODO put correct bundle
        info.setContent(intent1);
        
        // MyPosts Tab
        TabSpec myPosts = tabHost.newTabSpec("MY POSTS");
        myPosts.setIndicator("MY POSTS");
        Intent intent2 = new Intent(this, QuestionSortByDate.class);
        intent2.putExtra("tagPressed", " ");
        intent2.putExtra("UserID", userId);
        intent2.putExtra("typeSearch", 4); //get questions of a specific user
        myPosts.setContent(intent2);
        
        // FavPosts Tab
        TabSpec favPosts = tabHost.newTabSpec("FAVORITES");
        favPosts.setIndicator("FAVORITES");
        Intent intent3 = new Intent(this, QuestionSortByDate.class);
        intent3.putExtra("tagPressed", " ");
        intent3.putExtra("UserID", userId);
        intent3.putExtra("typeSearch", 5); //get favorites of a user
        favPosts.setContent(intent3);
        
        // myTags Tab
        TabSpec myTags = tabHost.newTabSpec("MY TAGS");
        myTags.setIndicator("MY TAGS");
        Intent intent4 = new Intent(this, TagCloudActivity.class);
        //intent4.putExtra("tagPressed", " ");
        intent4.putExtra("UserID", userId);
        intent4.putExtra("See_my_tags", true);
        intent4.putExtra("isMultitag",false);
       // intent4.putExtra("typeSearch", 6);
        myTags.setContent(intent4);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(info); // Adding info tab
        tabHost.addTab(myPosts); // Adding myPosts tab
        tabHost.addTab(favPosts); // Adding favPosts tab
        tabHost.addTab(myTags); // Adding myTags tab
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; 
		getMenuInflater().inflate(R.menu.profile, menu);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayShowTitleEnabled(true);
		}
		MenuItem heatMenuItem = menu.findItem(R.id.menu_heat_cloud_profile); 
		int currentTab = tabHost.getCurrentTab();
		if (currentTab==3)
			heatMenuItem.setVisible(true);
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
