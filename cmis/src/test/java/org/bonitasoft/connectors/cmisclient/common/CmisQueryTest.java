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

package org.bonitasoft.connectors.cmisclient.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.bonitasoft.connectors.cmisclient.CheckIfObjectExist;
import org.bonitasoft.connectors.cmisclient.CreateSubfolder;
import org.bonitasoft.connectors.cmisclient.DeleteFolderByPath;
import org.bonitasoft.connectors.cmisclient.DeleteObject;
import org.bonitasoft.connectors.cmisclient.DownloadDocument;
import org.bonitasoft.connectors.cmisclient.GetObject;
import org.bonitasoft.connectors.cmisclient.GetRepositories;
import org.bonitasoft.connectors.cmisclient.GetRootFolder;
import org.bonitasoft.connectors.cmisclient.ListFolderByPath;
import org.bonitasoft.connectors.cmisclient.ListVersionOfDocument;
import org.bonitasoft.connectors.cmisclient.ModifyDocument;
import org.bonitasoft.connectors.cmisclient.UploadFile;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * 
 * @author Frederic Bouquet
 * 
 */
public class CmisQueryTest extends TestCase {

	private static final String WEBSERVICE = "WebService";
	private static final String HOSTNAME_WS = "http://cmis.alfresco.com/cmis";
	private static final String ATOM = "ATOM";
	private static final String HOSTNAME_ATOM = "http://cmis.alfresco.com/service/cmis";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "admin";
	private String repositoryId;
	private String repositoryName;
	private AbstractCmisConnector abstractCmisConnector;

