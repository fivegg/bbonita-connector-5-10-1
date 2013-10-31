package org.bonitasoft.connectors.bonita;

import org.ow2.bonita.connector.core.Connector;

public class SetVarsConnectorTest extends ConnectorTest {

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return SetVarsConnector.class;
  }

}