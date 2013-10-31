/**
 * Copyright (C) 2009-2011 BonitaSoft S.A.
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
package org.bonitasoft.connectors.webdav.exo;

import java.util.List;
import java.util.logging.Level;

import org.apache.jackrabbit.webdav.client.methods.UncheckoutMethod;
import org.bonitasoft.connectors.webdav.exo.common.ExoConnector;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Jordi Anguela, Yanyan Liu
 * 
 */
public class ExoCancelCheckout extends ExoConnector {

    private String uri;

    /**
     * set the uri
     * 
     * @param uri the URI
     */
    public void setUri(final String uri) {
        this.uri = uri;
    }

    @Override
    protected void validateFunctionParameters(final List<ConnectorError> errors) {
        this.validateUri(errors, uri, "uri");
    }

    @Override
    protected void executeFunction() throws Exception {
        this.cancelCheckout(uri);
    }

    /**
     * cancel checkout for the URI specified file, the change will be discarded.
     * 
     * @param uri
     * @throws Exception
     */
    public void cancelCheckout(final String uri) throws Exception {

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("cancelCheckout '" + uri + "'");
        }

        final UncheckoutMethod httpMethod = new UncheckoutMethod(uri);
        client.executeMethod(httpMethod);

        processResponse(httpMethod, true);
        httpMethod.releaseConnection();
    }

}
