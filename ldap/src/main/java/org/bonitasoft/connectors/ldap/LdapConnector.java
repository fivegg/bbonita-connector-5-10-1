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
package org.bonitasoft.connectors.ldap;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.naming.ldap.StartTlsRequest;
import javax.naming.ldap.StartTlsResponse;

import org.ow2.bonita.connector.core.Connector;
import org.ow2.bonita.connector.core.ConnectorError;

/**
 * This connector provides an LDAP service of querying directory service. This connector does the search operation.
 * 
 * @author Matthieu Chaffotte
 * 
 */
public class LdapConnector extends Connector {

  /**
   * The host name of the directory service.
   */
  private String host = null;

  /**
   * The port number of the directory service.
   */
  private int port = 389;

  /**
   * The protocol used by the directory service. It can be LDAP, LDAPS(SSL) or TLS.
   */
  private LdapProtocol protocol = null;

  /**
   * The user name if authentication is needed.
   */
  private String userName = null;

  /**
   * The password if authentication is needed.
   */
  private String password = null;

  private String certificatePath;

  /**
   * 
   */
  private String baseObject = null;

  /**
   * 
   */
  private LdapScope scope = LdapScope.BASE;
  private String filter = null;
  private LdapDereferencingAlias derefAliases = LdapDereferencingAlias.ALWAYS;
  private String[] attributes = null;
  private long sizeLimit = 0;
  private int timeLimit = 0;
  private String referralHandling = "ignore";

  // output
  private List<List<LdapAttribute>> result = new ArrayList<List<LdapAttribute>>();

  private String getHost() {
    return host;
  }

  private Integer getPort() {
    return port;
  }

  public LdapProtocol getProtocol() {
    return protocol;
  }

  public String getUserName() {
    return userName;
  }

  public String getPassword() {
    return password;
  }

  public String getCertificatePath() {
    return certificatePath;
  }

  public String getBaseObject() {
    return baseObject;
  }

  public LdapScope getScope() {
    return scope;
  }

  public String getFilter() {
    return filter;
  }

  public LdapDereferencingAlias getDerefAliases() {
    return derefAliases;
  }

  public String[] getAttributes() {
    return attributes;
  }

  public long getSizeLimit() {
    return sizeLimit;
  }

  public int getTimeLimit() {
    return timeLimit;
  }

  public List<List<LdapAttribute>> getLdapAttributeList() {
    return result;
  }

  public String getReferralHandling() {
    return referralHandling;
  }

  public void setHost(final String host) {
    this.host = host;
  }

  public void setPort(final Long port) {
    if (port == null) {
      setPort(Integer.MIN_VALUE);
    } else {
      setPort(port.intValue());
    }
  }

  public void setPort(final int port) {
    this.port = port;
  }

  public void setProtocol(final LdapProtocol protocol) {
    this.protocol = protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = LdapProtocol.LDAPS;
    if (protocol != null) {
      protocol = protocol.toUpperCase();
      if (protocol.equals(LdapProtocol.TLS.toString())) {
        this.protocol = LdapProtocol.TLS;
      } else if (protocol.equals(LdapProtocol.LDAP.toString())) {
        this.protocol = LdapProtocol.LDAP;
      }
    }
  }

