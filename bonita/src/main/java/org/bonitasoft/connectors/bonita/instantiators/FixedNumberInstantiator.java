/**
 * Copyright (C) 2010 BonitaSoft S.A.
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
package org.bonitasoft.connectors.bonita.instantiators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.ow2.bonita.connector.core.MultipleInstancesInstantiator;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class FixedNumberInstantiator extends MultipleInstancesInstantiator {

  private int activityNumber;

  public void setActivityNumber(int activityNumber) {
    this.activityNumber = activityNumber;
  }
  
  public void setActivityNumber(final long activityNumber) {
    this.activityNumber = (int) activityNumber;
  }

  @Override
  protected List<Map<String, Object>> defineActivitiesContext() throws Exception {
    if (activityNumber < 1) {
      throw new NumberFormatException("The number of activities is less than 1");
    }
    final List<Map<String, Object>> context = new ArrayList<Map<String, Object>>();
    for (int i = 0; i < activityNumber; i++) {
      Map<String, Object> activityContext = Collections.emptyMap();
      context.add(activityContext);
    }
    return context;
  }

}
