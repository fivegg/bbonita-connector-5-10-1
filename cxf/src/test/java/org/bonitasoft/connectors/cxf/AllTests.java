package org.bonitasoft.connectors.cxf;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
//		suite.addTestSuite(CXFConnectorTest.class);
//		suite.addTestSuite(POJOWebServiceConnectorTest.class);
//		suite.addTestSuite(XMLWebServiceConntectorTest.class);
		return suite;
	}

}
