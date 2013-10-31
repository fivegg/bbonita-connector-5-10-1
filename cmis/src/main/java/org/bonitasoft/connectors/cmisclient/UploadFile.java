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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.bonitasoft.connectors.cmisclient.common.AbstractCmisConnector;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.facade.impl.StandardAPIAccessorImpl;

/**
 * 
 * @author Frederic Bouquet, Yanyan Liu
 * 
 */
public class UploadFile extends AbstractCmisConnector {

	private org.ow2.bonita.facade.runtime.AttachmentInstance file;
	private String fileName;
	private String destinationFolder;

	@Override
	protected void executeConnector() throws Exception {
		final StandardAPIAccessorImpl apiAccessor = new StandardAPIAccessorImpl();
		byte[] attachmentValue = apiAccessor.getQueryRuntimeAPI().getDocumentContent(file.getUUID());
		final ByteArrayInputStream inputStream = new ByteArrayInputStream(attachmentValue);
		uploadFileToFolder(username, password, url, binding_type,
				repositoryName, destinationFolder, inputStream, fileName);
		inputStream.close();
	}

	@Override
	protected List<ConnectorError> validateValues() {
		return null;
	}

	public void setFile(
			final org.ow2.bonita.facade.runtime.AttachmentInstance file) {
		this.file = file;
	}

	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	public void setDestinationFolder(final String destinationFolder) {
		this.destinationFolder = destinationFolder;
	}

	public void uploadFileToFolder(final String username,
			final String password, final String url, final String bindingType,
			final String repositoryName, final String path,
			final InputStream inputStream, final String name) {
		final Folder folder = getFolderByPath(username, password, url,
				bindingType, repositoryName, path);
		final Map<String, String> newDocProps = new HashMap<String, String>();
		newDocProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:document");
		newDocProps.put(PropertyIds.NAME, name);
		final ContentStream contentStream = new ContentStreamImpl(name, null,
				"plain/text", inputStream);
		folder.createDocument(newDocProps, contentStream, null, null, null,
				null, session.getDefaultContext());

	}
}
