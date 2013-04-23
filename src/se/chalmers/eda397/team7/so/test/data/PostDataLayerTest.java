package se.chalmers.eda397.team7.so.test.data;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import se.chalmers.eda397.team7.so.data.entity.EntityFactory;
import se.chalmers.eda397.team7.so.data.entity.EntityUtils;
import se.chalmers.eda397.team7.so.data.entity.Post;
import se.chalmers.eda397.team7.so.data.entity.Comment;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;

@SuppressLint({ "SdCardPath", "UseSparseArrays" })
public class PostDataLayerTest extends InstrumentationTestCase {

	private final SQLiteDatabase   db              ;
	private final PostDataLayer    postDL          ;
	private final EntityFactory    entityFactory   ;
	private final DataLayerFactory dataLayerFactory;
	
	public PostDataLayerTest (){
		db = SQLiteDatabase.openDatabase("/data/data/se.chalmers.eda397.team7.so/databases/so.sqlite", null, SQLiteDatabase.OPEN_READONLY);
		
		dataLayerFactory = new DataLayerFactory(db);
		
		postDL = dataLayerFactory.createPostDataLayer();
		
		entityFactory = new EntityFactory(dataLayerFactory);
	}
	
	
	@Override
	public void setUp(){
		
		db.execSQL("BEGIN TRANSACTION;");
	}
	
	public void tearDown(){
		db.execSQL("ROLLBACK;");
	}

	@Override
	protected void finalize() throws Throwable{
		db.execSQL("END TRANSACTION;");
		db.close();
		
		super.finalize();
	}
	
	//////////////////////////////////////////////////////////
	////////// Tests
	//////////////////////////////////////////////////////////
	
	public void testGetPostById(){
		
		Map<String,String> randomPostAttValues ;
		Post               retPost             ;
		
		
		randomPostAttValues = generateRandomPostValues();
		retPost = this.postDL.getPostById(Integer.valueOf(randomPostAttValues.get("id")));
		
		assertPostAttributeValue(randomPostAttValues, retPost);
	}
	
	public void testGetComments(){
		
		Post           p        ;
		List<Comment> comments  ;
		String        query     ;
		Cursor        cur       ;
		Map<Integer, Comment>  commentsIds; 
		
		do{
			p = generateRandomPostInstance();
			comments = p.getComments();
		}while(p.getComments().size()==0);
		
		/* There's no referencial integrity in the database, so if this value is not correct we just alert */
		//assertEquals((Integer)comments.size(), p.getAnswer_count());
		
		/* We check that all the ids selected from the database exists in the retrieved set */
		query = "SELECT id FROM comments WHERE post_id=?";
		cur = this.db.rawQuery(query, new String[]{p.getId().toString()});
		
		//Extract the ids
		commentsIds = new HashMap<Integer, Comment>(comments.size());
		for(Comment c : comments){
			commentsIds.put(c.getId(), c);
		}
		
		for(Map<String, String> commentRow: getAllCursorValues(cur)){
			
			assertTrue("The id " + commentRow.get("id") + " is not in the retrieved set", commentsIds.keySet().contains(Integer.valueOf(commentRow.get("id"))));
		}
		
		
		
	}
	
	//////////////////////////////////////////////////////////
	////////// Auxiliary methods
	//////////////////////////////////////////////////////////
	
	//TODO: this not very thorough...
	private void assertPostAttributeValue(Map<String, String> attValues, Post p){
		
		assertEquals(Integer.valueOf(attValues.get("id")), p.getId()) ;
	}
	
	private List<Map<String, String>> getAllCursorValues(Cursor cur){
		
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
	
	private Map<String,String> generateRandomPostValues(){
		
		String query;
		Cursor cur  ;
		Map<String, String> postRow;
		
		query = "SELECT * FROM posts WHERE rowid=(SELECT abs(random())%(SELECT count(*) FROM posts));";
		
		cur = null;
		
		while(cur==null || cur.getCount()==0){
			cur = this.db.rawQuery(query, new String[]{});
		}
		
		postRow = new HashMap<String, String>(cur.getColumnCount());
		cur.moveToNext();
		for(String s : cur.getColumnNames()){
			postRow.put(s, cur.getString(cur.getColumnIndex(s)));
		}
		
		return postRow;
	}
	
	private Post generateRandomPostInstance(){
		
		String query;
		Cursor cur  ;
		
		query = "SELECT * FROM posts WHERE rowid=(SELECT abs(random())%(SELECT count(*) FROM posts));";
		
		cur = null;
		
		cur = this.db.rawQuery(query, new String[]{});
		
		cur.moveToNext();
		
		Post p = EntityUtils.createPostFromCursor(this.entityFactory, cur);
		
		return p;
	}
	
	
	
	

	
	
}

