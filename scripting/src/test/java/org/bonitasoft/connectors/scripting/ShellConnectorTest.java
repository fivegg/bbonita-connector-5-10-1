package org.bonitasoft.connectors.scripting;

import org.ow2.bonita.connector.core.Connector;

public class ShellConnectorTest extends ConnectorTest {

  private String getShellScript() {
    return isUnix() || isMac() ? "ls -lia" : "cmd /c dir";
  }

  public void testCurrentDirectory() throws Exception {
    if (isUnix() || isWindows() || isMac()) {
      ShellConnector shell = new ShellConnector();
      shell.setShell(getShellScript());
      shell.execute();
      String actual = shell.getResult();
      assertTrue(actual.length() >= 0);
    }
  }

  public void testExit() {
    if (isMac() || isUnix()) {
      ShellConnector shell = new ShellConnector();
      shell.setShell("exit 3");
      try {
        shell.execute();
        fail("The script should fail due to not good exit value");
      } catch (Exception e) {
        
      }
    }
  }

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return ShellConnector.class;
  }

}
