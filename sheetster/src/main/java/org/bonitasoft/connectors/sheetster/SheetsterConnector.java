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
package org.bonitasoft.connectors.sheetster;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Matthieu Chaffotte, Yanyan Liu
 * 
 */
public abstract class SheetsterConnector extends Connector {

  private String serverURL;
  private String username;
  private String password;

  private static String session;

  public String getSession() {
    return session;
  }

  public void setServerURL(final String serverURL) {
    this.serverURL = serverURL;
  }

  public void setUsername(final String username) {
    this.username = username;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  @Override
  protected void executeConnector() throws Exception {
    authenticate();
    executeTask();
  }

  protected abstract void executeTask() throws Exception;

  private void authenticate() throws Exception {
    final StringBuilder loginURL = new StringBuilder(serverURL);
    loginURL.append("/workbook/id/-2/txt/system/login/?username=").append(username).append("&password=")
        .append(password);
    getHTTPBytes(loginURL.toString());
  }

  public byte[] getHTTPBytes(String urlstr) throws Exception {
    if (!urlstr.startsWith(serverURL)) {
      urlstr = serverURL + urlstr;
    }
    final URL pageURL = new URL(urlstr);
    URLConnection pageConnection;
    pageConnection = pageURL.openConnection();
    if (session != null) {
      pageConnection.addRequestProperty("Cookie", session);
    }
    pageConnection.setConnectTimeout(10000);

    InputStream webPageInputStream;
    pageConnection.connect();
    webPageInputStream = pageConnection.getInputStream();
    final String enc = pageConnection.getContentEncoding();
    try {
      if (enc.equalsIgnoreCase("gzip")) {
        webPageInputStream = new GZIPInputStream(pageConnection.getInputStream());
      }
    } catch (final Exception e) {
      // normal
    }

    final ByteArrayOutputStream webPageData = new ByteArrayOutputStream();
    boolean moreToRead = true;
    final byte[] readBuf = new byte[2048];
    while (moreToRead) {
      int numBytesRead = 0;
      try {
        numBytesRead = webPageInputStream.read(readBuf);
      } catch (final IOException e) {
        moreToRead = false;
      }
      if (numBytesRead > 0) {
        webPageData.write(readBuf, 0, numBytesRead);
      } else {
        moreToRead = false;
      }
    }
    webPageInputStream.close();
    // Pull the session ID out of the connection headers
    /*
     * try to get cookie every time in case cookie expired
     */
    final String s = pageConnection.getHeaderField("Set-Cookie");
    try {
      if (s != null) {
        session = s;
        if (!session.startsWith("sessionid=")) {
          throw new Exception("invalid session cookie");
        }
        // Strip off any extraneous header fields
        if (session.contains(";")) {
          session = session.substring(0, session.indexOf(';'));
        }
      }
    } catch (final Exception e) {
      throw new RuntimeException("failed to parse session cookie", e);
    }
    return webPageData.toByteArray();
  }

  public String getHTTPData(final String urlstr) throws Exception {
    return new String(getHTTPBytes(urlstr));
  }

  public Object getHTTPObject(final String urlstr) throws Exception {
    // Create a URL object from urlString
    final String ret = new String(getHTTPBytes(urlstr));
    // try numbers
    try {
      return Double.parseDouble(ret);
    } catch (final NumberFormatException de) {
      try {
        return Float.parseFloat(ret);
      } catch (final NumberFormatException fe) {
        try {
          return Integer.parseInt(ret);
        } catch (final NumberFormatException ie) {
          // ok not a number
        }
      }
    }

    // try boolean
    if (ret.equalsIgnoreCase("true")) {
      return Boolean.TRUE;
    } else if (ret.equalsIgnoreCase("false")) {
      return Boolean.FALSE;
    }
    // finally returns string val
    return ret;
  }

  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    if (serverURL.length() == 0) {
      final ConnectorError error = new ConnectorError("serverURL", new IllegalArgumentException(
          "The server URL is empty!"));
      errors.add(error);
    }
    return errors;
  }

}
