package se.chalmers.eda397.team7.so.datalayer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import se.chalmers.eda397.team7.so.data.entity.EntityUtils;
import se.chalmers.eda397.team7.so.data.entity.Post;
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
	
}





















