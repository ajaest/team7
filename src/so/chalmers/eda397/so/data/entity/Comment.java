package so.chalmers.eda397.so.data.entity;

import so.chalmers.eda397.team7.so.datalayer.DataLayerFactory;

public class Comment extends Entity {

	protected Comment(DataLayerFactory dl) {
		super(dl);
	}

	@Override
	public boolean commit() {
		return false;
	}


}
