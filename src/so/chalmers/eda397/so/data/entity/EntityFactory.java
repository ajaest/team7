package so.chalmers.eda397.so.data.entity;

import so.chalmers.eda397.team7.so.datalayer.DataLayer;
import so.chalmers.eda397.team7.so.datalayer.DataLayerFactory;

/**
 * This class creates raw entity instances that may not be synchronized in any
 * database.
 * 
 * <b>WARNING</b>: This class should only be used in {@link DataLayer} instances
 * 
 * @author Luis A. Arce
 */
public class EntityFactory {
	
	private DataLayerFactory dlf;
	
	public EntityFactory(DataLayerFactory dlf){
		super();
		
		this.dlf = dlf;
	}
	
	public Post    createPost   (
			Integer id
			/* filled with arguments */
	){
		
		return new Post(
			this.dlf,
			/* Attributes */
			id
			/*, TODO: filled with arguments */
		);
	}
	
	public User    createUser   (/* filled with arguments */){
		return new User(
			this.dlf
			/* Attributes */
			/*, TODO: filled with arguments */);
		
	}
	
	public Comment createComment(/* TODO: filled with arguments */){
		
		return new Comment(
			this.dlf
			/* Attributes */
			/*, TODO: filled with arguments */);
	}
}
