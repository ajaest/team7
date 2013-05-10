package se.chalmers.eda397.team7.so.data.entity;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.R.string;

import se.chalmers.eda397.team7.so.datalayer.CommentDataLayer;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;
import se.chalmers.eda397.team7.so.datalayer.UserDataLayer;

public class Post extends Entity implements FullTextable {
	
	/* Shared static objects */
	private final static Pattern WORD_PATTERN;
	
	static{
		
		WORD_PATTERN = Pattern.compile("([a-zA-Z_]+)");
	}
	
	/* Entity relation factories */
	private final CommentDataLayer commendDl;
	private final PostDataLayer    postDl   ;
	private final UserDataLayer    userDl   ; 
	
	/* Attributes */
	private  Integer id                       ;
	private  Integer post_type_id             ;
	private  Integer parent_id                ;
	private  Integer accepted_answer_id       ;
	private  Date    creation_date            ;
	private  Integer score                    ;
	private  Integer view_count               ;
	private  String  body                     ;
	private  Integer owner_user_id            ;
	private  Integer last_editor_user_id      ;
	private  String  last_editor_display_name ;
	private  Date    last_edit_date           ;
	private  Date    last_activity_date       ;
	private  Date    community_owned_date     ;
	private  Date    closed_date              ;
	private  String  title                    ;
	private  String  tags                     ;
	private  Integer answer_count             ;
	private  Integer comment_count            ;
	private  Integer favorite_count           ;
	
	protected Post(
			DataLayerFactory dataLayerFactory,

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
	) {
		super(dataLayerFactory);
		
		this.commendDl = dataLayerFactory.createCommentDataLayer();
		this.postDl    = dataLayerFactory.createPostDataLayer   ();
		this.userDl    = dataLayerFactory.createUserDataLayer   ();
		

		this. id                         = id                       ;
		this. post_type_id               = post_type_id             ;
		this. parent_id                  = parent_id                ;
		this. accepted_answer_id         = accepted_answer_id       ;
		this. creation_date              = creation_date            ;
		this. score                      = score                    ;
		this. view_count                 = view_count               ;
		this. body                       = body                     ;
		this. owner_user_id              = owner_user_id            ;
		this. last_editor_user_id        = last_editor_user_id      ;
		this. last_editor_display_name   = last_editor_display_name ;
		this. last_edit_date             = last_edit_date           ;
		this. last_activity_date         = last_activity_date       ;
		this. community_owned_date       = community_owned_date     ;
		this. closed_date                = closed_date              ;
		this. title                      = title                    ;
		this. tags                       = tags                     ;
		this. answer_count               = answer_count             ;
		this. comment_count              = comment_count            ;
		this. favorite_count             = favorite_count           ;
	}

	/*
	 * Instances could have access to database as well if the entity
	 * relations require it, an example
	 */
	/**
	 * Returns the sorted set of reply comments to this.setDirty(true);

		this post
	 * 
	 * @author Luis A. Arce
	 */
	public List<Comment> getComments(){

		return this.commendDl.getComentsByPostId(this.getId());
	}
	
	
	public Integer getId() {
		return id;
	}

	public Integer getPost_type_id() {
		return post_type_id;
	}

	public Integer getParent_id() {
		return parent_id;
	}

