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
package org.bonitasoft.connectors.drools.examples.agerules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.definition.KnowledgePackage;
import org.drools.event.rule.DebugAgendaEventListener;
import org.drools.event.rule.DebugWorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.StatelessKnowledgeSession;

/**
 * @author Jordi Anguela
 * 
 * This is an example to show StatelessKnowledgeSession logic
 */
public class AgeRules {

    
	/**
	 * @param args
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
	    
		final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

		// this will parse and compile in one step
		kbuilder.add(ResourceFactory.newClassPathResource("./ageRules.drl", AgeRules.class), ResourceType.DRL);

		// Check the builder for errors
		if (kbuilder.hasErrors()) {
			System.out.println(kbuilder.getErrors().toString());
			throw new RuntimeException("Unable to compile \"ageRules.drl\".");
		}
		
		// get the compiled packages (which are serializable)
		final Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
		
		// add the packages to a knowledgebase (deploy the knowledge packages).
		final KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(pkgs);
		

		executeStateless(kbase);
		//executeStateful(kbase);
	}
	
	public static void executeStateless(KnowledgeBase kbase) {
		final StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
		
		ksession.addEventListener(new DebugAgendaEventListener());
		ksession.addEventListener(new DebugWorkingMemoryEventListener());
		
		// setup the audit logging
		KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "log/applicant");
		
		Collection<Object> list = getListOfFacts();

		System.out.println(list);
		ksession.execute(list);

		System.out.println(list);

		// Let's execute only the first one
		list = getListOfFacts();
		ksession.execute(list.iterator().next());
		System.out.println(list);  // Only the first one should be modified
				
		logger.close();
	}
	
	public static void executeStateful(KnowledgeBase kbase) {
		final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		
		ksession.addEventListener(new DebugAgendaEventListener());
		ksession.addEventListener(new DebugWorkingMemoryEventListener());
		
		// setup the audit logging
		KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "log/applicant");
		
		List<Object> applicantsList = new ArrayList<Object>();
		ksession.setGlobal("applicantsList", applicantsList);
		

		Collection<Object> list = getListOfFacts();
		
		for (Object fact : list) {
			ksession.insert( fact );
		}
		
        ksession.fireAllRules();
        
        ksession.dispose();
        
		System.out.println(list);
		System.out.println(applicantsList);
		
		logger.close();
	}
	
	public static Collection<Object> getListOfFacts() {
		Applicant applicant1 = new Applicant( "Jordi", 8 );
		Applicant applicant2 = new Applicant( "Marc", 15 );
		Applicant applicant3 = new Applicant( "Albert", 32 );
		
		Collection<Object> list = new ArrayList<Object>();
		list.add(applicant1);
		list.add(applicant2);
		list.add(applicant3);
		
		return list;
	}
	
}
