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
package org.bonitasoft.connectors.xwiki.common;

import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.*;
import org.apache.commons.httpclient.methods.*;

import org.xwiki.rest.model.jaxb.Property;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

/**
 * 
 * @author Jordi Anguela
 *
 */
public class XWikiRestClient {
  private static Logger LOGGER = Logger.getLogger(XWikiRestClient.class.getName());

  private String server; // host + port
  private String username;
  private String password;
  private String host;
  private int port;

  public XWikiRestClient(String host, int port, String user, String passw) {
    this.username = user;
    this.password = passw;
    this.host = host;
    this.port = port;
    this.server = "http://" + host + ":" + port;
  }	

  /**
   * Update an XWiki MetaData using REST
   * @throws IOException 
   * @throws HttpException 
   */
  @SuppressWarnings("deprecation")
  public boolean updateMetadata(String wikiName, String spaceName, String pageName, String className, String propertyName, String propertyValue, XWikiConnector conn) throws HttpException, IOException {
    try {
      if (LOGGER.isLoggable(Level.INFO)) {
        LOGGER.info("updateMetadata wikiName=" + wikiName + " spaceName=" + spaceName + " pageName=" + pageName + " className=" + className + " propertyName=" + propertyName + " value=" + propertyValue);
      }

      String uri = server + "/xwiki/rest/wikis/" + wikiName + "/spaces/" + spaceName + "/pages/" + pageName + "/objects/" + className + "/0/properties/" + propertyName;
      if (LOGGER.isLoggable(Level.INFO)) {
        LOGGER.info("PUT " + uri);
      }

      HttpClient httpClient = new HttpClient();
      JAXBContext context = JAXBContext.newInstance("org.xwiki.rest.model.jaxb");
      Unmarshaller unmarshaller = context.createUnmarshaller();

      httpClient.getParams().setAuthenticationPreemptive(true);
      Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
      httpClient.getState().setCredentials(new AuthScope(host, port, AuthScope.ANY_REALM), defaultcreds);

      PutMethod putMethod = new PutMethod(uri);
      putMethod.addRequestHeader("Accept", "application/xml");
      putMethod.setRequestHeader("Content-Type","text/plain");
      putMethod.setRequestBody(propertyValue);
      httpClient.executeMethod(putMethod);

      Object result = "";
      String returnString = putMethod.getResponseBodyAsString();
      if (LOGGER.isLoggable(Level.INFO)) {
        LOGGER.info("HTTP Result: " + returnString);
      }
      conn.setResponse(returnString); 
      try {
        Property prop = (Property) unmarshaller.unmarshal(new StreamSource( new StringReader(returnString)));
        result = prop.getValue();
        boolean status = result.equals(propertyValue);
        conn.setStatus(status);
        return status;
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error reading xwiki rest call result", e);
        conn.setStatus(false);
        return false;
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error in xwiki connector", e);
      conn.setStatus(false);
      conn.setResponse("updateMetadata failed with exception: " + e.getMessage());
      return false;
    }  
  } 

  /**
   * GET an XWiki MetaData using REST
   * @throws IOException 
   * @throws HttpException 
   */
  public String getMetadata(String wikiName, String spaceName, String pageName, String className, String propertyName, XWikiConnector conn) throws HttpException, IOException {
    try {
      if (LOGGER.isLoggable(Level.INFO)) {
        LOGGER.info("getMetadata wikiName=" + wikiName + " spaceName=" + spaceName + " pageName=" + pageName + " className=" + className + " propertyName=" + propertyName);
      }

      String uri = server + "/xwiki/rest/wikis/" + wikiName + "/spaces/" + spaceName + "/pages/" + pageName + "/objects/" + className + "/0/properties/" + propertyName;
      if (LOGGER.isLoggable(Level.INFO)) {
        LOGGER.info("GET " + uri);
      }

      HttpClient httpClient = new HttpClient();
      JAXBContext context = JAXBContext.newInstance("org.xwiki.rest.model.jaxb");
      Unmarshaller unmarshaller = context.createUnmarshaller();

      httpClient.getParams().setAuthenticationPreemptive(true);
      Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
      httpClient.getState().setCredentials(new AuthScope(host, port, AuthScope.ANY_REALM), defaultcreds);

      GetMethod getMethod = new GetMethod(uri);
      getMethod.addRequestHeader("Accept", "application/xml");
      httpClient.executeMethod(getMethod);

      Object result = "";
      String returnString = getMethod.getResponseBodyAsString();
      if (LOGGER.isLoggable(Level.INFO)) {
        LOGGER.info("HTTP Result: " + returnString);
      }
      conn.setResponse(returnString); 
      try {
        Property prop = (Property) unmarshaller.unmarshal(new StreamSource( new StringReader(returnString)));
        result = prop.getValue();
        conn.setStatus(true);
        if (result==null)
          return null;
        else
          return result.toString(); 
      } catch (Exception e) {
        LOGGER.log(Level.SEVERE, "Error reading xwiki rest call result", e);
        conn.setStatus(false);
        return null; 
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error in xwiki connector", e);
      conn.setStatus(false);
      conn.setResponse("getMetadata failed with exception: " + e.getMessage());
      return null;
    }  
  } 
}




