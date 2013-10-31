/**
 * Copyright (C) 2009-2011 BonitaSoft S.A.
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
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;

/**
 * 
 * @author Jordi Anguela, Yanyan Liu
 * 
 */
public class CreateReportFromDataBase extends ProcessConnector {

  // Data base configuration
  private String dbDriver;
  private String jdbcUrl;
  private String user;
  private String password;

  // Report settings
  private String jrxmlFilePath;
  private Map<String, String> parameters = null;
  private String outputFilePath;
  private String outputFormat;

  private static final Logger LOGGER = Logger.getLogger(CreateReportFromDataBase.class.getName());
  private static Connection conn = null;

  private enum OutputFormat {
    html, pdf, xml
  }

  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();

    if (dbDriver.trim().length() == 0) {
      errors.add(new ConnectorError("dbDriver", new IllegalArgumentException("Cannot be empty!")));
    }
    if (jdbcUrl.trim().length() == 0) {
      errors.add(new ConnectorError("jdbcUrl", new IllegalArgumentException("Cannot be empty!")));
    }
    if (jrxmlFilePath.trim().length() == 0) {
      errors.add(new ConnectorError("jrxmlFilePath", new IllegalArgumentException("Cannot be empty!")));
    }
    if (outputFilePath.trim().length() == 0) {
      errors.add(new ConnectorError("outputFilePath", new IllegalArgumentException("Cannot be empty!")));
    }
    outputFormat = outputFormat.trim();
    if (!OutputFormat.html.name().equalsIgnoreCase(outputFormat)
        && !OutputFormat.pdf.name().equalsIgnoreCase(outputFormat)
        && !OutputFormat.xml.name().equalsIgnoreCase(outputFormat)) {
      errors.add(new ConnectorError("outputFormat", new IllegalArgumentException(
          "Accepted outputFormats are : 'html', 'pdf' or 'xml' !")));
    }

    // Load JDBC driver
    // Check that jrxmlFile exists
    // Test database connection
    try {
      this.databaseValidations(dbDriver, jrxmlFilePath, jdbcUrl, user, password);
    } catch (final ClassNotFoundException e) {
      errors.add(new ConnectorError("dbDriver", new ClassNotFoundException("JDBC Driver not found!")));
    } catch (final FileNotFoundException e) {
      errors.add(new ConnectorError("jrxmlFilePath", new FileNotFoundException("File '" + jrxmlFilePath
          + "' not found!")));
    } catch (final SQLException e) {
      errors
          .add(new ConnectorError("jdbcUrl", new SQLException(
              "Cannot connect to database. Check 'jdbcUrl', 'user' and 'password' parameters. Message: "
                  + e.getMessage())));
    }

