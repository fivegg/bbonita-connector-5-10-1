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
package org.bonitasoft.connectors.drools.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.bonitasoft.connectors.drools.DroolsClientConnectorTest;
import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.util.BonitaException;

/**
 * @author Yanyan Liu
 *
 */
public abstract class DroolsConnectorTest extends TestCase {
    private static final Logger LOG = Logger.getLogger(DroolsClientConnectorTest.class.getName());
    private static final File file = new File(DroolsClientConnectorTest.class.getClassLoader().getResource(".").getPath(), "config.properties");
    private static InputStream inputStream = null;
    protected static final Properties properties = new Properties();

    static {
        try {
            inputStream = new FileInputStream(file);
            properties.load(inputStream);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning(e.getMessage());
            }
        } catch (final IOException e) {
            e.printStackTrace();
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning(e.getMessage());
            }
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                    if (LOG.isLoggable(Level.WARNING)) {
                        LOG.warning(e.getMessage());
                    }
                }
        }
    }
    
    public static void configDroolsConnector(DroolsConnector droolsConnector){
//        droolsConnector.setHost(properties.getProperty("host"));
//        droolsConnector.setPort(properties.getProperty("port"));
        droolsConnector.setUsername(properties.getProperty("username"));
        droolsConnector.setPassword(properties.getProperty("password"));
        droolsConnector.setApiUrl(properties.getProperty("url"));
//        droolsConnector.setKsessionName(properties.getProperty("ksessionName"));        
    }
    
    public void testValidateConnector() throws BonitaException {
      Class<? extends Connector> connectorClass = getConnectorClass();
      List<ConnectorError> errors = Connector.validateConnector(connectorClass);
      for (ConnectorError error : errors) {
        System.out.println(error.getField() + " " + error.getError());
      }
      assertTrue(errors.isEmpty());
    }

    /**
     * @return
     */
    protected abstract Class<? extends Connector> getConnectorClass();
}
