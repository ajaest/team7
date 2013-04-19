package so.chalmers.eda397.team7.so.datalayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import so.chalmers.eda397.so.data.entity.EntityFactory;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public abstract class DataLayer  <E> {

	private final DataLayerFactory  dl;
	private final EntityFactory     ef;
	
	private final SQLiteDatabase    db;
	
	protected DataLayer(DataLayerFactory dl){
		this.dl = dl;
		this.db = dl.getDB();
		this.ef = new EntityFactory(dl);
	}
	
	protected final DataLayerFactory getDataLayerFactory(){
		return dl;
	}
	
	protected final SQLiteDatabase getDbInstance(){
		return db;
	}
	
	////////////////////////////////////////////
	//// Query methods
	////////////////////////////////////////////
	
	/**
	 * 
	 * Returns a set of instances retrieved from the database applying the
	 * selected query for retrieve the data.
	 * 
	 * <b>WARNING</b>: keep in mind that you're fully responsible that the
	 * provided query contains all the necessary attributes to generate E type
	 * instances
	 * 
	 * @param query
	 *            the query to execute
	 * @param queryArgs
	 *            the ordered list of values that are gonna replace and quote
	 *            "?" markers in the query
	 * @return a list of the selected entities ordered by the query criteria
	 */
	protected List<E> querySortedInstanceSet(String query, String queryArgs[]){
		
		Cursor  cur    ;
		List<E> result ;
		
		result = new ArrayList<E>();
		
		cur = this.getDbInstance().rawQuery(query, queryArgs);
		
		
		while(cur.moveToNext()){
			result.add(this.createNotSyncronizedInstance(cur));
		}
		
		cur.close();
		
		return result;
	}
	
	protected E querySingleInstance(String query, String queryArgs[]){
		
		Cursor cur;
		E      ins;
		
		cur = this.getDbInstance().rawQuery(query, queryArgs);
		
		ins = this.createNotSyncronizedInstance(cur);
		
		return ins;
		
	}
	
	protected void queryInsertOrReplace(String tableName, Map<String, String> attValues, Map<String, String> key){
		
		StringBuilder query   ;
		StringBuilder replace ;
		List<String>  attList ;
		Object[]      attArray;
		 
		query   = new StringBuilder("INSERT OR REPLACE INTO " + tableName + " (");
		replace = new StringBuilder("(");
		
		attList = new ArrayList<String>();
		for(String attName : attValues.keySet()){
			query  .append(attName + ", ");
			replace.append("?,"          );
			
			attList.add(attValues.get(attName));
			
		}
		
		query  .setLength(query  .length()-1);
		replace.setLength(replace.length()-1);
		
		query  .append(") ");
		replace.append(") ");
		
		query.append("VALUES (");
		query.append(replace   );
		
		if(key.size()>0){
			
			query.append("WHERE ");
			
			for(String keyName: key.keySet()){
				query.append(keyName + "=?");
				attList.add(key.get(keyName));
			}
		}
		
		attArray = new Object[attValues.size()];
		attArray = attList.toArray(attArray);
		
		this.getDbInstance().execSQL(query.toString(), attArray);
	}
	
	////////////////////////////////////////////
	//// Entity methods
	////////////////////////////////////////////
	
	
	/**
	 * Creates an raw instance of this entity given the current cursor. This
	 * class should read column values of the current cursor position to create
	 * an entity instance
	 * 
	 * <b>WARNING</b>: do nor move the current position of cur or data could be lost
	 * in methods using this function! 
	 */
	protected abstract E createNotSyncronizedInstance(Cursor cur);
	
	/**
	 * Returns the system's raw entity factory associated to this data layer
	 * @return
	 */
	protected EntityFactory getEntityFactory (){
		return this.ef;
	}
}
