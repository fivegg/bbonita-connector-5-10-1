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
package org.bonitasoft.connectors.legacy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ow2.bonita.connector.core.MultipleInstancesInstantiator;
import org.ow2.bonita.definition.MultiInstantiator;
import org.ow2.bonita.definition.MultiInstantiatorDescriptor;
import org.ow2.bonita.facade.QueryAPIAccessor;
import org.ow2.bonita.facade.impl.StandardQueryAPIAccessorImpl;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class MultiInstantiatorInstantiator extends MultipleInstancesInstantiator {
  
  private String className;
  private String variableName;

  public void setClassName(String className) {
    this.className = className;
  }

  public void setVariableName(String variableName) {
    this.variableName = variableName;
  }

  @Override @SuppressWarnings("unchecked")
  protected List<Map<String, Object>> defineActivitiesContext() throws Exception {
    Class <? extends MultiInstantiator> multiInstantiatorClass = (Class<? extends MultiInstantiator>)
      Class.forName(className, true, MultiInstantiatorInstantiator.class.getClassLoader());
    MultiInstantiator multiInstantiator = multiInstantiatorClass.newInstance();
    final QueryAPIAccessor queryAccessor = new StandardQueryAPIAccessorImpl();
    MultiInstantiatorDescriptor descriptor =
      multiInstantiator.execute(queryAccessor, getProcessInstanceUUID(), getActivityName(), getIterationId());
    List<Object> values = descriptor.getVariableValues();
    List<Map<String, Object>> contexts = new ArrayList<Map<String,Object>>();
    for (Object value : values) {
      Map<String, Object> context = new HashMap<String, Object>();
      context.put(variableName, value);
      contexts.add(context);
    }
    return contexts;
  }

}
