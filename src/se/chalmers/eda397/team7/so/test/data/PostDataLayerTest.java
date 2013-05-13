package se.chalmers.eda397.team7.so.test.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import se.chalmers.eda397.team7.so.data.entity.EntityFactory;
import se.chalmers.eda397.team7.so.data.entity.Post;
import se.chalmers.eda397.team7.so.data.entity.Question;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer.OrderCriteria;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer.PostIndexInformation;
import se.chalmers.eda397.team7.so.test.utils.TestUtils;
import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;

@SuppressLint({ "SdCardPath", "UseSparseArrays" })
public class PostDataLayerTest extends InstrumentationTestCase {

	private final SQLiteDatabase   db              ;
	private final PostDataLayer    postDL          ;
	private final EntityFactory    entityFactory   ;
	private final DataLayerFactory dataLayerFactory;
	private final TestUtils        testUtils       ;
	
	public PostDataLayerTest (){
		db = SQLiteDatabase.openDatabase("/data/data/se.chalmers.eda397.team7.so/databases/so.sqlite", null, SQLiteDatabase.OPEN_READONLY);
		
		dataLayerFactory = new DataLayerFactory(db);
		
		postDL = dataLayerFactory.createPostDataLayer();
		
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
////////// Tests
//////////////////////////////////////////////////////////
	
	//////////////////////////////////////
	////// PostDataLayer.createQuestion(...)
	//////////////////////////////////////
	
	public void test_createQuestion(){
		
		Integer newId = postDL.getMaxId();
		
//		Question q = postDL.createQuestion(
//			newId,  
//			parent_id, 
//			accepted_answer_id, 
//			creation_date, 
//			score, 
//			view_count, 
//			body, 
//			owner_user_id, 
//			last_editor_user_id, 
//			last_editor_display_name, 
//			last_edit_date, 
//			last_activity_date, 
//			community_owned_date, 
//			closed_date, 
//			title, 
//			tags, 
//			answer_count, 
//			comment_count, 
//			favorite_count
//		);
	}
	
	//////////////////////////////////////
	////// PostDataLayer.getPostById(Integer)
	//////////////////////////////////////
	
	public void test_getPostById(){
		
		Map<String,String> randomPostAttValues ;
		Post               retPost             ;
		
		
		randomPostAttValues = testUtils.generateRandomPostValues();
		retPost = this.postDL.getQuestionById(Integer.valueOf(randomPostAttValues.get("id")));
		
		assertPostAttributeValue(randomPostAttValues, retPost);
	}
	
	//////////////////////////////////////
	////// PostDataLayer.indexedfullTextSearch(Set<String>)
	//////////////////////////////////////
	
	public void test_indexedfullTextSearch(){
		//TODO:
		Set<String> words = new HashSet<String>(Arrays.asList(new String[]{"php", "mysql", "apache"}));
		
		@SuppressWarnings("unused")
		SortedSet<PostIndexInformation> indexResults = this.postDL.indexedfullTextSearch(words);
	}
	
	//////////////////////////////////////
	////// PostDataLayer.pagedfullTextSearch(Set<String>, Integer, Integer)
	//////////////////////////////////////
	
	public void test_pagedFullTextSearch(){
		
		Set<String> words                       ;
		List<PostIndexInformation> indexResults ;
		boolean enterLoop;
		
		words = new HashSet<String>(Arrays.asList(new String[]{"php", "mysql", "apache"}));
		indexResults = new ArrayList<PostIndexInformation>(this.postDL.indexedfullTextSearch(words));
		
		enterLoop = !(indexResults.size()>0);
		
		List<Question> postResults;
		for(int i=0; i<(indexResults.size()/10); i++){
			postResults = this.postDL.pagedFullTextSearch(words, 10, i);
			enterLoop = true;
			
			for(int j=0; j<postResults.size(); j++){
				assertEquals(indexResults.get(i*10 + j).getId(), postResults.get(j).getId());
			}
		}

		assertTrue(enterLoop);
	}	
	
	
	//////////////////////////////////////
	////// PostDataLayer.indexedTagSearch(Set<String>)
	//////////////////////////////////////
	
	public void test_indexedTagSearch1(){
		
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
	
	public void test_indexedTagSearch2(){
		
		Set<String> words = new HashSet<String>(Arrays.asList(new String[]{"eclipse"}));
		
		SortedSet<PostIndexInformation> indexResults = this.postDL.indexedTagSearch(words);
		
		assertTrue(indexResults.size()>0);
		
		Post curPost;
		Set<String> tagIntersection;
		for(PostIndexInformation postIdx : indexResults){
			curPost = postIdx.retrieveInstance();
			 
			tagIntersection = new HashSet<String>(words);
			tagIntersection.retainAll(curPost.getTags());
			
			
			assertEquals((Integer)tagIntersection.size(), postIdx.getTagsMatchs());			
		}
		
	}
	
	//////////////////////////////////////
	////// PostDataLayer.indexedTagSearch(Set<String>, OrderCriteria)
	//////////////////////////////////////
	
	public void test_indexedTagSearch_withOrderCriteria(){
		
		Set<String> words = new HashSet<String>(Arrays.asList(new String[]{"java"}));
		
		SortedSet<PostIndexInformation> indexResults = this.postDL.indexedTagSearch(words,OrderCriteria.CREATION_DATE);
		
		assertTrue(indexResults.size()>0);
		
		Post previousPost = null;
		Post curPost;
		Set<String> tagIntersection;
		for(PostIndexInformation postIdx : indexResults){
			curPost = postIdx.retrieveInstance();
			 
			tagIntersection = new HashSet<String>(words);
			tagIntersection.retainAll(curPost.getTags());
			
			assertEquals((Integer)tagIntersection.size(), postIdx.getTagsMatchs());
			
			if(previousPost!=null){
				assertTrue(previousPost.getCreation_date().compareTo(curPost.getCreation_date())>=0);
			}
			
			previousPost = curPost;
		}
		
	}
	
	public void test_indexedTagSearch_withAnswerCountCriteria(){
		
		Set<String> words = new HashSet<String>(Arrays.asList(new String[]{"java"}));
		
		SortedSet<PostIndexInformation> indexResults = this.postDL.indexedTagSearch(words,OrderCriteria.ANSWER_COUNT);
		
		assertTrue(indexResults.size()>0);
		
		Post previousPost = null;
		Post curPost;
		Set<String> tagIntersection;
		for(PostIndexInformation postIdx : indexResults){
			curPost = postIdx.retrieveInstance();
			 
			tagIntersection = new HashSet<String>(words);
			tagIntersection.retainAll(curPost.getTags());
			
			assertEquals((Integer)tagIntersection.size(), postIdx.getTagsMatchs());
			
			if(previousPost!=null){
				assertTrue(previousPost.getAnswer_count().compareTo(curPost.getAnswer_count())>=0);
			}
			
			previousPost = curPost;
		}
		
	}
	
	//////////////////////////////////////
	////// PostDataLayer.pagedTagSearch(Set<String>, Integer, Integer)
	//////////////////////////////////////
	
	public void test_pagedTagSearch(){
		
		Set<String> words                       ;
		List<PostIndexInformation> indexResults ;
		boolean enterLoop = true;;
		
		words = new HashSet<String>(Arrays.asList(new String[]{"php", "mysql", "apache"}));
		indexResults = new ArrayList<PostIndexInformation>(this.postDL.indexedTagSearch(words));
		
		enterLoop = !(indexResults.size()>0);
		
		List<Question> postResults;
		for(int i=0; i<(indexResults.size()/10+1); i++){
			enterLoop = true;

			postResults = this.postDL.pagedTagSearch(words,10, i);

			
			for(int j=0; j<postResults.size(); j++){
				assertEquals(indexResults.get(i*10 + j).getId(), postResults.get(j).getId());
			}
		}

		assertTrue(enterLoop);
	}
	
	public void test_pagedTagSearch1(){
		
		Set<String> words                       ;
		List<PostIndexInformation> indexResults ;
		boolean enterLoop;
		
		words = new HashSet<String>(Arrays.asList(new String[]{"eclipse"}));
		indexResults = new ArrayList<PostIndexInformation>(this.postDL.indexedTagSearch(words));
		
		
		enterLoop = !(indexResults.size()>0);
		
		assertTrue(indexResults.size()>0);
		
		List<Question> postResults;
		for(int i=0; i<(indexResults.size()/50 + 1); i++){
			postResults = this.postDL.pagedTagSearch(words, 50, i);

			enterLoop = true;
			
			for(int j=0; j<postResults.size(); j++){
				assertEquals(indexResults.get(i*10 + j).getId(), postResults.get(j).getId());
			}
		}
		
		assertTrue(enterLoop);
		
		/* Test cache */
		enterLoop = !(indexResults.size()>0);
		
		assertTrue(indexResults.size()>0);
		
		for(int i=0; i<(indexResults.size()/50 + 1); i++){
			postResults = this.postDL.pagedTagSearch(words, 50, i);

			enterLoop = true;
			
			for(int j=0; j<postResults.size(); j++){
				assertEquals(indexResults.get(i*10 + j).getId(), postResults.get(j).getId());
			}
		}
		
		assertTrue(enterLoop);

	}
	
//////////////////////////////////////////////////////////
////////// Auxiliary methods
//////////////////////////////////////////////////////////
	
	//TODO: this not very thorough...
	private void assertPostAttributeValue(Map<String, String> attValues, Post p){
		
		assertEquals(Integer.valueOf(attValues.get("id")), p.getId()) ;
	}
	
	
}

