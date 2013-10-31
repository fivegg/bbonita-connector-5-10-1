/**
 * Created by Jordi Anguela
 * 
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

package org.bonitasoft.connectors.alfresco;

import java.util.List;

import javax.xml.namespace.QName;

import org.apache.abdera.model.Document;
import org.apache.abdera.model.Element;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.ExtensibleElement;
import org.apache.abdera.model.Feed;
import org.apache.abdera.protocol.Response.ResponseType;
import org.bonitasoft.connectors.alfresco.common.AlfrescoResponse;
import org.bonitasoft.connectors.alfresco.common.AlfrescoRestClient;

public class TestAlfrescoConnector {

  private static final String HOST  = "localhost";
  private static final String PORT  = "8080";
  private static final String USER  = "admin";
  private static final String PASSWORD = "pass";

  AlfrescoRestClient alfCon = new AlfrescoRestClient(HOST, PORT, USER, PASSWORD);
  AlfrescoResponse response = null;

  public void createFolderByPath(String parentFolder, String folderName, String folderDescription) {
    try {
      response = alfCon.createFolderByPath(parentFolder, folderName, folderDescription);

      System.out.println("Response type : " + response.getResponseType());
      System.out.println("Status code is: " + response.getStatusCode());
      System.out.println("Status text is: " + response.getStatusText());

      if (ResponseType.SUCCESS.toString().equals(response.getResponseType())) {
        Document<Element> doc = response.getDocument();
        Entry responseEntry = (Entry) doc.getRoot();
        System.out.println("Title  : " + responseEntry.getTitle());     // prints newFoldersName
        System.out.println("Content: " + responseEntry.getContent());   // prints newFoldersID
      } else {
        // printStackTrace
        System.out.println("StackTrace: " + response.getStackTrace());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void listFolderByPath(String folderPath) {
    try {
      response = alfCon.listFolderByPath(folderPath);

      System.out.println("Response type : " + response.getResponseType());
      System.out.println("Status code is: " + response.getStatusCode());
      System.out.println("Status text is: " + response.getStatusText());

      if (ResponseType.SUCCESS.toString().equals(response.getResponseType())) {
        Document<Element> doc = response.getDocument();
        Feed feed = (Feed) doc.getRoot();
        System.out.println("FeedTitle: " + feed.getTitle());

        for (Entry entry : feed.getEntries()) {
          System.out.println("-------------- ENTRY ---------------");
          System.out.println("          Title : " + entry.getTitle());
          System.out.println("             ID : " + entry.getId());
          System.out.println("    ContentType : " + entry.getContentType());
          System.out.println("ContentMimeType : " + entry.getContentMimeType());
        }
      } else {
        // printStackTrace
        System.out.println("StackTrace: " + response.getStackTrace());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void deleteFolderByPath(String folderPath) {
    try {
      response = alfCon.deleteFolderByPath(folderPath);

      System.out.println("Response type : " + response.getResponseType());
      System.out.println("Status code is: " + response.getStatusCode());
      System.out.println("Status text is: " + response.getStatusText());

      if (ResponseType.SUCCESS.toString().equals(response.getResponseType())) {
        // no content returned
      } else {
        // printStackTrace
        System.out.println("StackTrace: " + response.getStackTrace());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void downloadFileById(String fileId, String outputFileFolder, String outputFileName) {
    try {
      response = alfCon.downloadFileById(fileId, outputFileFolder, outputFileName);

      System.out.println("Response type : " + response.getResponseType());
      System.out.println("Status code is: " + response.getStatusCode());
      System.out.println("Status text is: " + response.getStatusText());

      if (ResponseType.SUCCESS.toString().equals(response.getResponseType())) {
        // check that files was correctly download
      } else {
        // printStackTrace
        System.out.println("StackTrace: " + response.getStackTrace());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void uploadFile(Object fileObject, String fileName, String description, String mimeType, String destinationFolder)
  {
    try
    {
    	// Setup connector & execute it
    	UploadFileConnector connector = new UploadFileConnector();
    	connector.setHost(HOST);
    	connector.setPort(Long.parseLong(PORT));
    	connector.setPassword(PASSWORD);
    	connector.setUsername(USER);
    	connector.setFileObject(fileObject);
    	connector.setFileName(fileName);
    	connector.setDescription(description);
    	connector.setMimeType(mimeType);
    	connector.setDestinationFolder(destinationFolder);
    	connector.execute();
    	// Analyze Alfresco response
      response = connector.response;

      System.out.println("Response type : " + response.getResponseType());
      System.out.println("Status code is: " + response.getStatusCode());
      System.out.println("Status text is: " + response.getStatusText());

      if (ResponseType.SUCCESS.toString().equals(response.getResponseType())) {
        Document<Element> doc = response.getDocument();
        Entry responseEntry = (Entry) doc.getRoot();
        System.out.println("Title: " + responseEntry.getTitle());
        System.out.println("ID   : " + responseEntry.getId()); 
      } else {
        // printStackTrace
        System.out.println("StackTrace: " + response.getStackTrace());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void deleteItemById(String itemId) {
    try {
      response = alfCon.deleteItemById(itemId);

      System.out.println("Response type : " + response.getResponseType());
      System.out.println("Status code is: " + response.getStatusCode());
      System.out.println("Status text is: " + response.getStatusText());

      if (ResponseType.SUCCESS.toString().equals(response.getResponseType())) {
        // no content returned
      } else {
        // printStackTrace
        System.out.println("StackTrace: " + response.getStackTrace());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void checkout(String fileId) {
    try {
      response = alfCon.checkout(fileId);

      System.out.println("Response type : " + response.getResponseType());
      System.out.println("Status code is: " + response.getStatusCode());
      System.out.println("Status text is: " + response.getStatusText());

      if (ResponseType.SUCCESS.toString().equals(response.getResponseType())) {
        Document<Element> doc = response.getDocument();
        Entry responseEntry = (Entry) doc.getRoot();
        System.out.println("Title: " + responseEntry.getTitle());
        System.out.println("ID   : " + responseEntry.getId()); 
      } else {
        // printStackTrace
        System.out.println("StackTrace: " + response.getStackTrace());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void listCheckedOutFiles() {
    try {
      response = alfCon.listCheckedOutFiles();

      System.out.println("Response type : " + response.getResponseType());
      System.out.println("Status code is: " + response.getStatusCode());
      System.out.println("Status text is: " + response.getStatusText());

      if (ResponseType.SUCCESS.toString().equals(response.getResponseType())) {
        Document<Element> doc = response.getDocument();
        Feed feed = (Feed) doc.getRoot();
        System.out.println("FeedTitle: " + feed.getTitle());

        for (Entry entry : feed.getEntries()) {
          System.out.println("-------------- ENTRY ---------------");
          System.out.println("          Title : " + entry.getTitle());
          System.out.println("             ID : " + entry.getId());
          System.out.println("    ContentType : " + entry.getContentType());
          System.out.println("ContentMimeType : " + entry.getContentMimeType());
        }
      } else {
        // printStackTrace
        System.out.println("StackTrace: " + response.getStackTrace());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void cancelCheckout(String fileId) {
    try {
      response = alfCon.cancelCheckout(fileId);

      System.out.println("Response type : " + response.getResponseType());
      System.out.println("Status code is: " + response.getStatusCode());
      System.out.println("Status text is: " + response.getStatusText());

      if (ResponseType.SUCCESS.toString().equals(response.getResponseType())) {
        // no content
      } else {
        // printStackTrace
        System.out.println("StackTrace: " + response.getStackTrace());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateCheckedOutFile(String fileAbsolutePath, String description, String mimeType, String checkedOutFileId) {
    try {
      response = alfCon.updateCheckedOutFile(fileAbsolutePath, description, mimeType, checkedOutFileId);

      System.out.println("Response type : " + response.getResponseType());
      System.out.println("Status code is: " + response.getStatusCode());
      System.out.println("Status text is: " + response.getStatusText());

      if (ResponseType.SUCCESS.toString().equals(response.getResponseType())) {
        Document<Element> doc = response.getDocument();
        Entry responseEntry = (Entry) doc.getRoot();
        System.out.println("Title: " + responseEntry.getTitle());
        System.out.println("ID   : " + responseEntry.getId()); 
      } else {
        // printStackTrace
        System.out.println("StackTrace: " + response.getStackTrace());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void checkin(String checkedOutFileId, boolean isMajorVersion, String checkinComments) {
    try {
      response = alfCon.checkin(checkedOutFileId, isMajorVersion, checkinComments);

      System.out.println("Response type : " + response.getResponseType());
      System.out.println("Status code is: " + response.getStatusCode());
      System.out.println("Status text is: " + response.getStatusText());

      if (ResponseType.SUCCESS.toString().equals(response.getResponseType())) {
        Document<Element> doc = response.getDocument();
        Entry responseEntry = (Entry) doc.getRoot();
        System.out.println("ID: " + responseEntry.getId()); 
      } else {
        // printStackTrace
        System.out.println("StackTrace: " + response.getStackTrace());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void fileVersions(String fileId) {
    try {
      response = alfCon.fileVersions(fileId);

      System.out.println("Response type : " + response.getResponseType());
      System.out.println("Status code is: " + response.getStatusCode());
      System.out.println("Status text is: " + response.getStatusText());

      if (ResponseType.SUCCESS.toString().equals(response.getResponseType())) {
        Document<Element> doc = response.getDocument();
        Feed feed = (Feed) doc.getRoot();
        System.out.println("FeedTitle: " + feed.getTitle());

        for (Entry entry : feed.getEntries()) {
          System.out.println("-------------- ENTRY ---------------");
          System.out.println("          Title : " + entry.getTitle());
          System.out.println("             ID : " + entry.getId());
          System.out.println("    ContentType : " + entry.getContentType());
          System.out.println("ContentMimeType : " + entry.getContentMimeType());

          ExtensibleElement objElement = entry.getExtension(new QName(AlfrescoRestClient.NS_CMIS_RESTATOM, "object", AlfrescoRestClient.CMISRA));
          ExtensibleElement propsElement = objElement.getExtension(new QName(AlfrescoRestClient.NS_CMIS_CORE, "properties", AlfrescoRestClient.CMIS));

          List<ExtensibleElement> listExtensions = propsElement.getExtensions(new QName(AlfrescoRestClient.NS_CMIS_CORE, "propertyId", AlfrescoRestClient.CMIS));
          for(ExtensibleElement tmpExtension : listExtensions) {
            String attValue = tmpExtension.getAttributeValue("propertyDefinitionId");
            if ("cmis:objectId".equals(attValue)) {
              Element valueElement = tmpExtension.getExtension(new QName(AlfrescoRestClient.NS_CMIS_CORE, "value", AlfrescoRestClient.CMIS));
              String objectId = valueElement.getText();				
              System.out.println("       objectId : " + objectId);
            }
          }
        }
      } else {
        // printStackTrace
        System.out.println("StackTrace: " + response.getStackTrace());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void downloadFileByStoreAndId(String store, String fileId, String outputFileFolder, String outputFileName) {
    try {
      response = alfCon.downloadFileByStoreAndId(store, fileId, outputFileFolder, outputFileName);

      System.out.println("Response type : " + response.getResponseType());
      System.out.println("Status code is: " + response.getStatusCode());
      System.out.println("Status text is: " + response.getStatusText());

      if (ResponseType.SUCCESS.toString().equals(response.getResponseType())) {
        // check that files was correctly download
      } else {
        // printStackTrace
        System.out.println("StackTrace: " + response.getStackTrace());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
