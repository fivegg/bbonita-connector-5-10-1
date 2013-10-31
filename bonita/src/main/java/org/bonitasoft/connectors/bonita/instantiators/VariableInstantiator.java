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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.MultipleInstancesInstantiator;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class VariableInstantiator extends MultipleInstancesInstantiator {

  private String name;
  private List<Object> values;

  public void setName(String name) {
    this.name = name;
  }

  public void setValues(List<Object> values) {
    this.values = values;
  }

  @Override
  protected List<Map<String, Object>> defineActivitiesContext() throws Exception {
    final List<Map<String, Object>> contexts = new ArrayList<Map<String,Object>>();
    for (Object value : values) {
      Map<String, Object> context = new HashMap<String, Object>();
      context.put(name, value);
      contexts.add(context);
    }
    return contexts;
  }

  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> errors = super.validateValues();
    if ("".equals(name.trim())) {
      ConnectorError error = new ConnectorError("name", new IllegalArgumentException("Name is an empty string"));
      errors.add(error);
    }
    return errors;
  }

}
