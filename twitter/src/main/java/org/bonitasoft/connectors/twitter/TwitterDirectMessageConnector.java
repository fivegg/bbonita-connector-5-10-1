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
package org.bonitasoft.connectors.twitter;

import twitter4j.Twitter;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public class TwitterDirectMessageConnector extends TwitterConnector {

  private String message;
  private String recipientId;

  public String getMessage() {
    return message;
  }

  public String getRecipientId() {
    return recipientId;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setRecipientId(String recipientId) {
    this.recipientId = recipientId;
  }

  @Override
  protected void executeTask(Twitter twitter) throws Exception {
    twitter.sendDirectMessage(recipientId, message);
  }

}
