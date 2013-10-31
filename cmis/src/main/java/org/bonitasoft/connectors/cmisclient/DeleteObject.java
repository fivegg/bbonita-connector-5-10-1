/**
 * Copyright (C) 2010-2011 BonitaSoft S.A.
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

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.bonitasoft.connectors.cmisclient.common.AbstractCmisConnector;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Frederic Bouquet, Yanyan Liu
 * 
 */
public class DeleteObject extends AbstractCmisConnector {

  private String objectPath;

  @Override
  protected void executeConnector() throws Exception {
    deleteObjectByPath(username, password, url, binding_type,
        repositoryName, objectPath);
  }

  @Override
  protected List<ConnectorError> validateValues() {
    return null;
  }

  public void setObjectPath(final String objectPath) {
    this.objectPath = objectPath;
  }

  protected void deleteObjectByPath(final String username,
      final String password, final String url, final String bindingType,
      final String repositoryName, final String path) {
    final Session s = this.createSessionByName(username, password, url,
        bindingType, repositoryName);
    final CmisObject object = s.getObjectByPath(path);
    /*
     * judge. if the object is folder, invoke deleteTree
     */
    if(object instanceof Folder){
      Folder folder = (Folder) object;
      folder.deleteTree(true, UnfileObject.DELETE, false);
      return;
    }
    object.delete(true);
  }
}
