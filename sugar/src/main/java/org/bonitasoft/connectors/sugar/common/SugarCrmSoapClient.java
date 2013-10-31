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

package org.bonitasoft.connectors.sugar.common;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;
import javax.xml.rpc.ServiceException;

import org.bonitasoft.connectors.sugar.common.soap.v2.Entry_value;
import org.bonitasoft.connectors.sugar.common.soap.v2.Field;
import org.bonitasoft.connectors.sugar.common.soap.v2.Get_entry_list_result_version2;
import org.bonitasoft.connectors.sugar.common.soap.v2.Get_server_info_result;
import org.bonitasoft.connectors.sugar.common.soap.v2.Link_name_to_fields_array;
import org.bonitasoft.connectors.sugar.common.soap.v2.Link_name_value;
import org.bonitasoft.connectors.sugar.common.soap.v2.Module_list;
import org.bonitasoft.connectors.sugar.common.soap.v2.Name_value;
import org.bonitasoft.connectors.sugar.common.soap.v2.New_module_fields;
import org.bonitasoft.connectors.sugar.common.soap.v2.New_set_entry_result;
import org.bonitasoft.connectors.sugar.common.soap.v2.Sugarsoap;
import org.bonitasoft.connectors.sugar.common.soap.v2.SugarsoapLocator;
import org.bonitasoft.connectors.sugar.common.soap.v2.SugarsoapPortType;
import org.bonitasoft.connectors.sugar.common.soap.v2.User_auth;
import org.ow2.bonita.connector.core.ProcessConnector;

/**
 * 
 * @author Jordi Anguela, Yanyan Liu.
 * 
 */
public abstract class SugarCrmSoapClient extends ProcessConnector {

  private static final Logger LOG = Logger.getLogger(SugarCrmSoapClient.class.getName());

  private SugarsoapPortType port;
  private String sessionId;
  private String userId;

  /**
   * initiate sugarCRM soap client
   * 
   * @param sugarsoapPort
   * @param user
   * @param password
   * @param applicationName
   * @throws MalformedURLException
   * @throws ServiceException
   * @throws RemoteException
   * @throws LoginException
   * @throws Exception
   */
  public void initSugarCrmSoapClient(final String sugarsoapPort, final String user, final String password,
      final String applicationName) throws MalformedURLException, ServiceException, RemoteException, LoginException,
      Exception {

    final Sugarsoap service = new SugarsoapLocator();
    port = service.getsugarsoapPort(new URL(sugarsoapPort));

    // Get server info checking that we have correctly connect
    final Get_server_info_result serverInfo = port.get_server_info();

    if (LOG.isLoggable(Level.INFO)) {
      LOG.info("Server info: GMT_time=" + serverInfo.getGmt_time() + " sugarCrm_version=" + serverInfo.getVersion());
    }

    final User_auth userAuth = new User_auth();
    userAuth.setUser_name(user);
    final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    final String encodedPassword = getHexString(messageDigest.digest(password.getBytes()));
    userAuth.setPassword(encodedPassword);

    try {
      final Entry_value loginRes = port.login(userAuth, applicationName, null);
      sessionId = loginRes.getId();
      userId = port.get_user_id(sessionId);

      if (LOG.isLoggable(Level.INFO)) {
        LOG.info("User ID: " + userId + " logged with sessionId: " + sessionId);
      }
    } catch (final RemoteException e) {
      if (sessionId != null) {
        try {
          logout();
        } catch (final Exception e1) {
          LOG.warning("Error occured when logout!");
        }
      }
      throw new LoginException("Error during login user : " + user);
    }
  }

  /**
   * @return String
   */
  public String getUserId() {
    return userId;
  }

