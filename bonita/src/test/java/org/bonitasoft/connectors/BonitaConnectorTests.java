package org.bonitasoft.connectors;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.bonitasoft.connectors.bonita.ConnectorTests;
import org.bonitasoft.connectors.bonita.filters.FilterTests;
import org.bonitasoft.connectors.bonita.resolver.RoleResolverTests;
import org.bonitasoft.connectors.legacy.HookConnectorTest;
import org.bonitasoft.connectors.legacy.RoleMapperRoleResolverTest;
import org.bonitasoft.connectors.legacy.VariablePerformerAssignFilterTest;

public class BonitaConnectorTests extends TestCase {

  public static Test suite() {
    TestSuite suite = new TestSuite("Bonita Connector Tests");
    suite.addTest(ConnectorTests.suite());
    suite.addTest(FilterTests.suite());
    suite.addTest(RoleResolverTests.suite());

    suite.addTestSuite(HookConnectorTest.class);
    suite.addTestSuite(RoleMapperRoleResolverTest.class);
    suite.addTestSuite(VariablePerformerAssignFilterTest.class);
    return suite;
  }

}
