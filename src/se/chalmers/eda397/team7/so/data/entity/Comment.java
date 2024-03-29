package se.chalmers.eda397.team7.so.data.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import se.chalmers.eda397.team7.so.datalayer.CommentDataLayer;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;

public class Comment extends Entity implements FullTextable{

	private final CommentDataLayer cdl;
	
	private Integer id           ; 
	private Integer post_id      ; 
	private Integer score        ; 
	private String  text         ; 
	private Date    creation_date; 
	private Integer user_id      ; 
	
	protected Comment(
		DataLayerFactory dl,
		/* Attributes */
		Integer id           , 
		Integer post_id      , 
		Integer score        , 
		String  text         , 
		Date    creation_date, 
		Integer user_id 
	) {
		super(dl);
		
		this.cdl = dl.createCommentDataLayer();
		
		this.id            = id           ; 
		this.post_id       = post_id      ; 
		this.score         = score        ; 
		this.text          = text         ; 
		this.creation_date = creation_date; 
		this.user_id       = user_id      ;
	}

	public Integer getPost_id() {
		return post_id;
	}

	public void setPost_id(Integer post_id) {
		this.post_id = post_id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getId() {
		return id;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean commit() {
		
		boolean updated;
		
		if(this.isDirty()){
			
			String creation_date;
			
			Map<String, String> values;
			
			values = new HashMap<String, String>();
			
			values.put("id           ",this.getId     ()!=null ? this.getId           ().toString(): null); 
			values.put("post_id      ",this.getPost_id()!=null ? this.getPost_id      ().toString(): null); 
			values.put("score        ",this.getScore  ()!=null ? this.getScore        ().toString(): null); 
			values.put("text         ",this.getScore  ()!=null ? this.getText         ()           : null); 
			
			if(this.getCreation_date()!=null){
				creation_date = 
					"" + 
					(this.getCreation_date().getYear ()+1900) + "-" + 
					(this.getCreation_date().getMonth()+1   ) + "-" + 
					 this.getCreation_date().getDate ()        
				;
				values.put("creation_date",creation_date                     );
			}
			 
			values.put("user_id      ",this.getUser_id()!=null ? this.getUser_id      ().toString() : null);
			
			cdl.updateComment(this.getId(), values);
			
			updated = true;
		}else{
			updated = false;
		}
		
		
		return updated;
	}

	@Override
	public Map<String, Set<String>> getFullTextIndexes() {
		// TODO Auto-generated method stub
		return new HashMap<String, Set<String>>();
	}


}
