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
package org.bonitasoft.connectors.microsoft.exchange;


import java.util.List;
import java.util.logging.Level;

import org.bonitasoft.connectors.microsoft.exchange.common.ExchangeClient;
import org.bonitasoft.connectors.microsoft.exchange.common.ExchangeConnector;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Jordi Anguela
 */
public class CreateContact extends ExchangeConnector {

	// New Contact Parameters
	private String givenName;
	private String surname;
	private String companyName;
	private String jobTitle;
	private String phonenumber;
	private String email;				 // Mandatory

	@Override
	protected void validateSpecificParameters(List<ConnectorError> errors) {
		if(LOGGER.isLoggable(Level.INFO)){
			LOGGER.info("CreateContact parameters validattion --> givenName:" + givenName + 
					", surname:" + surname + 
					", companyName:" + companyName +
					", jobTitle:" + jobTitle +
					", phonenumber:" + phonenumber +
					", email:" + email);
		}
		if (email == null || email.length()<=0 ) {
			errors.add(new ConnectorError("email", new IllegalArgumentException("cannot be empty!")));
		}
	}
	
	@Override
	protected void executeSpecificConnector(ExchangeClient exchangeClient) throws Exception {
		response = exchangeClient.createContact(givenName, surname, companyName, jobTitle, email, phonenumber);
	}
	
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
}
