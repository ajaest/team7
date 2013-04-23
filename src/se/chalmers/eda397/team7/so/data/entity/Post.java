package se.chalmers.eda397.team7.so.data.entity;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.chalmers.eda397.team7.so.datalayer.CommentDataLayer;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.PostDataLayer;

public class Post extends Entity {
	
	/* Entity relation factories */
	private final CommentDataLayer commendDl;
	private final PostDataLayer    postDl   ; 
	
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

	public Integer getOwner_user_id() {
		return owner_user_id;
	}

	public Integer getLast_editor_user_id() {
		return last_editor_user_id;
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

	public String getTags() {
		return tags;
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

	public void setPost_type_id(Integer post_type_id) {
		this.setDirty(true);

		this.post_type_id = post_type_id;
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

	public void setLast_editor_display_name(String last_editor_display_name) {
		this.setDirty(true);

		this.last_editor_display_name = last_editor_display_name;
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
	public boolean commit() {
		
		boolean modified;
		
		if(this.isDirty()){
			
			Map<String, String> values;
			
			values = new HashMap<String, String>();
			
			values.put("id"                       ,getId                       ().toString());
			values.put("post_type_id"             ,getPost_type_id             ().toString());
			values.put("parent_id"                ,getParent_id                ().toString());
			values.put("accepted_answer_id"       ,getAccepted_answer_id       ().toString());
			values.put("creation_date"            ,formatDate(getCreation_date ()      )    );
			values.put("score"                    ,getScore                    ().toString());
			values.put("view_count"               ,getView_count               ().toString());
			values.put("body"                     ,getBody                     ().toString());
			values.put("owner_user_id"            ,getOwner_user_id            ().toString());
			values.put("last_editor_user_id"      ,getLast_editor_user_id      ().toString());
			values.put("last_editor_display_name" ,getLast_editor_display_name ().toString());
			values.put("last_edit_date"           ,formatDate(getLast_edit_date())          );
			values.put("last_activity_date"       ,formatDate(getLast_activity_date()  )    );
			values.put("community_owned_date"     ,formatDate(getCommunity_owned_date())    );
			values.put("closed_date"              ,formatDate(getClosed_date   ()      )    );
			values.put("title"                    ,getTitle                    ().toString());
			values.put("tags"                     ,getTags                     ().toString());
			values.put("answer_count"             ,getAnswer_count             ().toString());
			values.put("comment_count"            ,getComment_count            ().toString());
			values.put("favorite_count"           ,getFavorite_count           ().toString());
			
			this.postDl.updatePost(id, values);
			
			modified = true;
		}else{
			modified = false;
		}
		return modified;
	}
	
	@SuppressWarnings("deprecation")
	private String formatDate(Date d){
		return MessageFormat.format("{0}{1}{2}", d.getMonth(), d.getMonth(), d.getDay());
	}
}
