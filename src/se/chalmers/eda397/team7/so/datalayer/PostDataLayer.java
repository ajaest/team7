package se.chalmers.eda397.team7.so.datalayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import se.chalmers.eda397.team7.so.data.entity.EntityCreationException;
import se.chalmers.eda397.team7.so.data.entity.EntityUtils;
import se.chalmers.eda397.team7.so.data.entity.Question;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


@SuppressLint("UseValueOf")
public class PostDataLayer extends DataLayer<Question>{
	
	/* Lazy retrievers */
	private DataLayer.LazyRetriever<String, PostIndexInformation> indexedFullTextLazyRetriever;
	private DataLayer.LazyRetriever<String, PostIndexInformation> indexedTagLazyRetriever     ; 	
	
	private OrderCriteria currentOrderCriteria = null;
	/* Cache IDS */
	private static final Integer CACHE_ID_pagedFullTextSearchCache;
	private static final Integer CACHE_ID_pagedTagSearch          ;
	static{
		CACHE_ID_pagedFullTextSearchCache = "se.chalmers.eda397.team7.so.datalayer.PostDataLayer#pagedFullTextSearchCache(Set<String>, Integer, Integer)".hashCode();
		CACHE_ID_pagedTagSearch           = "se.chalmers.eda397.team7.so.datalayer.PostDataLayer#pagedTagSearch          (Set<String>, Integer, Integer)".hashCode();
	}
		
	protected PostDataLayer(DataLayerFactory dl) {
		super(dl);
		
		final PostDataLayer thiz = this;
		
		indexedFullTextLazyRetriever = new 
			DataLayer.LazyRetriever<String, PostIndexInformation>() {
				@Override
				public Collection<PostIndexInformation> retrieve(Set<String> words) {
					
					return thiz.indexedfullTextSearch(words);
				}
			}
		;
		
		indexedTagLazyRetriever = new 
				DataLayer.LazyRetriever<String, PostIndexInformation>() {
			
					@Override
					public Collection<PostIndexInformation> retrieve(Set<String> words) {
						
						return thiz.indexedTagSearch(words, thiz.currentOrderCriteria);
					}
					
					@Override
					public int hashCode(){
						
						int hashcode = super.hashCode();
						if(thiz.currentOrderCriteria!=null)
							hashcode += thiz.currentOrderCriteria.hashCode() * 31;	
						return hashcode;
						
					}
				
				}
			;
		
	}


	public Question createQuestion(
		Integer id                       ,
		Integer post_type_id             ,
		Integer parent_id                ,
		Integer accepted_answer_id       ,
		Date    creation_date            ,
		Integer score                    ,
		Integer view_count               ,
		String  body                     ,
		Integer owner_user_id            ,
		Integer last_editor_user_id      ,
		String  last_editor_display_name ,
		Date    last_edit_date           ,
		Date    last_activity_date       ,
		Date    community_owned_date     ,
		Date    closed_date              ,
		String  title                    ,
		String  tags                     ,
		Integer answer_count             ,
		Integer comment_count            ,
		Integer favorite_count 		
	){
		Question p = this.getEntityFactory().createQuestion(
				id                       ,
				parent_id                ,
				accepted_answer_id       ,
				creation_date            ,
				score                    ,
				view_count               ,
				body                     ,
				owner_user_id            ,
				last_editor_user_id      ,
				last_editor_display_name ,
				last_edit_date           ,
				last_activity_date       ,
				community_owned_date     ,
				closed_date              ,
				title                    ,
				tags                     ,
				answer_count             ,
				comment_count            ,
				favorite_count 
		);
		
		p.setDirty(true);
		p.commit();	
		
		return p;
	}
	
	public Integer getMaxId(){
		String queryString = "SELECT MAX(id) from posts where post_type_id=1";
		Cursor cur = this.getDbInstance().rawQuery(queryString, null);
		cur.moveToNext();
		Integer max = cur.getInt(0);
		cur.close();
		return max;
	}
	
