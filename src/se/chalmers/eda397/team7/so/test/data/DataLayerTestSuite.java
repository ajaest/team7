package se.chalmers.eda397.team7.so.test.data;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DataLayerTestSuite extends TestCase {
	public static Test suite() {
		
		TestSuite suite = new TestSuite(DataLayerTestSuite.class.getName());
		suite.setName("DataLayers tests");
		//$JUnit-BEGIN$
		suite.addTestSuite(CommentDataLayerTest.class);
		suite.addTestSuite(PostDataLayerTest   .class);
		suite.addTestSuite(TagDataLayerTest    .class);
		//$JUnit-END$
		return suite;
	}

}
