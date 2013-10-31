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
import java.util.Date;
import java.util.List;

/**
 * @author Jordi Anguela
 */
public class ExchangeClientTest {
	
	private static ExchangeClient exchangeClient;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println("==== Starting ExchangeClient ====");
		
		String username    = "USERNAME@COMPANY.emea.microsoftonline.com";
		String password    = "*********";
		String serverLocation = Constants.EXCHANGE_SERVER_LOCATION_EMEA;
		
		try {
			
			exchangeClient = new ExchangeClient(username, password, serverLocation);

			createContact();
			
			createCalendarItem();
			
			System.out.println("==== Finishing ExchangeClient ====");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private static void createContact() {
		try {
			String givenName   = "Albert";
			String surname     = "Anguela";
			String companyName = "AlbertBass";
			String jobTitle    = "Bass player";
			String phonenumber = "676123456";
			String email       = "jordi@hotmail.com";
			
			ExchangeResponse exchangeResponse = exchangeClient.createContact(givenName, surname, companyName, jobTitle, email, phonenumber);
			
			System.out.println(exchangeResponse);
			
		} catch (Exception e) {
			System.out.println("ERROR: createContact. Message: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void createCalendarItem() {
		try {
			String subject   = "Saint George book day";
			String body      = "Meeting : book selling strategy during Saint George's day<br>www.bonitasoft.com";
			String startTime = "2010-04-23T09:00:00";
			String endTime   = "2010-04-23T11:00:00";
			String location  = "Conference room 007";
			List<String> requiredAttendeesEmails = new ArrayList<String>();
			requiredAttendeesEmails.add("jordi@hotmail.com");
			requiredAttendeesEmails.add("jordi@gmail.com");
			
			Date startDate = ExchangeClient.validateStringDate(startTime);
			Date endDate = ExchangeClient.validateStringDate(endTime);
			
			ExchangeResponse exchangeResponse = exchangeClient.createCalendarItem(subject, body, startDate, endDate, location, requiredAttendeesEmails);
			
			System.out.println(exchangeResponse);
			
		} catch (Exception e) {
			System.out.println("ERROR: createCalendarItem. Message: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
