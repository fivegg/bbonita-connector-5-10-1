package org.bonitasoft.connectors.bonita.resolver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class RoleResolverTests extends TestCase {

  public static Test suite() {
    TestSuite suite = new TestSuite("Role Resolver Tests");
    suite.addTestSuite(ProcessInitiatorRoleResolverTest.class);
    suite.addTestSuite(UserListRoleResolverTest.class);
    suite.addTestSuite(UserRoleResolverTest.class);
    suite.addTestSuite(GroupUsersRoleResolverTest.class);
    suite.addTestSuite(GroupRoleUsersRoleResolverTest.class);
    suite.addTestSuite(DelegeeRoleResolverTest.class);
    suite.addTestSuite(ManagerRoleResolverTest.class);
    suite.addTestSuite(TeamMembersRoleResolverTest.class);
    return suite;
  }

}
