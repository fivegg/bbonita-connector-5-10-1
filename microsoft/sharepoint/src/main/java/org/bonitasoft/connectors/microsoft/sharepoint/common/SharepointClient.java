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
package org.bonitasoft.connectors.microsoft.sharepoint.common;

import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.BindingProvider;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

import com.microsoft.schemas.sharepoint.soap.GetListItems;
import com.microsoft.schemas.sharepoint.soap.GetListItemsResponse;
import com.microsoft.schemas.sharepoint.soap.Lists;
import com.microsoft.schemas.sharepoint.soap.ListsSoap;
import com.microsoft.schemas.sharepoint.soap.Versions;
import com.microsoft.schemas.sharepoint.soap.VersionsSoap;


/**
 * @author Jordi Anguela
 */
public class SharepointClient {
	
	private static Logger LOGGER = Logger.getLogger(SharepointClient.class.getName());

	/**
	 * Creates a port connected to the SharePoint Lists Web Service given.
	 * Authentication is done here. 
	 * @param userName SharePoint username
	 * @param password SharePoint password
	 * @return port ListsSoap port, connected with SharePoint
	 * @throws Exception in case of invalid parameters or connection error.
	 */
	public ListsSoap sharePointListsAuth(String userName, String password) throws Exception {
	    ListsSoap port = null;
	    if (userName != null && password != null) {
	        try {
	        	URL wsdlLocation = org.bonitasoft.connectors.microsoft.sharepoint.common.SharepointClient.class.getResource("Lists.wsdl");
	            Lists service = new Lists(wsdlLocation);
	            port = service.getListsSoap();
	    		if (LOGGER.isLoggable(Level.INFO)) {
	    			LOGGER.info("LISTS Web Service Auth Username: " + userName);
	    		}
	            ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, userName);
	            ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
	        } catch (Exception e) {
	            throw new Exception("Error: " + e.toString());
	        }
	    } else {
	        throw new Exception("Couldn't authenticate: Invalid connection details given.");
	    }
	    return port;
	}
	
	/**
	 * Creates a port connected to the SharePoint Versions Web Service given.
	 * Authentication is done here. 
	 * @param userName SharePoint username
	 * @param password SharePoint password
	 * @return port VersionsSoap port, connected with SharePoint
	 * @throws Exception in case of invalid parameters or connection error.
	 */
	public VersionsSoap sharePointVersionsAuth(String userName, String password) throws Exception {
		VersionsSoap port = null;
	    if (userName != null && password != null) {
	        try {
	        	URL wsdlLocation = org.bonitasoft.connectors.microsoft.sharepoint.common.SharepointClient.class.getResource("Versions.wsdl");
	            Versions service = new Versions(wsdlLocation);
	            port = service.getVersionsSoap();
	    		if (LOGGER.isLoggable(Level.INFO)) {
	    			LOGGER.info("VERSIONS Web Service Auth Username: " + userName);
	    		}
	            ((BindingProvider) port).getRequestContext().put(BindingProvider.USERNAME_PROPERTY, userName);
	            ((BindingProvider) port).getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, password);
	        } catch (Exception e) {
	            throw new Exception("Error: " + e.toString());
	        }
	    } else {
	        throw new Exception("Couldn't authenticate: Invalid connection details given.");
	    }
	    return port;
	}

	/**
	 * Creates a string from an XML file with start and end indicators
	 * @param docToString document to convert
	 * @return string of the xml document
	 */
	public static String xmlToString(Document docToString) {
	    String returnString = "";
	    try {
	        //create string from xml tree
	        //Output the XML
	        //set up a transformer
	        TransformerFactory transfac = TransformerFactory.newInstance();
	        Transformer trans;
	        trans = transfac.newTransformer();
	        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        trans.setOutputProperty(OutputKeys.INDENT, "yes");
	        StringWriter sw = new StringWriter();
	        StreamResult streamResult = new StreamResult(sw);
	        DOMSource source = new DOMSource(docToString);
	        trans.transform(source, streamResult);
	        String xmlString = sw.toString();
	        //print the XML
	        returnString = returnString + xmlString;
	    } catch (TransformerException ex) {
	    	LOGGER.severe(ex.toString());
	    }
	    return returnString;
	}

	/**
	 * Connects to a SharePoint Lists Web Service through the given open port,
	 * and reads all the elements of the given list. Only the ID and the given
	 * attributes (column names) are displayed, as well as a dump of the SOAP
	 * response from the Web Service (for debugging purposes).
	 * @param port an already authentificated SharePoint Online SOAP port
	 * @param listName original name of the Sharepoint list that is going to be read
	 * @param listColumnNames arraylist containing the various names of the Columns
	 * of the SharePoint list that are going to be read. If the column name isn't
	 * found, then an exception will be thrown
	 * @param rowLimit limits the number of rows (list items) that are going to
	 * be returned
	 * @throws Exception
	 */
	public void displaySharePointList(ListsSoap port, String listName, ArrayList<String> listColumnNames, String rowLimit) throws Exception {
	    if (port != null && listName != null && listColumnNames != null && rowLimit != null) {
	        try {

	            //Here are additional parameters that may be set
	            String viewName = "";
	            GetListItems.ViewFields viewFields = null;
	            GetListItems.Query query = null;
	            GetListItems.QueryOptions queryOptions = null;
	            String webID = "";

	            //Calling the List Web Service
	            GetListItemsResponse.GetListItemsResult result = port.getListItems(listName, viewName, query, viewFields, rowLimit, queryOptions, webID);
	            Object listResult = result.getContent().get(0);
	            if ((listResult != null) && (listResult instanceof Element)) {
	            	Element node = (Element) listResult;

	                //Dumps the retrieved info in the console
	                Document document = node.getOwnerDocument();
	                LOGGER.info("SharePoint Online Lists Web Service Response:" + SharepointClient.xmlToString(document));

	                //selects a list of nodes which have z:row elements
	                NodeList list = node.getElementsByTagName("z:row");
	                LOGGER.info("=> " + list.getLength() + " results from SharePoint Online");

	                //Displaying every result received from SharePoint, with its ID
	                for (int i = 0; i < list.getLength(); i++) {

	                    //Gets the attributes of the current row/element
	                    NamedNodeMap attributes = list.item(i).getAttributes();
	                    LOGGER.info("******** Item ID: " + attributes.getNamedItem("ows_ID").getNodeValue()+" ********");

	                    //Displays all the attributes of the list item that correspond to the column names given
	                    for (String columnName : listColumnNames) {
	                        String internalColumnName = "ows_" + columnName;
	                        if (attributes.getNamedItem(internalColumnName) != null) {
	                        	LOGGER.info(columnName + ": " + attributes.getNamedItem(internalColumnName).getNodeValue());
	                        } else {
	                            throw new Exception("Couldn't find the '" + columnName + "' column in the '" + listName + "' list in SharePoint.\n");
	                        }
	                    }
	                }
	            } else {
	                throw new Exception(listName + " list response from SharePoint is either null or corrupt\n");
	            }
	        } catch (Exception ex) {
	            throw new Exception("Exception. See stacktrace." + ex.toString() + "\n");
	        }
	    }
	}
	
	/**
	 * Connects to a SharePoint Lists Web Service through the given open port,
	 * and reads all the elements of the given list. 
	 * @param port an already authentificated SharePoint Online SOAP port
	 * @param listName original name of the Sharepoint list that is going to be read
	 * @return a String representing the Document object of the SOAP response.
	 * @throws Exception
	 */
	public String getListItems(ListsSoap port, String listName) throws Exception {
		String xmlToStrinResult = "";
		
	    if (port != null && listName != null ) {
	        try {
	            //Here are additional parameters that may be set
	            String viewName = "";
	            GetListItems.ViewFields viewFields = null;
	            GetListItems.Query query = null;
	            GetListItems.QueryOptions queryOptions = null;
	            String webID = "";
	            String rowLimit = "";

	            //Calling the List Web Service
	            GetListItemsResponse.GetListItemsResult result = port.getListItems(listName, viewName, query, viewFields, rowLimit, queryOptions, webID);
	            Object listResult = result.getContent().get(0);
	            if ((listResult != null) && (listResult instanceof Element)) {
	                Element node = (Element) listResult;

	                //Dumps the retrieved info in the console
	                Document document = node.getOwnerDocument();
	                xmlToStrinResult = SharepointClient.xmlToString(document);
	                LOGGER.info("SharePoint Online Lists Web Service Response:" + xmlToStrinResult);
	            } else {
	            	xmlToStrinResult = listName + " list response from SharePoint is either null or corrupt";
	            }
	        } catch (Exception ex) {
	        	xmlToStrinResult = "Exception occurred.\nPosible cause: invalid 'listName' parameter.\nStacktrace: " + ex.toString();
	        }
	    }
	    return xmlToStrinResult;
	}
	
	/**
	 * Checks-out the specified file
	 * @param port Lists web service port
	 * @param pageUrl
	 * @return true if the operation succeeded; otherwise, false. 
	 */
	public boolean checkOutFile(ListsSoap port, String pageUrl) {
		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("Checking-out pageUrl=" + pageUrl);
		}
		String checkoutToLocal = "true";
		String lastModified    = "";
		boolean result = port.checkOutFile(pageUrl, checkoutToLocal, lastModified);
		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("Check-out result = " + result);
		}
		return result;
	}
	
	/**
	 * Undo checked-out file
	 * @param port Lists web service port
	 * @param pageUrl
	 * @return true if the operation succeeded; otherwise, false. 
	 */
	public boolean undoCheckOutFile(ListsSoap port, String pageUrl) {
		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("Undo checkout pageUrl=" + pageUrl);
		}
		boolean result = port.undoCheckOut(pageUrl);
		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("Undo checkout result = " + result);
		}
		return result;
	}
	
	/**
	 * Checks-in the specified file
	 * @param port Lists web service port
	 * @param pageUrl
	 * @param comment
	 * @return true if the operation succeeded; otherwise, false. 
	 */
	public boolean checkInFile(ListsSoap port, String pageUrl, String comment) {
		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("Checking-in pageUrl=" + pageUrl + " comment=" + comment);
		}
		// checkinType = values 0, 1 or 2, where 0 = MinorCheckIn, 1 = MajorCheckIn, and 2 = OverwriteCheckIn.
		String checkinType = "0";
		boolean result = port.checkInFile(pageUrl, comment, checkinType);
		if (LOGGER.isLoggable(Level.INFO)) {
			LOGGER.info("Check-in result = " + result);
		}
		return result;
	}

}
