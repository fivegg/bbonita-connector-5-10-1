package org.bonitasoft.connectors.sap;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class Elo {

  private static final boolean releaseClient = false;
  private static boolean commitOnSuccess = true;
  private static final boolean rollbackOnFailure = true;
  private static final boolean printHtml = true;

  public static void main(String args[]) throws Exception {
	  printProperties();
	  
    final String existingConnectionName = SAPUtil.createClient();
    SAPUtil.commit(existingConnectionName, releaseClient);
    SAPUtil.rollback(existingConnectionName, releaseClient);
    callFunction(existingConnectionName);
    SAPUtil.releaseClient(existingConnectionName); 
  }

  
  private static void printProperties() {
	  /*
		 * Print the JVM properties. Can be commented to more relevant ouput
		 */
		System.out.println("========= PRINTING PROPERTIES =========");
		Properties properties = System.getProperties();
		Set<Object> keys = properties.keySet();
		for (Object propkey : keys) {
			System.out.println("[" + propkey.toString() + "=" + properties.getProperty(propkey.toString()) + "]");
		}
		System.out.println("========= END PROPERTIES =========");
  }

  private static void callFunction(final String existingConnectionName) throws Exception {
	  

    final List<List<Object>> inputParameters = new ArrayList<List<Object>>();
    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "PREQ_ITEM", "00010");
    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "DOC_TYPE", "ZN");
    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "PREQ_DATE", "20120301");
    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "MATERIAL", "000000000000401887");
    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "PLANT", "O101");
    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "STORE_LOC", "DP01");
    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "QUANTITY", "1");
    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "DELIV_DATE", "20120301");
    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "UNIT", "L");
    
//    List<Object> l = new ArrayList<Object>();
//    l.add("00010");
//    l.add("00011");
//    
//    List<Object> l2 = new ArrayList<Object>();
//    l2.add("ZN");
//    l2.add("ZO");
//    
//    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "PREQ_ITEM", l);
//    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "DOC_TYPE", l2);
//    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "PREQ_DATE", "20120301");
//    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "MATERIAL", "000000000000401887");
//    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "PLANT", "O101");
//    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "STORE_LOC", "DP01");
//    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "QUANTITY", "1");
//    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "DELIV_DATE", "20120301");
//    SAPUtil.addInputRowow(inputParameters, SAPCallFunctionConnector.TABLE_INPUT, "REQUISITION_ITEMS", "UNIT", "L");
//    
    
    
    final List<List<String>> outputParameters = new ArrayList<List<String>>();
  

    SAPUtil.callFunction(existingConnectionName, releaseClient, commitOnSuccess, rollbackOnFailure, printHtml, "BAPI_REQUISITION_CREATE", inputParameters, outputParameters);
  }

}
