package se.chalmers.eda397.team7.so.datalayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import se.chalmers.eda397.team7.so.data.entity.EntityUtils;
import se.chalmers.eda397.team7.so.data.entity.Post;
import se.chalmers.eda397.team7.so.data.entity.User;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

@SuppressLint("UseValueOf")
public class PostDataLayer extends DataLayer<Post>{
	
	private List<PostIndexInformation> cachedSearch;
	private SparseArray<Post>          cachedPosts ;
	private Integer                    cacheHash   ;
	
	protected PostDataLayer(DataLayerFactory dl) {
		super(dl);
		
		cachedSearch = new ArrayList<PostDataLayer.PostIndexInformation>();
		cachedPosts  = new SparseArray<Post>();
		cacheHash    = null;
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

		String query;
		
		query = "SELECT * FROM posts WHERE id=?";
		
		return this.querySingleInstance(query, new String[]{id.toString()});
	}

	@Override
	protected Post createNotSyncronizedInstance(Cursor cur) {
		
		return EntityUtils.createPostFromCursor(this.getEntityFactory(), cur);
	}

	public void updatePost(Integer id, Map<String, String> attValues) {
		
		Map<String, String> key;
		
		key = new HashMap<String, String>();
		
		key.put("id", id.toString());
		
		this.queryInsertOrReplace("posts", attValues, key);
		
	}
	
	public ArrayList<Post> getQuestionList(){
		String queryString = "SELECT * FROM posts WHERE post_type_id=1 LIMIT 40";
		return (ArrayList<Post>)this.querySortedInstanceSet(queryString, new String[]{});
	}
	
	public List<Post> pagedFullText(Set<String> words, Integer pageSize, Integer page){
		
		List<Post> retPost;
		
		Integer wordsHashCode;
		Integer startIdx, endIdx;
		
		wordsHashCode = words.hashCode();
		
		if(this.cacheHash==null || !this.cacheHash.equals(wordsHashCode)){
			cacheHash    = words.hashCode();
			cachedSearch.clear();
			
			cachedSearch.addAll(fullText(words));
			
		}
		
		retPost = new ArrayList<Post>();
		
		startIdx = pageSize * page;
		endIdx   = pageSize * page + pageSize;
		
		if(startIdx > this.cachedSearch.size()){
			//No result
			startIdx = 0;
			endIdx   = 0;
		}else{
			if(endIdx > this.cachedSearch.size()){
				endIdx = this.cachedSearch.size();
			}
		}
		
		Post p;
		for(PostIndexInformation pIdx: cachedSearch.subList(startIdx, endIdx)){
			if(null==(p=cachedPosts.get(pIdx.getPostId()))){
				p = this.getPostById(pIdx.getPostId());
				this.cachedPosts.put(p.getId(),p);
			}
			
			retPost.add(p);
		}
		
		//TODO: more smart caching strategy
		if(cachedPosts.size()>500){
			cachedPosts.clear();
		}
		
		return retPost;
	}
	
