package se.chalmers.eda397.team7.so.datalayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import se.chalmers.eda397.team7.so.data.entity.EntityFactory;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;


public abstract class DataLayer  <E> {

	private final DataLayerFactory  dl;
	private final EntityFactory     ef;
	
	private final SQLiteDatabase    db;
	
	private List<EntityIndex<E>> cachedSearch;
	private SparseArray<E>       cachedPosts ;
	private Integer              cacheHash   ;
	
	protected DataLayer(DataLayerFactory dl){
		this.dl = dl;
		this.db = dl.getDB();
		this.ef = new EntityFactory(dl);
		
		cachedSearch = new ArrayList<EntityIndex<E>>();
		cachedPosts  = new SparseArray<E>();
		cacheHash    = null;
	}
	
	protected final DataLayerFactory getDataLayerFactory(){
		return dl;
	}
	
	protected final SQLiteDatabase getDbInstance(){
		return db;
	}
	
	public void clearCache(){
		cachedPosts.clear();
	}
	
	////////////////////////////////////////////
	//// Auxiliary Query methods
	////////////////////////////////////////////
	
	/**
	 * 
	 * Given an index collection this function retrieves blocks of object
	 * instances from the database
	 * 
	 * @param searchArgs
	 *            the search arguments
	 * @param pageSize
	 *            the size of the list to be returned
	 * @param page
	 *            the offset of the page that it's going to be extracted from
	 *            the index list starting from 0.
	 * @param lazyRetriever
	 *            an implementation of a lazy index searcher
	 * @param cacheId
	 *            a unique id which identifies the data service which uses this
	 *            function It is used to keep the current state of the cache. If
	 *            thre consecutive calls to
	 *            {@link #pagedSearch(Set, Integer, Integer, LazyRetriever, Integer)}
	 *            with different {@code cacheId} are performed, the cache will
	 *            be cleared twice (1- fill the cache, 2- clear cache for new
	 *            call and fill, 3- clear cache for new call and fill)
	 * @return
	 */
	public <A,T extends EntityIndex<E>> List<E> pagedSearch(Set<A> searchArgs, Integer pageSize, Integer page, LazyRetriever<A,T> lazyRetriever, Integer cacheId){
		
		List<E> retPost;
		
		Integer wordsHashCode;
		Integer startIdx, endIdx;
		
		long tStart, tEnd;
		
		tStart = System.currentTimeMillis();
		
		wordsHashCode = searchArgs.hashCode() + 31*cacheId;
		
		if(this.cacheHash==null || !this.cacheHash.equals(wordsHashCode)){
			cacheHash    = wordsHashCode;
			cachedSearch.clear();
			
			cachedSearch.addAll(lazyRetriever.retrieve(searchArgs));
			
		}
		
		retPost = new ArrayList<E>();
		
		startIdx = pageSize * page;
		endIdx   = pageSize * page + pageSize;
		
		if(startIdx > this.cachedSearch.size()){
			//No result
			startIdx = 0;
			endIdx   = 0;
		}else{
			if(endIdx > this.cachedSearch.size()){
				endIdx = this.cachedSearch.size();
			}
		}
		
		E p;
		for(EntityIndex<E> pIdx: cachedSearch.subList(startIdx, endIdx)){
			if(null==(p=cachedPosts.get(pIdx.getId()))){
				p = pIdx.retrieveInstance();
				this.cachedPosts.put(pIdx.getId(),p);
			}
			
			retPost.add(p);
		}
		
		//TODO: more smart caching strategy
		if(cachedPosts.size()>500){
			cachedPosts.clear();
		}
		
		tEnd= System.currentTimeMillis();
		
		
		
		Log.d("se.chalmers.eda397.team7.so.datalayer.DataLayer#pagedSearch", "Finished retrieving " + (endIdx - startIdx) + " instances from the database in  " + (tEnd - tStart) + "ms");
		
		return retPost;
	}
	
	
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
		
		if(cur.moveToNext()){
			ins = this.createNotSyncronizedInstance(cur);
		}else{
			ins = null;
		}
		
		
		return ins;
		
	}
	
	/**
	 * Returns a query with the following structure
	 * 
	 * <pre>
	 * SELECT 
	 * 		id 
	 * FROM 
	 * 		&lt;tableName&gt; 
	 * NATURAL JOIN 
	 * 		&lt;naturalJoinValue&gt; 
	 * WHERE 
	 * 		&lt;attName&gt;=?
	 * 		(OR &lt;attName&gt;=?){&lt;wordCount&gt;-1}
	 * </pre>
	 */
	protected String generateIdSearchQuery(Integer wordCount, String tableName, String naturalJoinValue, String attName){
		
		StringBuilder sb = new StringBuilder("SELECT id FROM ");
		sb.append(tableName);
		
		if(naturalJoinValue!=null && naturalJoinValue.length()>0){
			sb.append(" NATURAL JOIN ");
			sb.append(naturalJoinValue);
			sb.append(" ");
		}
		
		sb.append(" WHERE ");
		
		if(wordCount>0){
			sb.append(attName);
			sb.append("=?");
			
			for(int i=1; i<wordCount; i++){
				sb.append(" OR ");
				sb.append(attName);
				sb.append("=?");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * Returns a query with the following structure
	 * 
	 * <pre>
	 * SELECT 
	 * 		id 
	 * FROM 
	 * 		&lt;tableName&gt; 
	 * WHERE 
	 * 		&lt;attName&gt;=?
	 * 		(OR &lt;attName&gt;=?){&lt;wordCount&gt;-1}
	 * </pre>
	 */
	protected String generateSearchQuery(Integer wordCount, String tableName, String attName){
		
		return generateIdSearchQuery(wordCount, tableName, null, attName);
	}
	
	
	/**
	 * Executes a query generating the following SQL query from input values
	 * 
	 * <pre>
	 * INSERT OR REPLACE INTO 
	 * 		&lt;tableName&gt; 
	 * 		(
	 * 			&lt;attValues.keySet()&gt;
	 * 		)
	 * VALUES
	 * 		(	
	 * 			&lt;attValues.values()&gt;
	 * 		)
	 * WHERE 
	 * 		&lt;key.key1&gt;=&lt;key.value1&gt;
	 * 		(OR &lt;key.keyX&gt;=&lt;key.valueX&gt;){&lt;key.size()&gt;-1}
	 * </pre>
	 */
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
		
		/* Inserting a new value in the database can make the current cache invalid */
		this.clearCache();
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
	
	////////////////////////////////////////////
	//// Auxiliary classes
	////////////////////////////////////////////
	
	protected static interface LazyRetriever <A,T> {
		
		public Collection<T> retrieve(Set<A> args);		
	}
	
	protected static abstract class EntityIndex <J> {
		
		private Integer id;
		
		protected EntityIndex(Integer id){
			this.id = id;
		}
		
		public Integer getId(){
			return this.id;
		}
		
		public abstract J retrieveInstance();
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("rawtypes")
			EntityIndex other = (EntityIndex) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

	}
}
