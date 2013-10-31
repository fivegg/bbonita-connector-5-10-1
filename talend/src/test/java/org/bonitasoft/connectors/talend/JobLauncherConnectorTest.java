package org.bonitasoft.connectors.talend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

import junit.framework.TestCase;

public class JobLauncherConnectorTest extends TestCase {

  public void testTalendJobLauncher() throws Exception {
    String directory = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath() + File.separator;
    String fileName = null;
    File expectedFile = null;
    do {
      fileName = "talendOutput" + new Random().nextInt(1000000);
      expectedFile = new File(directory + fileName + ".csv");
    } while (expectedFile.exists());

    Map<String, String> jobParameters = new HashMap<String, String>();
    jobParameters.put("new1", directory);
    jobParameters.put("new2", fileName);

    JobLauncherConnector jobLauncher = new JobLauncherConnector();
    jobLauncher.setProjectName("fgb");
    jobLauncher.setJobName("test");
    jobLauncher.setJobVersion("0.1");
    jobLauncher.setJobParameters(jobParameters);
    jobLauncher.setPrintOutput(true);
    assertFalse("Generated file must not exists before the talend job is executed: " + expectedFile, expectedFile.exists());
    jobLauncher.execute();
    assertTrue("Generated file must exists after the talend job is executed: " + expectedFile, expectedFile.exists());
    assertTrue("The generated file must contain many lines with at least one containing James;", checkContains(expectedFile, ";"));
    String[][] bufferOutput = jobLauncher.getBufferOutput();
    assertEquals("0", bufferOutput[0][0]);
    expectedFile.delete();
  }

  public void testTalendJobLauncherWithOutput() throws Exception {
    String directory = new File(System.getProperty("java.io.tmpdir")).getAbsolutePath() + File.separator;
    String fileName = null;
    File expectedFile = null;
    do {
      fileName = "talendOutput" + new Random().nextInt(1000000);
      expectedFile = new File(directory + fileName + ".csv");
    } while (expectedFile.exists());

    Map<String, String> jobParameters = new HashMap<String, String>();
    jobParameters.put("new1", directory);
    jobParameters.put("new2", fileName);

    JobLauncherConnector jobLauncher = new JobLauncherConnector();
    jobLauncher.setProjectName("fgb");
    jobLauncher.setJobName("test");
    jobLauncher.setJobVersion("0.1");
    jobLauncher.setJobParameters(jobParameters);
    jobLauncher.setPrintOutput(true);

    assertFalse("Generated file must not exists before the talend job is executed: " + expectedFile, expectedFile.exists());
    jobLauncher.execute();
    String[][] actual = jobLauncher.getBufferOutput();
    String[][] expected = new String[][] {{"0"}};
    for (int i = 0; i < expected.length; i++) {
      for (int j = 0; j < expected[0].length; j++) {
        assertEquals(expected[i][j], actual[i][j]);
      }
    }
    assertTrue("Generated file must exists after the talend job is executed: " + expectedFile, expectedFile.exists());
    assertTrue("The generated file must contain many lines with at least one containing James;", checkContains(expectedFile, ";"));
    expectedFile.delete();
  }

  public boolean checkContains(File sourceFile, String s) throws FileNotFoundException {
    Scanner scanner = new Scanner(sourceFile);
    try {
      while (scanner.hasNextLine()){
        if (scanner.nextLine().contains(s)) {
          return true;
        }
      }
      return false;
    } finally {
      scanner.close();
    }
  }
}
