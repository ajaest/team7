package so.chalmers.eda397.team7.so.datalayer;

import java.util.List;

import so.chalmers.eda397.so.data.entity.Comment;
import android.database.Cursor;

public class CommentDataLayer extends DataLayer<Comment> {
	
	protected CommentDataLayer(DataLayerFactory db) {
		super(db);
	}
	
	public List<Comment> getComentsByPostId(Integer id){
	
		final String query = "SELECT * FROM comments WHERE post_id = ?";
		
		return querySortedInstanceSet(query, new String[]{id.toString()});
	}
	
	
	@Override
	protected Comment createNotSyncronizedInstance(Cursor cur) {
		
		return this.getEntityFactory().createComment();
	}
	
}
