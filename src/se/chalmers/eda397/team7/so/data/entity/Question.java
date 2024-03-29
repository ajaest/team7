package se.chalmers.eda397.team7.so.data.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import se.chalmers.eda397.team7.so.datalayer.AnswerDataLayer;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;

public class Question extends Post implements FullTextable {

	private final AnswerDataLayer adl;
	
	protected Question(
			DataLayerFactory dataLayerFactory,

			Integer id                       ,
			/*Integer post_type_id             ,*/
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
		super(
				dataLayerFactory,		

				id                       ,
				/*post_type_id*/ 1       , /*post_type_id=1 for question*/
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
		
		this.adl = dataLayerFactory.createAnswerDataLayer();
	}
	
	public List<Answer> getAnswers(){

		return adl.getAnswersByPostId(this.getId());
	}

	
	@Override
	public Map<String, Set<String>> getFullTextIndexes() {
		
		Map<String, Set<String>> superTags = super.getFullTextIndexes();
		
		if(superTags.containsKey("post_titles")){
			superTags.put("question_titles", superTags.remove("post_titles"));
		}
		
		if(superTags.containsKey("posts")){
			superTags.put("questions", superTags.remove("posts"));
		}	
		
	return superTags;
	}
}
