package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.util.ArrayList;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.User;
import se.chalmers.eda397.team7.so.UserListAdapter;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

public class UserListActivity extends Activity {

	private ArrayList<User> userList = new ArrayList<User>();
	private ListView userListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		
		userList = retrieveUsers();
		userListView = (ListView)findViewById(R.id.listViewUsers);
		userListView.setAdapter(new UserListAdapter(this, userList, R.layout.user_item));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_list, menu);
		return true;
	}

	
	private ArrayList<User> retrieveUsers(){
		ArrayList<User> users = new ArrayList<User>();
		User user;
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			SQLiteDatabase db = test.getWritableDatabase();
			Cursor c = db.rawQuery("SELECT up_votes, down_votes, reputation, display_name FROM users LIMIT 40", new String[]{});
				while(c.moveToNext()){
					user = new User(Integer.parseInt(c.getString(0)), Integer.parseInt(c.getString(1)), Integer.parseInt(c.getString(2)), c.getString(3));
					users.add(user);
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}
}
