package org.bonitasoft.connectors.legacy;

import org.bonitasoft.connectors.bonita.ConnectorTest;
import org.ow2.bonita.connector.core.Connector;

public class HookConnectorTest extends ConnectorTest {

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return HookConnector.class;
  }

}