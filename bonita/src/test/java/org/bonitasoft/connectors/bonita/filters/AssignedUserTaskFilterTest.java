package org.bonitasoft.connectors.bonita.filters;

import org.bonitasoft.connectors.bonita.ConnectorTest;
import org.ow2.bonita.connector.core.Connector;

public class AssignedUserTaskFilterTest extends ConnectorTest {

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return AssignedUserTaskFilter.class;
  }

}