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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;

import com.microsoft.schemas.exchange.services._2006.messages.ArrayOfResponseMessagesType;
import com.microsoft.schemas.exchange.services._2006.messages.CreateItemResponseType;
import com.microsoft.schemas.exchange.services._2006.messages.CreateItemType;
import com.microsoft.schemas.exchange.services._2006.messages.ExchangeServicePortType;
import com.microsoft.schemas.exchange.services._2006.messages.ExchangeServices;
import com.microsoft.schemas.exchange.services._2006.messages.ItemInfoResponseMessageType;
import com.microsoft.schemas.exchange.services._2006.messages.ResponseMessageType;
import com.microsoft.schemas.exchange.services._2006.types.AttendeeType;
import com.microsoft.schemas.exchange.services._2006.types.BodyType;
import com.microsoft.schemas.exchange.services._2006.types.BodyTypeType;
import com.microsoft.schemas.exchange.services._2006.types.CalendarItemCreateOrDeleteOperationType;
import com.microsoft.schemas.exchange.services._2006.types.CalendarItemType;
import com.microsoft.schemas.exchange.services._2006.types.ContactItemType;
import com.microsoft.schemas.exchange.services._2006.types.DistinguishedFolderIdNameType;
import com.microsoft.schemas.exchange.services._2006.types.DistinguishedFolderIdType;
import com.microsoft.schemas.exchange.services._2006.types.EmailAddressDictionaryEntryType;
import com.microsoft.schemas.exchange.services._2006.types.EmailAddressDictionaryType;
import com.microsoft.schemas.exchange.services._2006.types.EmailAddressKeyType;
import com.microsoft.schemas.exchange.services._2006.types.EmailAddressType;
import com.microsoft.schemas.exchange.services._2006.types.ExchangeVersionType;
import com.microsoft.schemas.exchange.services._2006.types.ItemType;
import com.microsoft.schemas.exchange.services._2006.types.LegacyFreeBusyType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfAllItemsType;
import com.microsoft.schemas.exchange.services._2006.types.NonEmptyArrayOfAttendeesType;
import com.microsoft.schemas.exchange.services._2006.types.PhoneNumberDictionaryEntryType;
import com.microsoft.schemas.exchange.services._2006.types.PhoneNumberDictionaryType;
import com.microsoft.schemas.exchange.services._2006.types.PhoneNumberKeyType;
import com.microsoft.schemas.exchange.services._2006.types.RequestServerVersion;
import com.microsoft.schemas.exchange.services._2006.types.ResponseClassType;
import com.microsoft.schemas.exchange.services._2006.types.ServerVersionInfo;
import com.microsoft.schemas.exchange.services._2006.types.TargetFolderIdType;

/**
 * @author Jordi Anguela
 */
public class ExchangeClient {
	private static Logger LOGGER = Logger.getLogger(ExchangeClient.class.getName());
	
	private ExchangeServices exchangeServices;
	private ExchangeServicePortType exchangeServicePort;
	
	
	public ExchangeClient(String username, String password, String serverLocation) throws Exception {
		ExchangeServerPortFactory exchangeServer = new ExchangeServerPortFactory();
		exchangeServer.setUid(username);
		exchangeServer.setPwd(password);
		
		java.net.URL wsdlURl;
		if ( Constants.EXCHANGE_SERVER_LOCATION_APAC.equals(serverLocation) ) {
			wsdlURl = org.bonitasoft.connectors.microsoft.exchange.common.ExchangeClient.class.getResource("Services_apac.wsdl");
		} 
		else if ( Constants.EXCHANGE_SERVER_LOCATION_EMEA.equals(serverLocation) ) {
			wsdlURl = org.bonitasoft.connectors.microsoft.exchange.common.ExchangeClient.class.getResource("Services_emea.wsdl");
		} 
		else { // We take by default North America Server Location
			wsdlURl = org.bonitasoft.connectors.microsoft.exchange.common.ExchangeClient.class.getResource("Services_northAmerica.wsdl");
		}
		
		String localModifiedWsdlUri = wsdlURl.toString();
		exchangeServer.setUri(localModifiedWsdlUri);
		
		exchangeServices = exchangeServer.getExchangeServices();
		exchangeServicePort = exchangeServices.getExchangeServicePort();
	}

