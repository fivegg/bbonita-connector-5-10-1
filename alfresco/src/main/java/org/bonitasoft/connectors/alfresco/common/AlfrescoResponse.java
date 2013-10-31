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
package org.bonitasoft.connectors.alfresco.common;

import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;

/**
 * 
 * @author Jordi Anguela
 *
 */
public class AlfrescoResponse {

  private String responseType = "";
  private String statusCode   = "";
  private String statusText   = "";
  private Document<Element> document; // Entry or Feed
  private String stackTrace   = "";

  public AlfrescoResponse() {
  }

  public AlfrescoResponse(String type, String code, String text) {
    responseType = type;
    statusCode = code;
    statusText = text;
  }

  public String getResponseType() {
    return responseType;
  }

  public void setResponseType(String responseType) {
    this.responseType = responseType;
  }

  public String getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(String statusCode) {
    this.statusCode = statusCode;
  }

  public String getStatusText() {
    return statusText;
  }

  public void setStatusText(String statusText) {
    this.statusText = statusText;
  }

  public Document<Element> getDocument() {
    return document;
  }

  public void setDocument(Document<Element> document) {
    this.document = document;
  }

  public String getStackTrace() {
    return stackTrace;
  }

  public void setStackTrace(String stackTrace) {
    this.stackTrace = stackTrace;
  }
}
