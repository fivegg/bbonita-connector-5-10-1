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
package org.bonitasoft.connectors.cxf;

import java.io.StringReader;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;

/**
 * 
 * @author Charles Souillard
 *
 */
public class CXFConnector extends ProcessConnector {

  private String targetNS;
  private String serviceName;
  private String portName;
  private String request;
  private String endpointAddress;
  private String binding;
  private String soapAction;

  private Source response;

  @Override
  protected void executeConnector() throws Exception {
    final QName serviceQName = new QName(targetNS, serviceName);
    final QName portQName = new QName(targetNS, portName);
    final Service service = Service.create(serviceQName);
    service.addPort(portQName, binding, endpointAddress);
    final Dispatch<Source> dispatch = service.createDispatch(portQName, Source.class, Service.Mode.MESSAGE);
    
    if (soapAction != null) {
      dispatch.getRequestContext().put(BindingProvider.SOAPACTION_USE_PROPERTY, true);
      dispatch.getRequestContext().put(BindingProvider.SOAPACTION_URI_PROPERTY, soapAction);
    }
    
    this.response = dispatch.invoke(new StreamSource(new StringReader(request)));
  }

  @Override
  protected List<ConnectorError> validateValues() {
    return null;
  }

  public Source getResponse() {
    return response;
  }

  public void setEndpointAddress(String endpointAddress) {
    this.endpointAddress = endpointAddress;
  }

  public void setBinding(String binding) {
    this.binding = binding;
  }

  public void setTargetNS(String targetNS) {
    this.targetNS = targetNS;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public void setPortName(String portName) {
    this.portName = portName;
  }

  public void setRequest(String request) {
    this.request = request;
  }
  
  public void setSoapAction(String soapAction) {
    this.soapAction = soapAction;
  }
}
