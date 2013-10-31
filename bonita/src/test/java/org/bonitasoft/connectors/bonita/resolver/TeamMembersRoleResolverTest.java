package org.bonitasoft.connectors.bonita.resolver;

import org.bonitasoft.connectors.bonita.ConnectorTest;
import org.bonitasoft.connectors.bonita.resolvers.TeamMembersRoleResolver;
import org.ow2.bonita.connector.core.Connector;

public class TeamMembersRoleResolverTest extends ConnectorTest {

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return TeamMembersRoleResolver.class;
  }

}
