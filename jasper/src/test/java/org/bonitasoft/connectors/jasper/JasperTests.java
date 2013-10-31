package org.bonitasoft.connectors.jasper;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class JasperTests extends TestCase {

  public static Test suite() {
    TestSuite suite = new TestSuite("Jasper Tests");
    // TODO need a database
    //suite.addTestSuite(CreateReportFromDataBaseTest.class);
    return suite;
  }
}
