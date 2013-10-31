/**
 * Copyright (C) 2009-2011 BonitaSoft S.A.
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

package org.bonitasoft.connectors.webdav.exo.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;

/**
 * 
 * @author Jordi Anguela, Yanyan Liu
 * 
 */
public abstract class ExoConnector extends ProcessConnector {

    protected static final Logger LOGGER = Logger.getLogger(ExoConnector.class.getName());

    private String host;
    private Long port;
    private String username;
    private String password;

    protected HttpClient client = null;

    private String statusCode = "";
    private String statusText = "";
    protected String response = "";

    @Override
    protected void executeConnector() throws Exception {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("executing ExoConnector with params: " + host + ":" + port.toString() + " " + username + ":" + password);
        }
        this.initClient(host, port.intValue(), username, password);
        executeFunction();
    }

    /**
     * execute functions such as createFolder, uploadFile, checkin methods and so on.
     * 
     * @throws Exception
     */
    protected abstract void executeFunction() throws Exception;

    @Override
    protected List<ConnectorError> validateValues() {
        final List<ConnectorError> errors = new ArrayList<ConnectorError>();
        if (port != null) {
            if (port < 0) {
                errors.add(new ConnectorError("port", new IllegalArgumentException("cannot be less than 0!")));
            } else if (port > 65535) {
                errors.add(new ConnectorError("port", new IllegalArgumentException("cannot be greater than 65535!")));
            }
        }
        validateFunctionParameters(errors);
        return errors;
    }

    /**
     * validate parameters for function such as createFolder, uploadFile, checkin methods and so on.
     * 
     * @param errors
     */
    protected abstract void validateFunctionParameters(final List<ConnectorError> errors);

    /**
     * validate URI
     * 
     * @param errors
     * @param uri
     * @param field
     */
    public void validateUri(final List<ConnectorError> errors, final String uri, final String field) {
        if (!(uri.length() > 0)) {
            errors.add(new ConnectorError(field, new IllegalArgumentException("cannot be empty")));
        }
    }

    /**
     * initiate client
     * 
     * @param host
     * @param port
     * @param username
     * @param password
     */
    public void initClient(final String host, final int port, final String username, final String password) {

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("WebDAVConnector {host=" + host + ", port=" + port + ", username=" + username + ", password=******}");
        }

        final HostConfiguration hostConfig = new HostConfiguration();
        hostConfig.setHost(host, port);

        final HttpConnectionManager connectionManager = new SimpleHttpConnectionManager();
        final HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        final int maxHostConnections = 20;
        params.setMaxConnectionsPerHost(hostConfig, maxHostConnections);
        connectionManager.setParams(params);

        client = new HttpClient(connectionManager);
        final Credentials creds = new UsernamePasswordCredentials(username, password);
        client.getState().setCredentials(AuthScope.ANY, creds);
        client.setHostConfiguration(hostConfig);
    }

    /**
     * process response to get statusCode, statusText and response string.
     * 
     * @param httpMethod
     * @param getResponseAsString
     */
    protected void processResponse(final HttpMethod httpMethod, final boolean getResponseAsString) {

        String statusCode = "-1";
        if (httpMethod.getStatusCode() > 0) {
            statusCode = String.valueOf(httpMethod.getStatusCode());
        }
        final String statusText = httpMethod.getStatusText();

        String responseString = "";
        if (getResponseAsString) {
            try {
                responseString = httpMethod.getResponseBodyAsString();
                if (responseString == null) {
                    responseString = "";
                }
            } catch (final IOException e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning("IOException while getting responseAsString: " + e.getMessage());
                }
                e.printStackTrace();
            }
        }

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("status CODE: " + statusCode + ", status TEXT: " + statusText + "\n response string: " + responseString);
        }
        this.statusCode = statusCode;
        this.statusText = statusText;
        this.response = responseString;
    }

    /**
     * set the port
     * 
     * @param port
     */
    public void setPort(final Long port) {
        this.port = port;
    }

    /**
     * set the username
     * 
     * @param username the user name to login the eXo
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * set the host
     * 
     * @param host the host of the eXo
     */
    public void setHost(final String host) {
        this.host = host;
    }

    /**
     * set the password
     * 
     * @param password the password to login the eXo
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * get the status code
     * 
     * @return String
     */
    public String getStatusCode() {
        return this.statusCode;
    }

    /**
     * get the status text
     * 
     * @return String
     */
    public String getStatusText() {
        return this.statusText;
    }

    /**
     * get the response text.
     * 
     * @return String
     */
    public String getResponse() {
        return this.response;
    }
}
