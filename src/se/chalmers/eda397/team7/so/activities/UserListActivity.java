package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.util.ArrayList;


import se.chalmers.eda397.team7.so.R;
import so.chalmers.eda397.so.data.entity.User;
import so.chalmers.eda397.so.data.entity.UserListAdapter;
import so.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import so.chalmers.eda397.team7.so.datalayer.DataLayer;
import so.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import so.chalmers.eda397.team7.so.datalayer.UserDataLayer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

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
		userListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){

				Integer ide = userList.get(position).getId();
				Intent intent = new Intent(view.getContext(), UserActivity.class);
				intent.putExtra("idUser", ide);
				startActivity(intent);
			}
		});        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_list, menu);
		return true;
	}

	
	private ArrayList<User> retrieveUsers(){
		ArrayList<User> users = new ArrayList<User>();
		
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			SQLiteDatabase db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);
			UserDataLayer userDataLayer= factory.createUserDataLayer();
			users = userDataLayer.getListUsers();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}
}
