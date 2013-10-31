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
package org.bonitasoft.connectors.bonita.filters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.ow2.bonita.connector.core.ConnectorError;
import org.ow2.bonita.connector.core.Filter;

/**
 * This filter chooses N candidates between available members
 * @author Matthieu Chaffotte
 *
 */
public class RandomMultipleFilter extends Filter {

	private Integer candidateNumber;

  public Integer getCandidateNumber() {
  	return candidateNumber;
  }

  public void setCandidateNumber(Integer candidateNumber) {
  	this.candidateNumber = candidateNumber;
  }
  
  @Override
  protected Set<String> getCandidates(Set<String> members) throws Exception {
  	int cn = getCandidateNumber();
  	Random rand = new Random();
  	Set<String> temp = new HashSet<String>(members);
  	Set<String> candidates = new HashSet<String>();
	  for (long i = 0; i < cn; i++) {
	    int candidatesNb = temp.size();
		  int next = rand.nextInt(candidatesNb);
		  String member = get(temp, next);
		  candidates.add(member);
		  temp.remove(member);
    }
	  return candidates;
  }

  @Override
  protected List<ConnectorError> validateValues() {
  	List<ConnectorError> errors = new ArrayList<ConnectorError>();
  	if (getCandidateNumber() < 1) {
  		 errors.add(new ConnectorError("candidateNumber",
           new IllegalArgumentException("cannot be less than 1")));
  	} else if (getCandidateNumber() > getMembers().size()) {
  		errors.add(new ConnectorError("candidateNumber",
          new IllegalArgumentException("cannot be greater than the candidates number")));
  	}
	  return errors;
  }
}
