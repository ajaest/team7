package se.chalmers.eda397.team7.so.datalayer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.chalmers.eda397.team7.so.data.entity.Comment;
import android.annotation.SuppressLint;
import android.database.Cursor;

@SuppressLint("UseValueOf")
public class CommentDataLayer extends DataLayer<Comment> {
	
	protected CommentDataLayer(DataLayerFactory db) {
		super(db);
	}
	
	public List<Comment> getCommentsByPostId(Integer idPost){
		String query = "SELECT * FROM comments WHERE post_id = ? ";
		return this.querySortedInstanceSet(query, new String[]{idPost.toString()});
	}	
	
	@SuppressWarnings({ "deprecation" })
	@Override
	protected Comment createNotSyncronizedInstance(Cursor cur) {
				
		String[] split;
		
		Integer id           ; 
		Integer post_id      ; 
		Integer score        ; 
		String  text         ; 
		Date    creation_date; 
		Integer user_id      ;
		
		id      = cur.getInt   (cur.getColumnIndex("id"     ));
		post_id = cur.getInt   (cur.getColumnIndex("post_id"));
		score   = cur.getInt   (cur.getColumnIndex("score"  ));
		text    = cur.getString(cur.getColumnIndex("text"  ));
		
		split = cur.getString(cur.getColumnIndex("creation_date")).split("-");
		creation_date = new Date();
		creation_date.setYear (new Integer(split[0]) - 1900);
		creation_date.setMonth(new Integer(split[1]));
		creation_date.setDate (new Integer(split[2]));
		
		user_id = cur.getInt   (cur.getColumnIndex("user_id"));
		
		return this.getEntityFactory().createComment(
			id           , 
			post_id      , 
			score        , 
			text         , 
			creation_date, 
			user_id 
		);
	}
	
	public void updateComment(Integer id, Map<String, String> attValues) {

		Map<String, String> key;
		
		key = new HashMap<String, String>();
		
		key.put("id", id.toString());
		
		this.queryInsertOrReplace("comments", attValues, key);
	}
	
	public void createComment(String text, Integer post_id,  Integer user_id, Integer id){
		
		String query = new String();
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = "";
		dateString = format.format(new Date());
		

		
		query = "INSERT OR REPLACE INTO comments (id,post_id,score,text,creation_date,user_id) VALUES(" + id+ "," +post_id+"," +0+ ",'" + text + "', '" +dateString+"' ," + user_id + ")";
		

		// Don't know this
		this.queryInsertComment(query);

	}
	public Integer getMaxId(){
		String queryString = "SELECT MAX(id) from comments";
		Cursor cur = this.getDbInstance().rawQuery(queryString, null);
		cur.moveToNext();
		Integer max = cur.getInt(0);
		cur.close();
		return max;
	}
	
	
	
}
