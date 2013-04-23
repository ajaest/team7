package se.chalmers.eda397.team7.so.test.data;

import java.util.HashMap;
import java.util.Map;

import se.chalmers.eda397.team7.so.data.entity.EntityFactory;
import se.chalmers.eda397.team7.so.data.entity.EntityUtils;
import se.chalmers.eda397.team7.so.data.entity.Post;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;

@SuppressLint("SdCardPath")
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
	
	/**
	 * 
	 */
	public void testGetPostById(){
		
		Map<String,String> randomPostAttValues = generateRandomPostValues();
		
		Post retPost = this.postDL.getPostById(Integer.valueOf(randomPostAttValues.get("id")));
		
		assertPostAttributeValue(randomPostAttValues, retPost);
	}
	
	private void assertPostAttributeValue(Map<String, String> attValues, Post p){
		//TODO: Implement an attribute value checkthis check is not very thorough... only against the id
		assertEquals(Integer.valueOf(attValues.get("id")), p.getId()) ;
	}
	
	private Map<String,String> generateRandomPostValues(){
		
		String query;
		Cursor cur  ;
		Map<String, String> postRow;
		
		query = "SELECT (id) FROM posts WHERE rowid=(SELECT abs(random())%(SELECT count(*) FROM posts));";
		
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
	
	@SuppressWarnings("unused")
	private Post generateRandomPostInstance(){
		
		String query;
		Cursor cur  ;
		
		query = "SELECT (id) FROM posts WHERE rowid=(SELECT abs(random())%(SELECT count(*) FROM posts));";
		
		cur = null;
		
		while(cur==null || cur.getCount()==0){
			cur = this.db.rawQuery(query, new String[]{});
		}
		
		Post p = EntityUtils.createPostFromCursor(this.entityFactory, cur);
		
		return p;
	}
	
	
	
	

	
	
}
