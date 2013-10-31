/**
 * Copyright (C) 2009  BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA  02110-1301, USA.
 **/
package org.bonitasoft.connectors.bonita.filters;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.bonitasoft.connectors.bonita.ConnectorTest;
import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.util.BonitaException;

/**
 * @author Matthieu Chaffotte
 * 
 */
public class RandomMultipleFilterTest extends ConnectorTest {

  private String john = "john";
  private String joe = "joe";
  private String james = "james";
  private String jessy = "jessy";
  private String jack = "jack";
  private RandomMultipleFilter random;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    random = getRandomMultipleFilter();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
    random = null;
  }
  
  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return RandomMultipleFilter.class;
  }

  public void testValidateValues() throws BonitaException {
    random.setCandidateNumber(6);
    List<ConnectorError> errors = random.validate();
    Assert.assertEquals(1, errors.size());
  }

  public void testCandidateNumberUpperThanAllCandididates() throws BonitaException {
    random.setCandidateNumber(6);
    List<ConnectorError> errors = random.validate();
    Assert.assertEquals(1, errors.size());
  }

  public void testNegativeCandidateNumber() throws BonitaException {
    random.setCandidateNumber(-6);
    List<ConnectorError> errors = random.validate();
    Assert.assertEquals(1, errors.size());
  }

  public void testNoCandidates() throws BonitaException {
    random.setCandidateNumber(2);
    random.setMembers(new HashSet<String>());
    List<ConnectorError> errors = random.validate();
    Assert.assertEquals(1, errors.size());
  }

  public void testGetSelectedUsers() throws Exception {
    random.execute();
    Set<String> selectedUsers = random.getCandidates();
    Assert.assertEquals(3, selectedUsers.size());
    for (String user : selectedUsers) {
      Assert.assertTrue(user.equals(jack) || user.equals(james)
          || user.equals(jessy) || user.equals(joe) || user.equals(john));
    }
  }

  private RandomMultipleFilter getRandomMultipleFilter() {
    RandomMultipleFilter random = new RandomMultipleFilter();
    random.setCandidateNumber(3);
    Set<String> members = new HashSet<String>();
    members.add(jack);
    members.add(james);
    members.add(jessy);
    members.add(joe);
    members.add(john);
    random.setMembers(members);
    return random;
  }
}
