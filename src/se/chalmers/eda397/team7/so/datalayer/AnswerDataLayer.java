package se.chalmers.eda397.team7.so.datalayer;

import java.util.List;

import android.database.Cursor;
import se.chalmers.eda397.team7.so.data.entity.Answer;
import se.chalmers.eda397.team7.so.data.entity.EntityUtils;

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

}
