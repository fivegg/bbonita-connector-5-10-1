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

package org.bonitasoft.connectors.cmisclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.Document;
import org.bonitasoft.connectors.cmisclient.common.AbstractCmisConnector;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Frederic Bouquet
 * 
 */
public class DownloadDocument extends AbstractCmisConnector {

	private String documentPath;
	private String destinationFileName;

	@Override
	protected void executeConnector() throws Exception {
		downloadFileByPath(username, password, url, binding_type,
				repositoryName, documentPath, destinationFileName);
	}

	@Override
	protected List<ConnectorError> validateValues() {
		final List<ConnectorError> errors = new ArrayList<ConnectorError>();
		final File destinationFile = new File(destinationFileName);
		if (destinationFile.isDirectory()) {
			errors.add(new ConnectorError("destinationFileName", new Exception(
					"Can't be a directory")));
		}

		return errors;
	}

	public void setDocumentPath(final String documentPath) {
		this.documentPath = documentPath;
	}

	public void setDestinationFileName(final String destinationFileName) {
		this.destinationFileName = destinationFileName;
	}

	protected File downloadFileByPath(final String username,
			final String password, final String url, final String bindingType,
			final String repositoryName, final String path,
			final String destinationPath) throws IOException {
		final Document d = (Document) getObjectByPath(username, password, url,
				bindingType, repositoryName, path);
		final File f = new File(destinationPath);

		final InputStream is = d.getContentStream().getStream();
		final FileOutputStream fos = new FileOutputStream(f);

		Integer b;
		while ((b = is.read()) != -1) {
			fos.write(b);
		}

		fos.close();
		is.close();

		return f;
	}
}
