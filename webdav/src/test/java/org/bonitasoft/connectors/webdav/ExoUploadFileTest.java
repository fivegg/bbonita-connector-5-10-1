/**
 * Copyright (C) 2011 BonitaSoft S.A.
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
package org.bonitasoft.connectors.webdav;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.bonitasoft.connectors.webdav.common.ExoConnectorTestUtil;
import org.bonitasoft.connectors.webdav.exo.ExoUploadFile;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Yanyan Liu
 * 
 */
public class ExoUploadFileTest extends TestCase {

    private static final Logger LOG = Logger.getLogger(ExoUploadFileTest.class.getName());

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        if (LOG.isLoggable(Level.WARNING)) {
            LOG.warning("======== Starting test: " + this.getClass().getName() + "." + this.getName() + "() ==========");
        }
    }

    @Override
    protected void tearDown() throws Exception {
        if (LOG.isLoggable(Level.WARNING)) {
            LOG.warning("======== Ending test: " + this.getName() + " ==========");
        }
        super.tearDown();
    }

    /**
     * test upload file to server
     * 
     * @throws Exception
     */
    public void testUplosdFile() throws Exception {
        final ExoUploadFile exoUploadFile = getExoUploadFileWorker();
        List<ConnectorError> errorList = new ArrayList<ConnectorError>();
        exoUploadFile.validateUri(errorList, ExoConnectorTestUtil.getProperty("uploadFileUri"), "uploadFileUri");
        assertTrue(errorList.size() == 0);
        errorList = exoUploadFile.validate();
        assertNotNull(errorList);
        assertEquals(0, errorList.size());
        exoUploadFile.execute();
        // judge the returned status code. 201 if success.
        assertEquals("201", exoUploadFile.getStatusCode());
    }

    /**
     * 
     * @return ExoUploadFile
     */
    private ExoUploadFile getExoUploadFileWorker() {
        final ExoUploadFile exoUploadFile = new ExoUploadFile();

        final String host = ExoConnectorTestUtil.getProperty("host");
        final Long port = new Long(ExoConnectorTestUtil.getProperty("port"));
        final String username = ExoConnectorTestUtil.getProperty("username");
        final String password = ExoConnectorTestUtil.getProperty("password");

        final String destinationUri = ExoConnectorTestUtil.getProperty("uploadFileUri");
        final String filePath = ExoConnectorTestUtil.getProperty("filePath");
        final String contentType = ExoConnectorTestUtil.getProperty("contentType");

        exoUploadFile.setHost(host);
        exoUploadFile.setPort(port);
        exoUploadFile.setUsername(username);
        exoUploadFile.setPassword(password);

        exoUploadFile.setDestinationUri(destinationUri);
        exoUploadFile.setFile(filePath);
        exoUploadFile.setContentType(contentType);

        return exoUploadFile;
    }
}
