package se.chalmers.eda397.team7.so.datalayer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import se.chalmers.eda397.team7.so.data.entity.EntityUtils;
import se.chalmers.eda397.team7.so.data.entity.Post;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

@SuppressLint("UseValueOf")
public class PostDataLayer extends DataLayer<Post>{
	
	/* Lazy retrievers */
	private DataLayer.LazyRetriever<String, PostIndexInformation> indexedFullTextLazyRetriever; 	

	/* Cache IDS */
	private static final Integer CACHE_ID_pagedFullTextSearchCache;
	static{
		CACHE_ID_pagedFullTextSearchCache = "se.chalmers.eda397.team7.so.datalayer.PostDataLayer#pagedFullTextSearchCache(Set<String>, Integer, Integer)".hashCode();
	}
		
	protected PostDataLayer(DataLayerFactory dl) {
		super(dl);
		
		final PostDataLayer                     thiz               = this;
		
		indexedFullTextLazyRetriever = new 
			DataLayer.LazyRetriever<String, PostIndexInformation>() {
				@Override
				public Collection<PostIndexInformation> retrieve(Set<String> words) {
					
					return thiz.indexedfullTextSearch(words);
				}
			}
		;
		
	}

	/**
	 * In this method the Entity factory is used
	 * @return
	 */
	public Post createPost(
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
		Post p = this.getEntityFactory().createPost(
				id                       ,
				post_type_id             ,
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
	
	/**
	 * In this method ones use the sql objet
	 */
	public Post getPostById(Integer id){

		return this.querySingleInstance("posts", new String[]{id.toString()});
	}



	public void updatePost(Integer id, Map<String, String> attValues) {
		
		Map<String, String> key;
		
		key = new HashMap<String, String>();
		
		key.put("id", id.toString());
		
		this.queryInsertOrReplace("posts", attValues, key);
		
	}
	
	//TODO: this is just for testing, remove! 
	public List<Post> getQuestionList(){
		
		return pagedFullTextSearch(new HashSet<String>(Arrays.asList(new String[]{"java"})), 4, 1);
	}

	//////////////////////////////////////
	/////////// Paged searches
	//////////////////////////////////////	
	
	public List<Post> pagedFullTextSearch(Set<String> words, Integer pageSize, Integer page){
		
		return this.pagedSearch(words, pageSize, page, this.indexedFullTextLazyRetriever, PostDataLayer.CACHE_ID_pagedFullTextSearchCache);
	}
	
	//////////////////////////////////////
	/////////// Indexed searches
	//////////////////////////////////////
	
	
	public SortedSet<PostIndexInformation> indexedTagSearch(Set<String> words){
		
		String query;		
		Cursor cur;
		SortedSet<PostIndexInformation> postIds;
		
		if(words.size()>0){		
			query = generateIdSearchQuery(words.size(), "searchindex_tags", "posts", "tag");
			
			cur = this.getDbInstance().rawQuery(query, words.toArray(new String[0]));
			
			postIds = this.extractSortedPostIndexesFromCursor(cur, EnumSet.of(INDEX_MODE.TAG));
			
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
//	@SuppressLint("UseSparseArrays")
//	public SortedSet<PostIndexInformation> indexedfullTextSearch(Set<String> words){
//		
//		Map<Integer,PostIndexInformation> indexInfos    ;
//		SortedSet<PostIndexInformation> sortedIndexInfos;
//				
//		SQLiteDatabase db ;
//		Cursor         cur;
//		
//		String[] wordsArray;
//		
//		String titleIndexQuery    ;
//		String postIndexQuery     ;
//		String commentsIndexQuery ;
//		String tagsIndexQuery     ;
//		
//		PostIndexInformation postInfo;
//		Integer              postId  ;
//		
//		long tStart, tEnd;
//		
//		
//		tStart = System.currentTimeMillis();
//		
//		titleIndexQuery    = generateSearchQuery(words.size(), "searchindex_post_titles", "word");
//		tagsIndexQuery     = generateSearchQuery(words.size(), "searchindex_tags"       , "tag" );
//		postIndexQuery     = generateSearchQuery(words.size(), "searchindex_posts"      , "word");
//		commentsIndexQuery = generateSearchQuery(words.size(), "searchindex_comments"   , "word");
//		
//		indexInfos = new HashMap<Integer,PostIndexInformation>();
//		
//		db = this.getDbInstance();
//		
//		
//		wordsArray = new String[words.size()];
//		wordsArray = words.toArray(wordsArray);
//		
//		/* Post titles */
//		cur = db.rawQuery(titleIndexQuery, wordsArray);
//		
//		while(cur.moveToNext()){
//			postId = cur.getInt(0);
//			
//			postInfo = indexInfos.get(postId);
//			
//			if(postInfo==null){
//				postInfo = new PostIndexInformation(this,postId);
//				indexInfos.put(postId, postInfo);
//			}
//			
//			postInfo.incrementTitleMatch();
//		}
//		
//		/* Tags */
//		cur = db.rawQuery(tagsIndexQuery, wordsArray);
//		
//		while(cur.moveToNext()){
//			postId = cur.getInt(0);
//			
//			postInfo = indexInfos.get(postId);
//			
//			if(postInfo==null){
//				postInfo = new PostIndexInformation(this,postId);
//				indexInfos.put(postId, postInfo);
//			}
//			
//			postInfo.incrementTagMatch();
//		}
//		
//		/* Posts */
//		cur = db.rawQuery(postIndexQuery, wordsArray);
//		
//		while(cur.moveToNext()){
//			postId = cur.getInt(0);
//			
//			postInfo = indexInfos.get(postId);
//			
//			if(postInfo==null){
//				postInfo = new PostIndexInformation(this,postId);
//				indexInfos.put(postId, postInfo);
//			}
//			
//			postInfo.incrementPostMatch();
//		}
//		
//		/* Comments */
//		cur = db.rawQuery(commentsIndexQuery, wordsArray);
//		
//		while(cur.moveToNext()){
//			postId = cur.getInt(0);
//			
//			postInfo = indexInfos.get(postId);
//			
//			if(postInfo==null){
//				postInfo = new PostIndexInformation(this,postId);
//				indexInfos.put(postId, postInfo);
//			}
//			
//			postInfo.incrementCommentMatch();
//		}
//		
//		/* Now we sort the results */
//		sortedIndexInfos = new TreeSet<PostDataLayer.PostIndexInformation>();
//		sortedIndexInfos.addAll(indexInfos.values());
//		
//		/* And retrieve the posts in order */
//		
//		tEnd = System.currentTimeMillis();
//		
//		Log.d("se.chalmers.eda397.team7.so.datalayer.DataLayer#pagedSearch", "Finished retrieving a " + sortedIndexInfos.size() + " sized list from a full text search from the database in  " + (tEnd - tStart) + "ms");
//		
//		return sortedIndexInfos;
//	}
	
	@SuppressLint("UseSparseArrays")
	public SortedSet<PostIndexInformation> indexedfullTextSearch(Set<String> words){
		
		Map<Integer,PostIndexInformation> indexInfos    ;
		SortedSet<PostIndexInformation>   sortedIndexInfos;
				
		SQLiteDatabase db ;
		Cursor         cur;
		
		String[] wordsArray;
		
		String titleIndexQuery    ;
		String postIndexQuery     ;
		String commentsIndexQuery ;
		String tagsIndexQuery     ;

		EnumSet<INDEX_MODE> mode ;
		
		long tStart, tEnd;
		
		tStart = System.currentTimeMillis();
		
		titleIndexQuery    = generateSearchQuery(words.size(), "searchindex_post_titles", "word");
		tagsIndexQuery     = generateSearchQuery(words.size(), "searchindex_tags"       , "tag" );
		postIndexQuery     = generateSearchQuery(words.size(), "searchindex_posts"      , "word");
		commentsIndexQuery = generateSearchQuery(words.size(), "searchindex_comments"   , "word");
		
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
		mode = EnumSet.of(INDEX_MODE.POST);
		cur = db.rawQuery(postIndexQuery, wordsArray);
		extractMappedPostIndexesFromCursor(cur,mode,indexInfos);
		
		/* Comments */
		mode = EnumSet.of(INDEX_MODE.COMMENT);
		cur = db.rawQuery(commentsIndexQuery, wordsArray);
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
	protected Post createNotSyncronizedInstance(Cursor cur) {
		
		return EntityUtils.createPostFromCursor(this.getEntityFactory(), cur);
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
			
			if(mode.contains(INDEX_MODE.TAG  )){
				current.incrementTagMatch();
			}
			if(mode.contains(INDEX_MODE.TITLE)){
				current.incrementTitleMatch();
			}
			if(mode.contains(INDEX_MODE.POST)){
				current.incrementPostMatch();
			}
			if(mode.contains(INDEX_MODE.COMMENT)){
				current.incrementCommentMatch();
			}
		}
		
		return postIdsMap;
	}
	
	//////////////////////////////////////
	/////////// Public internal clases
	//////////////////////////////////////
	
	public static class PostIndexInformation extends DataLayer.EntityIndex<Post> implements Comparable<PostIndexInformation>{
				
		private final PostDataLayer pdl;
		
		private Integer titleMatchs   ;
		private Integer postMatchs    ;
		private Integer tagsMatchs    ;
		private Integer commentsMatchs;
		
		protected PostIndexInformation(PostDataLayer pdl,Integer postId){
			super(postId);
			
			this.pdl = pdl;
			
			titleMatchs = 0;
			tagsMatchs = 0;
			commentsMatchs = 0;
			postMatchs= 0;
			
			
		}
		
		public void incrementTitleMatch(){
			this.titleMatchs++;
		}
		
		public void incrementPostMatch(){
			this.postMatchs++;
		}
		
		public void incrementTagMatch(){
			this.tagsMatchs++;
		}
		
		public void incrementCommentMatch(){
			this.commentsMatchs++;
		}

		
		public Post retrieveInstance(){
			return this.pdl.getPostById(this.getId());
		}
		
		public Integer getTitleMatchs() {
			return titleMatchs;
		}

		public Integer getPostMatchs() {
			return postMatchs;
		}

		public Integer getTagsMatchs() {
			return tagsMatchs;
		}

		public Integer getCommentsMatchs() {
			return commentsMatchs;
		}

		@Override
		public String toString(){
			
			return "<[" + this.getId() + "]{" + this.titleMatchs + "," + this.tagsMatchs + "," + this.postMatchs + "," + this.commentsMatchs + "}>";
		}		
		

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime
					* result
					+ ((commentsMatchs == null) ? 0 : commentsMatchs.hashCode());
			result = prime * result + ((pdl == null) ? 0 : pdl.hashCode());
			result = prime * result
					+ ((postMatchs == null) ? 0 : postMatchs.hashCode());
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
			if (pdl == null) {
				if (other.pdl != null)
					return false;
			} else if (!pdl.equals(other.pdl))
				return false;
			if (postMatchs == null) {
				if (other.postMatchs != null)
					return false;
			} else if (!postMatchs.equals(other.postMatchs))
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
		public int compareTo(PostIndexInformation  rhs){
			int compare = 0;
			
			compare = this.titleMatchs.compareTo(rhs.titleMatchs);
			
			if(compare == 0){
				compare = this.tagsMatchs.compareTo(rhs.tagsMatchs);
				
				if(compare==0){
					compare = this.postMatchs.compareTo(rhs.postMatchs);
					
					if(compare==0){
						compare = this.postMatchs.compareTo(rhs.postMatchs);
						
							if(compare==0){
								compare = this.getId().compareTo(rhs.getId());
							}
					}
				}
			}
			
			return -compare;
		}
	}
	
	private enum INDEX_MODE{
		TAG, TITLE, POST, COMMENT 
	};
	
}





















