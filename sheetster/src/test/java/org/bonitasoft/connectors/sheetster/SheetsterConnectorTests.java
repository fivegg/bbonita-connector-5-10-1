package org.bonitasoft.connectors.sheetster;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SheetsterConnectorTests extends TestCase {

  public static Test suite() throws Exception {
    final TestSuite suite = new TestSuite("Sheetster Tests");
    //suite.addTestSuite(GetCellContentConnectorTest.class);
    return suite;
  }
}