	@SuppressLint("UseSparseArrays")
	public SortedSet<PostIndexInformation> fullText(Set<String> words){
		
		Map<Integer,PostIndexInformation> indexInfos    ;
		SortedSet<PostIndexInformation> sortedIndexInfos;
				
		SQLiteDatabase db ;
		Cursor         cur;
		
		String[] wordsArray;
		
		String titleIndexQuery    ;
		String postIndexQuery     ;
		String commentsIndexQuery ;
		String tagsIndexQuery     ;
		
		PostIndexInformation postInfo;
		Integer              postId  ;
		
		titleIndexQuery    = generateSearchQuery(words.size(), "searchindex_post_titles", "word");
		tagsIndexQuery     = generateSearchQuery(words.size(), "searchindex_tags"       , "tag" );
		postIndexQuery     = generateSearchQuery(words.size(), "searchindex_posts"      , "word");
		commentsIndexQuery = generateSearchQuery(words.size(), "searchindex_comments"   , "word");
		
		indexInfos = new HashMap<Integer,PostIndexInformation>();
		
		db = this.getDbInstance();
		
		
		wordsArray = new String[words.size()];
		wordsArray = words.toArray(wordsArray);
		
		/* Post titles */
		cur = db.rawQuery(titleIndexQuery, wordsArray);
		
		while(cur.moveToNext()){
			postId = cur.getInt(0);
			
			postInfo = indexInfos.get(postId);
			
			if(postInfo==null){
				postInfo = new PostIndexInformation(postId);
				indexInfos.put(postId, postInfo);
			}
			
			postInfo.incrementTitleMatch();
		}
		
		/* Tags */
		cur = db.rawQuery(tagsIndexQuery, wordsArray);
		
		while(cur.moveToNext()){
			postId = cur.getInt(0);
			
			postInfo = indexInfos.get(postId);
			
			if(postInfo==null){
				postInfo = new PostIndexInformation(postId);
				indexInfos.put(postId, postInfo);
			}
			
			postInfo.incrementTagMatch();
		}
		
		/* Posts */
		cur = db.rawQuery(postIndexQuery, wordsArray);
		
		while(cur.moveToNext()){
			postId = cur.getInt(0);
			
			postInfo = indexInfos.get(postId);
			
			if(postInfo==null){
				postInfo = new PostIndexInformation(postId);
				indexInfos.put(postId, postInfo);
			}
			
			postInfo.incrementPostMatch();
		}
		
		/* Comments */
		cur = db.rawQuery(commentsIndexQuery, wordsArray);
		
		while(cur.moveToNext()){
			postId = cur.getInt(0);
			
			postInfo = indexInfos.get(postId);
			
			if(postInfo==null){
				postInfo = new PostIndexInformation(postId);
				indexInfos.put(postId, postInfo);
			}
			
			postInfo.incrementCommentMatch();
		}
		
		/* Now we sort the results */
		sortedIndexInfos = new TreeSet<PostDataLayer.PostIndexInformation>();
		sortedIndexInfos.addAll(indexInfos.values());
		
		/* And retrieve the posts in order */
		
		return sortedIndexInfos;
	}
	
	private String generateSearchQuery(Integer wordCount, String tableName, String attName){
		
		StringBuilder sb = new StringBuilder("SELECT id FROM ");
		sb.append(tableName);
		sb.append(" WHERE ");
		
		if(wordCount>0){
			sb.append(attName);
			sb.append("=?");
			
			for(int i=1; i<wordCount; i++){
				sb.append(" OR ");
				sb.append(attName);
				sb.append("=?");
			}
		}
		
		return sb.toString();
	}
	
	public static class PostIndexInformation implements Comparable<PostIndexInformation>{
		
		private final Integer postId;
		
		private Integer titleMatchs   ;
		private Integer postMatchs    ;
		private Integer tagsMatchs    ;
		private Integer commentsMatchs;
		
		public PostIndexInformation(Integer postId){
			this.postId = postId;
			
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

		public Integer getPostId() {
			return postId;
		}
		
		@Override
		public String toString(){
			
			return "<[" + this.postId + "]{" + this.titleMatchs + "," + this.tagsMatchs + "," + this.postMatchs + "," + this.commentsMatchs + "}>";
		}
		
		
		
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((postId == null) ? 0 : postId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PostIndexInformation other = (PostIndexInformation) obj;
			if (postId == null) {
				if (other.postId != null)
					return false;
			} else if (!postId.equals(other.postId))
				return false;
			return true;
		}

		@Override
		public int compareTo(PostIndexInformation rhs){
			int compare = 0;
			
			compare = this.titleMatchs.compareTo(rhs.titleMatchs);
			
			if(compare == 0){
				compare = this.tagsMatchs.compareTo(rhs.tagsMatchs);
				
				if(compare==0){
					compare = this.postMatchs.compareTo(rhs.postMatchs);
					
					if(compare==0){
						compare = this.postMatchs.compareTo(rhs.postMatchs);
						
							if(compare==0){
								compare = this.getPostId().compareTo(rhs.getPostId());
							}
					}
				}
			}
			
			return -compare;
		}
	}
	
	

	public User getOwnerQuestion(Integer idQuestion){
		UserDataLayer userDataLayer = this.getDataLayerFactory().createUserDataLayer();
		String queryString = "SELECT * FROM users where" +
				" id = (SELECT owner_user_id FROM posts WHERE id=?)";
		return userDataLayer.querySingleInstance(queryString, new String[]{idQuestion.toString()});
	}
	
	public List<Post> getAnswersByPostId(Integer id){
		String query = "SELECT * FROM posts WHERE parent_id = ? ";
		return this.querySortedInstanceSet(query, new String[]{id.toString()});
	}

}





















