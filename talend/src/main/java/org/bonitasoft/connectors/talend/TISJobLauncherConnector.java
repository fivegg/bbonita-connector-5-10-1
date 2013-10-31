package org.bonitasoft.connectors.talend;

import java.util.List;

import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;
import org.talend.administrator.webservices.AdministratorWebServiceProxy;

public class TISJobLauncherConnector extends Connector {

  private String login;
  private String password;
  private String wsURL = "http://localhost:8080/org.talend.administrator/services/AdministratorWebService?wsdl";
  private int taskId;

  public void setTaskId(final int taskId) {
    this.taskId = taskId;
  }

  public void setWsURL(final String wsURL) {
    this.wsURL = wsURL;
  }

  public void setLogin(final String login) {
    this.login = login;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  @Override
  protected void executeConnector() throws Exception {
    final AdministratorWebServiceProxy awsp = new AdministratorWebServiceProxy(wsURL);
    final String sessionId = awsp.initSession(login, password);
    final String[][] contextParams = null;
    awsp.scheduleTaskForInstantRun(sessionId, taskId , true, contextParams);
    awsp.closeSession(sessionId);
  }

  @Override
  protected List<ConnectorError> validateValues() {
    // TODO Auto-generated method stub
    return null;
  }

}