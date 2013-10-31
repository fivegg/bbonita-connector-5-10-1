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
import org.bonitasoft.connectors.webdav.exo.ExoCheckin;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * @author Yanyan Liu
 * 
 */
public class ExoCheckinTest extends TestCase {
    private static final Logger LOG = Logger.getLogger(ExoCheckinTest.class.getName());

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
     * test check in(here means save) the URI specified file to server.
     * 
     * @throws Exception
     */
    public void testExoCheckin() throws Exception {
        final ExoCheckin exoCheckin = getExoCheckinWorker();
        List<ConnectorError> errorList = new ArrayList<ConnectorError>();
        exoCheckin.validateUri(errorList, ExoConnectorTestUtil.getProperty("checkinFileUri"), "checkinFileUri");
        assertTrue(errorList.size() == 0);
        errorList = exoCheckin.validate();
        assertNotNull(errorList);
        assertEquals(0, errorList.size());
        exoCheckin.execute();
        // judge the returned status code. 200 if success.
        assertEquals("200", exoCheckin.getStatusCode());
    }

    /**
     * @return ExoCheckin
     */
    private ExoCheckin getExoCheckinWorker() {
        final ExoCheckin exoCheckin = new ExoCheckin();

        final String host = ExoConnectorTestUtil.getProperty("host");
        final Long port = new Long(ExoConnectorTestUtil.getProperty("port"));
        final String username = ExoConnectorTestUtil.getProperty("username");
        final String password = ExoConnectorTestUtil.getProperty("password");

        final String checkinFileUri = ExoConnectorTestUtil.getProperty("checkinFileUri");

        exoCheckin.setHost(host);
        exoCheckin.setPort(port);
        exoCheckin.setUsername(username);
        exoCheckin.setPassword(password);

        exoCheckin.setUri(checkinFileUri);

        return exoCheckin;
    }
}