	public CmisQueryTest(String name) {
		super(name);
		abstractCmisConnector = new AbstractCmisConnector() {
			@Override
			protected void executeConnector() throws Exception {
			}

			@Override
			protected List<ConnectorError> validateValues() {
				return null;
			}

		};
		this.repositoryName = "Main Repository";
		GetRepositories connector = new GetRepositories();
		this.repositoryId = connector.getRepositoryIdByName(USERNAME, PASSWORD,
				HOSTNAME_ATOM, ATOM, repositoryName);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetRepositoriesViaAtom() throws Exception {
		GetRepositories connector = new GetRepositories();
		connector.setUsername(USERNAME);
		connector.setPassword(PASSWORD);
		connector.setUrl(HOSTNAME_ATOM);
		connector.setBinding_type(ATOM);
		connector.execute();
		ArrayList<Repository> repos = connector.getRepositories();
		Assert.assertEquals(1, repos.size());
	}

	public void testGetRepositoriesViaWebServices() throws Exception {

		GetRepositories connector = new GetRepositories();
		connector.setUsername(USERNAME);
		connector.setPassword(PASSWORD);
		connector.setUrl(HOSTNAME_WS);
		connector.setBinding_type(WEBSERVICE);
		connector.execute();
		ArrayList<Repository> repos = connector.getRepositories();
		Assert.assertEquals(1, repos.size());
	}

	public void testGetFirstRepositoryId() throws Exception {

		GetRepositories connector = new GetRepositories();
		connector.setUsername(USERNAME);
		connector.setPassword(PASSWORD);
		connector.setUrl(HOSTNAME_ATOM);
		connector.setBinding_type(ATOM);
		connector.execute();
		String id = connector.getRepositories().get(0).getId();
		assertEquals(this.repositoryId, id);
	}

	public void testCreateATOMSessionById() {
		Session s = abstractCmisConnector.createSessionById(USERNAME, PASSWORD,
				HOSTNAME_ATOM, ATOM, repositoryId);
		assertNotNull(s);
	}

	public void testCreateWSSessionById() {
		Session s = abstractCmisConnector.createSessionById(USERNAME, PASSWORD,
				HOSTNAME_WS, WEBSERVICE, repositoryId);
		assertNotNull(s);
	}

	public void testCreateSessionByName() {
		Session s = abstractCmisConnector.createSessionByName(USERNAME,
				PASSWORD, HOSTNAME_WS, WEBSERVICE, repositoryName);
		assertNotNull(s);
	}

	public void testGetRootFolder() throws Exception {
		GetRootFolder connector = new GetRootFolder();
		connector.setUsername(USERNAME);
		connector.setPassword(PASSWORD);
		connector.setUrl(HOSTNAME_ATOM);
		connector.setBinding_type(ATOM);
		connector.setRepositoryName(repositoryName);
		connector.execute();
		Folder rootFolder = connector.getRootFolder();
		String folderId = rootFolder.getId();
		assertEquals(
				"workspace://SpacesStore/87b2f129-3ad0-4a46-a6ea-05ecbfb54aa1",
				folderId);
	}

	public void testGetFolderByPath() {

		Folder folder = abstractCmisConnector.getFolderByPath(USERNAME,
				PASSWORD, HOSTNAME_ATOM, ATOM, repositoryName, "/Sites");
		assertEquals(
				"workspace://SpacesStore/61573686-3f75-4b42-81e0-7ed9fa23d4b4",
				folder.getId());
	}

	public void testGetObjectByPath() throws Exception {
		GetObject connector = new GetObject();
		connector.setUsername(USERNAME);
		connector.setPassword(PASSWORD);
		connector.setUrl(HOSTNAME_ATOM);
		connector.setBinding_type(ATOM);
		connector.setRepositoryName(repositoryName);
		connector.setObjectPath("/Sites");
		connector.execute();
		CmisObject object = connector.getCmisObject();
		assertNotNull("return object was null", object);
		assertNotNull("return type was null", object.getBaseType());
	}

	public void testListFolderByPath() throws Exception {
		ListFolderByPath connector = new ListFolderByPath();
		connector.setUsername(USERNAME);
		connector.setPassword(PASSWORD);
		connector.setUrl(HOSTNAME_ATOM);
		connector.setBinding_type(ATOM);
		connector.setRepositoryName(repositoryName);
		connector.setFolderPath("/");
		connector.execute();
		Iterable<CmisObject> iterable = connector.getFolderContent();
		assertTrue(iterable.iterator().hasNext());
	}

	public void testCreateSubFolderByPath() throws Exception {
		DeleteFolderByPath deleteConnector = new DeleteFolderByPath();
		try {
			deleteConnector.setUsername(USERNAME);
			deleteConnector.setPassword(PASSWORD);
			deleteConnector.setUrl(HOSTNAME_ATOM);
			deleteConnector.setBinding_type(ATOM);
			deleteConnector.setRepositoryName(repositoryName);
			deleteConnector.setFolderPath("/Bonita");
			deleteConnector.execute();
		} catch (Exception e) {

		}
		CreateSubfolder createConnector = new CreateSubfolder();
		createConnector.setUsername(USERNAME);
		createConnector.setPassword(PASSWORD);
		createConnector.setUrl(HOSTNAME_ATOM);
		createConnector.setBinding_type(ATOM);
		createConnector.setRepositoryName(repositoryName);
		createConnector.setParentFolderPath("/");
		createConnector.setSubfolderName("Bonita");
		createConnector.execute();
		Folder folder = deleteConnector.getFolderByPath(USERNAME, PASSWORD,
				HOSTNAME_ATOM, ATOM, repositoryName, "/Bonita");
		assertNotNull(folder);
		assertEquals("Bonita", folder.getName());
		deleteConnector.execute();
	}

	public void testDeleteFolderByPath() throws Exception {
		try {
			CreateSubfolder createConnector = new CreateSubfolder();
			createConnector.setUsername(USERNAME);
			createConnector.setPassword(PASSWORD);
			createConnector.setUrl(HOSTNAME_ATOM);
			createConnector.setBinding_type(ATOM);
			createConnector.setRepositoryName(repositoryName);
			createConnector.setParentFolderPath("/");
			createConnector.setSubfolderName("Bonita");
			createConnector.execute();
		} catch (Exception e) {

		}
		DeleteFolderByPath deleteConnector = new DeleteFolderByPath();
		deleteConnector.setUsername(USERNAME);
		deleteConnector.setPassword(PASSWORD);
		deleteConnector.setUrl(HOSTNAME_ATOM);
		deleteConnector.setBinding_type(ATOM);
		deleteConnector.setRepositoryName(repositoryName);
		deleteConnector.setFolderPath("/Bonita");
		deleteConnector.execute();
		try {
			abstractCmisConnector.getFolderByPath(USERNAME, PASSWORD,
					HOSTNAME_ATOM, ATOM, repositoryName, "/Bonita");
		} catch (CmisObjectNotFoundException e) {
			return;
		}
		fail("folder was not deleted");
	}

	public void testGetRepositoryIdByName() {
		String repositoryName = "Main Repository";
		String id = abstractCmisConnector.getRepositoryIdByName(USERNAME,
				PASSWORD, HOSTNAME_ATOM, ATOM, repositoryName);

		assertEquals("371554cd-ac06-40ba-98b8-e6b60275cca7", id);
	}

	public void testCheckIfObjectExists() throws Exception {
		Boolean exists;
		try {
			CreateSubfolder createConnector = new CreateSubfolder();
			createConnector.setUsername(USERNAME);
			createConnector.setPassword(PASSWORD);
			createConnector.setUrl(HOSTNAME_ATOM);
			createConnector.setBinding_type(ATOM);
			createConnector.setRepositoryName(repositoryName);
			createConnector.setParentFolderPath("/");
			createConnector.setSubfolderName("Bonita");
			createConnector.execute();
		} catch (Exception e) {

		}
		CheckIfObjectExist checkConnector = new CheckIfObjectExist();
		checkConnector.setUsername(USERNAME);
		checkConnector.setPassword(PASSWORD);
		checkConnector.setUrl(HOSTNAME_ATOM);
		checkConnector.setBinding_type(ATOM);
		checkConnector.setRepositoryName(repositoryName);
		checkConnector.setObjectPath("/Bonita");
		checkConnector.execute();
		exists = checkConnector.getObjectExists();
		assertTrue(exists);

		DeleteFolderByPath deleteConnector = new DeleteFolderByPath();
		deleteConnector.setUsername(USERNAME);
		deleteConnector.setPassword(PASSWORD);
		deleteConnector.setUrl(HOSTNAME_ATOM);
		deleteConnector.setBinding_type(ATOM);
		deleteConnector.setRepositoryName(repositoryName);
		deleteConnector.setFolderPath("/Bonita");
		deleteConnector.execute();
	}

	public void testIfObjectDoesNotExist() throws Exception {
		Boolean exists;
		try {
			DeleteFolderByPath deleteConnector = new DeleteFolderByPath();
			deleteConnector.setUsername(USERNAME);
			deleteConnector.setPassword(PASSWORD);
			deleteConnector.setUrl(HOSTNAME_ATOM);
			deleteConnector.setBinding_type(ATOM);
			deleteConnector.setRepositoryName(repositoryName);
			deleteConnector.setFolderPath("/Bonita");
			deleteConnector.execute();
		} catch (Exception e) {

		}
		CheckIfObjectExist checkConnector = new CheckIfObjectExist();
		checkConnector.setUsername(USERNAME);
		checkConnector.setPassword(PASSWORD);
		checkConnector.setUrl(HOSTNAME_ATOM);
		checkConnector.setBinding_type(ATOM);
		checkConnector.setRepositoryName(repositoryName);
		checkConnector.setObjectPath("/Bonita");
		checkConnector.execute();
		exists = checkConnector.getObjectExists();
		assertFalse(exists);
	}

	public void testDeleteObjectByPath() throws Exception {
		Boolean exists;
		try {

			CreateSubfolder createConnector = new CreateSubfolder();
			createConnector.setUsername(USERNAME);
			createConnector.setPassword(PASSWORD);
			createConnector.setUrl(HOSTNAME_ATOM);
			createConnector.setBinding_type(ATOM);
			createConnector.setRepositoryName(repositoryName);
			createConnector.setParentFolderPath("/");
			createConnector.setSubfolderName("Bonita");
			createConnector.execute();
		} catch (Exception e) {

		}
		DeleteFolderByPath deleteConnector = new DeleteFolderByPath();
		deleteConnector.setUsername(USERNAME);
		deleteConnector.setPassword(PASSWORD);
		deleteConnector.setUrl(HOSTNAME_ATOM);
		deleteConnector.setBinding_type(ATOM);
		deleteConnector.setRepositoryName(repositoryName);
		deleteConnector.setFolderPath("/Bonita");
		deleteConnector.execute();

		CheckIfObjectExist checkConnector = new CheckIfObjectExist();
		checkConnector.setUsername(USERNAME);
		checkConnector.setPassword(PASSWORD);
		checkConnector.setUrl(HOSTNAME_ATOM);
		checkConnector.setBinding_type(ATOM);
		checkConnector.setRepositoryName(repositoryName);
		checkConnector.setObjectPath("/Bonita");
		checkConnector.execute();
		exists = checkConnector.getObjectExists();
		assertFalse("object was not deleted", exists);
	}

	public void testUploadFileToFolderByPath() throws Exception {

		try {
			CreateSubfolder createConnector = new CreateSubfolder();
			createConnector.setUsername(USERNAME);
			createConnector.setPassword(PASSWORD);
			createConnector.setUrl(HOSTNAME_ATOM);
			createConnector.setBinding_type(ATOM);
			createConnector.setRepositoryName(repositoryName);
			createConnector.setParentFolderPath("/");
			createConnector.setSubfolderName("Bonita-upload");
			createConnector.execute();
		} catch (Exception e) {

		}
		try {
			DeleteObject deleteConnector = new DeleteObject();
			deleteConnector.setUsername(USERNAME);
			deleteConnector.setPassword(PASSWORD);
			deleteConnector.setUrl(HOSTNAME_ATOM);
			deleteConnector.setBinding_type(ATOM);
			deleteConnector.setRepositoryName(repositoryName);
			deleteConnector.setObjectPath("/Bonita-upload/testBonitaDoc.txt");
			deleteConnector.execute();
		} catch (Exception e) {

		}

		File f = File.createTempFile("testBonitaDoc", "txt");
		f.createNewFile();
		FileOutputStream fos = new FileOutputStream(f);
		PrintStream prt = new PrintStream(fos);
		prt.println("The content !");
		prt.close();
		fos.close();

		UploadFile uploadConnector = new UploadFile();
		uploadConnector.uploadFileToFolder(USERNAME, PASSWORD, HOSTNAME_ATOM,
				ATOM, repositoryName, "/Bonita-upload", new FileInputStream(f),
				"testBonitaDoc.txt");

		CheckIfObjectExist checkConnector = new CheckIfObjectExist();
		checkConnector.setUsername(USERNAME);
		checkConnector.setPassword(PASSWORD);
		checkConnector.setUrl(HOSTNAME_ATOM);
		checkConnector.setBinding_type(ATOM);
		checkConnector.setRepositoryName(repositoryName);
		checkConnector.setObjectPath("/Bonita-upload/testBonitaDoc.txt");
		checkConnector.execute();
		assertTrue(checkConnector.getObjectExists());
	}

	public void testDownloadFileByPath() throws Exception {

		DownloadDocument download = new DownloadDocument();
		download.setUsername(USERNAME);
		download.setPassword(PASSWORD);
		download.setUrl(HOSTNAME_ATOM);
		download.setBinding_type(ATOM);
		download.setRepositoryName(repositoryName);
		download.setDocumentPath("/Bonita-upload/testBonitaDoc.txt");
		File destFile = File.createTempFile("plop", ".tmp");
		destFile.delete();
		download.setDestinationFileName(destFile.getAbsolutePath());
		download.execute();

		FileInputStream fis = new FileInputStream(destFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		assertEquals("The content !", br.readLine());
		fis.close();

		destFile.delete();
	}

	public void testModifyDocument() throws Exception {

		ModifyDocument modifyConnector = new ModifyDocument();

		modifyConnector.modifyFile(USERNAME, PASSWORD, HOSTNAME_ATOM, ATOM,
				repositoryName,
				new ByteArrayInputStream("an other content".getBytes()),
				"/Bonita-upload/testBonitaDoc.txt");

		DownloadDocument download = new DownloadDocument();
		download.setUsername(USERNAME);
		download.setPassword(PASSWORD);
		download.setUrl(HOSTNAME_ATOM);
		download.setBinding_type(ATOM);
		download.setRepositoryName(repositoryName);
		download.setDocumentPath("/Bonita-upload/testBonitaDoc.txt");
		File destFile = File.createTempFile("plop", ".tmp");
		destFile.delete();
		download.setDestinationFileName(destFile.getAbsolutePath());
		download.execute();
		FileInputStream fis = new FileInputStream(destFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		assertEquals("an other content", br.readLine());
		fis.close();

		destFile.delete();
	}

	public void testListVersionsOfObjectByPath() throws Exception {
		ListVersionOfDocument listversion = new ListVersionOfDocument();
		listversion.setUsername(USERNAME);
		listversion.setPassword(PASSWORD);
		listversion.setUrl(HOSTNAME_ATOM);
		listversion.setBinding_type(ATOM);
		listversion.setRepositoryName(repositoryName);
		listversion.setDocumentPath("/Bonita-upload/testBonitaDoc.txt");
		listversion.execute();
		listversion.getVersionsList();
		assertTrue("not more than one version", listversion.getVersionsList()
				.size() > 1);
	}

	public void testDeleteObject() throws Exception {
		DeleteObject deleteConnector = new DeleteObject();
		deleteConnector.setUsername(USERNAME);
		deleteConnector.setPassword(PASSWORD);
		deleteConnector.setUrl(HOSTNAME_ATOM);
		deleteConnector.setBinding_type(ATOM);
		deleteConnector.setRepositoryName(repositoryName);
		deleteConnector.setObjectPath("/Bonita-upload/testBonitaDoc.txt");
		deleteConnector.execute();
		CheckIfObjectExist objectExist = new CheckIfObjectExist();
		objectExist.setUsername(USERNAME);
		objectExist.setPassword(PASSWORD);
		objectExist.setUrl(HOSTNAME_ATOM);
		objectExist.setBinding_type(ATOM);
		objectExist.setRepositoryName(repositoryName);
		objectExist.setObjectPath("/Bonita-upload/testBonitaDoc.txt");
		objectExist.execute();
		assertFalse("object not deleted", objectExist.getObjectExists());
	}

}