	public Question getQuestionById(Integer id){


		return this.querySingleInstance("SELECT * FROM posts WHERE id=? AND post_type_id=1", new String[]{id.toString()});
	}
	
	public ArrayList<String> getListOfTags(){
		ArrayList<String> tempString = new ArrayList<String>();
		
		String query = "SELECT DISTINCT  tag FROM searchindex_tags";
		Cursor cur = this.getDbInstance().rawQuery(query, null);
		
		while(cur.moveToNext()){		
			tempString.add(cur.getString(0));
			
		}
		cur.close();
			
		return tempString;
	}
	

	public void updatePost(Integer id, Map<String, String> attValues) {
		
		Map<String, String> key;
		
		key = new HashMap<String, String>();
		
		key.put("id", id.toString());
		
		this.queryInsertOrReplace("posts", attValues, key);
		
	}
	
	public void updateIndex(Integer id, Map<String, Set<String>> indexValues) {
		
		Cursor cur;
		SQLiteDatabase sqlDb;
		
		String reusable_query_delete  = "DELETE FROM searchindex_%s WHERE id=?";
		String reausable_query_insert = "INSERT INTO searchindex_%s VALUES (?,?)";
		
		sqlDb = this.getDbInstance();
		
		String query;
		for(String index_suffix : indexValues.keySet()){
			/* Remove all previously existing indexes in this table */
			query = String.format(reusable_query_delete, index_suffix);
			cur = sqlDb.rawQuery(query, new String[]{id.toString()});
			cur.close();
			
			query = String.format(reausable_query_insert, index_suffix);
			
			/* Add new indexes */
			for(String word: indexValues.get(index_suffix)){
				
				cur = sqlDb.rawQuery(query, new String[]{id.toString(), word});
				cur.close();
			}
		}
		
	}

	public List<Question> getQuestionSortedBy(String sortCriteria){
		String queryString = "SELECT * FROM posts WHERE post_type_id=1 ORDER BY "+ sortCriteria +" DESC LIMIT 40";
		
		return this.querySortedInstanceSet(queryString, new String[]{});
	}
	
	
	
	public List<Question> getQuestionsOfUser(Integer idUser, OrderCriteria orderCriteria){
		String query = "SELECT * FROM posts WHERE post_type_id=1 AND owner_user_id = ? ORDER BY ?";
		
		return this.querySortedInstanceSet(query, new String[]{idUser.toString(), orderCriteria.toString()});
	}
	
	
	/////////////////////////////////
	/// Methods that query information of tags
	/////////////////////////////////
	
	/*
     * Gets the 4 tags more related to the given tag.
     */
    public List<String> getCloseTags(String tag){
    	ArrayList<String> closeTags = new ArrayList<String>();
    	Cursor cursor;
    	cursor = this.getDbInstance().rawQuery("SELECT sum(weight), tag1,tag2 from tag_graph " +
    			"where tag1<>tag2 and tag1=? " +
    			" group by tag1,tag2 order by 1 desc LIMIT 4",new String[]{tag});
    	while (  cursor.moveToNext()) {
			closeTags.add(cursor.getString(2));
		}
    	cursor.close();
    	return closeTags;
    }
    
    /*
     * Returns the list of tags in our system order by alphabetical order
     */
    public List<String> getAllTags(){
    	Cursor cur;
    	List<String> tagsList = new ArrayList<String>();
  
		cur = this.getDbInstance().rawQuery("SELECT tag, count(*) FROM searchindex_tags GROUP BY tag ORDER BY 1 ", null);
		while(cur.moveToNext())
				tagsList.add(cur.getString(0));
		
		cur.close();
    	return tagsList;
    }
	
	
	
	
	//////////////////////////////////////
	/////////// Favorite posts things
	//////////////////////////////////////	
	public void addFavourite(int post_id, int user_id)
	{
		String query ="";
		query = "INSERT INTO favourite_posts (post_id,user_id) VALUES ("+ post_id+","+user_id+");";
		this.getDbInstance().execSQL(query);
		
	}
	
