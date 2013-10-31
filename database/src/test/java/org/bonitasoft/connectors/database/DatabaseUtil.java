package org.bonitasoft.connectors.database;

public final class DatabaseUtil {

  public static final String getTableName(String tableName) {
    if (tableName == null) {
      tableName = "test" + getOSName();
    }
    return tableName;
  }

  public static final String getOSName() {
    String osName = new String();
    try {
      String name = System.getProperty("os.name");
      if (name.startsWith("Lin")) {
        osName = "lin";
      } else if (name.startsWith("Win")) {
        osName = "win";
      } else if (name.startsWith("Mac")) {
        osName = "mac";
      } else {
        osName = "undifined" + Math.random();
      }
    } catch (Exception e) {
      osName = "undifined" + Math.random();
    }
    return osName;
  }

}
