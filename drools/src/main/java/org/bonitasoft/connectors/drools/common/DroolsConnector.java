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
package org.bonitasoft.connectors.drools.common;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Yanyan Liu
 * 
 */
public abstract class DroolsConnector extends Connector {

  private String apiUrl;
  private String username;
  private String password;

  private String proxyHost;
  private int proxyPort;
  private String proxyUsername;
  private String proxyPassword;
  private int connectionTimeout;
  private int readTimeout;

  private String status;
  private String response;

  private static final Logger LOGGER = Logger.getLogger(DroolsConnector.class.getName());

  @Override
  protected void executeConnector() throws Exception {
    final String request = getRequest();
    if (LOGGER.isLoggable(Level.FINE)) {
      LOGGER.fine("request = " + request);
    }
    final HttpClient httpClient = getHttpClient();
    final PostMethod postMethod = new PostMethod(apiUrl);
    try {
      if (request == null) {
        throw new Exception("Request can not be empty!");
      }
      postMethod.setRequestEntity(new StringRequestEntity(request, "text/plain", "UTF-8"));
      httpClient.executeMethod(postMethod);
      status = String.valueOf(postMethod.getStatusCode());
      response = postMethod.getResponseBodyAsString();
      if (LOGGER.isLoggable(Level.FINE)) {
        LOGGER.fine("status = " + status);
        LOGGER.fine("response = " + response);
      }
    } catch (final Exception e) {
      if (LOGGER.isLoggable(Level.WARNING)) {
        LOGGER.warning(e.getMessage());
      }
      throw e;
    } finally {
      // release connection
      if (postMethod != null) {
        postMethod.releaseConnection();
      }
    }
  }

  protected abstract String getRequest();

  private HttpClient getHttpClient() {
    final HttpClient httpClient = new HttpClient();
    // set proxy
    if (proxyHost != null && proxyPort != 0) {
      httpClient.getHostConfiguration().setProxy(proxyHost, proxyPort);
    }
    // set proxy username & password
    if (proxyPassword != null && proxyUsername != null) {
      final Credentials creds = new UsernamePasswordCredentials(proxyUsername, proxyPassword);
      httpClient.getState().setProxyCredentials(AuthScope.ANY, creds);
    }
    // set username & password
    if (username != null && password != null) {
      final Credentials creds = new UsernamePasswordCredentials(username, password);
      httpClient.getState().setCredentials(AuthScope.ANY, creds);
    }
    // set connection/read timeout
    final HttpConnectionManagerParams managerParams = httpClient.getHttpConnectionManager().getParams();
    if (connectionTimeout != 0) {
      managerParams.setConnectionTimeout(connectionTimeout);
    }
    if (readTimeout != 0) {
      managerParams.setSoTimeout(readTimeout);
    }
    return httpClient;
  }

  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    // validate apiUrl and ksessionName
    final ConnectorError emptyApiUrlError = this.getErrorIfNullOrEmptyParam(apiUrl, "apiURL");
    if (emptyApiUrlError != null) {
      errors.add(emptyApiUrlError);
      return errors;
    }
    // validate port and timeout number
    if (proxyPort < 0) {
      errors.add(new ConnectorError("proxyPort", new IllegalArgumentException("cannot be less than 0!")));
      return errors;
    } else if (proxyPort > 65535) {
      errors.add(new ConnectorError("proxyPort", new IllegalArgumentException("cannot be greater than 65535!")));
      return errors;
    }
    if (connectionTimeout < 0) {
      errors.add(new ConnectorError("connectionTimeout", new IllegalArgumentException("cannot be less than 0!")));
      return errors;
    }
    if (readTimeout < 0) {
      errors.add(new ConnectorError("readTimeout", new IllegalArgumentException("cannot be less than 0!")));
      return errors;
    }
    // validate extra parameters
    final List<ConnectorError> extraErrors = validateExtraParams();
    // add extra error to error list
    if (extraErrors != null) {
      errors.addAll(extraErrors);
    }
    return errors;
  }

  protected abstract List<ConnectorError> validateExtraParams();

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

  public String getResponse() {
    return response;
  }

  public String getStatus() {
    return this.status;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public void setApiUrl(final String apiUrl) {
    this.apiUrl = apiUrl;
  }

  public void setProxyHost(final String proxyHost) {
    this.proxyHost = proxyHost;
  }

  public void setProxyPort(final int proxyPort) {
    this.proxyPort = proxyPort;
  }

  public void setProxyUsername(final String proxyUsername) {
    this.proxyUsername = proxyUsername;
  }

  public void setProxyPassword(final String proxyPassword) {
    this.proxyPassword = proxyPassword;
  }

  public void setConnectionTimeout(final int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public void setReadTimeout(final int readTimeout) {
    this.readTimeout = readTimeout;
  }

}