  /**
   * do query and get entry list
   * 
   * @param module
   * @param query
   * @param orderBy
   * @param fieldsToRetrieve
   * @param linkNameToFieldsArray
   * @return List<List<Object>>
   * @throws Exception
   */
  public List<List<Object>> getEntryList(final String module, final String query, final String orderBy,
      final String[] fieldsToRetrieve, final Link_name_to_fields_array[] linkNameToFieldsArray) throws Exception {
    if (LOG.isLoggable(Level.INFO)) {
      LOG.info("Getting entries from Module: " + module + " query: " + query);
    }
    final int offset = 0;
    final int maxResults = Integer.MAX_VALUE;
    final int getDeleted = 0;

    final Get_entry_list_result_version2 resultEntryList = port.get_entry_list(sessionId, module, query, orderBy,
        offset, fieldsToRetrieve, linkNameToFieldsArray, maxResults, getDeleted);

    final List<List<Object>> responseEntryList = new ArrayList<List<Object>>();

    final Entry_value[] entryList = resultEntryList.getEntry_list();
    final Link_name_value[][] linkNameValue = resultEntryList.getRelationship_list();
    for (int i = 0; i < entryList.length; i++) {
      final List<Object> retrievedEntryFields = new ArrayList<Object>();
      final Name_value[] nvl = entryList[i].getName_value_list();
      for (int a = 0; a < fieldsToRetrieve.length; a++) {
        retrievedEntryFields.add(nvl[a].getValue());
        if (LOG.isLoggable(Level.INFO)) {
          LOG.info(nvl[a].getValue() + " - ");
        }
      }
      if (LOG.isLoggable(Level.INFO)) {
        LOG.info("\n");
      }
      List<Object> retrievedLinks = null;
      // add if sentence to avoid java.lang.ArrayIndexOutOfBoundsException
      if (linkNameValue != null && linkNameValue.length > 0) {
        for (int j = 0; j < linkNameValue[i].length; j++) {
          final Name_value[][] records = linkNameValue[i][j].getRecords();
          retrievedLinks = new ArrayList<Object>();

          for (int x = 0; x < records.length; x++) {
            final List<Object> retrievedLinkFields = new ArrayList<Object>();
            if (LOG.isLoggable(Level.INFO)) {
              LOG.info("      ");
            }
            for (int y = 0; y < records[x].length; y++) {
              retrievedLinkFields.add(records[x][y].getValue());
              if (LOG.isLoggable(Level.INFO)) {
                LOG.info(records[x][y].getValue() + " - ");
              }
            }
            retrievedLinks.add(retrievedLinkFields);
            if (LOG.isLoggable(Level.INFO)) {
              LOG.info("\n");
            }
          }
          retrievedEntryFields.add(retrievedLinks);
        }
      }
      if (retrievedLinks == null) {
        // If there are not links set and empty array. Necessary when you have several links to retrieve
        retrievedEntryFields.add(new ArrayList<Object>());
      }
      responseEntryList.add(retrievedEntryFields);
    }

    return responseEntryList;

  }

  /**
   * insert a record to the specified module
   * 
   * @param module
   * @param nameValueList
   * @return the entry Id
   * @throws Exception
   */
  public String setEntry(final String module, final Name_value[] nameValueList) throws Exception {
    if (LOG.isLoggable(Level.INFO)) {
      LOG.info("Setting Module: " + module);
    }

    final New_set_entry_result result = port.set_entry(sessionId, module, nameValueList);
    final String returnedId = result.getId();

    if (LOG.isLoggable(Level.INFO)) {
      LOG.info("Returned id: " + returnedId);
    }
    return returnedId;
  }

  /**
   * just get available modules
   * 
   * @throws Exception
   */
  public void getAvaliableModules() throws Exception {
    final Module_list modules = port.get_available_modules(sessionId);
    final String[] moduleNames = modules.getModules();
    for (int i = 0; i < moduleNames.length; i++) {
      if (LOG.isLoggable(Level.INFO)) {
        LOG.info("Module : " + moduleNames[i]);
      }
    }
  }

  /**
   * just get fields for the specified module
   * 
   * @param moduleName
   * @throws Exception
   */
  public void getModuleFields(final String moduleName) throws Exception {

    if (LOG.isLoggable(Level.INFO)) {
      LOG.info("--- " + moduleName + " fields -----------------------------------------------------------");
    }
    final New_module_fields moduleFields = port.get_module_fields(sessionId, moduleName, null);
    final Field[] fields = moduleFields.getModule_fields();
    for (int i = 0; i < fields.length; i++) {
      if (LOG.isLoggable(Level.INFO)) {
        LOG.info("Field = Name: " + fields[i].getName() + " -- Label: " + fields[i].getLabel() + " -- Type: "
            + fields[i].getType() + " -- Required: " + fields[i].getRequired());
      }
    }
  }

  /**
   * just get relationships between modules
   * 
   * @param moduleName
   * @param moduleId
   * @param relatedModule
   * @param relatedModuleQuery
   * @throws Exception
   */
  public void getRelationships(final String moduleName, final String moduleId, final String relatedModule,
      final String relatedModuleQuery) throws Exception {
    if (LOG.isLoggable(Level.INFO)) {
      LOG.info("--- " + moduleName + ":" + moduleId + " relationships with " + relatedModule
          + " -----------------------------------------------------------");
    }
    final String linkFieldName = "";
    final Link_name_to_fields_array[] relatedModuleLinkNameToFieldsArray = null;
    final String[] relatedFields = null;
    port.get_relationships(sessionId, moduleName, moduleId, linkFieldName, relatedModuleQuery, relatedFields,
        relatedModuleLinkNameToFieldsArray, 0);
  }

  /**
   * 
   * @throws Exception
   */
  public void logout() throws Exception {
    port.logout(sessionId);
  }

  /**
   * convert byte[] to Hex String
   * 
   * @param data
   * @return
   * @throws Exception
   */
  private String getHexString(final byte[] data) throws Exception {
    final StringBuffer stringBuffer = new StringBuffer();
    for (int i = 0; i < data.length; i++) {
      int halfbyte = data[i] >>> 4 & 0x0F;
      int twoHalfs = 0;
      do {
        if (0 <= halfbyte && halfbyte <= 9) {
          stringBuffer.append((char) ('0' + halfbyte));
        } else {
          stringBuffer.append((char) ('a' + (halfbyte - 10)));
        }
        halfbyte = data[i] & 0x0F;
      } while (twoHalfs++ < 1);
    }
    return stringBuffer.toString();
  }
}
