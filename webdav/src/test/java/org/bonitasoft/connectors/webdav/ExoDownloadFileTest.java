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
import org.bonitasoft.connectors.webdav.exo.ExoDownloadFile;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Yanyan Liu
 * 
 */
public class ExoDownloadFileTest extends TestCase {
    private static final Logger LOG = Logger.getLogger(ExoDownloadFileTest.class.getName());

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
     * test download the URI specified file to the specified path with a specified name.
     * 
     * @throws Exception
     */
    public void testExoDownloadFile() throws Exception {
        final ExoDownloadFile exoDownloadFile = getExoDownloadFileWorker();
        List<ConnectorError> errorList = new ArrayList<ConnectorError>();
        exoDownloadFile.validateUri(errorList, ExoConnectorTestUtil.getProperty("downloadFileUri"), "downloadFileUri");
        assertTrue(errorList.size() == 0);
        errorList = exoDownloadFile.validate();
        assertNotNull(errorList);
        assertEquals(0, errorList.size());
        exoDownloadFile.execute();
        // judge the returned status code. 200 if success.
        assertEquals("200", exoDownloadFile.getStatusCode());
    }

    /**
     * @return ExoDownloadFile
     */
    private ExoDownloadFile getExoDownloadFileWorker() {
        final ExoDownloadFile exoDownloadFile = new ExoDownloadFile();

        final String host = ExoConnectorTestUtil.getProperty("host");
        final Long port = new Long(ExoConnectorTestUtil.getProperty("port"));
        final String username = ExoConnectorTestUtil.getProperty("username");
        final String password = ExoConnectorTestUtil.getProperty("password");

        final String downloadFileUri = ExoConnectorTestUtil.getProperty("downloadFileUri");
        final String outputFileFolder = ExoConnectorTestUtil.getProperty("outputFileFolder");
        final String outputFileName = ExoConnectorTestUtil.getProperty("outputFileName");

        exoDownloadFile.setHost(host);
        exoDownloadFile.setPort(port);
        exoDownloadFile.setUsername(username);
        exoDownloadFile.setPassword(password);

        exoDownloadFile.setFileUri(downloadFileUri);
        exoDownloadFile.setOutputFileFolder(outputFileFolder);
        exoDownloadFile.setOutputFileName(outputFileName);

        return exoDownloadFile;
    }
}
