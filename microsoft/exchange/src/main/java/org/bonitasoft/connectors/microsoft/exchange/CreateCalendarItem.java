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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;

import org.bonitasoft.connectors.microsoft.exchange.common.ExchangeClient;
import org.bonitasoft.connectors.microsoft.exchange.common.ExchangeConnector;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Jordi Anguela
 */
public class CreateCalendarItem extends ExchangeConnector {

	// New CalendarItem Parameters
	String subject;
	String body;
	String startTime;
	String endTime;
	String location;
	String requiredAttendeesEmails;
	
	Date startDate;
	Date endDate;
	List<String> attendeesEmailsList;

	@Override
	protected void validateSpecificParameters(List<ConnectorError> errors) {
		if(LOGGER.isLoggable(Level.INFO)){
			LOGGER.info("CreateCalendarItem parameters validattion --> subject:" + subject + 
					", body:" + body + 
					", startTime:" + startTime +
					", endTime:" + endTime +
					", location:" + location +
					", requiredAttendeesEmails:" + requiredAttendeesEmails);
		}
		if (startTime == null || startTime.length()<=0 ) {
			errors.add(new ConnectorError("startTime", new IllegalArgumentException("cannot be empty! format: 'yyyy-MM-ddTHH:mm:ss'")));
		} else {
			startDate = ExchangeClient.validateStringDate(startTime);
			if (startDate == null) {
				errors.add(new ConnectorError("startTime", new IllegalArgumentException("invalid format! format: 'yyyy-MM-ddTHH:mm:ss'")));
			}
		}
		if (endTime == null || endTime.length()<=0 ) {
			errors.add(new ConnectorError("endTime", new IllegalArgumentException("cannot be empty! format: 'yyyy-MM-ddTHH:mm:ss'")));
		} else {
			endDate = ExchangeClient.validateStringDate(endTime);
			if (endDate == null) {
				errors.add(new ConnectorError("endTime", new IllegalArgumentException("invalid format! format: 'yyyy-MM-ddTHH:mm:ss'")));
			}
		}
		if (requiredAttendeesEmails == null || requiredAttendeesEmails.length()<=0 ) {
			errors.add(new ConnectorError("requiredAttendeesEmails", new IllegalArgumentException("cannot be empty!")));
		}
	}
	
	@Override
	protected void executeSpecificConnector(ExchangeClient exchangeClient) throws Exception {
		attendeesEmailsList = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(requiredAttendeesEmails, ",");
		while ( st.hasMoreElements() ) {
			String email = st.nextToken();
			if (email != null && email.length() > 0) {
				email = email.trim();
			}
			attendeesEmailsList.add(email);
		}
		
		response = exchangeClient.createCalendarItem(subject, body, startDate, endDate, location, attendeesEmailsList);
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setRequiredAttendeesEmails(String requiredAttendeesEmails) {
		this.requiredAttendeesEmails = requiredAttendeesEmails;
	}
}
