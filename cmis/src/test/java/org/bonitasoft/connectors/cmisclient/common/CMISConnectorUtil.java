/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.connectors.cmisclient.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bonitasoft.connectors.cmisclient.CheckOut;
import org.bonitasoft.connectors.cmisclient.DeleteObject;
import org.bonitasoft.connectors.cmisclient.GetObject;
import org.bonitasoft.connectors.cmisclient.UploadFile;

/**
 * @author Yanyan Liu
 */
public final class CMISConnectorUtil {

    private static final Logger LOG = Logger.getLogger(CMISConnectorUtil.class.getName());

    private static final File file = new File(CMISConnectorUtil.class.getClassLoader().getResource(".").getPath(), "config.properties");

    private static InputStream inputStream = null;

    protected static final Properties properties = new Properties();

    static {
        try {
            inputStream = new FileInputStream(file);
            properties.load(inputStream);
        } catch (final IOException e) {
            e.printStackTrace();
            if (LOG.isLoggable(Level.WARNING)) {
                LOG.warning(e.getMessage());
            }
        } finally {
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                    if (LOG.isLoggable(Level.WARNING)) {
                        LOG.warning(e.getMessage());
                    }
                }
        }
    }

    public static void configConnector(final AbstractCmisConnector cmisConnector) {
        cmisConnector.setUsername(properties.getProperty("username"));
        cmisConnector.setPassword(properties.getProperty("password"));
        cmisConnector.setUrl(properties.getProperty("url"));
        cmisConnector.setBinding_type(properties.getProperty("binding_type"));
        cmisConnector.setRepositoryName(properties.getProperty("repositoryName"));
    }

    /**
     * @param path
     * @return
     * @throws Exception
     */
    public static String getDocumentIDbyPath(String path) throws Exception {
        final GetObject getObject = new GetObject();
        configConnector(getObject);
        getObject.setObjectPath(path);
        getObject.execute();
        final String documentID = getObject.getCmisObject().getId();
        return documentID;
    }

    public static String getProperty(final String propertyName) {
        return properties.getProperty(propertyName);
    }

    /**
     * @param fileName
     * @return
     * @throws Exception
     */
    public static String getPWCDocumentId(String fileName) throws Exception {
        // create document
        createDocument(fileName);
        final String path = "/" + fileName + ".txt";
        // get document id
        final String docId = getDocumentIDbyPath(path);
        // get pwcDocumentId
        String pwcDocumentId = getPWCId(docId);
        return pwcDocumentId;
    }

    /**
     * @param docId
     * @return
     * @throws Exception
     */
    public static String getPWCId(String docId) throws Exception {
        final CheckOut checkOutConnector = new CheckOut();
        configConnector(checkOutConnector);
        checkOutConnector.setDocumentID(docId);
        checkOutConnector.execute();
        final String pWCdocumentId = checkOutConnector.getPWCdocumentId();
        return pWCdocumentId;
    }

    /**
     * @param fileName
     * @throws IOException
     */
    public static void createDocument(String fileName) throws IOException {
        final File f = File.createTempFile(fileName, "txt");
        f.createNewFile();
        final String fullFilename = fileName + ".txt";
        final UploadFile uploadConnector = new UploadFile();
        uploadConnector.uploadFileToFolder(properties.getProperty("username"), properties.getProperty("password"), properties.getProperty("url"),
                properties.getProperty("binding_type"), properties.getProperty("repositoryName"), "/", new FileInputStream(f), fullFilename);
    }

    /**
     * @param path
     * @throws Exception
     */
    public static void deleteDocByPath(String path) throws Exception {
        DeleteObject deleteObject = new DeleteObject();
        configConnector(deleteObject);
        deleteObject.setObjectPath(path);
        deleteObject.execute();
    }

    public static String getFileName() {
        String filename = CMISConnectorUtil.getProperty("filename") + System.currentTimeMillis();
        return filename;
    }

}
