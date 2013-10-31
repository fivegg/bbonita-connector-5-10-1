package org.bonitasoft.connectors.ldap;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.util.BonitaException;

public class LdapRoleResolverTest extends TestCase {

  protected static final Logger LOG = Logger.getLogger(LdapRoleResolverTest.class.getName());

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    if (LdapRoleResolverTest.LOG.isLoggable(Level.WARNING)) {
      LdapRoleResolverTest.LOG.warning("======== Starting test: " + this.getClass().getName() + "." + getName()
          + "() ==========");
    }
  }

  @Override
  protected void tearDown() throws Exception {
    if (LdapRoleResolverTest.LOG.isLoggable(Level.WARNING)) {
      LdapRoleResolverTest.LOG.warning("======== Ending test: " + getName() + " ==========");
    }
    super.tearDown();
  }

  public void testValidateConnector() throws BonitaException {
    final List<ConnectorError> errors = Connector.validateConnector(LdapRoleResolver.class);
    assertTrue(errors.isEmpty());
  }

  public void testSearchMembers() throws Exception {
    final LdapRoleResolver resolver = new LdapRoleResolver();
    resolver.setHost("192.168.1.212");
    resolver.setPort(10389L);
    resolver.setProtocol(LdapProtocol.LDAP.name());
    resolver.setUserName("cn=Directory Manager");
    resolver.setPassword("bonita-secret");
    resolver.setBaseObjectGroup("ou=groups,dc=bonita,dc=org");
    resolver.setFilter("(cn=*)");
    resolver.setBaseObjectPeople("ou=people,dc=bonita,dc=org");
    resolver.setMemberAttribute("uniqueMember");
    resolver.setUserAttribute("uid");
    final Set<String> members = resolver.getMembersSet("null");
    assertEquals(5, members.size());
    assertTrue(members.contains("martini"));
    assertTrue(members.contains("duponp"));
    assertTrue(members.contains("doej"));
    assertTrue(members.contains("lechanm"));
    assertTrue(members.contains("landyv"));
  }

  public void testSearchmembers2() throws Exception {
    final LdapRoleResolver resolver = new LdapRoleResolver();
    resolver.setHost("192.168.1.212");
    resolver.setPort(10389L);
    resolver.setProtocol(LdapProtocol.LDAP.name());
    resolver.setUserName("cn=Directory Manager");
    resolver.setPassword("bonita-secret");
    resolver.setBaseObjectGroup("ou=groups,dc=bonita,dc=org");
    resolver.setFilter("(cn=*)");
    resolver.setBaseObjectPeople("ou=people,dc=bonita,dc=org");
    final Set<String> members = resolver.getMembersSet("null");
    assertEquals(5, members.size());
    assertTrue(members.contains("martini"));
    assertTrue(members.contains("duponp"));
    assertTrue(members.contains("doej"));
    assertTrue(members.contains("lechanm"));
    assertTrue(members.contains("landyv"));
  }

}
