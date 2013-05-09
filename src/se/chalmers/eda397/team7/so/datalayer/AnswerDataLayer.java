package se.chalmers.eda397.team7.so.datalayer;

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


}