	public ExchangeResponse createContact(String givenName, 
										  String surname, 
										  String companyName, 
										  String jobTitle, 
										  String email, 
										  String phonenumber) throws Exception {
		
		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("creating Contact with parameters= givenName:" + givenName +
														", surname:" + surname +
														", companyName:" + companyName +
														", jobTitle:" + jobTitle +
														", email:" + email +
														", phonenumber:" + phonenumber);
		}
		
		// Add contact information
		ContactItemType contact = new ContactItemType();
		contact.setFileAs(givenName + " " + surname);
		contact.setGivenName(givenName);
		contact.setSurname(surname);
		contact.setCompanyName(companyName);
		contact.setJobTitle(jobTitle);
		
		EmailAddressDictionaryEntryType emailAddress = new EmailAddressDictionaryEntryType(); //Email purpose
		emailAddress.setKey(EmailAddressKeyType.EMAIL_ADDRESS_1);
		emailAddress.setValue(email);
		EmailAddressDictionaryType emailAddressEntry = new EmailAddressDictionaryType();
		emailAddressEntry.getEntry().add(emailAddress);
		contact.setEmailAddresses(emailAddressEntry);
		
		PhoneNumberDictionaryEntryType phonenumberEntry = new PhoneNumberDictionaryEntryType();
		phonenumberEntry.setKey(PhoneNumberKeyType.PRIMARY_PHONE);
		phonenumberEntry.setValue(phonenumber);
		PhoneNumberDictionaryType phonenumbers = new PhoneNumberDictionaryType();
		phonenumbers.getEntry().add(phonenumberEntry);
		contact.setPhoneNumbers(phonenumbers);

		ExchangeResponse exchangeResponse = createItem(contact, DistinguishedFolderIdNameType.CONTACTS);
		
