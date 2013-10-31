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

import org.bonitasoft.connectors.alfresco.TestAlfrescoConnector;

public class TestSuiteAlfrescoConnector {
	public static void main(String[] args) {

		TestAlfrescoConnector test = new TestAlfrescoConnector();
		
		// CREATE FOLDER 
		//test.createFolderByPath("/User%20Homes/user1", "TestFolder9", "Description folder1");
		
		// LIST A FOLDER 
		//test.listFolderByPath("/User%20Homes/user1");
		
		// DELETE FOLDER 
		//test.deleteFolderByPath("/User%20Homes/user1/TestFolder8");

		// DOWNLOAD A FILE 
		//test.downloadFileById("92a97813-652c-48fa-b932-f1e717100c0b", "E:/Documents and Settings/Jordito/Escritorio/Connector Alfresco/", "demoNew.txt");
		
		// UPLOAD A FILE 
		//test.uploadFileByPath("E:/Documents and Settings/Jordito/Escritorio/Connector Alfresco/articlePDF.pdf", "File description", "aplication/pdf", "/User%20Homes/user1/TestFolder8");
		//test.listFolderByPath("/User%20Homes/user1/TestFolder7");
		test.uploadFile("C:/temp.txt", "test_file.txt", "Test file", "text/plain", "/test_directory/");
		
		// DELETE A FILE 
		//test.deleteItemById("80e77a87-6203-48d5-b734-ff598250bfa2");
		
		// CHECKOUT A FILE 
		//test.checkout("92a97813-652c-48fa-b932-f1e717100c0b");
		
		// LIST CHECKEDOUT FILES 
		//test.listCheckedOutFiles();
		
		// CANCEL CHECKEDOUT FILE 
		//test.cancelCheckout("8036b8ad-1a8d-4211-9841-d843da54f44c");
		//test.listCheckedOutFiles();
		
		// UPDATE CHECKEDOUT FILE 
		//test.checkout("92a97813-652c-48fa-b932-f1e717100c0b");
		//test.listCheckedOutFiles();
		//test.downloadFileById("54836ab4-f5db-4914-a833-5c16e540bf88", "E:/Documents and Settings/Jordito/Escritorio/Connector Alfresco/", "demo (Working Copy).txt");
		// (add changes to download file)
		//test.updateCheckedOutFile("E:/Documents and Settings/Jordito/Escritorio/Connector Alfresco/demo (Working Copy).txt", "File description V4", "text/plain", "54836ab4-f5db-4914-a833-5c16e540bf88");
		
		// CHECKIN FILE minor version
		//test.checkin("5db9ae95-0ca2-4072-84eb-a0f22120e850", false, "4th version");
		
		// CHECKIN FILE major version 
		//test.checkin("54836ab4-f5db-4914-a833-5c16e540bf88", true, "This is a major checkin");
		
		// FILE VERSIONS
		//test.fileVersions("92a97813-652c-48fa-b932-f1e717100c0b");
		
		// DOWNLOAD VERSIONED FILE
		//test.downloadFileByStoreAndId("versionStore://version2Store", "a89fb67c-8656-4289-a320-21d3b23e97ce", "E:/Documents and Settings/Jordito/Escritorio/Connector Alfresco/", "demo 1_0.txt");

	}
}
