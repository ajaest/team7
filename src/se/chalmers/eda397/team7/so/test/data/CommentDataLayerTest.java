package se.chalmers.eda397.team7.so.test.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.chalmers.eda397.team7.so.data.entity.Comment;
import se.chalmers.eda397.team7.so.data.entity.EntityFactory;
import se.chalmers.eda397.team7.so.data.entity.Post;
import se.chalmers.eda397.team7.so.datalayer.CommentDataLayer;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.test.utils.TestUtils;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;

@SuppressLint("UseSparseArrays")
public class CommentDataLayerTest extends InstrumentationTestCase {
	
	private final SQLiteDatabase   db              ;
	private final CommentDataLayer commentDL       ;
	private final EntityFactory    entityFactory   ;
	private final DataLayerFactory dataLayerFactory;
	private final TestUtils        testUtils       ;
	
	@SuppressLint("SdCardPath")
	public CommentDataLayerTest (){
		db = SQLiteDatabase.openDatabase("/data/data/se.chalmers.eda397.team7.so/databases/so.sqlite", null, SQLiteDatabase.OPEN_READONLY);
		
		dataLayerFactory = new DataLayerFactory(db);
		
		commentDL = dataLayerFactory.createCommentDataLayer();
		
		entityFactory = new EntityFactory(dataLayerFactory);
		
		testUtils = new TestUtils(db, entityFactory);
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
//////////Tests
//////////////////////////////////////////////////////////
	
	/////////////////////////////////////
	// PostDataLayer.getComentsByPostId(Integer)
	/////////////////////////////////////
	
	public void test_getComments(){
		
		Post           p        ;
		List<Comment> comments  ;
		String        query     ;
		Cursor        cur       ;
		Map<Integer, Comment>  commentsIds; 
		
		do{
			p = testUtils.generateRandomPostInstance();
			comments = this.commentDL.getCommentsByPostId(p.getId());
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
		
		for(Map<String, String> commentRow: testUtils.getAllCursorValues(cur)){
			
			assertTrue("The id " + commentRow.get("id") + " is not in the retrieved set", commentsIds.keySet().contains(Integer.valueOf(commentRow.get("id"))));
		}
	}

}
