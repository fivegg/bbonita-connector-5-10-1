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
package org.bonitasoft.connectors.bonita.filters;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.Filter;

/**
 * This filter chooses a candidate between all possible candidates
 * @author Matthieu Chaffotte
 *
 */
public class UniqueRandomFilter extends Filter {

  @Override
  protected List<ConnectorError> validateValues() {
    return super.validateValues();
  }

  @Override
  protected Set<String> getCandidates(Set<String> members) throws Exception {
    int candidatesNumber = members.size();
    Random rand = new Random();
    int next = rand.nextInt(candidatesNumber);
    String selectedUser = get(members, next);
    Set<String> users = new HashSet<String>();
    users.add(selectedUser);
    return users;
  }

}
