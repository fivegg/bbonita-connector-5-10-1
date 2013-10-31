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
package org.bonitasoft.connectors.scripting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.util.BonitaRuntimeException;

/**
 * @author Yanyan Liu
 * 
 */
public abstract class AbstractShellConnector extends Connector {

  private String result;

  @Override
  protected void executeConnector() throws Exception {
    try {
      BufferedReader input = null;
      Process pr = null;
      try {
        pr = executeShellCommand();
        input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = input.readLine();
        final StringBuilder builder = new StringBuilder();
        while (line != null) {
          builder.append(line);
          builder.append(System.getProperty("line.separator"));
          line = input.readLine();
        }
        result = builder.toString();
        final int exitVal = pr.waitFor();
        if (exitVal != 0) {
          throw new BonitaRuntimeException("The script execution failed. Exit value=" + exitVal);
        }
      } catch (final Exception e) {
        throw new BonitaRuntimeException(e.getMessage(), e.getCause());
      } finally {
        if (input != null) {
          input.close();
        }
        if (pr != null) {
          pr.destroy();
        }
      }
    } catch (final Exception e) {
      throw new BonitaRuntimeException(e.getMessage(), e.getCause());
    }
  }

  protected abstract Process executeShellCommand() throws IOException;

  public String getResult() {
    return result;
  }
}
