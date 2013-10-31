package org.bonitasoft.connectors.sap;


import java.util.ArrayList;
import java.util.List;

public class BAPI_COMPANY_GETLIST {

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

    final List<List<String>> outputParameters = new ArrayList<List<String>>();
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.TABLE_OUTPUT, "COMPANY_LIST", "COMPANY");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.TABLE_OUTPUT, "COMPANY_LIST", "NAME1");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RETURN", "TYPE");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RETURN", "CODE");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RETURN", "MESSAGE");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RETURN", "LOG_NO");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RETURN", "LOG_MSG_NO");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RETURN", "MESSAGE_V1");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RETURN", "MESSAGE_V2");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RETURN", "MESSAGE_V3");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RETURN", "MESSAGE_V4");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RETURN", "");

    SAPUtil.callFunction(existingConnectionName, releaseClient, commitOnSuccess, rollbackOnFailure, printHtml, "BAPI_COMPANY_GETLIST", inputParameters, outputParameters);
  }

}
