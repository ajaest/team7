package se.chalmers.eda397.team7.so.data;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLiteSODatabaseHelper extends SQLiteOpenHelper{

	private final static String DB_NAME = "so.sqlite";
			
	private final Context        ctx     ;
	
	private boolean        databaseOverriden = false;
	@SuppressWarnings("unused")
	private SQLiteDatabase overridenDatabase = null ;
	 
	public SQLiteSODatabaseHelper(Context context) throws IOException {
		super(context, DB_NAME, null, 1);
		
		this.ctx = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		try {
			
			//If it exists a raw sqlite database, import it using copying method
			if(Arrays.binarySearch(this.ctx.getAssets().list(""), DB_NAME + ".gzip")>=0){
				initSQLFromRaw(SQLiteSODatabaseHelper.DB_NAME,this.ctx);
				this.databaseOverriden = true;
			}else //Method two, import a SQL text dump
			if(Arrays.binarySearch(this.ctx.getAssets().list(""), DB_NAME.replaceAll(".sqlite$", "sql") + ".gzip")>=0){
				initSQLFromQuery(db, this.ctx);
			}else{
				throw new SQLDataRuntimeException("There's no data to import into the database! please include a \"*.sql.gzip\" or a \"*.sqlite.gzip\" file into the assets folder before compiling");
			}
			
		} catch (IOException e) {
			throw new SQLDataRuntimeException("An unexpected IO error ocurred when trying to import the initial data into de local database", e);
		}
		
	}
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.setVersion(db.getVersion()+1);
		onCreate(db);				
	}
	
	public static void initSQLFromQuery(SQLiteDatabase db, Context ctx) {
		
		BufferedReader sqlScriptStream;		
		try {
			sqlScriptStream = new BufferedReader(new InputStreamReader(new GZIPInputStream(ctx.getAssets().open("so.sql.gzip"))));
			
			String query;
			while(null != (query = sqlScriptStream.readLine())){
				db.execSQL(query); 
			}
			
		} catch (IOException e) {
			Log.d("so.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper::initSQLFromQuery", "Problem when opening the SQL script", e);
		}
			
	}
	
	public static void initSQLFromRaw(String db_name, Context ctx) throws IOException {
		
		InputStream import_db  ;
		String      db_path    ;
		File        old_db_file;
		

		db_path = ctx.getDatabasePath(SQLiteSODatabaseHelper.DB_NAME).getAbsolutePath();

		old_db_file = new File(db_path);
		
		if(old_db_file.exists()){
			Log.d("so.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper::initSQLFromRaw", "deleting old db at \"" + db_path + "\"");
			new File(db_path).delete();
		}
		
		Log.d("so.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper::initSQLFromRaw", "creating new db at \"" + db_path + "\"");
        new File(db_path).createNewFile();
		
        Log.d("so.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper::initSQLFromRaw", "opening stream to import db from \"" + db_name + ".gzip\" asset");
        import_db = new BufferedInputStream(new GZIPInputStream(ctx.getAssets().open(db_name + ".gzip")));

        Log.d("so.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper::initSQLFromRaw", "importing data into new db at \"" + db_path + "\"");
        OutputStream new_db = new FileOutputStream(db_path);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = import_db.read(buffer))>0) {
        	new_db.write(buffer,0,length);
        }
        
        Log.d("so.chalmers.eda397.team7.so.data.SQLiteSODatabaseHelper::initSQLFromRaw", "data import finished at \"" + db_path + "\"");
        new_db   .flush();
        new_db   .close();
        import_db.close();
		
	}
	
	@Override
	public SQLiteDatabase getWritableDatabase(){
		
		SQLiteDatabase retDb;
		
		retDb = super.getWritableDatabase();
		
		if(databaseOverriden){
			String db_path ;
			
			retDb.close();
			
			db_path = ctx.getDatabasePath(SQLiteSODatabaseHelper.DB_NAME).getAbsolutePath();
			
			retDb = SQLiteDatabase.openDatabase(db_path, null, SQLiteDatabase.OPEN_READONLY);
			
			this.overridenDatabase = retDb;
		}
		
		return retDb;
		
	}
	
	@Override
	public SQLiteDatabase getReadableDatabase(){
		
		SQLiteDatabase retDb;
		
		retDb = super.getReadableDatabase();
		
		if(databaseOverriden){
			String db_path ;
			
			retDb.close();
			
			db_path = ctx.getDatabasePath(SQLiteSODatabaseHelper.DB_NAME).getAbsolutePath();
			
			retDb = SQLiteDatabase.openDatabase(db_path, null, SQLiteDatabase.OPEN_READONLY);
			
			this.overridenDatabase = retDb;
		}
		
		return retDb;
		
	}
	
	
}