		return exchangeResponse;       
	}
	
	public ExchangeResponse createCalendarItem(String subject,
											   String body,
											   Date startDate,
											   Date endDate,
											   String location,
											   List<String> requiredAttendeesEmails) throws ParseException, DatatypeConfigurationException {


		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("creating CalendarItem with parameters= subject:" + subject +
															", body:" + body +
															", startDate:" + startDate +
															", endDate:" + endDate +
															", location:" + location +
															", requiredAttendeesEmails:" + requiredAttendeesEmails);
		}
		
		// Add CalendarItem information
		CalendarItemType newCalendarItem = new CalendarItemType();
		newCalendarItem.setSubject(subject);
		
		BodyType bodyType = new BodyType();
		bodyType.setBodyType(BodyTypeType.HTML);
		bodyType.setValue(body);
		newCalendarItem.setBody(bodyType);
		
		if (startDate != null) {
			GregorianCalendar startCalendar = new GregorianCalendar();
			startCalendar.setTime(startDate);
			XMLGregorianCalendar startXmlGC = DatatypeFactory.newInstance().newXMLGregorianCalendar(startCalendar);
			newCalendarItem.setStart(startXmlGC);
		}
		
		if (endDate != null) {
			GregorianCalendar endCalendar = new GregorianCalendar();
			endCalendar.setTime(endDate);
			XMLGregorianCalendar endXmlGC = DatatypeFactory.newInstance().newXMLGregorianCalendar(endCalendar);
			newCalendarItem.setEnd(endXmlGC);
		}
		
		// newCalendarItem.setTimeZone();

		newCalendarItem.setLocation(location);
		
		NonEmptyArrayOfAttendeesType arrayOfAttendees = new NonEmptyArrayOfAttendeesType();
		for (String requiredAttendeeEmail : requiredAttendeesEmails) {
			AttendeeType attendee = new AttendeeType();
			EmailAddressType emailAddress = new EmailAddressType();
			emailAddress.setEmailAddress(requiredAttendeeEmail);
			attendee.setMailbox(emailAddress);
			arrayOfAttendees.getAttendee().add(attendee);
		}
		newCalendarItem.setRequiredAttendees(arrayOfAttendees);
		
		// Fixed parameters
		newCalendarItem.setReminderIsSet(true);
		newCalendarItem.setReminderMinutesBeforeStart("60");
		newCalendarItem.setIsAllDayEvent(false);
		newCalendarItem.setLegacyFreeBusyStatus(LegacyFreeBusyType.BUSY);
		
		ExchangeResponse exchangeResponse = createItem(newCalendarItem, DistinguishedFolderIdNameType.CALENDAR);
		
		return exchangeResponse;          
	}
	
	public ExchangeResponse createItem(ItemType newItem, DistinguishedFolderIdNameType itemType) {
		
		ExchangeResponse exchangeResponse = new ExchangeResponse();
		
		CreateItemType request = new CreateItemType();
		
		NonEmptyArrayOfAllItemsType items = new NonEmptyArrayOfAllItemsType();
		items.getItemOrMessageOrCalendarItem().add(newItem);
		request.setItems(items);

		if (DistinguishedFolderIdNameType.CALENDAR.equals(itemType)) {
			request.setSendMeetingInvitations(CalendarItemCreateOrDeleteOperationType.SEND_ONLY_TO_ALL);
		}
		
		// Set destination Folder
		TargetFolderIdType targetType = new TargetFolderIdType(); 
		// folder id purpose
		DistinguishedFolderIdType dIdType = new DistinguishedFolderIdType();
		dIdType.setId(itemType);
		targetType.setDistinguishedFolderId(dIdType);
		request.setSavedItemFolderId(targetType);
		
		CreateItemResponseType createItemResponseType = new CreateItemResponseType();
		Holder<CreateItemResponseType> createItemResult = new Holder<CreateItemResponseType>(createItemResponseType);

		ServerVersionInfo serverVersionInfo = new ServerVersionInfo();
		serverVersionInfo.setMajorVersion(Constants.MAJOR_VERSION);
		serverVersionInfo.setMinorVersion(Constants.MINOR_VERSION);
		serverVersionInfo.setMajorBuildNumber(Constants.MAJOR_BUILD_VERSION);
		serverVersionInfo.setMinorBuildNumber(Constants.MINOR_BUILD_VERSION);
		serverVersionInfo.setVersion(Constants.EXCHANGE_VERSION);
		Holder<ServerVersionInfo> serverVersion = new Holder<ServerVersionInfo>(serverVersionInfo);
		
		RequestServerVersion requestVersion = new RequestServerVersion();
		requestVersion.setVersion(ExchangeVersionType.EXCHANGE_2007);
		
		exchangeServicePort.createItem(request, requestVersion , createItemResult, serverVersion);
		
		try {
			CreateItemResponseType response = createItemResult.value;
			ArrayOfResponseMessagesType responseMessages = response.getResponseMessages();
			List<JAXBElement<? extends ResponseMessageType>> jaxbElements = responseMessages.getCreateItemResponseMessageOrDeleteItemResponseMessageOrGetItemResponseMessage();
			for (JAXBElement<? extends ResponseMessageType> jaxbElement : jaxbElements) {
				ItemInfoResponseMessageType itemInfoResponseMessage = (ItemInfoResponseMessageType) jaxbElement.getValue();
				ResponseClassType responseClass = itemInfoResponseMessage.getResponseClass();
				String responseClassValue = responseClass.value();
				String responseCode = itemInfoResponseMessage.getResponseCode();
				String newContactId = "";
				try {
					newContactId = itemInfoResponseMessage.getItems().getItemOrMessageOrCalendarItem().iterator().next().getItemId().getId();
				} catch(Exception e) {
				}
				exchangeResponse.setStatusCode(responseCode);
				exchangeResponse.setStatusText(responseClassValue);
				exchangeResponse.setResponse(newContactId);
				if(LOGGER.isLoggable(Level.INFO)){
					LOGGER.info("New Response --> statusCode:" + exchangeResponse.getStatusCode() + 
											   ", statusText:" + exchangeResponse.getStatusText() + 
											    ", newItemId:" + exchangeResponse.getResponse());
				}
			}
		} catch(Exception e) {
			LOGGER.warning("could not parse createItem '" + itemType + "' response. Message: " + e.getMessage());
		}
		
		return exchangeResponse;         
	}
	
	public static Date validateStringDate(String date) {
		Date newDate = null;
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		TimeZone systemTimeZone = Calendar.getInstance().getTimeZone();
		dateFormatter.setTimeZone(systemTimeZone);
		try {
			newDate = dateFormatter.parse(date);
		} catch (ParseException e) {
			LOGGER.warning("date:'" + date + "' cannot be parsed with format:'" + dateFormatter.toString() + "'");
		}
		return newDate;
	}
}
