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

import java.io.File;
import java.util.ArrayList;

import org.ow2.bonita.connector.core.Connector;

/**
 * @author Yanyan Liu
 *
 */
public class ComplexShellConnectorTest extends ConnectorTest {


  public void testWhiteSpaceInPath() throws Exception{
    String filename = System.currentTimeMillis()+" shellTest";
    String parentPath = System.getProperty("user.home") + System.getProperty("file.separator");
    String filePath = parentPath+filename;
    ComplexShellConnector complexShellConnector = new ComplexShellConnector();
    ArrayList<String> command = new ArrayList<String>();
    prepareCommand(command,parentPath,filename);
    complexShellConnector.setCommand(command);
    complexShellConnector.execute();
    File file = new File(filePath);
    if(file.exists()){
      LOG.info("file exists, so delete it...");
      file.delete();
    }else{
      LOG.info("file name = " + file.getName());
      fail();
    }
  }

  //test symbol "|" in Linux OS
  public void testSpecialSimbol() throws Exception{
    if(!isWindows()){
      ComplexShellConnector complexShellConnector = new ComplexShellConnector();
      ArrayList<String> command = new ArrayList<String>();
      command.add("/bin/sh");
      command.add("-c");
      command.add("ps -ef | grep java");
      complexShellConnector.setCommand(command);
      complexShellConnector.execute();
      LOG.info("result = "+complexShellConnector.getResult());
    }
  }

  /**
   * @param command
   */
  private void prepareCommand(ArrayList<String> command, String parentPath, String filename     ) {
    if(isWindows()){
      command.add("cmd");
      command.add("/C");
    } else {
      command.add("/bin/sh");
      command.add("-c");
    }
    command.add("echo bonita>"+parentPath+"\"" + filename + "\"");
  }

  /* (non-Javadoc)
   * @see org.bonitasoft.connectors.scripting.ConnectorTest#getConnectorClass()
   */
  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return ComplexShellConnector.class;
  }
}
