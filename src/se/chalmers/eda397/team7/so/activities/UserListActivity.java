package se.chalmers.eda397.team7.so.activities;

import java.io.IOException;
import java.util.ArrayList;

import se.chalmers.eda397.team7.so.R;
import se.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper;
import se.chalmers.eda397.team7.so.data.entity.User;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.UserDataLayer;
import so.chalmers.eda397.so.data.entity.UserListAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class UserListActivity extends Activity {

	private ArrayList<User> userList = new ArrayList<User>();
	private ListView userListView;
	private Bundle bundle;
	String query="";
	int typeSearch = 0;
	UserDataLayer userDataLayer = null;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_list);
		
		try {
			SQLiteSODatabaseHelper test = new SQLiteSODatabaseHelper(this.getApplicationContext());
			SQLiteDatabase db = test.getWritableDatabase();
			DataLayerFactory factory = new DataLayerFactory(db);
			userDataLayer= factory.createUserDataLayer();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		bundle = getIntent().getExtras();
		if (bundle != null){
			typeSearch = bundle.getInt("typeSearch");
			query = bundle.getString("query");
		}
		if (typeSearch == 3){ // It user search
			userList = retriveUsersByName(userDataLayer, query);
		}
		else {
		
			userList = retrieveUsers(userDataLayer);
		}


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


	private ArrayList<User> retrieveUsers(UserDataLayer userDataLayer){
		ArrayList<User> users = new ArrayList<User>();

		users = userDataLayer.getListUsers();
		return users;
	}
	private ArrayList<User> retriveUsersByName(UserDataLayer userDataLayer, String query){
		ArrayList<User> users = new ArrayList<User>();
		users = userDataLayer.searchForUser(query);
		return users;
	}
}
