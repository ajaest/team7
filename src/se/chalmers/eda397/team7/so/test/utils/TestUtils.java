package se.chalmers.eda397.team7.so.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.chalmers.eda397.team7.so.data.entity.EntityFactory;
import se.chalmers.eda397.team7.so.data.entity.EntityUtils;
import se.chalmers.eda397.team7.so.data.entity.Post;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class TestUtils {
	
	private EntityFactory  ef;
	private SQLiteDatabase db;
	
	public TestUtils (SQLiteDatabase db, EntityFactory ef){
		
		this.ef = ef;
		this.db = db;
	}
	
	public Post generateRandomPostInstance(){
		
		String query;
		Cursor cur  ;
		
		do{
			query = "SELECT * FROM posts WHERE rowid=(SELECT abs(random())%(SELECT count(*) FROM posts)) AND post_type_id=1;";
			
			cur = null;
			
			cur = this.db.rawQuery(query, new String[]{});
			
			
		}while(cur.getCount()==0);
			
		cur.moveToNext();
		Post p = EntityUtils.createQuestionFromCur(this.ef, cur);
		
		return p;
	}	
	
	public List<Map<String, String>> getAllCursorValues(Cursor cur){
		
		List<Map<String, String>> allVals;
		
		allVals = new ArrayList<Map<String, String>>();
		
		while(cur.moveToNext()){
			Map <String, String> curValues;
			
			curValues = new HashMap<String, String>();
			for(String columnName : cur.getColumnNames()){
				curValues.put(columnName, cur.getString(cur.getColumnIndex(columnName)));				
			}
		}
		
		return allVals;
	}
	
	public Map<String,String> generateRandomPostValues(){
			
			String query;
			Cursor cur  ;
			Map<String, String> postRow;
			
			query = "SELECT * FROM posts WHERE rowid=(SELECT abs(random())%(SELECT count(*) FROM posts)) AND post_type_id=1;";
			
			cur = this.db.rawQuery(query, new String[]{});
			
			do{
				cur = this.db.rawQuery(query, new String[]{});
			}while(cur.getCount()==0);
			
			postRow = new HashMap<String, String>(cur.getColumnCount());
			cur.moveToNext();
			for(String s : cur.getColumnNames()){
				postRow.put(s, cur.getString(cur.getColumnIndex(s)));
			}
			
			return postRow;
		}
}
