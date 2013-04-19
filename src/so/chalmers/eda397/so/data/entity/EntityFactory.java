package so.chalmers.eda397.so.data.entity;

import java.net.URL;
import java.util.Date;

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
	
	public Post createPost   (
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
		
		return new Post(
			this.dlf,
			
			/* Attributes */
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
	}
	
	public User createUser
		(
			Integer id              ,
			Integer reputation      ,
			Date    creation_date   ,
			String  display_name    ,
			String  email_hash      ,
			Date    last_access_date,
			URL     website_url     ,
			String  location        ,
			Integer age             ,
			String  about_me        ,
			Integer views           ,
			Integer   up_votes      ,
			Integer down_votes
		)
	{
		
		return new User(
			this.dlf         ,
			/* Attributes */
			id              ,
			reputation      ,
			creation_date   ,
			display_name    ,
			email_hash      ,
		    last_access_date,
			website_url     ,
			location        ,
			age             ,
			about_me        ,
			views           ,
			 up_votes      ,
			down_votes
		);
	}
	
	public Comment createComment(/* TODO: filled with arguments */){
		
		return new Comment(
			this.dlf
			/* Attributes */
			/*, TODO: filled with arguments */);
	}
}
