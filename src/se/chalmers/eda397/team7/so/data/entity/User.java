package se.chalmers.eda397.team7.so.data.entity;

import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.UserDataLayer;

public class User extends Entity{

	private final UserDataLayer udl;
	
	/* Attributes */
	private Integer id              ;
	private Integer reputation      ;
	private Date    creation_date   ;
	private String  display_name    ;
	private String  email_hash      ;
	private Date    last_access_date;
	private URL     website_url     ;
	private String  location        ;
	private Integer age             ;
	private String  about_me        ;
	private Integer views           ;
	private Integer   up_votes      ;
	private Integer down_votes      ;

	protected User(
		DataLayerFactory dl,
		
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
	) {
		super(dl);
		
		this.udl = dl.createUserDataLayer();
		
		this.id               = id              ;
		this.reputation       = reputation      ;
		this.creation_date    = creation_date   ;
		this.display_name     = display_name    ;
		this.email_hash       = email_hash      ;
		this.last_access_date = last_access_date;
		this.website_url      = website_url     ;
		this.location         = location        ;
		this.age              = age             ;
		this.about_me         = about_me        ;
		this.views            = views           ;
		this. up_votes        =  up_votes       ;
		this.down_votes       = down_votes      ;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
		this.setDirty(true);
	}

	public Integer getReputation() {
		return reputation;
	}

	public void setReputation(Integer reputation) {
		this.reputation = reputation;
		this.setDirty(true);
	}

	public Date getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
		this.setDirty(true);
	}

	public String getDisplay_name() {
		return display_name;
	}

	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
		this.setDirty(true);
	}

	public String getEmail_hash() {
		return email_hash;
	}

	public void setEmail_hash(String email_hash) {
		this.email_hash = email_hash;
		this.setDirty(true);
	}

	public Date getLast_access_date() {
		return last_access_date;
	}

	public void setLast_access_date(Date last_access_date) {
		this.last_access_date = last_access_date;
		this.setDirty(true);
	}

	public URL getWebsite_url() {
		return website_url;
	}

	public void setWebsite_url(URL website_url) {
		this.website_url = website_url;
		this.setDirty(true);
	}

	public String getLocation() {
	if (location.equals("NULL"))
		return "";
	return location;
	}

	public void setLocation(String location) {
		this.location = location;
		this.setDirty(true);
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
		this.setDirty(true);
	}

	public String getAbout_me() {
		if (about_me.equals("NULL"))
			return "";
		return about_me;
	}

	public void setAbout_me(String about_me) {
		this.about_me = about_me;
		this.setDirty(true);
	}

	public Integer getViews() {
		return views;
	}

	public void setViews(Integer views) {
		this.views = views;
		this.setDirty(true);
	}

	public Integer getUp_votes() {
		return up_votes;
	}

	public void setUp_votes(Integer up_votes) {
		this.up_votes = up_votes;
		this.setDirty(true);
	}

	public Integer getDown_votes() {
		return down_votes;
	}
	
	public void setDown_votes(Integer down_votes) {
		this.down_votes = down_votes;
		this.setDirty(true);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean commit() {
		
		boolean updated;
		
		if(this.isDirty()){
			
			Map<String, String> values;
			String creation_date, last_access_date;
			
			creation_date = "" +  
				this.getCreation_date().getYear()  + "-" + 
				this.getCreation_date().getMonth() + "-" + 
				this.getCreation_date().getDay()
			;
				
			last_access_date = "" +  
				this.getLast_access_date().getYear()  + "-" + 
				this.getLast_access_date().getMonth() + "-" + 
				this.getLast_access_date().getDay()
			;
			
			values = new HashMap<String, String>();
			values.put("reputation"      ,this.   getReputation   ().toString());
			values.put("creation_date"   ,        creation_date             );
			values.put("display_name"    ,this.   getDisplay_name ()           );
			values.put("email_hash"      ,this.   getEmail_hash   ()           );
			values.put("last_access_date",        last_access_date             );
			values.put("website_url"     ,this.   getWebsite_url  ().toString());
			values.put("location"        ,this.   getLocation     ()           );
			values.put("age"             ,this.   getAge          ().toString());
			values.put("about_me"        ,this.   getAbout_me     ()           );
			values.put("views"           ,this.   getViews        ().toString());
			values.put("up_votes"        ,this.   getUp_votes     ().toString());
			values.put("down_votes"      ,this.   getDown_votes   ().toString());			
			
			udl.updateUser(this.getId(), values);
			
			updated = true;
			
			this.setDirty(false);
			
		}else{
			updated = false;
		}
		
		return updated;
	}

}
