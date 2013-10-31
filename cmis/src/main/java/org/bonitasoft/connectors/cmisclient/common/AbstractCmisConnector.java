/**
 * Copyright (C) 2011 BonitaSoft S.A.
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.ow2.bonita.connector.core.ProcessConnector;

/**
 * 
 * @author Baptiste Mesta
 * 
 * Abstract CMIS connector containing all common methods of all cmis connectors
 * 
 */
public abstract class AbstractCmisConnector extends ProcessConnector {

	protected String username;
	protected String binding_type;
	protected String repositoryName;
	protected String password;
	protected String url;
	protected Session session;

	public void setUsername(final String username) {
		this.username = username;
	}

	public void setBinding_type(final String binding_type) {
		this.binding_type = binding_type;
	}

	public void setRepositoryName(final String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

	public AbstractCmisConnector() {
		session = null;
	}

	protected ArrayList<Repository> getRepositories(final String username,
			final String password, final String url, final String bindingType) {

		final SessionFactory f = SessionFactoryImpl.newInstance();
		final Map<String, String> parameter = fixParameters(username, password,
				url, bindingType);
		final ArrayList<Repository> repositories = new ArrayList<Repository>(
				f.getRepositories(parameter));

		return repositories;
	}

	private Map<String, String> fixParameters(final String username,
			final String password, final String url, String bindingType) {
		final Map<String, String> parameter = new HashMap<String, String>();

		if ("ATOM".equals(bindingType)) {
			bindingType = BindingType.ATOMPUB.value();
		} else if ("WebService".equals(bindingType)) {
			bindingType = BindingType.WEBSERVICES.value();
			parameter.put(SessionParameter.WEBSERVICES_ACL_SERVICE, url
					+ "/ACLService?wsdl");
			parameter.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, url
					+ "/DiscoveryService?wsdl");
			parameter.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, url
					+ "/MultiFilingService?wsdl");
			parameter.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, url
					+ "/NavigationService?wsdl");
			parameter.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, url
					+ "/ObjectService?wsdl");
			parameter.put(SessionParameter.WEBSERVICES_POLICY_SERVICE, url
					+ "/PolicyService?wsdl");
			parameter.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE,
					url + "/RelationshipService?wsdl");
			parameter.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE, url
					+ "/RepositoryService?wsdl");
			parameter.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, url
					+ "/VersioningService?wsdl");
		}

		parameter.put(SessionParameter.ATOMPUB_URL, url);
		parameter.put(SessionParameter.USER, username);
		parameter.put(SessionParameter.PASSWORD, password);
		parameter.put(SessionParameter.BINDING_TYPE, bindingType);

		return parameter;
	}

	protected Session createSessionById(final String username,
			final String password, final String url, final String bindingType,
			final String repositoryId) {

		final SessionFactory f = SessionFactoryImpl.newInstance();
		final Map<String, String> parameter = fixParameters(username, password,
				url, bindingType);
		parameter.put(SessionParameter.REPOSITORY_ID, repositoryId);
		session = f.createSession(parameter);

		return session;
	}

	protected Session createSessionByName(final String username,
			final String password, final String url, final String bindingType,
			final String repositoryName) {

		final String repositoryId = getRepositoryIdByName(username, password,
				url, bindingType, repositoryName);
		session = createSessionById(username, password, url, bindingType,
				repositoryId);
		return session;
	}

	protected Folder getFolderByPath(final String username,
			final String password, final String url, final String binding,
			final String repositoryName, final String path) {
		final Session s = createSessionByName(username, password, url, binding,
				repositoryName);
		return (Folder) s.getObjectByPath(path);
	}

	protected CmisObject getObjectByPath(final String username,
			final String password, final String url, final String binding,
			final String repositoryName, final String path) {
		final Session s = createSessionByName(username, password, url, binding,
				repositoryName);
		return s.getObjectByPath(path);
	}

	protected String getRepositoryIdByName(final String username,
			final String password, final String url, final String binding,
			final String repositoryName) {
		final List<Repository> repositories = getRepositories(username,
				password, url, binding);
		Integer index = 0;

		while (index < repositories.size() && !repositories.get(index).getName().equals(repositoryName)) {
			index++;
		}

		if (index == repositories.size())
			return repositories.get(0).getId();
		else
			return repositories.get(index).getId();
	}

	public Session getSession() {
		return session;
	}

}
