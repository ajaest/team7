package se.chalmers.eda397.team7.so.test.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import se.chalmers.eda397.team7.so.data.entity.Comment;
import se.chalmers.eda397.team7.so.data.entity.EntityFactory;
import se.chalmers.eda397.team7.so.data.entity.EntityUtils;
import se.chalmers.eda397.team7.so.data.entity.Post;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer.PostIndexInformation;
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
	
	public void test_getPostById(){
		
		Map<String,String> randomPostAttValues ;
		Post               retPost             ;
		
		
		randomPostAttValues = generateRandomPostValues();
		retPost = this.postDL.getPostById(Integer.valueOf(randomPostAttValues.get("id")));
		
		assertPostAttributeValue(randomPostAttValues, retPost);
	}
	
	public void test_getComments(){
		
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
	
	public void test_indexedfullTextSearch(){
		//TODO:
		Set<String> words = new HashSet<String>(Arrays.asList(new String[]{"php", "mysql", "apache"}));
		
		@SuppressWarnings("unused")
		SortedSet<PostIndexInformation> indexResults = this.postDL.indexedfullTextSearch(words);
	}
	
	public void test_pagedFullTextSearch(){
		
		Set<String> words                       ;
		List<PostIndexInformation> indexResults ;
		
		
		words = new HashSet<String>(Arrays.asList(new String[]{"php", "mysql", "apache"}));
		indexResults = new ArrayList<PostIndexInformation>(this.postDL.indexedfullTextSearch(words));
		
		List<Post> postResults;
		for(int i=0; i<(indexResults.size()/10); i++){
			postResults = this.postDL.pagedFullTextSearch(words, 10, i);
			
			for(int j=0; j<postResults.size(); j++){
				assertEquals(indexResults.get(i*10 + j).getId(), postResults.get(j).getId());
			}
		}

	}
	
	
	public void test_updatePost(){
		
		//TODO
	}
	
	public void test_indexedTagSearch(){
		
		Set<String> words = new HashSet<String>(Arrays.asList(new String[]{"php", "mysql", "apache"}));
		
		SortedSet<PostIndexInformation> indexResults = this.postDL.indexedTagSearch(words);
		
		Post curPost;
		Set<String> tagIntersection;
		for(PostIndexInformation postIdx : indexResults){
			curPost = postIdx.retrieveInstance();
			 
			tagIntersection = new HashSet<String>(words);
			tagIntersection.retainAll(curPost.getTags());
			
			
			assertEquals((Integer)tagIntersection.size(), postIdx.getTagsMatchs());			
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

