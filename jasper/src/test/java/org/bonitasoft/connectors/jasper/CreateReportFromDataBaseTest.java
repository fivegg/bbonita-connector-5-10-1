/**
 * Created by Jordi Anguela, Yanyan Liu
 * 
 * Copyright (C) 2009-201 BonitaSoft S.A.
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

package org.bonitasoft.connectors.jasper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.util.BonitaException;

public class CreateReportFromDataBaseTest extends TestCase {
    protected static final Logger LOG = Logger.getLogger(CreateReportFromDataBaseTest.class.getName());
    private static final File file = new File(CreateReportFromDataBaseTest.class.getClassLoader().getResource(".").getPath(), "config.properties");
    private static InputStream inputStream = null;
    private static final Properties properties = new Properties();

    static {
        try {
            inputStream = new FileInputStream(file);
            properties.load(inputStream);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
            LOG.warning(e.getMessage());
        } catch (final IOException e) {
            e.printStackTrace();
            LOG.warning(e.getMessage());
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                    LOG.warning(e.getMessage());
                }
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        if (CreateReportFromDataBaseTest.LOG.isLoggable(Level.WARNING)) {
            CreateReportFromDataBaseTest.LOG.warning("======== Starting test: " + this.getClass().getName() + "." + this.getName() + "() ==========");
        }
    }

    @Override
    protected void tearDown() throws Exception {
        if (CreateReportFromDataBaseTest.LOG.isLoggable(Level.WARNING)) {
            CreateReportFromDataBaseTest.LOG.warning("======== Ending test: " + this.getName() + " ==========");
        }
        super.tearDown();
    }

    /**
     * test good parameters than will not cause fault
     * 
     * @throws BonitaException
     */
    public void testGoodParameters() throws BonitaException {
        final CreateReportFromDataBase connector = getWorkingConnector();
        final List<ConnectorError> validation = connector.validate();
        Assert.assertEquals(0, validation.size());
    }

    /**
     * test wrong database driver. make sure provide a wrong database driver
     * 
     * @throws BonitaException
     */
    public void testWrongDbDriver() throws BonitaException {
        final CreateReportFromDataBase connector = getWorkingConnector();
        final String wrongDbDriver = properties.getProperty("wrongDbDriver");
        connector.setDbDriver(wrongDbDriver);
        final List<ConnectorError> validation = connector.validate();
        Assert.assertEquals(1, validation.size());
        final ConnectorError error = validation.get(0);
        Assert.assertEquals("dbDriver", error.getField());
    }

    /**
     * test wrong jrxml file path. make sure provide a wrong file path in config.properties.
     * 
     * @throws BonitaException
     */
    public void testWrongJrxmlFilePath() throws BonitaException {
        final CreateReportFromDataBase connector = getWorkingConnector();
        final String wrongJrxmlFilePath = properties.getProperty("wrongJrxmlFilePath");
        connector.setJrxmlFilePath(wrongJrxmlFilePath);
        final List<ConnectorError> validation = connector.validate();
        Assert.assertEquals(1, validation.size());
        final ConnectorError error = validation.get(0);
        Assert.assertEquals("jrxmlFilePath", error.getField());
    }

    /**
     * test wrong JDBC Url. please provide a wrong JDBC Url in config.properties
     * 
     * @throws BonitaException
     */
    public void testWrongJdbcUrl() throws BonitaException {
        final CreateReportFromDataBase connector = getWorkingConnector();
        final String wrongJdbcUrl = properties.getProperty("wrongJdbcUrl");
        connector.setUser(wrongJdbcUrl);
        List<ConnectorError> validation = connector.validate();
        Assert.assertEquals(1, validation.size());
        final ConnectorError error = validation.get(0);
        Assert.assertEquals("jdbcUrl", error.getField());
    }

    /**
     * test wrong database username. please provide a wrong user name in config.properties
     * 
     * @throws BonitaException
     */
    public void testWrongDbUser() throws BonitaException {
        final CreateReportFromDataBase connector = getWorkingConnector();
        final String wrongUserName = properties.getProperty("wrongUserName");
        connector.setUser(wrongUserName);
        List<ConnectorError> validation = connector.validate();
        Assert.assertEquals(1, validation.size());
        final ConnectorError error = validation.get(0);
        Assert.assertEquals("jdbcUrl", error.getField());
    }

    /**
     * test wrong database password. please provide a wrong password in config.properties
     * 
     * @throws BonitaException
     */
    public void testWrongDbPassword() throws BonitaException {
        final CreateReportFromDataBase connector = getWorkingConnector();
        final String wrongPassword = properties.getProperty("wrongPassword");
        connector.setPassword(wrongPassword);
        final List<ConnectorError> validation = connector.validate();
        Assert.assertEquals(1, validation.size());
        final ConnectorError error = validation.get(0);
        Assert.assertEquals("jdbcUrl", error.getField());
    }

    /**
     * test wrong output format. other format except "xml", "html" and "pdf" will cause an error.
     * 
     * @throws BonitaException
     */
    public void testOutputFormat() throws BonitaException {
        final CreateReportFromDataBase connector = getWorkingConnector();
        String outputFormat = "xml";
        connector.setOutputFormat(outputFormat);
        List<ConnectorError> validation = connector.validate();
        Assert.assertEquals(0, validation.size());

        outputFormat = "html";
        connector.setOutputFormat(outputFormat);
        validation = connector.validate();
        Assert.assertEquals(0, validation.size());

        outputFormat = "other_format";
        connector.setOutputFormat(outputFormat);
        validation = connector.validate();
        Assert.assertEquals(1, validation.size());
        final ConnectorError error = validation.get(0);
        Assert.assertEquals("outputFormat", error.getField());
    }

    /**
     * test create a report fail.
     */
    public void testCreateAReportFail() {
        final CreateReportFromDataBase connector = getWorkingConnector();
        final String dbDriver = connector.getDbDriver();
        final String jdbcUrl = connector.getJdbcUrl();
        final String user = connector.getUser();
        final String password = connector.getPassword() + "wrong";

        final String jrxmlFilePath = connector.getJrxmlFilePath();
        final Map<String, String> parameters = connector.getParameters();

        final String outputFilePath = connector.getOutputFilePath();
        final String outputFormat = connector.getOutputFormat();

        try {
            connector.createJasperReportFromDataBase(dbDriver, jdbcUrl, user, password, jrxmlFilePath, parameters, outputFilePath, outputFormat);
            fail();
        } catch (final Exception e) {
            Assert.assertTrue(true);
        }
    }

    /**
     * test create a report successfully.
     * 
     * @throws Exception
     */
    public void testCreateAReport() throws Exception {
        final CreateReportFromDataBase connector = getWorkingConnector();
        final String dbDriver = connector.getDbDriver();
        final String jdbcUrl = connector.getJdbcUrl();
        final String user = connector.getUser();
        final String password = connector.getPassword();

        final String jrxmlFilePath = connector.getJrxmlFilePath();
        final Map<String, String> parameters = connector.getParameters();

        final String outputFilePath = connector.getOutputFilePath();
        final String outputFormat = connector.getOutputFormat();

        connector.createJasperReportFromDataBase(dbDriver, jdbcUrl, user, password, jrxmlFilePath, parameters, outputFilePath, outputFormat);
    }

    private CreateReportFromDataBase getWorkingConnector() {

        final CreateReportFromDataBase connector = new CreateReportFromDataBase();

        final String dbDriver = properties.getProperty("dbDriver");
        final String jdbcUrl = properties.getProperty("jdbcUrl");
        final String user = properties.getProperty("user");
        final String password = properties.getProperty("password");

        final String jrxmlFilePath = properties.getProperty("jrxmlFilePath");
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("author", "Jordi Anguela");

        final String outputFilePath = properties.getProperty("outputFilePath");
        final String outputFormat = properties.getProperty("outputFormat");

        connector.setDbDriver(dbDriver);
        connector.setJdbcUrl(jdbcUrl);
        connector.setUser(user);
        connector.setPassword(password);

        connector.setJrxmlFilePath(jrxmlFilePath);
        connector.setParameters(parameters);
        connector.setOutputFilePath(outputFilePath);
        connector.setOutputFormat(outputFormat);

        return connector;
    }

}
