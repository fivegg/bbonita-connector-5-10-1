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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.httpclient.methods.GetMethod;
import org.bonitasoft.connectors.webdav.exo.common.ExoConnector;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Jordi Anguela, Yanyan Liu
 * 
 */
public class ExoDownloadFile extends ExoConnector {

    private String fileUri;
    private String outputFileFolder;
    private String outputFileName;

    /**
     * set the fileUri
     * 
     * @param fileUri the file URI
     */
    public void setFileUri(final String fileUri) {
        this.fileUri = fileUri;
    }

    /**
     * set the outputFileFolder
     * 
     * @param outputFileFolder the folder for the output file
     */
    public void setOutputFileFolder(final String outputFileFolder) {
        this.outputFileFolder = outputFileFolder;
    }

    /**
     * set the outputFileName
     * 
     * @param outputFileName the output file name
     */
    public void setOutputFileName(final String outputFileName) {
        this.outputFileName = outputFileName;
    }

    @Override
    protected void validateFunctionParameters(final List<ConnectorError> errors) {
        this.validateUri(errors, fileUri, "fileUri");

        if (!(outputFileName.length() > 0)) {
            errors.add(new ConnectorError("outputFileName", new IllegalArgumentException("cannot be empty")));
        }
    }

    @Override
    protected void executeFunction() throws Exception {
        this.downloadFile(fileUri, outputFileFolder, outputFileName);
    }

    /**
     * download fileUri specified file
     * 
     * @param fileUri
     * @param outputFileFolder
     * @param outputFileName
     * @throws Exception
     */
    public void downloadFile(final String fileUri, final String outputFileFolder, final String outputFileName) throws Exception {

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("downloadFile '" + fileUri + "' and save it to '" + outputFileFolder + outputFileName + "'");
        }

        final GetMethod httpMethod = new GetMethod(fileUri);
        client.executeMethod(httpMethod);

        processResponse(httpMethod, false);

        // Save output file
        if (httpMethod.getResponseContentLength() > 0) {
            final InputStream inputStream = httpMethod.getResponseBodyAsStream();
            final File responseFile = new File(outputFileFolder + outputFileName);
            OutputStream outputStream = new FileOutputStream(responseFile);
            final byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            outputStream.close();
            inputStream.close();
        }

        httpMethod.releaseConnection();
    }

}
