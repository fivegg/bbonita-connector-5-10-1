package org.bonitasoft.connectors.sap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class SAPUtil {

  static {
    final URL librfc32URL = SAPUtil.class.getResource("librfc32.dll");
    final URL sapjcorfcURL = SAPUtil.class.getResource("sapjcorfc.dll");
    if (librfc32URL != null && sapjcorfcURL != null) {
      final File librfc32 = new File(librfc32URL.getFile());
      final File sapjcorfc = new File(sapjcorfcURL.getFile());
      final String key = "java.library.path";
      final String value = System.getProperty(key) + ":" + librfc32.getParent() + ":" + sapjcorfc.getParent();
      System.err.println("Setting " + key + " to " + value);
      System.setProperty(key, value);
    }
  }
  private SAPUtil() { }
  
  public static void addInputRowow(final List<List<Object>> inputParameters, final String parameterType, final String tableName, final String parameterName, final String parameterValue) {
    final List<Object> row = new ArrayList<Object>();
    row.add(parameterType);row.add(tableName);row.add(parameterName);row.add(parameterValue);
    inputParameters.add(row);
  }
  
  public static void addInputRowow(final List<List<Object>> inputParameters, final String parameterType, final String tableName, final String parameterName, final List<Object> parameterValue) {
	    final List<Object> row = new ArrayList<Object>();
	    row.add(parameterType);row.add(tableName);row.add(parameterName);row.add(parameterValue);
	    inputParameters.add(row);
	  }
  
  public static void addOutputRow(final List<List<String>> outputParameters, final String parameterType, final String tableName, final String xpath) {
    final List<String> row = new ArrayList<String>();
    row.add(parameterType);row.add(tableName);row.add(xpath);row.add("");
    outputParameters.add(row);
  }
  
  public static String createClient() throws Exception {
    System.err.println("Starting createClient...");
    final SAPCreateClientConnector con = new SAPCreateClientConnector();
    initClient(con);
    con.execute();
    final String createdConnectionName = con.getCreatedConnectionName();
    System.err.println("Ending createClient...");
    return createdConnectionName;
  }

  public static void commit(final String existingConnectionName, final boolean releaseClient) throws Exception {
    System.err.println("Starting commit...");
    final SAPCommitConnector con = new SAPCommitConnector();
    con.setExistingConnectionName(existingConnectionName);
    con.setReleaseClient(releaseClient);
    con.setRepository(SAPConstants.DEFAULT_REPOSITORY_NAME);
    con.execute();
    System.err.println("Ending commit...");
  }

  public static void rollback(final String existingConnectionName, final boolean releaseClient) throws Exception {
    System.err.println("Starting rollback...");
    final SAPRollbackConnector con = new SAPRollbackConnector();
    con.setExistingConnectionName(existingConnectionName);
    con.setReleaseClient(releaseClient);
    con.setRepository(SAPConstants.DEFAULT_REPOSITORY_NAME);
    con.execute();
    System.err.println("Ending rollback...");
  }

  public static void releaseClient(final String existingConnectionName) throws Exception {
    System.err.println("Starting releaseClient...");
    final SAPReleaseClientConnector con = new SAPReleaseClientConnector();
    con.setExistingConnectionName(existingConnectionName);
    con.execute();
    System.err.println("Ending releaseClient...");
  }
  
  public static void callFunction(final String existingConnectionName, final boolean releaseClient, 
      final boolean commitOnSuccess, final boolean rollbackOnFailure, final boolean printHtml, final String functionName, 
      final List<List<Object>> inputParameters, final List<List<String>> outputParameters) throws Exception {

    System.err.println("Starting callFunction...");
    final String htmlPath = System.getProperty("java.io.tmpdir") + File.separator + functionName + "_" + System.currentTimeMillis() + ".html";

    final SAPCallFunctionConnector con = new SAPCallFunctionConnector();
    initClient(con);
    con.setUseExitingConnection(existingConnectionName != null);
    con.setExistingConnectionName(existingConnectionName);

    con.setCommitOnSuccess(commitOnSuccess);
    con.setRollbackOnFailure(rollbackOnFailure);
    con.setRepository(SAPConstants.DEFAULT_REPOSITORY_NAME);
    con.setReleaseClient(releaseClient);

    con.setFunctionName(functionName);
    con.setHtmlOutput(htmlPath);

    con.setInputParameters(inputParameters);
    con.setOutputParameters(outputParameters);

    con.execute();

    final List<Serializable> results = con.getResults();
    System.err.println("\n");
    System.err.println("****************");
    System.err.println("PRINTING RESULTS");
    System.err.println("****************");
    if (results == null) {
      System.err.println("Returned results is null.");
    } else if (results.isEmpty()) {
      System.err.println("Returned results is empty.");
    } else {
      int i = 0;
      String offset = "   ";
      for (Serializable ser : results) {
    	if (i == 10) {
    		offset = "  ";
    	} else if (i == 100) {
    		offset = " ";
    	}
    	if (ser == null) {
        	System.err.println("results[" + i + "]" + offset + "= " + ser);
    	} else {
        	System.err.println(ser.getClass().getSimpleName() + "  -  results[" + i + "]" + offset + "= " + ser);
    	}
        i++;
      }
    }
    System.err.println("\n");
    System.err.println("****************");
    System.err.println("       END      ");
    System.err.println("****************");

    System.err.println("\n");
    System.err.println("HTML file available: " + htmlPath);
    if (printHtml) {
    System.err.println("*********************");
    System.err.println("PRINTING HTML CONTENT");
    System.err.println("*********************");
    System.err.println(readFile(new File(htmlPath)));
    System.err.println("\n\n");
    System.err.println("*********************");
    System.err.println("       END           ");
    System.err.println("*********************");
    }
    System.err.println("Ending callFunction...");
  }

  private static String readFile(final File aFile) throws Exception {
    final StringBuilder contents = new StringBuilder();
    final BufferedReader input =  new BufferedReader(new FileReader(aFile));
    try {
      String line = null;
      while (( line = input.readLine()) != null){
        contents.append(line);
        contents.append(System.getProperty("line.separator"));
      }
    }
    finally {
      input.close();
    }
    return contents.toString();
  }
  
  private static void initClient(final SAPAbstractConnector con) {
    con.setServerType(SAPAbstractConnector.SERVER_TYPE_APPLICATION_SERVER);
    con.setClient("300");
    con.setUser("bpm2");
    con.setPassword("projetobpm");
    con.setLanguage("PT");
    con.setHost("192.168.20.141");

    con.setSystemNumber("00");
    //readConnector.setSystemId("");
    //readConnector.setGroupName("");
  }
  
}
