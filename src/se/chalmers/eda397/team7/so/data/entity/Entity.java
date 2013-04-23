package se.chalmers.eda397.team7.so.data.entity;

import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;

public abstract class Entity {

	private final DataLayerFactory dl;
	private boolean isDirty = true;
	
	protected Entity(DataLayerFactory dl){
		this.dl= dl;
	}
	
	protected DataLayerFactory getDataLayerFactory(){
		return this.dl;
	}
	
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
	
	
}
