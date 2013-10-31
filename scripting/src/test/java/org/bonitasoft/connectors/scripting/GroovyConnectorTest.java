package org.bonitasoft.connectors.scripting;

import org.ow2.bonita.connector.core.Connector;

public class GroovyConnectorTest extends ConnectorTest {

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return GroovyConnector.class;
  }

}