	public List<Question> getAllFavourite(int user_id, OrderCriteria orderCriteria){
		String query="";
		List<Question> retList = new ArrayList<Question>();
		query = "SELECT * FROM favourite_posts JOIN posts ON post_id=id WHERE user_id=? ORDER BY ? DESC";
		retList = this.querySortedInstanceSet(query, new String[]{""+user_id, orderCriteria.toString()});
		return retList;
		
	}
	
	public boolean isFavourite(int post_id,int user_id){
		String query = "";
		boolean exist = false;
		
		query = "SELECT count(*) FROM favourite_posts WHERE post_id=? AND user_id=?"; 
		
		Cursor cur = this.getDbInstance().rawQuery(query, new String[]{"" + post_id, "" + user_id});
		cur.moveToNext();
		exist = cur.getInt(0)>0;
		cur.close();
		return exist;

	}
	
	/***********************************
	 * TestFunction**********************
	**************************************/
	public String isFavouriteS(int post_id,int user_id){
		String query = "";
		query ="SELECT EXISTS(SELECT 1 FROM favourite_posts WHERE post_id="+post_id+" AND user_id="+user_id+" LIMIT 1);";
		this.getDbInstance().rawQuery(query, null);
		Cursor cur = this.getDbInstance().rawQuery(query, null);
		String test = "";

	
		while(cur.moveToNext()){		
			test = test + (cur.getString(0));		
		}
		cur.close();
		return test;

	}
	
	
	public void removeFavourite(int post_id,int user_id){
		String query ="";
		query = "DELETE FROM favourite_posts WHERE post_id="+post_id+" AND user_id="+user_id+";";
		this.getDbInstance().execSQL(query);
		
	}
	

	//////////////////////////////////////
	/////////// Paged searches
	//////////////////////////////////////	
	
	public List<Question> pagedFullTextSearch(Set<String> words, Integer pageSize, Integer page){
		
		
		return this.pagedSearch(words, pageSize, page, this.indexedFullTextLazyRetriever, PostDataLayer.CACHE_ID_pagedFullTextSearchCache, "post_type_id=1");
	}
	
	public List<Question> pagedTagSearch(Set<String> words, Integer pageSize, Integer page){
		
		return this.pagedTagSearch(words, null, pageSize, page);
	}
	
	public List<Question> pagedTagSearch(Set<String> words,OrderCriteria criteria, Integer pageSize, Integer page){
		
		this.currentOrderCriteria = criteria;
				
		return this.pagedSearch(words, pageSize, page, this.indexedTagLazyRetriever, PostDataLayer.CACHE_ID_pagedTagSearch, "post_type_id=1");
	}
	
	

	//////////////////////////////////////
	/////////// Indexed searches
	//////////////////////////////////////
	
	public SortedSet<PostIndexInformation> indexedTagSearch(Set<String> words){
		
		return indexedTagSearch(words, null);
	}
	
	public enum OrderCriteria{
		
		CREATION_DATE,
		ANSWER_COUNT;
		
		@Override
		public String toString(){
			
			switch (this) {
			case CREATION_DATE:
				
				return "creation_date";
			case ANSWER_COUNT:
				return "answer_count";
			default:
				throw new RuntimeException("WTF! you forgot to add an OrderCriteria !");
			}
		}		
	}
	
