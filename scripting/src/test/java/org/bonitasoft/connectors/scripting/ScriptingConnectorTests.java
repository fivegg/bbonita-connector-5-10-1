package org.bonitasoft.connectors.scripting;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ScriptingConnectorTests extends TestCase {

  public static Test suite() {
    TestSuite suite = new TestSuite("LDAP Connector Tests");
    suite.addTestSuite(GroovyConnectorTest.class);
    suite.addTestSuite(ShellConnectorTest.class);
    return suite;
  }

}
