package org.bonitasoft.connectors.bonita.resolver;

import org.bonitasoft.connectors.bonita.ConnectorTest;
import org.bonitasoft.connectors.bonita.resolvers.ProcessInitiatorRoleResolver;
import org.ow2.bonita.connector.core.Connector;

public class ProcessInitiatorRoleResolverTest extends ConnectorTest {

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return ProcessInitiatorRoleResolver.class;
  }

}