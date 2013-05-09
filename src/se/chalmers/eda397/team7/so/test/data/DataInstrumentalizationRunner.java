package se.chalmers.eda397.team7.so.test.data;

import junit.framework.TestSuite;
import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;

public class DataInstrumentalizationRunner extends InstrumentationTestRunner {

	private final InstrumentationTestSuite testSuite;
	
	public DataInstrumentalizationRunner() {
		super();
		
		testSuite = new InstrumentationTestSuite(this);
		
		testSuite.addTestSuite(DataLayerTestSuite.suite().getClass());
	}
	
	@Override
	public TestSuite getAllTests(){
		
		return testSuite;
	}

}