	public SortedSet<PostIndexInformation> indexedTagSearch(Set<String> words, OrderCriteria orderCriteria){
		
		String query, orderTail;		
		Cursor cur;
		SortedSet<PostIndexInformation> postIds;
		EnumSet<INDEX_MODE> searchMode;
		
		searchMode = EnumSet.of(INDEX_MODE.TAG);
		
		if(orderCriteria!=null ){
			orderTail = " 1=1 ORDER BY "  + orderCriteria.toString() + " DESC";
			searchMode.add(INDEX_MODE.QUERY_ORDER);
		}else{
			orderTail = null;
		}
		
		if(words.size()>0){		
			query = generateIdSearchQuery(words.size(), "searchindex_tags", "posts", "tag", orderTail);
			
			cur = this.getDbInstance().rawQuery(query, words.toArray(new String[0]));
			
			postIds = this.extractSortedPostIndexesFromCursor(cur, searchMode);
			
		}else{
			postIds = new TreeSet<PostDataLayer.PostIndexInformation>();
		}
		
		return postIds;
	}
	
	
	/**
	 * Returns the indexes of the posts which contains in any of the specified
	 * words in any of their token set post title, tags, post body and post
	 * comments ordered by the number of found matches.
	 * 
	 * The first criteria to order is the number of matches in the post title.
	 * If to posts have the same number of matches the second criteria is the
	 * number of tags, and so on with post bodies and related comment bodies.
	 * 
	 * @param words
	 *            the list of words to match
	 * @return The list of Post index references sorted by number of word
	 *         matches.
	 *         
	 * @see PostIndexInformation
	 * @see PostDataLayer#pagedFullTextSearch(Set, Integer, Integer)
	 */
	@SuppressLint("UseSparseArrays")
	public SortedSet<PostIndexInformation> indexedfullTextSearch(Set<String> words){
		
		Map<Integer,PostIndexInformation> indexInfos    ;
		SortedSet<PostIndexInformation>   sortedIndexInfos;
				
		SQLiteDatabase db ;
		Cursor         cur;
		
		String[] wordsArray;
		
		String titleIndexQuery     ;
		String postIndexQuery      ;
		String responsesIndexQuery ;
		String commentsIndexQuery  ;
		String tagsIndexQuery      ;

		EnumSet<INDEX_MODE> mode ;
		
		long tStart, tEnd;
		
		tStart = System.currentTimeMillis();
		
		titleIndexQuery       = generateSearchQuery(words.size(), "searchindex_question_titles", "word");
		tagsIndexQuery        = generateSearchQuery(words.size(), "searchindex_tags"           , "tag" );
		postIndexQuery        = generateSearchQuery(words.size(), "searchindex_questions"      , "word");
		responsesIndexQuery   = generateSearchQuery(words.size(), "searchindex_responses"      , "word");
		commentsIndexQuery    = generateSearchQuery(words.size(), "searchindex_comments"       , "word");
		
		indexInfos = new HashMap<Integer,PostIndexInformation>();
		
		db = this.getDbInstance();
	
		wordsArray = words.toArray(new String[0]);
		
		/* Post titles */
		mode = EnumSet.of(INDEX_MODE.TITLE);
		cur = db.rawQuery(titleIndexQuery, wordsArray);
		extractMappedPostIndexesFromCursor(cur,mode,indexInfos);
		
		/* Tags */
		mode = EnumSet.of(INDEX_MODE.TAG);
		cur = db.rawQuery(tagsIndexQuery, wordsArray);
		extractMappedPostIndexesFromCursor(cur,mode,indexInfos);
		
		/* Posts */
		mode = EnumSet.of(INDEX_MODE.QUESTION);
		cur = db.rawQuery(postIndexQuery, wordsArray);
		extractMappedPostIndexesFromCursor(cur,mode,indexInfos);
		
		/* Comments */
		mode = EnumSet.of(INDEX_MODE.COMMENT);
		cur = db.rawQuery(commentsIndexQuery, wordsArray);
		extractMappedPostIndexesFromCursor(cur,mode,indexInfos);
		
		/* Responses */
		mode = EnumSet.of(INDEX_MODE.RESPONSE);
		cur = db.rawQuery(responsesIndexQuery, wordsArray);
		extractMappedPostIndexesFromCursor(cur,mode,indexInfos);
		
		/* Now we sort the results */
		sortedIndexInfos = new TreeSet<PostDataLayer.PostIndexInformation>();
		sortedIndexInfos.addAll(indexInfos.values());
				
		tEnd = System.currentTimeMillis();
		
		Log.d("se.chalmers.eda397.team7.so.datalayer.DataLayer#pagedSearch", "Finished retrieving a " + sortedIndexInfos.size() + " sized list from a full text search from the database in  " + (tEnd - tStart) + "ms");
		
		return sortedIndexInfos;
	}
	
