package se.chalmers.eda397.team7.so.datalayer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.chalmers.eda397.team7.so.data.SQLDataRuntimeException;
import se.chalmers.eda397.team7.so.data.entity.User;
import android.annotation.SuppressLint;
import android.database.Cursor;


public class UserDataLayer extends DataLayer<User>{

	protected UserDataLayer(DataLayerFactory dl) {
		super(dl);
	}

	public ArrayList<User> getListUsers(){
		String query;
		query = "SELECT * FROM users LIMIT 50";
		return (ArrayList<User>) this.querySortedInstanceSet(query, null);
	}

	
	public ArrayList<String> getDistinctListOfUsers(){
		ArrayList<String> tempString = new ArrayList<String>();
		
		String query = "SELECT DISTINCT  display_name FROM users";
		Cursor cur = this.getDbInstance().rawQuery(query, null);
		
		while(cur.moveToNext()){		
			tempString.add(cur.getString(0));
			
		}
	
			
		return tempString;
	}
	

	public User getUserById(Integer id){
		
		String query;
		
		query = "SELECT * FROM users WHERE id=?";
		
		return this.querySingleInstance(query, new String[]{id.toString()});
	}
	

	public User login(String userHash, String userPass){
		//TODO userPass
		String query;
		
		query = "SELECT * FROM users WHERE email_hash=?";
		
		return this.querySingleInstance(query, new String[]{userHash.toString()});
	}

	//user sorting criteria
	public List<User> getUserSortedByAlphabeth(String sortCriteria){
		String queryString = "SELECT * FROM users ORDER BY "+ sortCriteria +" ASC LIMIT 50";
		return this.querySortedInstanceSet(queryString, new String[]{});
	}
	
	//user sorting criteria
		public List<User> getUserSortedByReputation(String sortCriteria){
			String queryString = "SELECT * FROM users ORDER BY "+ sortCriteria +" DESC LIMIT 50";
			return this.querySortedInstanceSet(queryString, new String[]{});
		}
		
	// Henriks stupid search
	public ArrayList<User> searchForUser(String searchString){
		String query;
		query = "SELECT * FROM users WHERE display_name LIKE '%"+searchString+"%'";
		return (ArrayList<User>) this.querySortedInstanceSet(query, null);
	}
	
	public void updateUser(Integer id, Map<String, String> attValues) throws SQLDataRuntimeException{
		
		Map<String, String> key;
		
		key = new HashMap<String, String>();
		
		key.put("id", id.toString());
		
		this.queryInsertOrReplace("users", attValues, key);		
	}
	
	@SuppressLint("UseValueOf")
	@SuppressWarnings({ "deprecation"})
	@Override
	protected User createNotSyncronizedInstance(Cursor cur) {
		
		String split[];
		
		
		Integer id              ;
		Integer reputation      ;
		Date    creation_date   ;
		String  display_name    ;
		String  email_hash      ;
		Date    last_access_date;
		URL     website_url     ;
		String  location        ;
		Integer age             ;
		String  about_me        ;
		Integer views           ;
		Integer up_votes        ;
		Integer down_votes      ;
		
		id            = cur.getInt(cur.getColumnIndex("id"        ));
		reputation    = cur.getInt(cur.getColumnIndex("reputation"));
		
		split = cur.getString(cur.getColumnIndex("creation_date")).split("-");
		creation_date = new Date(
			new Integer(split[0]) - 1900, 
			new Integer(split[1]), 
			new Integer(split[2])
		);
		display_name = cur.getString(cur.getColumnIndex("display_name"));
		email_hash   = cur.getString(cur.getColumnIndex("email_hash"  ));
		
		split = cur.getString(cur.getColumnIndex("last_access_date")).split("-");
		last_access_date = new Date(
				new Integer(split[0]) - 1900, 
				new Integer(split[1]), 
				new Integer(split[2])
			);
		
		try {
			website_url = new URL(cur.getString(cur.getColumnIndex("website_url")));
		} catch (MalformedURLException e) {
			//TODO: 
			website_url = null;
		}
		location   = cur.getString(cur.getColumnIndex("location"  ));
		age        = cur.getInt   (cur.getColumnIndex("age"       ));
		about_me   = cur.getString(cur.getColumnIndex("about_me"  ));
		views      = cur.getInt   (cur.getColumnIndex("views"     ));
		up_votes   = cur.getInt   (cur.getColumnIndex("up_votes"  ));
		down_votes = cur.getInt   (cur.getColumnIndex("down_votes"));
		
		return this.getEntityFactory().createUser(
			id              ,
			reputation      ,
			creation_date   ,
			display_name    ,
			email_hash      ,
			last_access_date,
			website_url     ,
			location        ,
			age             ,
			about_me        ,
			views           ,
			  up_votes      ,
			down_votes
		);
	}
}
