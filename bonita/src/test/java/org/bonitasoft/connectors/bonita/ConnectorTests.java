package org.bonitasoft.connectors.bonita;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ConnectorTests extends TestCase {

  public static Test suite() {
    TestSuite suite = new TestSuite("Connector Tests");
    suite.addTestSuite(AddCommentConnectorTest.class);
    suite.addTestSuite(ExecuteTaskConnectorTest.class);
    suite.addTestSuite(FinishTaskConnectorTest.class);
    suite.addTestSuite(SetVarConnectorTest.class);
    suite.addTestSuite(SetVarsConnectorTest.class);
    suite.addTestSuite(StartTaskConnectorTest.class);
    return suite;
  }

}