    return errors;
  }

  @Override
  protected void executeConnector() throws Exception {
    this.createJasperReportFromDataBase(dbDriver, jdbcUrl, user, password, jrxmlFilePath, parameters, outputFilePath,
        outputFormat);
  }

  /**
   * validate the database
   * 
   * @param dbDriver
   * @param jrxmlFilePath
   * @param jdbcUrl
   * @param user
   * @param password
   * @throws ClassNotFoundException
   * @throws FileNotFoundException
   * @throws SQLException
   */
  public void databaseValidations(final String dbDriver, final String jrxmlFilePath, final String jdbcUrl,
      final String user, final String password) throws ClassNotFoundException, FileNotFoundException, SQLException {

    // Load JDBC driver
    try {
      Class.forName(dbDriver);
    } catch (final ClassNotFoundException e) {
      if (LOGGER.isLoggable(Level.WARNING)) {
        LOGGER.warning("JDBC Driver not found. dbDriver=" + dbDriver);
      }
      throw e;
    }

    // Test that jrxmlFile exists
    final File jrxmlFile = new File(jrxmlFilePath);
    if (!jrxmlFile.exists()) {
      if (LOGGER.isLoggable(Level.WARNING)) {
        LOGGER.warning("File '" + jrxmlFilePath + "' not found.");
      }
      throw new FileNotFoundException("File '" + jrxmlFilePath + "' not found.");
    }

    // Test database connection. this method is just for validation. no need close conn in finally code block.
    try {
      conn = DriverManager.getConnection(jdbcUrl, user, password);
      conn.setAutoCommit(false);
    } catch (final SQLException e) {
      if (LOGGER.isLoggable(Level.WARNING)) {
        LOGGER.warning("Connection error: " + e.getMessage());
      }
      try {
        if (conn != null) {
          conn.close();
        }
      } catch (final Exception e1) {
        if (LOGGER.isLoggable(Level.WARNING)) {
          LOGGER.warning("Exception during finally. Message: " + e1.getMessage());
        }
      }
      throw e;
    }

  }

  /**
   * create jasper report from database
   * 
   * @param dbDriver
   * @param jdbcUrl
   * @param user
   * @param password
   * @param jrxmlFilePath
   * @param parameters
   * @param outputFilePath
   * @param outputFormat
   * @throws Exception
   */
  public void createJasperReportFromDataBase(final String dbDriver, final String jdbcUrl, final String user,
      final String password, final String jrxmlFilePath, final Map<String, String> parameters,
      final String outputFilePath, final String outputFormat) throws Exception {
    if (LOGGER.isLoggable(Level.INFO)) {
      LOGGER.info("Creating a new Jasper Report from database");
    }

    databaseValidations(dbDriver, jrxmlFilePath, jdbcUrl, user, password);

    try {
      final JasperReport report = JasperCompileManager.compileReport(jrxmlFilePath);
      final Map<String, Object> typedParameters = getTypedParameters(report, parameters);
      final JasperPrint print = JasperFillManager.fillReport(report, typedParameters, conn);

      // Export file to selected format
      if (OutputFormat.pdf.name().equals(outputFormat)) {
        JasperExportManager.exportReportToPdfFile(print, outputFilePath);
      } else if (OutputFormat.html.name().equals(outputFormat)) {
        JasperExportManager.exportReportToHtmlFile(print, outputFilePath);
      } else if (OutputFormat.xml.name().equals(outputFormat)) {
        JasperExportManager.exportReportToXmlFile(print, outputFilePath, true);
      } else {
        final String errorMessage = "Accepted outputFormats are : 'html', 'pdf' or 'xml' !";
        if (LOGGER.isLoggable(Level.WARNING)) {
          LOGGER.warning(errorMessage);
        }
        throw new IllegalArgumentException(errorMessage);
      }
      // Visualize new report file
      // JasperViewer.viewReport(print, false);
    } catch (final Exception e) {
      if (LOGGER.isLoggable(Level.WARNING)) {
        LOGGER.warning(e.getMessage());
      }
      throw e;
    } finally {
      // Cleanup before exit.
      try {
        if (conn != null) {
          conn.rollback();
          conn.close();
        }
      } catch (final Exception e) {
        if (LOGGER.isLoggable(Level.WARNING)) {
          LOGGER.warning("Exception during finally. Message: " + e.getMessage());
        }
        throw e;
      }
    }
  }

  private Map<String, Object> getTypedParameters(final JasperReport report, final Map<String, String> parameters) {
    final Map<String, Object> typedParameters = new HashMap<String, Object>();
    if(parameters == null){
        return typedParameters;
    }
    for (final JRParameter param : report.getParameters()) {
      final String paramName = param.getName();
      final String paramType = param.getValueClassName();
      final String value = parameters.get(paramName);
      if (value != null && paramType != null) {
        if (paramType.equals(String.class.getName())) {
          typedParameters.put(paramName, value);
        } else if (paramType.equals(Integer.class.getName())) {
          try {
            final Integer typedValue = Integer.parseInt(value);
            typedParameters.put(paramName, typedValue);
          } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Invalid parameter type for " + paramName + ": "
                + Integer.class.getName() + " value is expected, current is " + value);
          }
        } else if (paramType.equals(Short.class.getName())) {
          try {
            final Short typedValue = Short.parseShort(value);
            typedParameters.put(paramName, typedValue);
          } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Invalid parameter type for " + paramName + ": " + Short.class.getName()
                + " value is expected, current is " + value);
          }
        } else if (paramType.equals(Long.class.getName())) {
          try {
            final Long typedValue = Long.parseLong(value);
            typedParameters.put(paramName, typedValue);
          } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Invalid parameter type for " + paramName + ": " + Long.class.getName()
                + " value is expected, current is " + value);
          }
        } else if (paramType.equals(Double.class.getName())) {
          try {
            final Double typedValue = Double.parseDouble(value);
            typedParameters.put(paramName, typedValue);
          } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Invalid parameter type for " + paramName + ": "
                + Double.class.getName() + " value is expected, current is " + value);
          }
        } else if (paramType.equals(Float.class.getName())) {
          try {
            final Float typedValue = Float.parseFloat(value);
            typedParameters.put(paramName, typedValue);
          } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Invalid parameter type for " + paramName + ": " + Float.class.getName()
                + " value is expected, current is " + value);
          }
        } else if (paramType.equals(BigDecimal.class.getName())) {
          try {
            final BigDecimal typedValue = new BigDecimal(value);
            typedParameters.put(paramName, typedValue);
          } catch (final NumberFormatException e) {
            throw new IllegalArgumentException("Invalid parameter type for " + paramName + ": "
                + BigDecimal.class.getName() + " value is expected, current is " + value);
          }
        } else if (paramType.equals(Date.class.getName())) {
          try {
            final Date typedValue = new SimpleDateFormat().parse(value);
            typedParameters.put(paramName, typedValue);
          } catch (final ParseException e) {
            throw new IllegalArgumentException("Invalid parameter type for " + paramName + ": " + Date.class.getName()
                + " value is expected, current is " + value);
          }
        } else if (paramType.equals(Boolean.class.getName())) {
          final Boolean typedValue = Boolean.parseBoolean(value);
          typedParameters.put(paramName, typedValue);
        }else{
        	throw new IllegalArgumentException("Invalid parameter type for " + paramName + ": " + paramType
                    + " is not a supported type for this connector input.");
        }
      }
    }
    return typedParameters;
  }

  /**
   * set the dbDriver
   * 
   * @param dbDriver the dbDriver
   */
  public void setDbDriver(final String dbDriver) {
    this.dbDriver = dbDriver;
  }

  /**
   * set the jdbcUrl
   * 
   * @param jdbcUrl the jdbcUrl
   */
  public void setJdbcUrl(final String jdbcUrl) {
    this.jdbcUrl = jdbcUrl;
  }

  /**
   * set the user name
   * 
   * @param user the user name for the database
   */
  public void setUser(final String user) {
    this.user = user;
  }

  /**
   * set the password
   * 
   * @param password the password for the database
   */
  public void setPassword(final String password) {
    this.password = password;
  }

  /**
   * set the jrxmlFilePath
   * 
   * @param jrxmlFilePath the .jrxml file path
   */
  public void setJrxmlFilePath(final String jrxmlFilePath) {
    this.jrxmlFilePath = jrxmlFilePath;
  }

  /**
   * set the parameters
   * 
   * @param parameters the parameters
   */
  public void setParameters(final Map<String, String> parameters) {
    this.parameters = parameters;
  }

  /**
   * set the parameters
   * 
   * @param parameters the parameters
   */
  public void setParameters(final List<List<Object>> parameters) {
    setParameters(listToMap(parameters));
  }

  private Map<String, String> listToMap(final List<List<Object>> array) {
    Map<String, String> map = null;
    if (array != null && array.size() > 0) {
      map = new HashMap<String, String>();
      for (final List<Object> list : array) {
        final Object key = list.get(0);
        final Object value = list.get(1);
        if (key != null && value != null) {
          map.put(key.toString(), value.toString());
        }
      }
    }
    return map;
  }

  /**
   * set the outputFilePath
   * 
   * @param outputFilePath the output file path
   */
  public void setOutputFilePath(final String outputFilePath) {
    this.outputFilePath = outputFilePath;
  }

  /**
   * set the output format
   * 
   * @param outputFormat the output format
   */
  public void setOutputFormat(final String outputFormat) {
    this.outputFormat = outputFormat;
  }

  /**
   * @return the dbDriver
   */
  public String getDbDriver() {
    return dbDriver;
  }

  /**
   * @return the jdbcUrl
   */
  public String getJdbcUrl() {
    return jdbcUrl;
  }

  /**
   * @return the user
   */
  public String getUser() {
    return user;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return password;
  }

  /**
   * @return the jrxmlFilePath
   */
  public String getJrxmlFilePath() {
    return jrxmlFilePath;
  }

  /**
   * @return the parameters
   */
  public Map<String, String> getParameters() {
    return parameters;
  }

  /**
   * @return the outputFilePath
   */
  public String getOutputFilePath() {
    return outputFilePath;
  }

  /**
   * @return the outputFormat
   */
  public String getOutputFormat() {
    return outputFormat;
  }

}
