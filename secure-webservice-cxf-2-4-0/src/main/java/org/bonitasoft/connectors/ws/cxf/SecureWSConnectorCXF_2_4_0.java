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
package org.bonitasoft.connectors.ws.cxf;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Charles Souillard
 * 
 */
public class SecureWSConnectorCXF_2_4_0 extends ProcessConnector {

  protected String authUserName;
  protected String authPassword;
  protected String serviceNS;
  protected String serviceName;
  protected String portName;
  protected String endpointAddress;
  protected String binding;
  protected String soapAction;
  protected boolean buildResponseDocumentEnveloppe;
  protected boolean buildResponseDocumentBody;
  protected boolean printRequestAndResponse;
  protected long readTimeout = -1;
  protected long connectionTimeout = -1;
  protected Map<String, List<String>> httpHeaders;
  protected String enveloppe;

  protected Document responseDocumentEnveloppe;
  protected Document responseDocumentBody;
  protected Source sourceResponse;

  private Transformer transformer;

  @Override
  protected void executeConnector() throws Exception {
    final QName serviceQName = new QName(serviceNS, serviceName);
    final QName portQName = new QName(serviceNS, portName);
    final Service service = Service.create(serviceQName);
    service.addPort(portQName, binding, endpointAddress);

    final Dispatch<Source> dispatch = service.createDispatch(portQName, Source.class, Service.Mode.MESSAGE);

    if (authUserName != null) {
      dispatch.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, authUserName);
      dispatch.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, authPassword);
    }

    if (soapAction != null) {
      dispatch.getRequestContext().put(BindingProvider.SOAPACTION_USE_PROPERTY, true);
      dispatch.getRequestContext().put(BindingProvider.SOAPACTION_URI_PROPERTY, soapAction);
    }

    /*
     * Adding HTTP header
     */

    if (httpHeaders != null) {
      dispatch.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, httpHeaders);
    }

    /*
     * Adding timeout
     */

    HTTPClientPolicy httpClientPolicy = null;

    if (readTimeout != -1 || connectionTimeout != -1) {
      final Client client = ((org.apache.cxf.jaxws.DispatchImpl) dispatch).getClient();
      final HTTPConduit http = (HTTPConduit) client.getConduit();
      httpClientPolicy = new HTTPClientPolicy();
      httpClientPolicy.setAllowChunking(false);
      http.setClient(httpClientPolicy);
    }
    if (readTimeout != -1) {
      httpClientPolicy.setReceiveTimeout(readTimeout);
    }
    if (connectionTimeout != -1) {
      httpClientPolicy.setConnectionTimeout(connectionTimeout);
    }

    // this.sourceResponse = client.invoke(new StreamSource(new StringReader(enveloppe)));
    sourceResponse = dispatch.invoke(new StreamSource(new StringReader(enveloppe)));

    if (isBuildResponseDocumentEnveloppe() || isBuildResponseDocumentBody()) {
      final DOMResult result = new DOMResult();
      getTransformer().transform(sourceResponse, result);
      if (result.getNode() instanceof Document) {
        responseDocumentEnveloppe = (Document) result.getNode();
      } else {
        responseDocumentEnveloppe = result.getNode().getOwnerDocument();
      }
    }

    if (isBuildResponseDocumentBody()) {
      if (responseDocumentEnveloppe != null) {
          try {
              responseDocumentBody = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
              final Node bodyContent = getEnveloppeBodyContent(responseDocumentEnveloppe);
              final Node clonedBodyContent = bodyContent.cloneNode(true);
              responseDocumentBody.adoptNode(clonedBodyContent);
              responseDocumentBody.importNode(clonedBodyContent, true);
              responseDocumentBody.appendChild(clonedBodyContent);
          } catch (ParserConfigurationException e) {
              throw new Exception("The output of the webservice is malformed", e);
          } catch (DOMException e) {
              throw new Exception("The output of the webservice is malformed", e);
          }
      }
    }

    if (printRequestAndResponse) {
      System.err.println("Request:");
      System.err.println(enveloppe);
      try {
        System.err.println("SourceResponse:");
        getTransformer().transform(sourceResponse, new StreamResult(System.err));

        if (isBuildResponseDocumentEnveloppe() || isBuildResponseDocumentBody()) {
          System.err.println("ResponseDocumentEnveloppe:");
          getTransformer().transform(new DOMSource(responseDocumentEnveloppe), new StreamResult(System.err));
        }
        if (isBuildResponseDocumentBody()) {
          System.err.println("ResponseDocumentBody:");
          getTransformer().transform(new DOMSource(responseDocumentBody), new StreamResult(System.err));
        }
      } catch (final TransformerException e) {
        e.printStackTrace();
      }
    }
  }

  private Node getEnveloppeBodyContent(final Document enveloppe) {
    final Node enveloppeNode = enveloppe.getFirstChild();
    final NodeList children = enveloppeNode.getChildNodes();

    Node enveloppeBody = null;

    for (int i = 0; i < children.getLength(); i++) {
      final Node child = children.item(i);
      if (child instanceof Element) {
        final Element element = (Element) child;
        if ("Body".equalsIgnoreCase(element.getLocalName())) {
          enveloppeBody = child;
          break;
        }
      }
    }
    if (enveloppeBody == null) {
      return enveloppeNode;
    }
    return enveloppeBody.getFirstChild();
  }

  private Transformer getTransformer() throws TransformerConfigurationException, TransformerFactoryConfigurationError {
    if (transformer == null) {
      transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    }
    return transformer;
  }

  private boolean isBuildResponseDocumentEnveloppe() {
    return buildResponseDocumentEnveloppe;
  }

  public void setBuildResponseDocumentEnveloppe(final boolean buildResponseDocumentEnveloppe) {
    this.buildResponseDocumentEnveloppe = buildResponseDocumentEnveloppe;
  }

  private boolean isBuildResponseDocumentBody() {
    return buildResponseDocumentBody;
  }

  public void setBuildResponseDocumentBody(final boolean buildResponseDocumentBody) {
    this.buildResponseDocumentBody = buildResponseDocumentBody;
  }

  @Override
  protected List<ConnectorError> validateValues() {
    return null;
  }

  public void setEndpointAddress(final String endpointAddress) {
    this.endpointAddress = endpointAddress;
  }

  public void setBinding(final String binding) {
    this.binding = binding;
  }

  public void setServiceNS(final String serviceNS) {
    this.serviceNS = serviceNS;
  }

  public void setServiceName(final String serviceName) {
    this.serviceName = serviceName;
  }

  public void setPortName(final String portName) {
    this.portName = portName;
  }

  public void setSoapAction(final String soapAction) {
    this.soapAction = soapAction;
  }

  public void setAuthUserName(final String authUserName) {
    this.authUserName = authUserName;
  }

  public void setAuthPassword(final String authPassword) {
    this.authPassword = authPassword;
  }

  public void setPrintRequestAndResponse(final boolean printRequestAndResponse) {
    this.printRequestAndResponse = printRequestAndResponse;
  }

  public void setEnveloppe(final String enveloppe) {
    this.enveloppe = enveloppe;
  }

  public Source getSourceResponse() {
    return sourceResponse;
  }

  public Document getResponseDocumentEnveloppe() {
    return responseDocumentEnveloppe;
  }

  public Document getResponseDocumentBody() {
    return responseDocumentBody;
  }

  public void setReadTimeout(final long readTimeout) {
    this.readTimeout = readTimeout;
  }

  public void setConnectionTimeout(final long connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public void setHttpHeaders(final List<List<Object>> httpHeaders) {
    final Map<String, String> map = bonitaListToMap(httpHeaders, String.class, String.class);
    if (map != null) {
      final Map<String, List<String>> headers = new HashMap<String, List<String>>();
      setHttpHeaders(headers);
      for (final Map.Entry<String, String> entry : map.entrySet()) {
        headers.put(entry.getKey(), split(entry.getValue()));
      }
    }
  }

  private List<String> split(final String commaSeparatedList) {
    if (commaSeparatedList == null) {
      return null;
    }
    final String[] headerValues = commaSeparatedList.split(",");
    if (headerValues.length > 0) {
      final List<String> list = new ArrayList<String>();
      for (final String headerValue : headerValues) {
        list.add(headerValue.trim());
      }
      return list;
    }
    return null;
  }

  public void setHttpHeaders(final Map<String, List<String>> httpHeaders) {
    this.httpHeaders = httpHeaders;
  }

}
