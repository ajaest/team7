package so.chalmers.eda397.team7.so.data;

import java.util.ArrayList;

import se.chalmers.eda397.team7.so.Question;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.RatingBar;

/*
 * 
 */
public class DatabaseManager {
	public SQLiteDatabase db;
	public static int MAXRESULT = 40;
	public static String POSTS = "posts";
	public static String ID = "id";
	public static String POST_TYPE_ID = "post_type_id";
	public static String ACCEPTED_ANSWER_ID = "accepted answer_id";
	public static String CREATION_DATE = "creation_date";
	public static String SCORE = "score";
	public static String VIEW_COUNT = "view_count";
	public static String BODY = "body";
	public static String OWNER_USER_ID = "owner_user_id";
	public static String LAST_EDITOR_USER_ID = "last_editor_user_id";
	public static String LAST_EDIT_DATE = "last_edit_date";
	public static String LAST_ACTIVITY_DATE = "last_activity_date";
	public static String TITLE = "title";
	public static String TAGS = "tags";
	public static String ANSWER_COUNT = "answer_count";
	public static String COMMENT_COUNT = "comment_count";
	public static String FAVORITE_COUNT = "favorite_count";
	
	/*
	 * getQuestionByDate
	 * GetQuestonByRating
	 * getQuestonByUser
	 * GetQuestionByTags
	 */
	public DatabaseManager(SQLiteDatabase database){
		db = database; 	
	}
	
	public ArrayList<Question> getQuestionsByDate(){
		Question question;
		ArrayList<Question> questions = new ArrayList<Question>();
		Cursor c = db.rawQuery("SELECT " + TITLE +", COALESCE("+ ANSWER_COUNT + ", 0), "+ ID +" FROM " + POSTS + " where  " +POST_TYPE_ID + "=1 ORDER BY "+ CREATION_DATE +" LIMIT " + MAXRESULT, new String[]{});
		while(c.moveToNext()){
			question = new Question(c.getString(0), Integer.parseInt(c.getString(1)));
			questions.add(question);

		}
		return questions;

	}
	public ArrayList<Question> getQuestionsByScore(){
		Question question;
		ArrayList<Question> questions = new ArrayList<Question>();
		Cursor c = db.rawQuery("SELECT " + TITLE +", COALESCE("+ ANSWER_COUNT + ", 0), "+ ID +" FROM " + POSTS + " where  " +POST_TYPE_ID + "=1 ORDER BY "+ SCORE +" LIMIT " + MAXRESULT, new String[]{});
		while(c.moveToNext()){
			question = new Question(c.getString(0), Integer.parseInt(c.getString(1)));
			questions.add(question);

		}
		return questions;

	}
	
	
	
	
}










