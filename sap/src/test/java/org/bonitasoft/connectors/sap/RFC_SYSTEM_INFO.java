package org.bonitasoft.connectors.sap;


import java.util.ArrayList;
import java.util.List;

public class RFC_SYSTEM_INFO {

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
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCPROTO");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCCHARTYP");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCINTTYP");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCFLOTYP");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCDEST");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCHOST");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCSYSID");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCDATABS");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCDBHOST");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCDBSYS");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCSAPRL");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCMACH");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCOPSYS");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCTZONE");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCDAYST");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCIPADDR");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCKERNRL");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCHOST2");
    SAPUtil.addOutputRow(outputParameters, SAPCallFunctionConnector.OUTPUT_STRUCTURE, "RFCSI_EXPORT", "RFCSI_RESV");//return may be empty for this one

    SAPUtil.callFunction(existingConnectionName, releaseClient, commitOnSuccess, rollbackOnFailure, printHtml, "RFC_SYSTEM_INFO", inputParameters, outputParameters);
  }

}
