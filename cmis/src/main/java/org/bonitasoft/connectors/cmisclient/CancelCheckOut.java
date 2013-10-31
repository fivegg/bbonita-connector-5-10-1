/**
 * Copyright (C) 2011-2012 BonitaSoft S.A.
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
package org.bonitasoft.connectors.cmisclient;

import java.util.List;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bonitasoft.connectors.cmisclient.common.AbstractCmisConnector;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Yanyan Liu
 * 
 */
public class CancelCheckOut extends AbstractCmisConnector {

  private String pWCDocumentID;
  private boolean isSucceed;

  private static final Log LOGGER = LogFactory.getLog(CheckIn.class.getClass());

  @Override
  protected void executeConnector() throws Exception {
    isSucceed = this.cancelCheckOutDocument();
  }

  @Override
  protected List<ConnectorError> validateValues() {
    return null;
  }

  /**
   * cancel check out pWCDocumentID specified document
   * 
   * @return
   * @throws Exception
   */
  public boolean cancelCheckOutDocument() throws Exception {
    final Session s = this.createSessionByName(username, password, url, binding_type, repositoryName);
    Document document = null;
    try {
      document = (Document) s.getObject(s.createObjectId(pWCDocumentID));
    } catch (final CmisObjectNotFoundException e1) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(e1.getMessage());
      }
      throw e1;
    } catch (final Exception e) {
      if (LOGGER.isErrorEnabled()) {
        LOGGER.error(e.getMessage());
      }
      throw e;
    }
    document.cancelCheckOut();
    return true;
  }

  /**
   * set the PWC document id
   */
  public void setPWCDocumentID(final String pWCDocumentID) {
    this.pWCDocumentID = pWCDocumentID;
  }

  public Boolean getIsSucceed() {
    return isSucceed;
  }

}
