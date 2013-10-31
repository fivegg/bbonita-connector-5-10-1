package org.bonitasoft.connectors.sap;


import java.util.ArrayList;
import java.util.List;

public class BAPI_PO_GETITEMS {

  private static final boolean releaseClient = false;
  private static boolean commitOnSuccess = true;
  private static final boolean rollbackOnFailure = true;
  private static final boolean printHtml = false;

  public static void main(String args[]) throws Exception {
    final String existingConnectionName = SAPUtil.createClient();
    SAPUtil.commit(existingConnectionName, releaseClient);
    SAPUtil.rollback(existingConnectionName, releaseClient);
    callFunction(existingConnectionName);
    SAPUtil.releaseClient(existingConnectionName); 
  }


  private static void callFunction(final String existingConnectionName) throws Exception {

    final List<List<Object>> inputParameters = new ArrayList<List<Object>>();
    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.INPUT_SINGLE, "", "PURCHASEORDER", "1234567890");

    final List<List<String>> outputParameters = new ArrayList<List<String>>();
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.TABLE_OUTPUT, "PO_ITEMS", "");
    
    SAPUtil.callFunction(existingConnectionName, releaseClient, commitOnSuccess, rollbackOnFailure, printHtml, "BAPI_PO_GETITEMS", inputParameters, outputParameters);
  }

}
