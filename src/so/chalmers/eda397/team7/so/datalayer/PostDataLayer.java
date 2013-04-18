package so.chalmers.eda397.team7.so.datalayer;

import so.chalmers.eda397.so.data.entity.Post;
import android.database.Cursor;

public class PostDataLayer extends DataLayer<Post>{
	
	
	protected PostDataLayer(DataLayerFactory dl) {
		super(dl);
	}

	/**
	 * In this method the Entity factory is used
	 * @return
	 */
	public Post createPost(/*TODO: arguments*/){
		
		// Here a not syncronized instance of a post is created TODO: create id
		Post post = this.getEntityFactory().createPost((int)(Math.random()*Math.pow(10, Math.random()*3)));
		
		// Here, before returning the instance we should insert the post in the database
		//TODO: syncronize post in database
		
		
		
		return post;
	}
	
	/**
	 * In this method ones use the sql objet
	 */
	public Post getPostById(Integer id){

		String query;
		
		query = "SELECT * FROM post WHERE id=?";
		
		return this.querySingleInstance(query, new String[]{id.toString()});
	}

	@Override
	protected Post createNotSyncronizedInstance(Cursor cur) {
		//TODO: extract info from cursor. Now it's generating a POST with IDs between 1 and 100
		return this.getEntityFactory().createPost((int)(Math.random()*Math.pow(10, Math.random()*3)));
	}
}





















