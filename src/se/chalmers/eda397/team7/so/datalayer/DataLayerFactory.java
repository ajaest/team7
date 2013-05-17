package se.chalmers.eda397.team7.so.datalayer;

import android.database.sqlite.SQLiteDatabase;

public class DataLayerFactory {

	private PostDataLayer    postDl    = null;
	private UserDataLayer    userDl    = null;
	private CommentDataLayer commentDl = null;
	private AnswerDataLayer  answerDl  = null;
	private TagDataLayer     tagDl     = null;
	
	private final SQLiteDatabase db ;
	
	public DataLayerFactory(SQLiteDatabase db){
		this.db = db;
	}
	
	public PostDataLayer createPostDataLayer(){
		
		if(this.postDl==null){
			this.postDl = new PostDataLayer(this);
		}
		
		return this.postDl;
	}
	
	public UserDataLayer createUserDataLayer(){
		
		if(this.userDl==null){
			this.userDl = new UserDataLayer(this);
		}
		
		return this.userDl;
	}

	public CommentDataLayer createCommentDataLayer() {

		if(this.commentDl==null){
			this.commentDl = new CommentDataLayer(this);
		}
		
		return this.commentDl;
	}
	
	public AnswerDataLayer createAnswerDataLayer(){
		
		if(this.answerDl==null){
			this.answerDl = new AnswerDataLayer(this);
		}
		
		return this.answerDl;
	}
	
	public TagDataLayer createTagDataLayer(){
		if(this.tagDl==null){
			this.tagDl = new TagDataLayer(this);
		}
		
		return this.tagDl;
	}

	public SQLiteDatabase getDB() {
		return this.db;
	}

}