  public void setUserName(final String userName) {
    this.userName = userName;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  public void setBaseObject(final String baseObject) {
    this.baseObject = baseObject;
  }

  public void setScope(final LdapScope scope) {
    this.scope = scope;
  }

  public void setScope(String scope) {
    this.scope = LdapScope.ONELEVEL;
    if (scope != null) {
      scope = scope.toUpperCase();
      if (scope.equals(LdapScope.BASE.toString())) {
        this.scope = LdapScope.BASE;
      } else if (scope.equals(LdapScope.SUBTREE.toString())) {
        this.scope = LdapScope.SUBTREE;
      }
    }
  }

  public void setFilter(final String filter) {
    this.filter = filter;
  }

  public void setDerefAliases(final LdapDereferencingAlias derefAliases) {
    this.derefAliases = derefAliases;
  }

  public void setDerefAliases(final String derefAliases) {
    this.derefAliases = LdapDereferencingAlias.ALWAYS;
    if (derefAliases.equals("never")) {
      this.derefAliases = LdapDereferencingAlias.NEVER;
    } else if (derefAliases.equals("finding")) {
      this.derefAliases = LdapDereferencingAlias.FINDING;
    } else if (derefAliases.equals("searching")) {
      this.derefAliases = LdapDereferencingAlias.SEARCHING;
    }
  }

  public void setAttributes(final String[] attributes) {
    if (attributes == null) {
      this.attributes = null;
    } else {
      this.attributes = new String[attributes.length];
      System.arraycopy(attributes, 0, this.attributes, 0, attributes.length);
    }
  }

  public void setAttributes(final String attributes) {
    if (attributes == null || "".equals(attributes.trim())) {
      this.attributes = null;
    } else {
      final StringTokenizer tokens = new StringTokenizer(attributes, ",");
      int i = 0;
      this.attributes = new String[tokens.countTokens()];
      while (tokens.hasMoreElements()) {
        this.attributes[i] = (String) tokens.nextElement();
        i++;
      }
    }
  }

  public void setSizeLimit(final long sizeLimit) {
    this.sizeLimit = sizeLimit;
  }

  public void setSizeLimit(final Long sizeLimit) {
    if (sizeLimit == null) {
      setSizeLimit(Long.MIN_VALUE);
    } else {
      setSizeLimit(sizeLimit.longValue());
    }
  }

  /**
   * Sets the time-limit during a search in seconds.
   * 
   * @param timeLimit
   */
  public void setTimeLimit(final int timeLimit) {
    this.timeLimit = timeLimit;
  }

  /**
   * Sets the time-limit during a search in seconds.
   * 
   * @param timeLimit
   */
  public void setTimeLimit(final Long timeLimit) {
    if (timeLimit == null) {
      setTimeLimit(Integer.MIN_VALUE);
    } else {
      setTimeLimit(timeLimit.intValue());
    }
  }

  public void setReferralHandling(final String referralHandling) {
    this.referralHandling = referralHandling;
  }

  private Hashtable<String, String> getEnvironment() {
    final Hashtable<String, String> environment = new Hashtable<String, String>();
    environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    environment.put(Context.PROVIDER_URL, "ldap://" + getHost() + ":" + getPort());
    if (getProtocol().equals(LdapProtocol.LDAPS)) {
      environment.put(Context.SECURITY_PROTOCOL, "ssl");
    }
    if (!LdapProtocol.TLS.equals(getProtocol()) && getUserName() != null && getPassword() != null) {
      environment.put(Context.SECURITY_AUTHENTICATION, "simple");
      environment.put(Context.SECURITY_PRINCIPAL, getUserName());
      environment.put(Context.SECURITY_CREDENTIALS, getPassword());
    } else {
      environment.put(Context.SECURITY_AUTHENTICATION, "none");
    }
    environment.put("java.naming.ldap.derefAliases", getDerefAliases().toString().toLowerCase());
    environment.put(Context.REFERRAL, getReferralHandling());
    return environment;
  }

  @Override
  protected void executeConnector() throws Exception {
    final Hashtable<String, String> env = getEnvironment();
    final LdapContext ctx = new InitialLdapContext(env, null);

    StartTlsResponse response = null;
    try {
      if (LdapProtocol.TLS.equals(getProtocol())) {
        final StartTlsRequest request = new StartTlsRequest();
        response = (StartTlsResponse) ctx.extendedOperation(request);
        response.negotiate();
        if (getUserName() != null && getPassword() != null) {
          ctx.addToEnvironment(Context.SECURITY_AUTHENTICATION, "simple");
          ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, getUserName());
          ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, getPassword());
        }
      }
      final SearchControls ctls = new SearchControls();
      ctls.setTimeLimit(getTimeLimit() * 1000);
      ctls.setCountLimit(getSizeLimit());
      ctls.setReturningAttributes(getAttributes());
      ctls.setSearchScope(getScope().value());
      final NamingEnumeration<SearchResult> answer = ctx.search(getBaseObject(), getFilter(), ctls);
      long count = getSizeLimit();
      // count is useful in case of the size-limit is defined
      // the search method does not care about size-limit. It returns all entries
      // which match with the filter.
      if (count == 0) {
        count = Long.MAX_VALUE;
      }
      result = new ArrayList<List<LdapAttribute>>();
      while (count > 0 && answer.hasMore()) {
        final SearchResult sr = answer.next();
        count--;
        final Attributes attribs = sr.getAttributes();
        final NamingEnumeration<? extends Attribute> enume = attribs.getAll();
        final List<LdapAttribute> elements = new ArrayList<LdapAttribute>();
        while (enume.hasMore()) {
          final Attribute attribute = enume.next();
          final NamingEnumeration<?> all = attribute.getAll();
          while (all.hasMore()) {
            final Object key = all.next();
            String value = null;
            if (key instanceof byte[]) {
              value = new String((byte[]) key, "UTF-8");
            } else {
              value = key.toString();
            }
            elements.add(new LdapAttribute(attribute.getID(), value));
          }
        }
        if (!elements.isEmpty()) {
          result.add(elements);
        }
      }
    } finally {
      if (LdapProtocol.TLS.equals(getProtocol()) && response != null) {
        response.close();
      }
      ctx.close();
    }
  }

  @Override
  protected List<ConnectorError> validateValues() {
    final List<ConnectorError> errors = new ArrayList<ConnectorError>();
    if (getHost().length() == 0) {
      errors.add(new ConnectorError("host", new IllegalArgumentException("cannot be empty!")));
    }
    if (getPort() < 0) {
      errors.add(new ConnectorError("port", new IllegalArgumentException("cannot be less than 0!")));
    } else if (getPort() > 65535) {
      errors.add(new ConnectorError("port", new IllegalArgumentException("cannot be greater than 65535!")));
    }
    switch (getProtocol()) {
      case LDAP:
      case LDAPS:
      case TLS:
        break;
      default:
        errors.add(new ConnectorError("Protocol", new IllegalArgumentException("unknown protocol")));
        break;
    }
    switch (getScope()) {
      case BASE:
      case ONELEVEL:
      case SUBTREE:
        break;
      default:
        errors.add(new ConnectorError("Scope", new IllegalArgumentException("unknown scope")));
        break;
    }
    if (getCertificatePath() != null) {
      final File temp = new File(certificatePath);
      if (!temp.exists()) {
        errors.add(new ConnectorError("certificatePath", new IllegalArgumentException(
            "the given path does not refer to a real file!")));
      }
    }
    if (getSizeLimit() < 0) {
      errors.add(new ConnectorError("sizeLimit", new IllegalArgumentException("null or negative value is forbidden!")));
    }
    if (getTimeLimit() < 0) {
      errors.add(new ConnectorError("timeLimit", new IllegalArgumentException("null or negative value is forbidden!")));
    }
    if (getReferralHandling() == null) {
      errors.add(new ConnectorError("referralHandling", new IllegalArgumentException("is null!")));
    } else if (!getReferralHandling().equals("ignore") && !getReferralHandling().equals("follow")) {
      errors.add(new ConnectorError("referralHandling",
          new IllegalArgumentException("must be either ignore or follow!")));
    }
    return errors;
  }

}
