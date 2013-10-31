/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.connectors.talend;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;

/**
 * 
 * @author Charles Souillard
 *
 */
public class JobLauncherConnector extends ProcessConnector {

  private static final String RUN_JOB_METHOD = "runJob";
  private static final String CONTEXT_PARAM_PREFIX = "--context_param:";
  private static final String CONTEXT_PARAM_SEPARATOR = "=";

  //IN
  private String projectName;
  private String jobName;
  private String jobVersion;
  private Map<String, String> jobParameters;

  private boolean printOutput;

  //OUT
  private java.lang.String[][] bufferOutput;
  
  @Override
  protected void executeConnector() throws Exception {
    final String jobClassName = projectName.toLowerCase() + "." + jobName.toLowerCase() + "_" + jobVersion.replace('.', '_') + "." + jobName;
    final Class< ? > clazz = Class.forName(jobClassName, true, Thread.currentThread().getContextClassLoader());

    final Method runJobMethod = clazz.getMethod(RUN_JOB_METHOD, new Class[]{String[].class});
    final Object jobInstance = clazz.newInstance();

    Collection<String> jobParams = new ArrayList<String>();
    if (jobParameters != null) {
      for (Map.Entry<String, String> parameter : jobParameters.entrySet()) {
        jobParams.add(CONTEXT_PARAM_PREFIX + parameter.getKey() + CONTEXT_PARAM_SEPARATOR + parameter.getValue());    
      }
    }

    this.bufferOutput = (java.lang.String[][]) runJobMethod.invoke(jobInstance, new Object[]{jobParams.toArray(new String[]{})});

    if (printOutput) {
      printBufferOutput();
    }
    //use case : use talend to get all products list: first column in french, second in english. TO be mapped to a combo box for example
    //add an input to say where output must be stored
  }

  @Override
  protected List<ConnectorError> validateValues() {
    List<ConnectorError> errors = new ArrayList<ConnectorError>();
    if (this.projectName == null) {
      errors.add(new ConnectorError("projectName", new IllegalArgumentException("cannot be null!")));
    }
    if (this.jobName == null) {
      errors.add(new ConnectorError("jobName", new IllegalArgumentException("cannot be null!")));
    }
    if (this.jobVersion == null) {
      errors.add(new ConnectorError("jobVersion", new IllegalArgumentException("cannot be null!")));
    }
    return errors;
  }
  
  private void printBufferOutput() {
    System.out.println("Buffer output for job: " + jobName + " " + jobVersion + " (project=" + projectName + "):");
    int line = 0;
    for (String[] columns : bufferOutput) {
      System.out.print("Line " + line + ": ");
      int column = 0;
      for (String s : columns) {
        System.out.print(s);
        if (column < (columns.length - 1)) {
          System.out.print(", ");
        }
        column++;
      }
      System.out.println();
      line++;
    }
  }
  
  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public void setJobVersion(String jobVersion) {
    this.jobVersion = jobVersion;
  }

  public void setJobParameters(Map<String, String> jobParameters) {
    this.jobParameters = jobParameters;
  }

  public void setJobParameters(final List<List<Object>> jobParameters) {
    setJobParameters(bonitaListToMap(jobParameters, String.class, String.class));
  }
  
  public void setPrintOutput(boolean printOutput) {
    this.printOutput = printOutput;
  }
  
  public java.lang.String[][] getBufferOutput() {
    return bufferOutput;
  }

}
