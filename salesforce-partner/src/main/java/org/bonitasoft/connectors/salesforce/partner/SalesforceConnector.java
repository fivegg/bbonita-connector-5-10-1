/**
 * Copyright (C) 2011-2012 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.connectors.salesforce.partner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * @author Charles Souillard, Matthieu Chaffotte
 * 
 */
public abstract class SalesforceConnector extends Connector {

  // why partner or enterprise: http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_partner.htm

  // partner jar
  // http://www.salesforce.com/us/developer/docs/api_asynch/Content/asynch_api_code_set_up_client.htm
  // http://code.google.com/p/sfdc-wsc/issues/detail?id=26
  // http://code.google.com/p/sfdc-wsc/downloads/list

  // getting a working jar file
  // http://www.salesforce.com/us/developer/docs/api/Content/sforce_api_quickstart_steps.htm

  // TODO: connector that retrieves a field or a list of fields of a given object. Maybe the query one can do that?

  private static final Logger LOGGER = Logger.getLogger(SalesforceConnector.class.getName());

  private String username;
  private String password_securityToken;
  private String authEndpoint;
  private String serviceEndpoint;
  private String restEndpoint;
  private String proxyHost;
  private int proxyPort;
  private String proxyUsername;
  private String proxyPassword;
  private int connectionTimeout;
  private int readTimeout;

  protected abstract void executeFunction(final PartnerConnection connection) throws Exception;

  protected abstract List<ConnectorError> validateExtraValues();

  @Override
  protected void executeConnector() throws Exception {
    final ConnectorConfig config = getConnectorConfiguration();
    final PartnerConnection connection = com.sforce.soap.partner.Connector.newConnection(config);
    if (LOGGER.isLoggable(Level.FINE)) {
      LOGGER.fine("User ID: " + " logged with sessionId: " + config.getSessionId());
    }
    try {
      this.executeFunction(connection);
    } finally {
      try {
        connection.logout();
      } catch (final ConnectionException e1) {
        if (LOGGER.isLoggable(Level.WARNING)) {
          LOGGER.warning("Error occured when logout!");
        }
      }
    }
  }

  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    // validate login / password are non-empty
    final ConnectorError usernameEmptyError = getErrorIfNullOrEmptyParam(username, "username");
    if (usernameEmptyError != null) {
      errors.add(usernameEmptyError);
      return errors;
    }
    final ConnectorError passwordEmptyError = getErrorIfNullOrEmptyParam(password_securityToken,
        "password_securityToken");
    if (passwordEmptyError != null) {
      errors.add(passwordEmptyError);
      return errors;
    }
    // validate timeout and port number
    if (connectionTimeout < 0) {
      errors.add(new ConnectorError("connectionTimeout", new IllegalArgumentException("cannot be less than 0!")));
      return errors;
    }
    if (readTimeout < 0) {
      errors.add(new ConnectorError("readTimeout", new IllegalArgumentException("cannot be less than 0!")));
      return errors;
    }
    if (proxyPort < 0) {
      errors.add(new ConnectorError("proxyPort", new IllegalArgumentException("cannot be less than 0!")));
      return errors;
    } else if (proxyPort > 65535) {
      errors.add(new ConnectorError("proxyPort", new IllegalArgumentException("cannot be greater than 65535!")));
      return errors;
    }
    // validate extra parameter values
    final List<ConnectorError> extraErrors = validateExtraValues();
    // add extra error to error list
    if (extraErrors != null) {
      errors.addAll(extraErrors);
    }
    return errors;
  }

  protected ConnectorConfig getConnectorConfiguration() {
    final ConnectorConfig config = new ConnectorConfig();
    if (username != null) {
      config.setUsername(username);
    }
    if (password_securityToken != null) {
      config.setPassword(password_securityToken);
    }
    if (authEndpoint != null) {
      config.setAuthEndpoint(authEndpoint);
    }
    if (proxyHost != null && proxyPort != 0) {
      config.setProxy(proxyHost, proxyPort);
    }
    if (proxyPassword != null) {
      config.setProxyPassword(proxyPassword);
    }
    if (proxyUsername != null) {
      config.setProxyUsername(proxyUsername);
    }
    if (connectionTimeout != 0) {
      config.setConnectionTimeout(connectionTimeout);
    }
    if (readTimeout != 0) {
      config.setReadTimeout(readTimeout);
    }
    if (restEndpoint != null) {
      config.setRestEndpoint(restEndpoint);
    }
    if (serviceEndpoint != null) {
      config.setServiceEndpoint(serviceEndpoint);
    }

    // config.setPrettyPrintXml(true);
    // config.setTraceMessage(true);
    return config;
  }

  protected ConnectorError getErrorIfIdLengthInvalid(String id) {
    id = id.trim();
    if (id.length() != 15 && id.length() != 18) {
      return new ConnectorError("sObjectId", new IllegalArgumentException("Length of sObjectId should be 15 or 18!"));
    }
    return null;
  }

  public Calendar convertString2Calendar(final String closedDate) throws ParseException {
    final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    sdf.setLenient(false);// set false for date strict check
    Date date = null;
    date = sdf.parse(closedDate);
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar;
  }

  /**
   * validation
   * 
   * @param param
   * @param paramName
   */
  protected ConnectorError getErrorIfNullOrEmptyParam(String param, final String paramName) {
    if (param != null) {
      param = param.trim();
      if (param.length() > 0) {
        return null;
      }
    }
    return new ConnectorError(paramName, new IllegalArgumentException("Cannot be empty!"));
  }

  public void setPassword_securityToken(final String password_securityToken) {
    this.password_securityToken = password_securityToken;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public void setAuthEndpoint(final String authEndpoint) {
    this.authEndpoint = authEndpoint;
  }

  public void setProxyPort(final int proxyPort) {
    this.proxyPort = proxyPort;
  }

  public void setProxyPassword(final String proxyPassword) {
    this.proxyPassword = proxyPassword;
  }

  public void setProxyHost(final String proxyHost) {
    this.proxyHost = proxyHost;
  }

  public void setProxyUsername(final String proxyUsername) {
    this.proxyUsername = proxyUsername;
  }

  public void setConnectionTimeout(final int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public void setReadTimeout(final int readTimeout) {
    this.readTimeout = readTimeout;
  }

  public void setRestEndpoint(final String restEndpoint) {
    this.restEndpoint = restEndpoint;
  }

  public void setServiceEndpoint(final String serviceEndpoint) {
    this.serviceEndpoint = serviceEndpoint;
  }

}
