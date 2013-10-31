package org.bonitasoft.connectors.database.roleresolver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DatabaseRoleResolverTests extends TestCase {

  public static Test suite() {
    TestSuite suite = new TestSuite("Database Role Resolver Tests");
    suite.addTestSuite(MySQLRoleResolverTest.class);
    suite.addTestSuite(PostgreSQLRoleResolverTest.class);
    return suite;
  }

}
