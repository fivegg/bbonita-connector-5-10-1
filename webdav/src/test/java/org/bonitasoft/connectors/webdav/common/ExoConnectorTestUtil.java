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
package org.bonitasoft.connectors.webdav.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Yanyan Liu
 * 
 */
public class ExoConnectorTestUtil {
    private static final Logger LOG = Logger.getLogger(ExoConnectorTestUtil.class.getName());
    private static final File file = new File(ExoConnectorTestUtil.class.getClassLoader().getResource(".").getPath(), "config.properties");
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

    public static String getProperty(final String propertyName) {
        return properties.getProperty(propertyName);
    }
}
