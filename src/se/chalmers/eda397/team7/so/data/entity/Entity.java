package se.chalmers.eda397.team7.so.data.entity;

import java.util.Map;
import java.util.Set;

import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;

public abstract class Entity implements FullTextable {

	private final DataLayerFactory dl;
	private boolean isDirty = true;
	
	protected Entity(DataLayerFactory dl){
		this.dl= dl;
	}
	
	protected DataLayerFactory getDataLayerFactory(){
		return this.dl;
	}
	
	public abstract Integer getId();
	
	/**
	 * Commit all attribute changes to the database.
	 * 
	 * @return boolean {@code true} if any data were modified in the database,
	 *         {@code false} otherwise
	 */
	public abstract boolean commit();	
	
	/**
	 * If an instance is dirty, it means that it attribute values were modified
	 * in this object, and therefore that it state will probably differ from
	 * this instance state in the database
	 */
	public boolean isDirty(){
		return isDirty;
	}
	
	public void setDirty(boolean dirty){
		this.isDirty = dirty;
	}
	
	public void commitSearchIndexes(){

		Map<String,Set<String>> asd = this.getFullTextIndexes();
		
		String partial_delete_query = "DELETE FROM searchindex_%s WHERE id=?";
		String partial_insert_query = "INSERT INTO searchindex_%s VALUES (?,?)";
		
		String query;
		for(String search_table : new String[]{"questions", "question_titles", "comments", "responses", "tags"}){
			query = String.format(partial_delete_query, search_table);
			dl.getDB().execSQL(query, new String[]{this.getId().toString()});
		}
		
		for(String search_table: asd.keySet()){
			query = String.format(partial_insert_query, search_table);
			
			for(String index_value: asd.get(search_table)){
				dl.getDB().execSQL(query, new String[]{this.getId().toString(), index_value});
			}
		}
	}
	
}