	//////////////////////////////////////
	/////////// Private support classes
	//////////////////////////////////////
	
	@Override
	protected Question createNotSyncronizedInstance(Cursor cur) {
		
		return EntityUtils.createQuestionFromCur(this.getEntityFactory(), cur);		
	}
	
	private SortedSet<PostIndexInformation> extractSortedPostIndexesFromCursor(Cursor cur, EnumSet<INDEX_MODE> mode){
		
		Map<Integer,PostIndexInformation> idMap;
		
		idMap = extractMappedPostIndexesFromCursor(cur, mode, null);
		
		return new TreeSet<PostDataLayer.PostIndexInformation>(idMap.values());
	}
	
	@SuppressLint("UseSparseArrays")
	private Map<Integer,PostIndexInformation> extractMappedPostIndexesFromCursor(Cursor cur, EnumSet<INDEX_MODE> mode, Map<Integer,PostIndexInformation> idMap){
		
		
		Map<Integer,PostIndexInformation> postIdsMap;
		PostIndexInformation current;
		Integer currentId;
		
		if(idMap==null)
			postIdsMap = new HashMap<Integer,PostDataLayer.PostIndexInformation>(cur.getCount());
		else
			postIdsMap = idMap;
		
		while(cur.moveToNext()) {
			
			currentId = cur.getInt(cur.getColumnIndex("id"));
			
			if(null ==(current = postIdsMap.get(currentId))){
				current = new PostIndexInformation(this,currentId);
				postIdsMap.put(currentId, current);
			}
			
			current.setInsertOrder(cur.getPosition());
			
			if(mode.contains(INDEX_MODE.TAG  )){
				current.incrementTagMatch();
			}
			if(mode.contains(INDEX_MODE.TITLE)){
				current.incrementTitleMatch();
			}
			if(mode.contains(INDEX_MODE.QUESTION)){
				current.incrementPostMatch();
			}
			if(mode.contains(INDEX_MODE.COMMENT)){
				current.incrementCommentMatch();
			}
			if(mode.contains(INDEX_MODE.RESPONSE)){
				current.incrementResponseMatch();
			}
			if(mode.contains(INDEX_MODE.QUERY_ORDER)){
				current.setCompareByInsertOrder(true);
			}
		}
		
		return postIdsMap;
	}
	
	//////////////////////////////////////
	/////////// Public internal clases
	//////////////////////////////////////
	
	public static class PostIndexInformation extends DataLayer.EntityIndex<Question> {
				
		private final PostDataLayer pdl;
		
		private Integer titleMatchs     ;
		private Integer questionMatchs      ;
		private Integer responseMatchs  ;
		private Integer tagsMatchs      ;
		private Integer commentsMatchs  ;
		
		
		
		protected PostIndexInformation(PostDataLayer pdl,Integer postId){
			this(pdl, postId, null);
		}
		
		/**
		 * 
		 * @param pdl the Post data layer
		 * @param postId the post id
		 * @param matchOrder whether this class uses match
		 */
		protected PostIndexInformation(PostDataLayer pdl,Integer postId, Integer insertOrder){
			super(postId,insertOrder);
			
			this.pdl = pdl;
			
			titleMatchs = 0;
			tagsMatchs = 0;
			commentsMatchs = 0;
			questionMatchs= 0;
			responseMatchs = 0;
			
		}
		

