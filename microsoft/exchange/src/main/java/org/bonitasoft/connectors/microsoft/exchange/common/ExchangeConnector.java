/**
 * Copyright (C) 2009 BonitaSoft S.A.
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
package org.bonitasoft.connectors.microsoft.exchange.common;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;

/**
 * @author Jordi Anguela
 */
public abstract class ExchangeConnector extends ProcessConnector {
	
	public static Logger LOGGER = Logger.getLogger(ExchangeConnector.class.getName());
	public ExchangeResponse response = new ExchangeResponse();

	private String username;			 // Mandatory
	private String password;			 // Mandatory
	private String serverLocation;		 // Mandatory
	
	protected abstract void validateSpecificParameters(List<ConnectorError> errors);
	protected abstract void executeSpecificConnector(ExchangeClient exchangeClient) throws Exception;
		
	@Override
	public List<ConnectorError> validateValues() {
		if(LOGGER.isLoggable(Level.INFO)){
			LOGGER.info("ExchangeConnector parameters validattion --> username:" + username + ", password:" + password + ", serverLocation:" + serverLocation);
		}
		List<ConnectorError> errors = new ArrayList<ConnectorError>();
		if (username == null || username.length()<=0 ) {
			errors.add(new ConnectorError("username", new IllegalArgumentException("cannot be empty!")));
		}
		if (password == null || password.length()<=0 ) {
			errors.add(new ConnectorError("password", new IllegalArgumentException("cannot be empty!")));
		}
		if (serverLocation == null || serverLocation.length()<=0 ) {
			errors.add(new ConnectorError("serverLocation", new IllegalArgumentException("cannot be empty!")));
		} else {
			if ( !Constants.EXCHANGE_SERVER_LOCATION_APAC.equals(serverLocation) &&
					!Constants.EXCHANGE_SERVER_LOCATION_EMEA.equals(serverLocation) &&
					!Constants.EXCHANGE_SERVER_LOCATION_NORTH_AMERICA.equals(serverLocation)) {
				errors.add(new ConnectorError("serverLocation", new IllegalArgumentException("Invalid value!")));
			}
		}
		validateSpecificParameters(errors);
		return errors;
	}
	
	@Override
	public void executeConnector() throws Exception {
		ExchangeClient exchangeClient = new ExchangeClient(username, password, serverLocation);
		executeSpecificConnector(exchangeClient);
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setServerLocation(String serverLocation) {
		this.serverLocation = serverLocation;
	}

	public String getStatusCode() {
		return response.getStatusCode();
	}

	public String getStatusText() {
		return response.getStatusText();
	}

	public String getResponse() {
		return response.getResponse();
	}
}
