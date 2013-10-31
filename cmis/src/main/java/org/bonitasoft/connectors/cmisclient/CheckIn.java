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
import org.apache.chemistry.opencmis.client.api.ObjectId;
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
public class CheckIn extends AbstractCmisConnector {

  private String pWCDocumentID;
  private boolean isMajorVersion;
  private String versionComment;
  private String documentID;

  private static final Log LOGGER = LogFactory.getLog(CheckIn.class.getClass());

  @Override
  protected void executeConnector() throws Exception {
    documentID = this.checkInDocument();
  }

  @Override
  protected List<ConnectorError> validateValues() {
    return null;
  }

  public String checkInDocument() throws Exception {
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
    // String checkinComment = "lyy check in comment";
    // Boolean major = true;
    // Map<String,String> map = new HashMap<String,String>();
    // map.put("cmis:versionLabel", "1.1");
    // map.put(PropertyIds.CHECKIN_COMMENT, "map check in comment");
    // map.put(PropertyIds.VERSION_LABEL, "1.2");
    // System.out.println("document check in id = " + document.checkIn(major, map, null, checkinComment, null, null,
    // null).getId());
    final ObjectId objectId = document.checkIn(isMajorVersion, null, null, versionComment, null, null, null);
    return objectId.getId();

  }

  public void setPWCDocumentID(final String pWCDocumentID) {
    this.pWCDocumentID = pWCDocumentID;
  }

  public void setIsMajorVersion(final Boolean isMajorVersion) {
    this.isMajorVersion = isMajorVersion;
  }

  public void setVersionComment(final String versionComment) {
    this.versionComment = versionComment;
  }

  public String getDocumentId() {
    return documentID;
  }

}
