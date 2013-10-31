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
package org.bonitasoft.connectors.drools.examples.firealarm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
import org.drools.runtime.rule.FactHandle;

/**
 * @author Jordi Anguela
 * 
 * This is an example to show StatefulKnowledgeSession logic
 */
public class FireAlarmTest {

	/**
	 * @param args
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void main(String[] args) throws InstantiationException, IllegalAccessException {
		
		String drlFile = "fireAlarm.drl";
	    
		final KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();

		// this will parse and compile in one step
		kbuilder.add(ResourceFactory.newClassPathResource( drlFile, FireAlarmTest.class ), ResourceType.DRL);

		// Check the builder for errors
		if (kbuilder.hasErrors()) {
			System.out.println(kbuilder.getErrors().toString());
			throw new RuntimeException("Unable to compile \"" + drlFile + "\".");
		}
		
		// get the compiled packages (which are serializable)
		final Collection<KnowledgePackage> pkgs = kbuilder.getKnowledgePackages();
		
		// add the packages to a knowledgebase (deploy the knowledge packages).
		final KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(pkgs);
		

		final StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
		
		ksession.addEventListener(new DebugAgendaEventListener());
		ksession.addEventListener(new DebugWorkingMemoryEventListener());
		
		// setup the audit logging
		KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, "log/fireAlarm");
		
		String[] names = new String[]{"kitchen", "bedroom", "office", "livingroom"};
		Map<String,Room> name2room = new HashMap<String,Room>();
		Map<String,Sprinkler> name2sprinkler = new HashMap<String,Sprinkler>();
		
		for( String name: names ){
		    Room room = new Room( name );
		    name2room.put( name, room );
		    ksession.insert( room );
		    
		    Sprinkler sprinkler = new Sprinkler( room );
		    name2sprinkler.put( name, sprinkler );
		    ksession.insert( sprinkler );
		}

		ksession.fireAllRules();
//		> Everything is ok'
		
		Fire kitchenFire = new Fire( name2room.get( "kitchen" ) );
		Fire officeFire = new Fire( name2room.get( "office" ) );

		FactHandle kitchenFireHandle = ksession.insert( kitchenFire );
		ksession.fireAllRules();
//		> Raise the alarm
//		> Turn on the sprinkler for room kitchen
		
		Sprinkler kitchenSprinkler = name2sprinkler.get( "kitchen" );
		System.out.println("--- Is kitchen sprinkler turned on? " + kitchenSprinkler.isOn());

		FactHandle officeFireHandle = ksession.insert( officeFire );
		ksession.fireAllRules();
//		> Turn on the sprinkler for room office

		ksession.retract( kitchenFireHandle );
		ksession.fireAllRules();
//		> Turn on the sprinkler for room office
		
		System.out.println("--- Is kitchen sprinkler turned on? " + kitchenSprinkler.isOn());
		
		ksession.retract( officeFireHandle );
		ksession.fireAllRules();
//		> Turn on the sprinkler for room kitchen
//		> Cancel the alarm
//		> Everything is ok
		
		logger.close();
		
		ksession.dispose();
	}
}
