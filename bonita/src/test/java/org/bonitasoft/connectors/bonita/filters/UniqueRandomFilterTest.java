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

/**
 * @author Matthieu Chaffotte
 *
 */
public class UniqueRandomFilterTest extends ConnectorTest {

  private String john = "john";
  private String joe = "joe";
  private String james = "james";
  private String jessy = "jessy";
  private String jack = "jack";

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return UniqueRandomFilter.class;
  }

  public void testGetUserFromAnEmptyList() {
    UniqueRandomFilter unique = new UniqueRandomFilter();
    unique.setMembers(new HashSet<String>());
    List<ConnectorError> errors = unique.validate();
    Assert.assertEquals(1, errors.size());
  }

  public void testGetAUser() throws Exception {
    UniqueRandomFilter unique = getUniqueRandomMapper();
    unique.execute();
    String actual = unique.getMembers().iterator().next();
    Assert.assertTrue(john.equals(actual) || james.equals(actual)
        || jessy.equals(actual) || jack.equals(actual) || joe.equals(actual));
  }

  private UniqueRandomFilter getUniqueRandomMapper() {
    Set<String> candidates = new HashSet<String>();
    candidates.add("john");
    candidates.add("joe");
    candidates.add("jack");
    candidates.add("james");
    candidates.add("jessy");
    UniqueRandomFilter unique = new UniqueRandomFilter();
    unique.setMembers(candidates);
    return unique;
  }
}
