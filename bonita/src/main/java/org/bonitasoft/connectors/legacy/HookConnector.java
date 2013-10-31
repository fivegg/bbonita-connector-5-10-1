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

import java.util.List;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;
import org.ow2.bonita.definition.Hook;
import org.ow2.bonita.definition.TxHook;
import org.ow2.bonita.facade.APIAccessor;
import org.ow2.bonita.facade.QueryAPIAccessor;
import org.ow2.bonita.facade.QueryRuntimeAPI;
import org.ow2.bonita.facade.impl.StandardQueryAPIAccessorImpl;
import org.ow2.bonita.facade.runtime.ActivityInstance;
import org.ow2.bonita.util.AccessorUtil;

/**
 * 
 * @author Mickael Istria, Matthieu Chaffotte
 *
 */
public class HookConnector extends ProcessConnector {

  private String className;

  public void setClassName(String className) {
    this.className = className;
  }

  @Override
  protected void executeConnector() throws Exception {
    Class<?> hookClass = (Class<?>) Class.forName(className, true, this.getClass().getClassLoader());
    Object object = hookClass.newInstance();
    final APIAccessor accessor = getApiAccessor();
    QueryRuntimeAPI queryRuntimeApi = accessor.getQueryRuntimeAPI(AccessorUtil.QUERYLIST_JOURNAL_KEY);
    ActivityInstance activity = queryRuntimeApi.getActivityInstance(getActivityInstanceUUID());
    if (object instanceof TxHook) {
      TxHook hook = (TxHook) object;
      hook.execute(accessor, activity);
    } else if (object instanceof Hook) {
      Hook hook = (Hook) object;
      final QueryAPIAccessor queryAccessor = new StandardQueryAPIAccessorImpl();
      hook.execute(queryAccessor, activity);
    }
  }

  @Override
  protected List<ConnectorError> validateValues() {
    return null;
  }

}
