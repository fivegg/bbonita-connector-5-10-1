package org.bonitasoft.connectors.bonita.resolver;

import org.bonitasoft.connectors.bonita.ConnectorTest;
import org.bonitasoft.connectors.bonita.resolvers.GroupUsersRoleResolver;
import org.ow2.bonita.connector.core.Connector;

public class GroupUsersRoleResolverTest extends ConnectorTest {

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return GroupUsersRoleResolver.class;
  }

}
