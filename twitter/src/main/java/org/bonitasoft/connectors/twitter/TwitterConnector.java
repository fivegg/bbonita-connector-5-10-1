/**
 * Copyright (C) 2009-2010 BonitaSoft S.A.
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

import java.util.ArrayList;
import java.util.List;

import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.http.AccessToken;
import twitter4j.http.OAuthAuthorization;

/**
 * 
 * @author Matthieu Chaffotte
 *
 */
public abstract class TwitterConnector extends Connector {

  private String proxyHost;
  private Integer proxyPort;
  private String proxyUser;
  private String proxyPass;
  private String consumerKey;
  private String consumerSecret;
  private String accessToken;
  private String accessTokenSecret;

  public String getProxyHost() {
    return proxyHost;
  }

  public void setProxyHost(String proxyHost) {
    this.proxyHost = proxyHost;
  }

  public Integer getProxyPort() {
    return proxyPort;
  }

  public void setProxyPort(Integer proxyPort) {
    this.proxyPort = proxyPort;
  }

  public void setProxyPort(Long port) {
    setProxyPort(port.intValue());
  }

  public String getProxyUser() {
    return proxyUser;
  }

  public void setProxyUser(String proxyUser) {
    this.proxyUser = proxyUser;
  }

  public String getProxyPass() {
    return proxyPass;
  }

  public void setProxyPass(String proxyPass) {
    this.proxyPass = proxyPass;
  }

  public void setConsumerKey(String consumerKey) {
    this.consumerKey = consumerKey;
  }

  public void setConsumerSecret(String consumerSecret) {
    this.consumerSecret = consumerSecret;
  }

  public void setAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }

  public void setAccessTokenSecret(String accessTokenSecret) {
    this.accessTokenSecret = accessTokenSecret;
  }

  @Override
  protected void executeConnector() throws Exception {
    ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
    if (getProxyHost() != null && getProxyPort() != null) {
      configurationBuilder.setHttpProxyHost(getProxyHost());
      configurationBuilder.setHttpProxyPort(getProxyPort());
      if (getProxyUser() != null && getProxyPass() != null) {
        configurationBuilder.setHttpProxyUser(getProxyUser());
        configurationBuilder.setHttpProxyPassword(getProxyPass());
      }
    }
    Configuration configuration = new ConfigurationBuilder().build();
    AccessToken token = new AccessToken(accessToken, accessTokenSecret);
    OAuthAuthorization authorization = new OAuthAuthorization(configuration, consumerKey, consumerSecret, token);
    Twitter twitter = new TwitterFactory().getInstance(authorization);
    executeTask(twitter);
  }

  protected abstract void executeTask(Twitter twitter) throws Exception;

  @Override
  protected List<ConnectorError> validateValues() {
    List<ConnectorError> errors = new ArrayList<ConnectorError>();
    if (getProxyPort() != null) {
      if (this.getProxyPort() < 0) {
        errors.add(new ConnectorError("proxyPort",
            new IllegalArgumentException("cannot be less than 0!")));
      } else if (this.getProxyPort() > 65535) {
        errors.add(new ConnectorError("proxyPort",
            new IllegalArgumentException("cannot be greater than 65535!")));
      }
    }
    return errors;
  }

}
