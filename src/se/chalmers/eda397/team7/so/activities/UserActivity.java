package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import se.chalmers.eda397.team7.so.R;
import so.chalmers.eda397.so.data.entity.User;
import so.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import so.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import so.chalmers.eda397.team7.so.datalayer.UserDataLayer;
import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.TextView.BufferType;

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
		
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);
		Log.d("datees", "Creation date:"+ user.getCreation_date()+ ", last access:"+ user.getLast_access_date());
		textViewMembershipTime.setText(df.format(user.getCreation_date()));
		textViewLastVisit.setText(df.format(user.getLast_access_date()));
		textViewProfileViews.setText(user.getViews().toString()+ " visits ");
		
		
	    textViewAboutMe.setText(extractText(user.getAbout_me()), BufferType.SPANNABLE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user, menu);
		return true;
	}


	private Spanned extractText(String htmlText){
		String noSlash = htmlText.replaceAll("\\\\.", " ");
		Spanned spannedContent = Html.fromHtml(noSlash);
		return spannedContent;
	}
}
