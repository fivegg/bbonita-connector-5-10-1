package org.bonitasoft.connectors.bonita.resolver;

import org.bonitasoft.connectors.bonita.ConnectorTest;
import org.bonitasoft.connectors.bonita.resolvers.DelegeeRoleResolver;
import org.ow2.bonita.connector.core.Connector;

public class DelegeeRoleResolverTest extends ConnectorTest {

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return DelegeeRoleResolver.class;
  }

}
