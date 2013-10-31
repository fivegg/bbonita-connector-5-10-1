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
package org.bonitasoft.connectors.drools;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.bonitasoft.connectors.drools.examples.agerules.Applicant;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.util.BonitaException;

/**
 * @author Jordi Anguela
 */
public class StatelessKnowledgeSessionExecuteTest extends TestCase {

  protected static final Logger LOG = Logger.getLogger(StatelessKnowledgeSessionExecuteTest.class.getName());

  private static String drlFilePath = "";

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    drlFilePath = this.getClass().getResource("ageRules.drl").toString();
    drlFilePath = drlFilePath.replaceFirst("file:", "");

    if (LOG.isLoggable(Level.WARNING)) {
      LOG.warning("======== Starting test: " + this.getClass().getName() + "." + this.getName() + "() ==========");
    }
  }

  @Override
  protected void tearDown() throws Exception {
    if (LOG.isLoggable(Level.WARNING)) {
      LOG.warning("======== Ending test: " + this.getName() + " ==========");
    }
    super.tearDown();
  }

  public void testEmptyDrlFilePath() throws BonitaException {
    StatelessKnowledgeSessionExecute connector = getConfiguredConnector();
    connector.setDrlFilePath("");
    try {
      List<ConnectorError> errors =  connector.validateValues();
      assertEquals(1, errors.size());
      ConnectorError error = errors.get(0);
      assertEquals("drlFilePath", error.getField());
    } catch (Exception e) {
      e.printStackTrace();
      fail("An exception should not be occured!");
    }
  }

  public void testEmptyListOfFacts() throws BonitaException {
    StatelessKnowledgeSessionExecute connector = getConfiguredConnector();
    connector.setListOfFacts( new ArrayList<Object>() );
    try {
      List<ConnectorError> errors =  connector.validateValues();
      assertEquals(1, errors.size());
      ConnectorError error = errors.get(0);
      assertEquals("listOfFacts", error.getField());
    } catch (Exception e) {
      e.printStackTrace();
      fail("An exception should not be occured!");
    }
  }

  public void testDrlFilePathIsNull() throws BonitaException {
    StatelessKnowledgeSessionExecute connector = getConfiguredConnector();
    connector.setDrlFilePath(null);
    try {
      List<ConnectorError> errors =  connector.validateValues();
      assertEquals(1, errors.size());
      ConnectorError error = errors.get(0);
      assertEquals("drlFilePath", error.getField());
    } catch (Exception e) {
      e.printStackTrace();
      fail("An exception should not be occured!");
    }
  }

  public void testListOfFactsIsNull() throws BonitaException {
    StatelessKnowledgeSessionExecute connector = getConfiguredConnector();
    connector.setListOfFacts(null);
    try {
      List<ConnectorError> errors =  connector.validateValues();
      assertEquals(1, errors.size());
      ConnectorError error = errors.get(0);
      assertEquals("listOfFacts", error.getField());
    } catch (Exception e) {
      e.printStackTrace();
      fail("An exception should not be occured!");
    }
  }

  public void testOk() throws BonitaException {
    StatelessKnowledgeSessionExecute connector = getConfiguredConnector();

    Applicant applicant1 = new Applicant( "Jordi", 8 );
    Applicant applicant2 = new Applicant( "Marc", 15 );
    Applicant applicant3 = new Applicant( "Albert", 32 );

    List<Object> listOfFacts = new ArrayList<Object>();
    listOfFacts.add(applicant1);
    listOfFacts.add(applicant2);
    listOfFacts.add(applicant3);

    connector.setListOfFacts(listOfFacts);

    try {
      List<ConnectorError> errors =  connector.validateValues();
      assertEquals(0, errors.size());
      assertNull(((Applicant)listOfFacts.get(0)).getStatus());
      assertNull(((Applicant)listOfFacts.get(1)).getStatus());
      assertNull(((Applicant)listOfFacts.get(2)).getStatus());

      connector.executeConnector();

      assertEquals("Child", ((Applicant)listOfFacts.get(0)).getStatus());
      assertEquals("Young", ((Applicant)listOfFacts.get(1)).getStatus());
      assertEquals("Adult", ((Applicant)listOfFacts.get(2)).getStatus());
    } catch (Exception e) {
      e.printStackTrace();
      fail("An exception should not be occured!");
    }
  }

  private StatelessKnowledgeSessionExecute getConfiguredConnector() {
    StatelessKnowledgeSessionExecute connector = new StatelessKnowledgeSessionExecute();

    Applicant applicant1 = new Applicant("Jordi", 8);
    Applicant applicant2 = new Applicant("Marc", 15);
    Applicant applicant3 = new Applicant("Albert", 32);

    List<Object> listOfFacts = new ArrayList<Object>();
    listOfFacts.add(applicant1);
    listOfFacts.add(applicant2);
    listOfFacts.add(applicant3);
    // set Parameters
    connector.setDrlFilePath(drlFilePath);
    connector.setListOfFacts(listOfFacts);
    return connector;
  }

}  
