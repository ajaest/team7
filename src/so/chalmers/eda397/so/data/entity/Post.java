package so.chalmers.eda397.so.data.entity;

import java.util.List;

import so.chalmers.eda397.team7.so.datalayer.CommentDataLayer;
import so.chalmers.eda397.team7.so.datalayer.DataLayerFactory;

public class Post extends Entity {
	
	/* Entity relation factories */
	CommentDataLayer commendDl;
	
	/* Attributes */
	private final Integer id;
	
	protected Post(
			DataLayerFactory dataLayerFactory,
			
			Integer id
			
	) {
		super(dataLayerFactory);
		
		this.commendDl = dataLayerFactory.createCommentDataLayer();
		
		this.id = id;
	}

	/*
	 * Instances could have access to database as well if the entity
	 * relations require it, an example
	 */
	/**
	 * Returns the sorted set of reply comments to this post
	 * 
	 * @author Luis A. Arce
	 */
	public List<Comment> getComments(){
		
		return this.commendDl.getComentsByPostId(this.id);
	}

	public Integer getId() {
		return id;
	}
}
