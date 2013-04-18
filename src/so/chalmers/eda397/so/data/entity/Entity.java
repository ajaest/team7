package so.chalmers.eda397.so.data.entity;

import so.chalmers.eda397.team7.so.datalayer.DataLayerFactory;

public abstract class Entity {

	private final DataLayerFactory dl;
	
	protected Entity(DataLayerFactory dl){
		this.dl= dl;
	}
	
	protected DataLayerFactory getDataLayerFactory(){
		return this.dl;
	}
	
}
