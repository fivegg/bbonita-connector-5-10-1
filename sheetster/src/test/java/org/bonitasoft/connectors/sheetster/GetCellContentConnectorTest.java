package org.bonitasoft.connectors.sheetster;

import junit.framework.TestCase;

public class GetCellContentConnectorTest extends TestCase {

  public void testTrueAnswer() throws Exception {
    GetCellContentConnector sheetster = new GetCellContentConnector();
    sheetster.setServerURL("http://192.168.1.109:8989");
    sheetster.setUsername("john.doe@bonita.fr");
    sheetster.setPassword("adminadmin");
    sheetster.setWorkBookId("3");
    sheetster.setSheetName("first");
    sheetster.setCell("B2");
    sheetster.execute();
    assertTrue((Boolean) sheetster.getCellContent());
  }

//  public void testCastExceptionAnswer() throws Exception {
//    GetCellContentConnector sheetster = new GetCellContentConnector();
//    sheetster.setServerURL("http://192.168.1.109:8989");
//    sheetster.setUsername("john.doe@bonita.fr");
//    sheetster.setPassword("adminadmin");
//    sheetster.setWorkBookId("3");
//    sheetster.setSheetName("first");
//    sheetster.setCell("B1");
//    sheetster.execute();
//    assertEquals("", sheetster.getCellContent());
//  }

}
