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
package org.bonitasoft.connectors.sugar;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;
import javax.xml.rpc.ServiceException;

import org.bonitasoft.connectors.sugar.common.SugarCrmSoapClient;
import org.bonitasoft.connectors.sugar.common.soap.v2.Name_value;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Jordi Anguela, Yanyan Liu
 * 
 */
public class SetSugarEntry extends SugarCrmSoapClient {

  private static final Logger LOG = Logger.getLogger(SetSugarEntry.class.getName());

  // SugarConfiguration
  private String sugarSoapPort = "";
  private String applicationName = "";
  private String user = "";
  private String password = "";

  // Input Parameters
  private String module = "";
  private Name_value[] nameValueList = null; /* you can send an List<List<Object>> instead */

  // Output parameters
  private String returnedId = "";

  @Override
  protected List<ConnectorError> validateValues() {
    if (LOG.isLoggable(Level.INFO)) {
      LOG.info("validatingValues");
    }
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();

    testEmptyVar(sugarSoapPort, "sugarSoapPort", errors);
    testEmptyVar(applicationName, "applicationName", errors);
    testEmptyVar(user, "user", errors);
    testEmptyVar(module, "module", errors);

    if (nameValueList == null || nameValueList.length == 0) {
      errors.add(new ConnectorError("nameValueList", new IllegalArgumentException(
          "Cannot be empty. Must have a name-value attribute!")));
    }
    try {
      initSugarCrmSoapClient(sugarSoapPort, user, password, applicationName);
    } catch (final MalformedURLException e) {
      errors.add(new ConnectorError("sugarSoapPort", new MalformedURLException("URL not valid! " + e.getMessage())));
    } catch (final LoginException e) {
      errors.add(new ConnectorError("user", new LoginException("Wrong user or password! " + e.getMessage())));
    } catch (final RemoteException e) {
      errors.add(new ConnectorError("sugarSoapPort", new RemoteException("Error accessing Sugar services! "
          + e.getMessage())));
    } catch (final ServiceException e) {
      errors.add(new ConnectorError("sugarSoapPort", new ServiceException("Error accessing Sugar services! "
          + e.getMessage())));
    } catch (final Exception e) {
      errors.add(new ConnectorError("sugarSoapPort", new Exception("Exception occurred! " + e.getMessage())));
    }

    return errors;
  }

  @Override
  protected void executeConnector() throws Exception {
    returnedId = setEntry(module, nameValueList);
  }

  /**
   * validation
   * 
   * @param var
   * @param varName
   * @param errors
   */
  public void testEmptyVar(String var, final String varName, final List<ConnectorError> errors) {
    if (var != null) {
      var = var.trim();
      if (var.length() > 0) {
        return;
      }
    }
    errors.add(new ConnectorError(varName, new IllegalArgumentException("Cannot be empty!")));
  }

  /**
   * set the sugarSoapPort
   * 
   * @param sugarSoapPort
   *          the sugar SOAP port
   */
  public void setSugarSoapPort(final String sugarSoapPort) {
    this.sugarSoapPort = sugarSoapPort;
  }

  /**
   * set the applicationName
   * 
   * @param applicationName
   *          the application name
   */
  public void setApplicationName(final String applicationName) {
    this.applicationName = applicationName;
  }

  /**
   * set the user
   * 
   * @param user
   *          the user name
   */
  public void setUser(final String user) {
    this.user = user;
  }

  /**
   * set the password
   * 
   * @param password
   *          the password
   */
  public void setPassword(final String password) {
    this.password = password;
  }

  /**
   * set the module
   * 
   * @param module
   *          the SugarCRM module
   */
  public void setModule(final String module) {
    this.module = module;
  }

  /**
   * set the nameValueList
   * 
   * @param nameValueList
   *          the name value list
   */
  public void setNameValueList(final Name_value[] nameValueList) {
    if (nameValueList == null) {
      this.nameValueList = null;
    } else {
      this.nameValueList = new Name_value[nameValueList.length];
      System.arraycopy(nameValueList, 0, this.nameValueList, 0, nameValueList.length);
    }
  }

  /**
   * set the nameValueList
   * 
   * @param nameValueArrayList
   *          the name value array list
   */
  public void setNameValueList(final List<List<Object>> nameValueArrayList) {
    // Transform List to Name_value[]
    nameValueList = convertToNameValue(nameValueArrayList);
  }

  /**
   * convert List to Name_Value array
   * 
   * @param arrayList
   * @return
   */
  private final Name_value[] convertToNameValue(final List<List<Object>> arrayList) {
    Name_value[] nameValue = null;
    if (arrayList != null && arrayList.size() > 0) {
      nameValue = new Name_value[arrayList.size()];
      int i = 0;
      String name;
      String value;
      for (final List<Object> list : arrayList) {
        name = (String) list.get(0);
        value = (String) list.get(1);
        nameValue[i] = new Name_value(name, value);
        i++;
      }
    }
    return nameValue;
  }

  /**
   * @return String
   */
  public String getReturnedId() {
    return returnedId;
  }

}
