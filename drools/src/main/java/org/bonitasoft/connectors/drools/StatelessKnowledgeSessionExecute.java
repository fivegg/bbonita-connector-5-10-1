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
package org.bonitasoft.connectors.drools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.CommandFactory;
import org.drools.definition.KnowledgePackage;
import org.drools.event.rule.DebugAgendaEventListener;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.ProcessConnector;

/**
 * @author Jordi Anguela
 */
public class StatelessKnowledgeSessionExecute extends ProcessConnector {
	
	private static Logger LOGGER = Logger.getLogger(StatelessKnowledgeSessionExecute.class.getName());

	private String drlFilePath;
	private List<Object> listOfFacts;
	
	@Override
	protected List<ConnectorError> validateValues() {
		LOGGER.info("Validating parameters: drlFilePath:" + drlFilePath + " listOfFacts:" + listOfFacts);
		List<ConnectorError> errors = new ArrayList<ConnectorError>();
		if ((drlFilePath == null) || !(drlFilePath.length() > 0)) {
			errors.add(new ConnectorError("drlFilePath", new IllegalArgumentException("drlFilePath cannot be empty!")));
		}
		if ((listOfFacts == null) || !(listOfFacts.size() > 0)) {
			errors.add(new ConnectorError("listOfFacts", new IllegalArgumentException("listOfFacts cannot be empty!")));
		}
		return errors;
	}

	@Override
	protected void executeConnector() throws Exception {
		LOGGER.info("Input parameters: drlFilePath:" + drlFilePath + " listOfFacts:" + listOfFacts);
		final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

		// this will parse and compile in one step
		kbuilder.add(ResourceFactory.newFileResource(drlFilePath), ResourceType.DRL);

		// Check the builder for errors
		if (kbuilder.hasErrors()) {
			System.out.println(kbuilder.getErrors().toString());
			throw new RuntimeException("Unable to compile \"" + drlFilePath + "\".");
		}

		// get the compiled packages (which are serializable)
		final Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();

		// add the packages to a knowledgebase (deploy the knowledge packages).
		final KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(pkgs);

		KnowledgeRuntimeLogger logger = null;

		StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();

		ksession.addEventListener(new DebugAgendaEventListener());
		ksession.addEventListener(new DebugWorkingMemoryEventListener());

		// setup the audit logging
		logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "drools");

		//ksession.execute( listOfFacts );
		ksession.execute( CommandFactory.newInsertElements( listOfFacts ) );

		LOGGER.severe("Output listOfFacts:" + listOfFacts);
		logger.close();
	}

	public void setDrlFilePath(String drlFilePath) {
		this.drlFilePath = drlFilePath;
	}

	public void setListOfFacts(List<Object> listOfFacts) {
		this.listOfFacts = listOfFacts;
	}
	
	public List<Object> getRevisedListOfFacts() {
		return this.listOfFacts;
	}

}
