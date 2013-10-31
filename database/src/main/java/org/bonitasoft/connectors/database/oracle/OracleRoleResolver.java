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
package org.bonitasoft.connectors.database.oracle;

import org.bonitasoft.connectors.database.RemoteDatabaseRoleResolver;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class OracleRoleResolver extends RemoteDatabaseRoleResolver {

  private Boolean oracleSID;
  private Boolean oracleServiceName;
  private Boolean oracleOCI;

  public Boolean isOracleSID() {
    return oracleSID;
  }

  public Boolean isOracleServiceName() {
    return oracleServiceName;
  }

  public Boolean isOracleOCI() {
    return oracleOCI;
  }

  public void setOracleSID(Boolean oracleSID) {
    this.oracleSID = oracleSID;
  }

  public void setOracleServiceName(Boolean oracleServiceName) {
    this.oracleServiceName = oracleServiceName;
  }

  public void setOracleOCI(Boolean oracleOCI) {
    this.oracleOCI = oracleOCI;
  }

  @Override
  public String getDriver() {
    return "oracle.jdbc.driver.OracleDriver";
  }

  @Override
  public String getUrl() {
    StringBuilder builder = new StringBuilder("jdbc:oracle:");
    if (isOracleSID()) {
      builder.append("thin:@");
      builder.append(getHostName());
      builder.append(":");
      builder.append(getPort());
      builder.append(":");
      builder.append(getDatabase());
    } else if (isOracleServiceName()) {
      builder.append("thin:@(description=(address=(protocol=tcp)(host=");
      builder.append(getHostName());
      builder.append(")(port=");
      builder.append(getPort());
      builder.append("))(connect_data=(service_name=");
      builder.append(getDatabase());
      builder.append(")))");
    } else {
      builder.append("oci8:@");
      builder.append(getDatabase());
    }
    return builder.toString();
  }

}
