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
package org.bonitasoft.connectors.database.hsqldb;

import org.bonitasoft.connectors.database.RemoteDatabaseRoleResolver;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class HSQLDBRoleResolver extends RemoteDatabaseRoleResolver {

  private Boolean local = true;
  private Boolean server = false;
  private Boolean webServer = false;
  private Boolean ssl = null;

  public Boolean isLocal() {
    return local;
  }

  public Boolean isServer() {
    return server;
  }

  public Boolean isWebServer() {
    return webServer;
  }

  public Boolean isSsl() {
    return ssl;
  }

  public void setLocal(Boolean local) {
    this.local = local;
  }

  public void setServer(Boolean server) {
    this.server = server;
  }

  public void setWebServer(Boolean webServer) {
    this.webServer = webServer;
  }

  public void setSsl(Boolean ssl) {
    this.ssl = ssl;
  }

  @Override
  public String getDriver() {
    return "org.hsqldb.jdbcDriver";
  }

  @Override
  public String getUrl() {
    StringBuilder builder = new StringBuilder("jdbc:hsqldb:");
    if (isServer()) {
      if (isSsl()) {
        builder.append("hsqls://");
      } else {
        builder.append("hsql://");
      }
      builder.append(getHostName());
      builder.append(":");
      builder.append(getPort());
      builder.append("/");
    } else if (isWebServer()) {
      if (isSsl()) {
        builder.append("https://");
      } else {
        builder.append("http://");
      }
      builder.append(getHostName());
      builder.append(":");
      builder.append(getPort());
      builder.append("/");
    } else {
      builder.append("file:");
    }
    builder.append(getDatabase());
    return builder.toString();
  }

}
