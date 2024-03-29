package se.chalmers.eda397.team7.so.data.entity;

import java.util.Date;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.text.Html;
import android.text.Spanned;

public class EntityUtils {

	@SuppressLint("UseValueOf")
	@SuppressWarnings("deprecation")
	public static Answer createAnswerFromCur(EntityFactory ef, Cursor cur){
		String dateString;
		String split[]   ;
		
		Integer id                       ;
		Integer post_type_id             ;
		Integer parent_id                ;
		Integer accepted_answer_id       ;
		Date    creation_date            ;
		Integer score                    ;
		Integer view_count               ;
		String  body                     ;
		Integer owner_user_id            ;
		Integer last_editor_user_id      ;
		String  last_editor_display_name ;
		Date    last_edit_date           ;
		Date    last_activity_date       ;
		Date    community_owned_date     ;
		Date    closed_date              ;
		String  title                    ;
		String  tags                     ;
		Integer answer_count             ;
		Integer comment_count            ;
		Integer favorite_count 		     ;
		
		id                       = cur.getInt(cur.getColumnIndex("id"                ));
		post_type_id             = cur.getInt(cur.getColumnIndex("post_type_id"      ));
		if(post_type_id.equals(0)){
			throw new EntityCreationException(
					"The current cursor value for \"post_type_id\"is not \"0\", the Post (if it's really a Post) it's not an Answer. Is it a Question?");
		}
		
		
		parent_id                = cur.getInt(cur.getColumnIndex("parent_id"         ));
		accepted_answer_id       = cur.getInt(cur.getColumnIndex("accepted_answer_id"));
		
		dateString  = cur.getString(cur.getColumnIndex("creation_date"));
		split = dateString.split("-");
		if(split.length==3){
			creation_date            = new Date(0,0,0,0,0,0);
			creation_date.setYear (new Integer(split[0]) - 1900);
			creation_date.setMonth(new Integer(split[1])-1);
			creation_date.setDate (new Integer(split[2]));
		}else{
			creation_date = null;
		}
		
		score                    = cur.getInt   (cur.getColumnIndex("score"                   ));
		view_count               = cur.getInt   (cur.getColumnIndex("view_count"              ));
		body                     = cur.getString(cur.getColumnIndex("body"                    ));
		owner_user_id            = cur.getInt   (cur.getColumnIndex("owner_user_id"           ));
		last_editor_user_id      = cur.getInt   (cur.getColumnIndex("last_editor_user_id"     ));
		last_editor_display_name = cur.getString(cur.getColumnIndex("last_editor_display_name"));
		
		dateString  = cur.getString(cur.getColumnIndex("last_edit_date"));
		split = dateString.split("-");
		if(split.length==3){
			last_edit_date            = new Date(0,0,0,0,0,0);
			last_edit_date.setYear (new Integer(split[0]) - 1900);
			last_edit_date.setMonth(new Integer(split[1])-1);
			last_edit_date.setDate (new Integer(split[2]));
		}else{
			last_edit_date = null;
		}
		
		dateString  = cur.getString(cur.getColumnIndex("last_activity_date"));
		split = dateString.split("-");
		if(split.length==3){
			last_activity_date            = new Date(0,0,0,0,0,0);
			last_activity_date.setYear (new Integer(split[0]) - 1900);
			last_activity_date.setMonth(new Integer(split[1])-1);
			last_activity_date.setDate (new Integer(split[2]));
		}else{
			last_activity_date = null;
		}
		
		
		dateString  = cur.getString(cur.getColumnIndex("community_owned_date"));
		split = dateString.split("-");
		if(split.length==3){
			community_owned_date            = new Date(0,0,0,0,0,0);
			community_owned_date.setYear (new Integer(split[0]) - 1900);
			community_owned_date.setMonth(new Integer(split[1])-1);
			community_owned_date.setDate (new Integer(split[2]));
		}else{
			community_owned_date = null;
		}
		
		dateString  = cur.getString(cur.getColumnIndex("closed_date"));
		split = dateString.split("-");
		if(split.length==3){
			closed_date            = new Date(0,0,0,0,0,0);
			closed_date.setYear (new Integer(split[0]) - 1900);
			closed_date.setMonth(new Integer(split[1])-1);
			closed_date.setDate (new Integer(split[2]));
		}else{
			closed_date = null;
		}
		
		title                    = cur.getString(cur.getColumnIndex("title"));
		tags                     = cur.getString(cur.getColumnIndex("tags"));
		answer_count             = cur.getInt   (cur.getColumnIndex("answer_count"     ));
		comment_count            = cur.getInt   (cur.getColumnIndex("comment_count"     ));
		favorite_count           = cur.getInt   (cur.getColumnIndex("favorite_count"     ));
		
		
		Answer p = ef.createAnswer(
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
		
		return p;
		
	}
	
	@SuppressLint("UseValueOf")
	@SuppressWarnings("deprecation")
	public static Question createQuestionFromCur(EntityFactory ef, Cursor cur){
		String dateString;
		String split[]   ;
		
		Integer id                       ;
		Integer post_type_id             ;
		Integer parent_id                ;
		Integer accepted_answer_id       ;
		Date    creation_date            ;
		Integer score                    ;
		Integer view_count               ;
		String  body                     ;
		Integer owner_user_id            ;
		Integer last_editor_user_id      ;
		String  last_editor_display_name ;
		Date    last_edit_date           ;
		Date    last_activity_date       ;
		Date    community_owned_date     ;
		Date    closed_date              ;
		String  title                    ;
		String  tags                     ;
		Integer answer_count             ;
		Integer comment_count            ;
		Integer favorite_count 		     ;
		
		id                       = cur.getInt(cur.getColumnIndex("id"                ));
		post_type_id             = cur.getInt(cur.getColumnIndex("post_type_id"      ));
		if(!post_type_id.equals(1)){
			throw new EntityCreationException(
					"The current cursor value for \"post_type_id\"is not \"1\", the Post (if it's really a Post) it's not a question. Is it an Answer?");
		}
		
		
		parent_id                = cur.getInt(cur.getColumnIndex("parent_id"         ));
		accepted_answer_id       = cur.getInt(cur.getColumnIndex("accepted_answer_id"));
		
		dateString  = cur.getString(cur.getColumnIndex("creation_date"));
		split = dateString.split("-");
		if(split.length==3){
			creation_date            = new Date(0,0,0,0,0,0);
			creation_date.setYear (new Integer(split[0]) - 1900);
			creation_date.setMonth(new Integer(split[1])-1);
			creation_date.setDate (new Integer(split[2]));
		}else{
			creation_date = null;
		}
		
		score                    = cur.getInt   (cur.getColumnIndex("score"                   ));
		view_count               = cur.getInt   (cur.getColumnIndex("view_count"              ));
		body                     = cur.getString(cur.getColumnIndex("body"                    ));
		owner_user_id            = cur.getInt   (cur.getColumnIndex("owner_user_id"           ));
		last_editor_user_id      = cur.getInt   (cur.getColumnIndex("last_editor_user_id"     ));
		last_editor_display_name = cur.getString(cur.getColumnIndex("last_editor_display_name"));
		
		dateString  = cur.getString(cur.getColumnIndex("last_edit_date"));
		split = dateString.split("-");
		if(split.length==3){
			last_edit_date            = new Date(0,0,0,0,0,0);
			last_edit_date.setYear (new Integer(split[0]) - 1900);
			last_edit_date.setMonth(new Integer(split[1])-1);
			last_edit_date.setDate (new Integer(split[2]));
		}else{
			last_edit_date = null;
		}
		
		dateString  = cur.getString(cur.getColumnIndex("last_activity_date"));
		split = dateString.split("-");
		if(split.length==3){
			last_activity_date            = new Date(0,0,0,0,0,0);
			last_activity_date.setYear (new Integer(split[0]) - 1900);
			last_activity_date.setMonth(new Integer(split[1])-1);
			last_activity_date.setDate (new Integer(split[2]));
		}else{
			last_activity_date = null;
		}
		
		
		dateString  = cur.getString(cur.getColumnIndex("community_owned_date"));
		split = dateString.split("-");
		if(split.length==3){
			community_owned_date            = new Date(0,0,0,0,0,0);
			community_owned_date.setYear (new Integer(split[0]) - 1900);
			community_owned_date.setMonth(new Integer(split[1])-1);
			community_owned_date.setDate (new Integer(split[2]));
		}else{
			community_owned_date = null;
		}
		
		dateString  = cur.getString(cur.getColumnIndex("closed_date"));
		split = dateString.split("-");
		if(split.length==3){
			closed_date            = new Date(0,0,0,0,0,0);
			closed_date.setYear (new Integer(split[0]) - 1900);
			closed_date.setMonth(new Integer(split[1])-1);
			closed_date.setDate (new Integer(split[2]));
		}else{
			closed_date = null;
		}
		
		title                    = cur.getString(cur.getColumnIndex("title"));
		tags                     = cur.getString(cur.getColumnIndex("tags"));
		answer_count             = cur.getInt   (cur.getColumnIndex("answer_count"     ));
		comment_count            = cur.getInt   (cur.getColumnIndex("comment_count"     ));
		favorite_count           = cur.getInt   (cur.getColumnIndex("favorite_count"     ));
		
		
		Question p = ef.createQuestion(
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
		
		return p;
		
	}
	
	
	public static Spanned extractText(String htmlText){
		String noSlash = htmlText.replaceAll("\\\\.", " ");
		Spanned spannedContent = Html.fromHtml(noSlash);
		return spannedContent;
	}
}
