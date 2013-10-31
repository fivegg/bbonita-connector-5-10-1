/**
 * Copyright (C) 2010 BonitaSoft S.A.
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

package org.bonitasoft.connectors.xwiki;

import java.util.List;

import org.bonitasoft.connectors.xwiki.common.XWikiConnector;
import org.bonitasoft.connectors.xwiki.common.XWikiRestClient;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Ludovic Dubost
 */
public class UpdateMetadataConnector extends XWikiConnector {

	private String wikiName;
	private String spaceName;
	private String pageName;
	private String className;
	private String propertyName;
	private String propertyValue;

	@Override
	protected List<ConnectorError> validateFunctionParameters() {
	  return null;
	}

	@Override
	protected void executeFunction(XWikiRestClient xwikiClient) throws Exception {
		xwikiClient.updateMetadata(getWikiName(), getSpaceName(), getPageName(), getClassName(), getPropertyName(), getPropertyValue(), this);
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setWikiName(String wikiName) {
		this.wikiName = wikiName;
	}

	public String getWikiName() {
		return wikiName;
	}

	public void setSpaceName(String spaceName) {
		this.spaceName = spaceName;
	}

	public String getSpaceName() {
		return spaceName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getPageName() {
		return pageName;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}

        public static void main(String[] args) throws Exception {
           UpdateMetadataConnector conn = new UpdateMetadataConnector();
           conn.setHost("localhost");
           conn.setPort(8080);
           conn.setWikiName("xwiki");
           conn.setPageName("Test");
           conn.setSpaceName("Test");
           conn.setUsername("Admin");
           conn.setPassword("admin");
           conn.setClassName("XWiki.TagClass");
           conn.setPropertyName("tags");
           conn.setPropertyValue("toto");
           conn.executeConnector();
        }
}

