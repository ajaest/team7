package se.chalmers.eda397.team7.so.test.data;

import se.chalmers.eda397.team7.so.data.entity.EntityFactory;
import se.chalmers.eda397.team7.so.datalayer.DataLayerFactory;
import se.chalmers.eda397.team7.so.datalayer.TagDataLayer;
import se.chalmers.eda397.team7.so.test.utils.TestUtils;
import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;

@SuppressLint("UseSparseArrays")
public class TagDataLayerTest extends InstrumentationTestCase {
	
	private final SQLiteDatabase   db              ;
	private final TagDataLayer     tagtDL       ;
	private final EntityFactory    entityFactory   ;
	private final DataLayerFactory dataLayerFactory;
	private final TestUtils        testUtils       ;
	
	@SuppressLint("SdCardPath")
	public TagDataLayerTest (){
		db = SQLiteDatabase.openDatabase("/data/data/se.chalmers.eda397.team7.so/databases/so.sqlite", null, SQLiteDatabase.OPEN_READONLY);
		
		dataLayerFactory = new DataLayerFactory(db);
		
		tagtDL = dataLayerFactory.createTagDataLayer();
		
		entityFactory = new EntityFactory(dataLayerFactory);
		
		testUtils = new TestUtils(db, entityFactory);
	}
	
	
	@Override
	public void setUp(){
		
		db.execSQL("BEGIN TRANSACTION;");
	}
	
	public void tearDown(){
		db.execSQL("ROLLBACK;");
	}

	@Override
	protected void finalize() throws Throwable{
		db.execSQL("END TRANSACTION;");
		db.close();
		
		super.finalize();
	}
	
	
//////////////////////////////////////////////////////////
//////////Tests
//////////////////////////////////////////////////////////
	
	/////////////////////////////////////
	// TagDataLayer.test_getTagGraphRelativeColor(String,String)
	/////////////////////////////////////
	
	public void test_getTagGraphRelativeColor(){
		
		Long c;
		String color;
		
		c = 0L + tagtDL.getTagGraphRelativeColor("php", "mysql");
		c = c & 0x0000000000ffffff;
		color = "#" + Long.toHexString(c);
		assertEquals("#ff00", color);
		
		c = 0L + tagtDL.getTagGraphRelativeColor("php", "arrays");
		c = c & 0x0000000000ffffff;
		color = "#" + Long.toHexString(c);
		assertEquals("#ff4700", color);
		
		c = 0L + tagtDL.getTagGraphRelativeColor("php", "jquery");
		c = c & 0x0000000000ffffff;
		color = "#" + Long.toHexString(c);
		assertEquals("#ff1c00", color);
		
		c = 0L + tagtDL.getTagGraphRelativeColor("php", "ajax");
		c = c & 0x0000000000ffffff;
		color = "#" + Long.toHexString(c);
		assertEquals("#ff0000", color);
	}

}



















