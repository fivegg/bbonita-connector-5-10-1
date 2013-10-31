package org.bonitasoft.connectors.database.connector;

import junit.framework.TestCase;

import org.bonitasoft.connectors.database.GenericJDBCBatchConnector;

public class GenericJDBCBatchConnectorTest extends TestCase {

  public void testScriptExecute() throws Exception {
    final StringBuilder builder = new StringBuilder();
    builder.append("DROP TABLE film IF EXISTS;");
    builder.append("DROP TABLE showtime IF EXISTS;");
    builder.append("DROP TABLE theater IF EXISTS;");
    builder.append("CREATE TABLE film (title VARCHAR2(255) PRIMARY KEY);");
    builder.append("CREATE TABLE showtime (time VARCHAR2(8) PRIMARY KEY);");
    builder
        .append("CREATE TABLE theater (id INT AUTO_INCREMENT PRIMARY KEY,film_id VARCHAR2(255),showtime_id VARCHAR2(8),free_seats INT,seats LONGVARCHAR);");
    builder.append("ALTER TABLE theater ADD CONSTRAINT fk_film FOREIGN KEY (film_id) REFERENCES film(title);");
    builder
        .append("ALTER TABLE theater ADD CONSTRAINT fk_showtime FOREIGN KEY (showtime_id) REFERENCES showtime(time);");

    final GenericJDBCBatchConnector batch = new GenericJDBCBatchConnector();
    batch.setDriver("org.h2.Driver");
    batch.setUrl("jdbc:h2:mem:movizz.db;MVCC=TRUE;DB_CLOSE_ON_EXIT=TRUE");
    batch.setUsername("sa");
    batch.setPassword("");
    batch.setScript(builder.toString());
    batch.setSeparator(";");
    batch.execute();
  }

}
