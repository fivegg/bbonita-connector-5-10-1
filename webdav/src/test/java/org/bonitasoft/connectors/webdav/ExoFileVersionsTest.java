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
import org.bonitasoft.connectors.webdav.exo.ExoFileVersions;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Yanyan Liu
 * 
 */
public class ExoFileVersionsTest extends TestCase {
    private static final Logger LOG = Logger.getLogger(ExoFileVersionsTest.class.getName());

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
     * test listing versions for the specified file URI.
     * 
     * @throws Exception
     */
    public void testExoFileVersions() throws Exception {
        final ExoFileVersions exoFileVersions = getExoFileVersionsWorker();
        List<ConnectorError> errorList = new ArrayList<ConnectorError>();
        exoFileVersions.validateUri(errorList, ExoConnectorTestUtil.getProperty("listFileVersionUri"), "listFileVersionUri");
        assertTrue(errorList.size() == 0);
        errorList = exoFileVersions.validate();
        assertNotNull(errorList);
        assertEquals(0, errorList.size());
        exoFileVersions.execute();
        // judge the returned status code. 207 if success.
        assertEquals("207", exoFileVersions.getStatusCode());
    }

    /**
     * @return ExoFileVersions
     */
    private ExoFileVersions getExoFileVersionsWorker() {
        final ExoFileVersions exoFileVersions = new ExoFileVersions();

        final String host = ExoConnectorTestUtil.getProperty("host");
        final Long port = new Long(ExoConnectorTestUtil.getProperty("port"));
        final String username = ExoConnectorTestUtil.getProperty("username");
        final String password = ExoConnectorTestUtil.getProperty("password");

        final String listFileVersionUri = ExoConnectorTestUtil.getProperty("listFileVersionUri");

        exoFileVersions.setHost(host);
        exoFileVersions.setPort(port);
        exoFileVersions.setUsername(username);
        exoFileVersions.setPassword(password);

        exoFileVersions.setUri(listFileVersionUri);

        return exoFileVersions;
    }
}
