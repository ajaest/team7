package se.chalmers.eda397.team7.so.datalayer;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;

@SuppressLint("UseValueOf")
public class TagDataLayer extends DataLayer<String> {

	
	/* 24 bits with alpha channel!*/
//	public static final Integer YELLOW = Color.argb(0xff, 0xff, 0xff, 0x00);
//	public static final Integer GREEN  = Color.argb(0x0f, 0xff, 0xff, 0x00);
//	public static final Integer RED    = Color.argb(0xff, 0x00, 0xff, 0x00);
	
	protected TagDataLayer(DataLayerFactory dl) {
		super(dl);
	}

	public Integer getTagGraphRelativeColor(String nodeTag, String tag){
		
		Integer      color       ;
		Integer      curTagWeight;
		List<String> closeTags   ;
		
		closeTags = this.getCloseTags(nodeTag);
		curTagWeight = this.getRelativeWeight(nodeTag, tag);
		
		if(!closeTags.contains(tag)){
			color = Color.RED;
		}else{
						
			Float maxValue;
			Float minValue;
			Float midValue;
			
			maxValue = Float.MIN_VALUE;
			minValue = Float.MAX_VALUE;
			
			Integer curValue;
			for(String closeTag: closeTags){
				
				curValue = closeTag.equals(tag) ? curTagWeight : getRelativeWeight(nodeTag, closeTag);
				
				if(curValue>maxValue){
					maxValue = 0.0f + curValue;
				}
				if(curValue<minValue){
					minValue = 0.0f + curValue;
				}
				
			}
			
			midValue = minValue + (maxValue-minValue)/2;
			
			if(curTagWeight>=midValue){
				color = new Float(256/(maxValue - midValue) * (curTagWeight-midValue)).intValue();
				color = color << 16; //move to red byte
				color = Color.GREEN + color;
			}else {
				color = new Float(256/(midValue - minValue) * (curTagWeight-minValue)).intValue();
				color = color << 8; //move to green byte
				color = Color.RED   + color;
			}
			
		}
		
		return color;
	}
	
	public Integer getRelativeWeight(String tag1, String tag2){
		
		String  query ;
		Integer weight;
		Cursor  c     ;
		
		query = "SELECT weight FROM tag_graph WHERE tag1=? and tag2=?";
		
		c = this.getDbInstance().rawQuery(query, new String[]{tag1,tag2});
		
		if(c.moveToNext()){
			weight = c.getInt(c.getColumnIndex("weight"));
		}else{
			weight = -1;
		}
		
		return weight;
	}
	
	/**
     * Gets the 4 tags more related to the given tag.
     */
    public List<String> getCloseTags(String tag){
    	ArrayList<String> closeTags = new ArrayList<String>();
    	Cursor cursor;
    	cursor = this.getDbInstance().rawQuery("SELECT sum(weight), tag1,tag2 from tag_graph " +
    			"where tag1<>tag2 and tag1=? " +
    			" group by tag1,tag2 order by 1 desc LIMIT 4",new String[]{tag});
    	while (  cursor.moveToNext()) {
			closeTags.add(cursor.getString(2));
		}
    	cursor.close();
    	return closeTags;
    }
    
    /**
     * Returns the list of tags in our system order by alphabetical order
     */
    public List<String> getAllTags(){
    	Cursor cur;
    	List<String> tagsList = new ArrayList<String>();
  
		cur = this.getDbInstance().rawQuery("SELECT tag, count(*) FROM searchindex_tags GROUP BY tag ORDER BY 1 ", null);
		while(cur.moveToNext())
				tagsList.add(cur.getString(0));
		
		cur.close();
    	return tagsList;
    }
    
	
	@Override
	protected String createNotSyncronizedInstance(Cursor cur) {

		String tag;
		
		if(cur.getColumnIndex("tag")!=-1){
			tag = cur.getString(cur.getColumnIndex("tag"));
		}else
		if(cur.getColumnIndex("tag1")!=-1){
			tag = cur.getString(cur.getColumnIndex("tag1"));
		}
		
		tag = null;
		
		return tag;
	}

}
