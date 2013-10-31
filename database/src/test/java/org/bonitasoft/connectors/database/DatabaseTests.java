package org.bonitasoft.connectors.database;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.bonitasoft.connectors.database.connector.DatabaseConnectorTests;
import org.bonitasoft.connectors.database.roleresolver.DatabaseRoleResolverTests;

public class DatabaseTests extends TestCase {

  public static Test suite() throws Exception {
    final TestSuite suite = new TestSuite("Database Tests");
    suite.addTest(DatabaseConnectorTests.suite());
    suite.addTest(DatabaseRoleResolverTests.suite());
    return suite;
  }

}
