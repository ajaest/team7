package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.User;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.UserDataLayer;
import android.app.ActionBar;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import se.chalmers.eda397.team7.so.data.entity.EntityUtils;

public class UserActivity extends Activity {

	private Bundle bundle;
	private TextView textViewUsername;
	private TextView textViewReputationNumber;
	private TextView textViewWebsite;
	private TextView textViewLocation;
	private TextView textViewAge;
	private TextView textViewMembershipTime;
	private TextView textViewLastVisit;
	private TextView textViewProfileViews;
	private TextView textViewAboutMe;
	
	private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		bundle = getIntent().getExtras();
		Integer idUser = bundle.getInt("idUser");
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			SQLiteDatabase db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);
			UserDataLayer userDataLayer= factory.createUserDataLayer();
			user = userDataLayer.getUserById(idUser);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setTitle("User: " + user.getDisplay_name());
		textViewUsername = (TextView) findViewById(R.id.textViewUserName);
		textViewReputationNumber = (TextView) findViewById(R.id.textViewReputationNumber);
		textViewWebsite = (TextView) findViewById(R.id.textViewWebsite);
		textViewLocation = (TextView) findViewById(R.id.textViewLocation);
		textViewAge = (TextView) findViewById(R.id.textViewAge);
		textViewMembershipTime = (TextView) findViewById(R.id.textViewMembershipTime);
		textViewLastVisit = (TextView) findViewById(R.id.textViewLastVisit);
		textViewProfileViews = (TextView) findViewById(R.id.textViewProfileViews);
		textViewAboutMe = (TextView) findViewById(R.id.textViewAboutMe);
		
		textViewUsername.setText(user.getDisplay_name());
		textViewReputationNumber.setText(user.getReputation().toString());
		if(user.getWebsite_url()==null)
			textViewWebsite.setText("");
		else
			textViewWebsite.setText(user.getWebsite_url().toExternalForm());
		textViewLocation.setText(user.getLocation());
		
		if (user.getAge()==0)
			textViewAge.setText("");
		else 
			textViewAge.setText(user.getAge().toString());
		
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		textViewMembershipTime.setText(df.format(user.getCreation_date()));
		textViewLastVisit.setText(df.format(user.getLast_access_date()));
		textViewProfileViews.setText(user.getViews().toString()+ " visits ");
		
		
	    textViewAboutMe.setText(EntityUtils.extractText(user.getAbout_me()), BufferType.SPANNABLE);
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
