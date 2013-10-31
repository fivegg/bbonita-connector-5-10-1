/**
 * Copyright (C) 2006  Bull S. A. S.
 * Bull, Rue Jean Jaures, B.P.68, 78340, Les Clayes-sous-Bois
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation
 * version 2.1 of the License.
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA  02110-1301, USA.
 **/
package org.bonitasoft.connectors.twitter;

import org.ow2.bonita.connector.core.Connector;

public class TwitterDirectMessageConnectorTest extends TwitterConnectorTest {

  @Override
  public TwitterConnector getTwitterConnector() {
    TwitterDirectMessageConnector message = new TwitterDirectMessageConnector();
    message.setConsumerKey("zerzer");
    message.setConsumerSecret("ezrezrzerzer");
    message.setAccessToken("zerzerzerzer");
    message.setAccessTokenSecret("erzerzrzerzr");
    message.setRecipientId("toto");
    message.setMessage("Finally on Twitter!");
    //message.setProxyHost("myproxy.bonitasoft.com");
    //message.setProxyPort(8080);
    return message;
  }

  @Override
  protected Class<? extends Connector> getConnectorClass() {
    return TwitterDirectMessageConnector.class;
  }
}