	public Integer getAccepted_answer_id() {
		return accepted_answer_id;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public Integer getScore() {
		return score;
	}

	public Integer getView_count() {
		return view_count;
	}

	public String getBody() {
		return body;
	}

	protected Integer getOwner_user_id() {
		return owner_user_id;
	}
	
	public User getOwnerUser(){
		return userDl.getUserById(this.getOwner_user_id());
	}

	protected Integer getLast_editor_user_id() {
		return last_editor_user_id;
	}
	
	public User getLastEditorUser(){
		return userDl.getUserById(this.getLast_editor_user_id());
	}

	public String getLast_editor_display_name() {
		return last_editor_display_name;
	}

	public Date getLast_edit_date() {
		return last_edit_date;
	}

	public Date getLast_activity_date() {
		return last_activity_date;
	}

	public Date getCommunity_owned_date() {
		return community_owned_date;
	}

	public Date getClosed_date() {
		return closed_date;
	}

	public String getTitle() {
		return title;
	}

	public Set<String> getTags() {
		
		String[] tagss;
		
		tagss = this.tags.substring(1, this.tags.length()-1).split("><");
		
		return new HashSet<String>(Arrays.asList(tagss));
	}

	public Integer getAnswer_count() {
		return answer_count;
	}

	public Integer getComment_count() {
		return comment_count;
	}

	public Integer getFavorite_count() {
		return favorite_count;
	}

	public void setParent_id(Integer parent_id) {
		this.setDirty(true);

		this.parent_id = parent_id;
	}

	public void setAccepted_answer_id(Integer accepted_answer_id) {
		this.setDirty(true);

		this.accepted_answer_id = accepted_answer_id;
	}

	public void setCreation_date(Date creation_date) {
		this.setDirty(true);

		this.creation_date = creation_date;
	}

	public void setScore(Integer score) {
		this.setDirty(true);

		this.score = score;
	}

	public void setView_count(Integer view_count) {
		this.setDirty(true);

		this.view_count = view_count;
	}

	public void setBody(String body) {
		this.setDirty(true);

		this.body = body;
	}

	public void setOwner_user_id(Integer owner_user_id) {
		this.setDirty(true);

		this.owner_user_id = owner_user_id;
	}

	public void setLast_editor_user_id(Integer last_editor_user_id) {
		this.setDirty(true);

		this.last_editor_user_id = last_editor_user_id;
	}

	public void setLast_edit_date(Date last_edit_date) {
		this.setDirty(true);

		this.last_edit_date = last_edit_date;
	}

	public void setLast_activity_date(Date last_activity_date) {
		this.setDirty(true);

		this.last_activity_date = last_activity_date;
	}

	public void setCommunity_owned_date(Date community_owned_date) {
		this.setDirty(true);

		this.community_owned_date = community_owned_date;
	}

	public void setClosed_date(Date closed_date) {
		this.setDirty(true);

		this.closed_date = closed_date;
	}

	public void setTitle(String title) {
		this.setDirty(true);

		this.title = title;
	}

	public void setTags(String tags) {
		this.setDirty(true);

		this.tags = tags;
	}

	public void setAnswer_count(Integer answer_count) {
		this.setDirty(true);

		this.answer_count = answer_count;
	}

	public void setComment_count(Integer comment_count) {
		this.setDirty(true);

		this.comment_count = comment_count;
	}

	public void setFavorite_count(Integer favorite_count) {
		this.setDirty(true);

		this.favorite_count = favorite_count;
	}

	@Override
	public Map<String, Set<String>> getFullTextIndexes() {
		
		Map<String, Set<String>> indexes       ;
		Set<String>              current       ;
		Matcher                  matchedResults;
		
		indexes  = new HashMap<String, Set<String>>();
		
		/* Post title */
		current = new HashSet<String>();
		if(this.getBody()!=null){
			matchedResults = Post.WORD_PATTERN.matcher(this.getTitle());
			for(int i=0; i<matchedResults.groupCount(); i++){
				current.add(matchedResults.group(i));
			}
		}		
		indexes.put("post_titles", current);
		
		/* Body search index */
		current = new HashSet<String>();
		if(this.getBody()!=null){
			matchedResults = Post.WORD_PATTERN.matcher(this.getBody());
			for(int i=0; i<matchedResults.groupCount(); i++){
				current.add(matchedResults.group(i));
			}
		}
		
		indexes.put("posts", current);
		
		/* Comment search index */
		current = new HashSet<String>();
		if(this.getComments().size()!=0){
			for(Comment comment : this.getComments()){
				matchedResults = Post.WORD_PATTERN.matcher(comment.getText());
				for(int i=0; i<matchedResults.groupCount(); i++){
					current.add(matchedResults.group(i));
				}
			}
		}
		
		indexes.put("comments", current);
		
		/* Tags search index */
		indexes.put("tags", this.getTags());
		
		
		return indexes;
	}
	
	@Override
	public boolean commit() {
		
		boolean modified;
		
		if(this.isDirty()){
			
			Map<String, String> values;
			
			values = new HashMap<String, String>();
			
			values.put("id"                       ,getId()!=null ? getId().toString() : "null" );
			values.put("post_type_id"             ,getPost_type_id()!=null ? getPost_type_id().toString() : "null");
			values.put("parent_id"                ,getParent_id() != null ? getParent_id().toString() : "null");
			values.put("accepted_answer_id"       ,getAccepted_answer_id()!=null ? getAccepted_answer_id().toString(): "null");
			values.put("creation_date"            ,getCreation_date()!=null ? formatDate(getCreation_date ()) : "null");
			values.put("score"                    ,getScore()!=null ? getScore().toString(): "null");
			values.put("view_count"               ,getView_count()!=null ? getView_count().toString(): "null");
			values.put("body"                     ,getBody()!=null ? getBody().toString(): "null");
			values.put("owner_user_id"            ,getOwner_user_id()!=null ? getOwner_user_id().toString(): "null");
			values.put("last_editor_user_id"      ,getLast_editor_user_id()!=null ? getLast_editor_user_id().toString(): "null");
			values.put("last_editor_display_name" ,getLast_editor_display_name()!=null ? getLast_editor_display_name().toString(): "null");
			values.put("last_edit_date"           ,getLast_edit_date()!=null ? formatDate(getLast_edit_date()) : "null");
			values.put("last_activity_date"       ,getLast_activity_date()!=null ? formatDate(getLast_activity_date()) : "null");
			values.put("community_owned_date"     ,getCommunity_owned_date()!=null ? formatDate(getCommunity_owned_date()) : "null");
			values.put("closed_date"              ,getClosed_date()!=null ? formatDate(getClosed_date())  : "null");
			values.put("title"                    ,getTitle()!=null ? getTitle().toString(): "null");
			values.put("tags"                     ,getTags()!=null ? this.tags: "null");
			values.put("answer_count"             ,getAnswer_count()!=null ? getAnswer_count().toString(): "null");
			values.put("comment_count"            ,getComment_count()!=null ? getComment_count().toString(): "null");
			values.put("favorite_count"           ,getFavorite_count()!=null ? getFavorite_count().toString(): "null");
			
			this.postDl.updatePost(id, values);
			
			modified = true;
		}else{
			modified = false;
		}
		
		
		return modified;
	}
	
	@SuppressWarnings("deprecation")
	private String formatDate(Date d){
		//String date = MessageFormat.format("{0}-{1}-%s", d.getYear() + 1900, d.getMonth() + 1, d.getDate());
		String date = Integer.toString(d.getYear()+1900) + "-" + Integer.toString(d.getMonth()+1) + "-" + Integer.toString(d.getDate());
		return date;
	}
	
	@Override
	public String toString(){
		return this.getId().toString();
	}
}