		public void incrementResponseMatch(){
			this.responseMatchs++;
		}
		
		public void incrementTitleMatch(){
			this.titleMatchs++;
		}
		
		public void incrementPostMatch(){
			this.questionMatchs++;
		}
		
		public void incrementTagMatch(){
			this.tagsMatchs++;
		}
		
		public void incrementCommentMatch(){
			this.commentsMatchs++;
		}

		
		public Question retrieveInstance(){
			
			Question q = this.pdl.getQuestionById(this.getId());;
			
			if(q==null){
				throw new EntityCreationException("The created task");
			}
			
			return q;
		}
		
		public Integer getTitleMatchs() {
			return titleMatchs;
		}

		public Integer getQuestionMatchs() {
			return questionMatchs;
		}
		
		public Integer getResponseMatchs() {
			return questionMatchs;
		}

		public Integer getTagsMatchs() {
			return tagsMatchs;
		}

		public Integer getCommentsMatchs() {
			return commentsMatchs;
		}

		@Override
		public String toString(){
			
			return "<[" + this.getId() + "]{" + this.titleMatchs + "," + this.tagsMatchs + "," + this.questionMatchs + "," + this.responseMatchs + "," + this.commentsMatchs + "}>";
		}		
		

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime
					* result
					+ ((commentsMatchs == null) ? 0 : commentsMatchs.hashCode());
			result = prime
					* result
					+ ((questionMatchs == null) ? 0 : questionMatchs.hashCode());
			result = prime
					* result
					+ ((responseMatchs == null) ? 0 : responseMatchs.hashCode());
			result = prime * result
					+ ((tagsMatchs == null) ? 0 : tagsMatchs.hashCode());
			result = prime * result
					+ ((titleMatchs == null) ? 0 : titleMatchs.hashCode());
			return result;
		}


		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (!super.equals(obj))
				return false;
			if (getClass() != obj.getClass())
				return false;
			PostIndexInformation other = (PostIndexInformation) obj;
			if (commentsMatchs == null) {
				if (other.commentsMatchs != null)
					return false;
			} else if (!commentsMatchs.equals(other.commentsMatchs))
				return false;
			if (questionMatchs == null) {
				if (other.questionMatchs != null)
					return false;
			} else if (!questionMatchs.equals(other.questionMatchs))
				return false;
			if (responseMatchs == null) {
				if (other.responseMatchs != null)
					return false;
			} else if (!responseMatchs.equals(other.responseMatchs))
				return false;
			if (tagsMatchs == null) {
				if (other.tagsMatchs != null)
					return false;
			} else if (!tagsMatchs.equals(other.tagsMatchs))
				return false;
			if (titleMatchs == null) {
				if (other.titleMatchs != null)
					return false;
			} else if (!titleMatchs.equals(other.titleMatchs))
				return false;
			return true;
		}


		@Override
		public int entityNaturalCompareTo(EntityIndex<Question> obj){
			int compare = 0;
			
			PostIndexInformation rhs = (PostIndexInformation)obj;
			/* If there are no order criteria defined, uses match count criteria*/

				
			compare = this.titleMatchs.compareTo(rhs.titleMatchs);
			
			if(compare == 0){
				compare = this.tagsMatchs.compareTo(rhs.tagsMatchs);
				
				if(compare==0){
					
					compare = this.questionMatchs.compareTo(rhs.questionMatchs);
					if(compare==0){
						
						compare = this.responseMatchs.compareTo(rhs.responseMatchs);
						if(compare==0){
							
							compare = this.commentsMatchs.compareTo(rhs.commentsMatchs);
							
								if(compare==0){
									compare = this.getId().compareTo(rhs.getId());
							}
						}
					}
				}
			}
						
			return -compare;
		}
	}
	
	private enum INDEX_MODE{
		TAG, TITLE, QUESTION, RESPONSE, COMMENT, QUERY_ORDER
	};
}

