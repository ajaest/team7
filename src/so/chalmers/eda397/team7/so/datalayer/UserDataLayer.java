package so.chalmers.eda397.team7.so.datalayer;

import so.chalmers.eda397.so.data.entity.User;
import android.database.Cursor;


public class UserDataLayer extends DataLayer<User>{

	protected UserDataLayer(DataLayerFactory dl) {
		super(dl);
	}

	@Override
	protected User createNotSyncronizedInstance(Cursor cur) {
		return this.getEntityFactory().createUser();
	}

}
