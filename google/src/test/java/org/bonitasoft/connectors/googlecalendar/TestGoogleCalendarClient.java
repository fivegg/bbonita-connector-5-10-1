/**
 * Created by Jordi Anguela
 * 
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

package org.bonitasoft.connectors.googlecalendar;

import org.bonitasoft.connectors.googlecalendar.common.GoogleCalendarClient;


public class TestGoogleCalendarClient {

	/**
	 * @param arg[0] = userEmail
	 *        arg[1] = password
	 */
	public static void main(String[] args) {
		
		if (args.length != 2) {
			System.out.println("To lunch this test send user email & password as parameters. arg[0] = userEmail, arg[1] = password");
		} else {
		
			String calendarURL = "http://www.google.com/calendar/feeds/<your_user_name>@gmail.com/private/full";
			
			try {
	
				GoogleCalendarClient gcc = new GoogleCalendarClient(args[0], args[1], calendarURL);
				
				String title1    = "Christmas";
				String title2    = "Merry Christmas";
				String content   = "Happy Christmas!";
				String startTime = "2010-12-24T20:00:00+01:00";
				String endTime   = "2010-12-24T23:00:00+01:00";
	
				gcc.createEvent(title1, content, startTime, endTime);
				gcc.createEvent(title2, content, startTime, endTime);
				
				// DELETE events
	//			gcc.deleteEnvents("Merry Christmas"); 
	//			gcc.deleteEnvents(""); // delete ALL events
				
				/*
				// Add a web page as WebContent
				String icon        = "http://www.google.com/favicon.ico";
        String iconTitle   = "Google icon";
        String contentType = "text/html";
        String url    = "http://www.bonitasoft.com";
        String width  = "600";
        String height = "480";
				gcc.createEvenWithWebContent(title1, content, startTime, endTime, icon, iconTitle, contentType, url, width, height, null);
				*/
				
				/*
				// Add an image as WebContent
				String urlImage  = "http://www.google.es/logos/holiday09_3.gif";
				String imageType = "image/*";
				gcc.createEvenWithWebContent(title1, content, startTime, endTime, icon, iconTitle, imageType, urlImage, "267", "161", null);
				*/
				
				/*
				// Add xml content as WebContent
				String xmlIcon  = "http://www.thefreedictionary.com/favicon.ico";
			  String xmlTitle = "Word of the Day";
				String xmlType  = "application/x-google-gadgets+xml";
				String xmlUrl   = "http://www.thefreedictionary.com/_/WoD/wod-module.xml";
				Map<String,String> prefs = new HashMap<String,String>();
				prefs.put("Format", "0");
				prefs.put("Days",   "1");
	      gcc.createEvenWithWebContent("", "", startTime, endTime, xmlIcon, xmlTitle, xmlType, xmlUrl, "300", "136", prefs);
	      */
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
