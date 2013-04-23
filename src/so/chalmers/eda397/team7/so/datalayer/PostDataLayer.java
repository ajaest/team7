package so.chalmers.eda397.team7.so.datalayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import so.chalmers.eda397.so.data.entity.Post;
import android.annotation.SuppressLint;
import android.database.Cursor;

@SuppressLint("UseValueOf")
public class PostDataLayer extends DataLayer<Post>{
	
	
	protected PostDataLayer(DataLayerFactory dl) {
		super(dl);
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
	
	public ArrayList<Post> getQuestionList(){
		String query;
		query = "SELECT * FROM posts WHERE post_type_id=1";
		return (ArrayList<Post>) this.querySortedInstanceSet(query, null);
	}
	
	/**
	 * In this method ones use the sql objet
	 */
	public Post getPostById(Integer id){

		String query;
		
		query = "SELECT * FROM post WHERE id=?";
		
		return this.querySingleInstance(query, new String[]{id.toString()});
	}

	@SuppressLint("UseValueOf")
	@SuppressWarnings("deprecation")
	@Override
	protected Post createNotSyncronizedInstance(Cursor cur) {
		
		String dateString;
		String split[]   ;
		
		Integer id                       ;
		Integer post_type_id             ;
		Integer parent_id                ;
		Integer accepted_answer_id       ;
		Date    creation_date            ;
		Integer score                    ;
		Integer view_count               ;
		String  body                     ;
		Integer owner_user_id            ;
		Integer last_editor_user_id      ;
		String  last_editor_display_name ;
		Date    last_edit_date           ;
		Date    last_activity_date       ;
		Date    community_owned_date     ;
		Date    closed_date              ;
		String  title                    ;
		String  tags                     ;
		Integer answer_count             ;
		Integer comment_count            ;
		Integer favorite_count 		     ;
		
		id                       = cur.getInt(cur.getColumnIndex("post_type_id"      ));
		post_type_id             = cur.getInt(cur.getColumnIndex("post_type_id"      ));
		parent_id                = cur.getInt(cur.getColumnIndex("parent_id"         ));
		accepted_answer_id       = cur.getInt(cur.getColumnIndex("accepted_answer_id"));
		
		dateString  = cur.getString(cur.getColumnIndex("creation_date"));
		split = dateString.split("-");
		if(split.length==3){
			creation_date            = new Date();
			creation_date.setYear (new Integer(split[0]));
			creation_date.setMonth(new Integer(split[1]));
			creation_date.setDate (new Integer(split[2]));
		}else{
			creation_date = null;
		}
		
		score                    = cur.getInt   (cur.getColumnIndex("view_count"              ));
		view_count               = cur.getInt   (cur.getColumnIndex("view_count"              ));
		body                     = cur.getString(cur.getColumnIndex("owner_user_id"           ));
		owner_user_id            = cur.getInt   (cur.getColumnIndex("owner_user_id"           ));
		last_editor_user_id      = cur.getInt   (cur.getColumnIndex("last_editor_user_id"     ));
		last_editor_display_name = cur.getString(cur.getColumnIndex("last_editor_display_name"));
		
		dateString  = cur.getString(cur.getColumnIndex("last_edit_date"));
		split = dateString.split("-");
		if(split.length==3){
			last_edit_date            = new Date();
			last_edit_date.setYear (new Integer(split[0]));
			last_edit_date.setMonth(new Integer(split[1]));
			last_edit_date.setDate (new Integer(split[2]));
		}else{
			last_edit_date = null;
		}
		
		dateString  = cur.getString(cur.getColumnIndex("last_activity_date"));
		split = dateString.split("-");
		if(split.length==3){
			last_activity_date            = new Date();
			last_activity_date.setYear (new Integer(split[0]));
			last_activity_date.setMonth(new Integer(split[1]));
			last_activity_date.setDate (new Integer(split[2]));
		}else{
			last_activity_date = null;
		}
		
		
		dateString  = cur.getString(cur.getColumnIndex("community_owned_date"));
		split = dateString.split("-");
		if(split.length==3){
			community_owned_date            = new Date();
			community_owned_date.setYear (new Integer(split[0]));
			community_owned_date.setMonth(new Integer(split[1]));
			community_owned_date.setDate (new Integer(split[2]));
		}else{
			community_owned_date = null;
		}
		
		dateString  = cur.getString(cur.getColumnIndex("closed_date"));
		split = dateString.split("-");
		if(split.length==3){
			closed_date            = new Date();
			closed_date.setYear (new Integer(split[0]));
			closed_date.setMonth(new Integer(split[1]));
			closed_date.setDate (new Integer(split[2]));
		}else{
			closed_date = null;
		}
		
		title                    = cur.getString(cur.getColumnIndex("title"));
		tags                     = cur.getString(cur.getColumnIndex("title"));
		answer_count             = cur.getInt   (cur.getColumnIndex("answer_count"     ));
		comment_count            = cur.getInt   (cur.getColumnIndex("comment_count"     ));
		favorite_count           = cur.getInt   (cur.getColumnIndex("favorite_count"     ));
		
		
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
		
		return p;
	}

	public void updatePost(Integer id, Map<String, String> attValues) {
		
		Map<String, String> key;
		
		key = new HashMap<String, String>();
		
		key.put("id", id.toString());
		
		this.queryInsertOrReplace("posts", attValues, key);
		
	}
}





















