package org.bonitasoft.connectors.sugar;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SugarConnectorTests extends TestCase {

  public static Test suite() {
    TestSuite suite = new TestSuite("Sugar Tests");
    // TODO need a database
    //suite.addTestSuite(CreateReportFromDataBaseTest.class);
    return suite;
  }
}
