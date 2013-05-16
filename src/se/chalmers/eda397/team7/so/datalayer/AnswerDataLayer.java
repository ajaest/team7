package se.chalmers.eda397.team7.so.datalayer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.database.Cursor;
import se.chalmers.eda397.team7.so.data.entity.Answer;
import se.chalmers.eda397.team7.so.data.entity.EntityUtils;
import se.chalmers.eda397.team7.so.data.entity.Question;

public class AnswerDataLayer extends DataLayer<Answer> {

	protected AnswerDataLayer(DataLayerFactory dl) {
		super(dl);
	}

	@Override
	protected Answer createNotSyncronizedInstance(Cursor cur) {
		return EntityUtils.createAnswerFromCur(this.getEntityFactory(), cur);
	}

	public List<Answer> getAnswersByPostId(Integer id){
		String query = "SELECT * FROM posts WHERE parent_id = ? AND post_type_id=2";

		return this.querySortedInstanceSet(query, new String[]{id.toString()});
	}

	//////////////////////////////////////
	////////// Vote
	//////////////////////////////////////
	public int getScore(Integer id){

		Answer a;

		String query = "SELECT * FROM posts WHERE id=? AND post_type_id=2";

		
		a =	this.querySingleInstance(query, new String[]{id.toString()});
		return a.getScore(); 

	}

	public void voteUp(int post_id){
		String query ="";
		query = "UPDATE posts SET score = score + 1 WHERE id="+post_id+";";
		this.getDbInstance().execSQL(query);

	}

	public void voteDown(int post_id){
		String query ="";
		query = "UPDATE posts SET score = score - 1 WHERE id="+post_id+";";
		this.getDbInstance().execSQL(query);

	}

	
	public Answer getAnswersId(Integer id){
		String query = "SELECT * FROM posts WHERE id=? AND post_type_id=2";

		return this.querySingleInstance(query, new String[]{id.toString()});
	}
	
	//////////////////////////////////////
	////////// Create
	//////////////////////////////////////
	
	public Integer getMaxId(){
		String queryString = "SELECT MAX(id) from posts WHERE post_type_id=2";
		Cursor cur = this.getDbInstance().rawQuery(queryString, null);
		cur.moveToNext();
		Integer max = cur.getInt(0);
		cur.close();
		return max;
	}
	
	public Answer createAnswer(
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
			Answer p = this.getEntityFactory().createAnswer(
					id                       ,
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

}
