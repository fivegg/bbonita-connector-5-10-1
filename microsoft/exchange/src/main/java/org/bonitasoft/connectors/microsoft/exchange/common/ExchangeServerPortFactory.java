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
package org.bonitasoft.connectors.microsoft.exchange.common;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;

import com.microsoft.schemas.exchange.services._2006.messages.ExchangeServicePortType;
import com.microsoft.schemas.exchange.services._2006.messages.ExchangeServices;


/**
 * 
 * <pre>
 * Copyright (c) 2000-2003 Yale University. All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, ARE EXPRESSLY
 * DISCLAIMED. IN NO EVENT SHALL YALE UNIVERSITY OR ITS EMPLOYEES BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED, THE COSTS OF
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED IN ADVANCE OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 * 
 * Redistribution and use of this software in source or binary forms,
 * with or without modification, are permitted, provided that the
 * following conditions are met:
 * 
 * 1. Any redistribution must include the above copyright notice and
 * disclaimer and this list of conditions in any related documentation
 * and, if feasible, in the redistributed software.
 * 
 * 2. Any redistribution must include the acknowledgment, "This product
 * includes software developed by Yale University," in any related
 * documentation and, if feasible, in the redistributed software.
 * 
 * 3. The names "Yale" and "Yale University" must not be used to endorse
 * or promote products derived from this software.
 * </pre>
 *
 * Trust Everyone...  These should be locked down servers we are working on...
 */
public class ExchangeServerPortFactory {

    String uri;
    String adDomain;
    String uid;
    String pwd;
    ExchangeServices service;
    Authenticator basicAuth;
    
    static{
            javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(new AllTrustingSocketFactory());
    }
    
    /**
     *  Set up Authentication
     *
     */
    protected void setupAuthenticator(){
            if(basicAuth == null){
                    basicAuth = new Authenticator(){
                            protected PasswordAuthentication getPasswordAuthentication(){
//                                    String superUid = ExchangeServerPortFactory.this.adDomain
//                                                    + "\\" 
//                                                    + ExchangeServerPortFactory.this.uid;
                            		String superUid = ExchangeServerPortFactory.this.uid;
                                    String superPwd = ExchangeServerPortFactory.this.pwd;
                                    return new PasswordAuthentication(superUid
                                                                     ,superPwd.toCharArray());
                            }
                    };
                    Authenticator.setDefault(basicAuth);
            }
    }

    public ExchangeServicePortType getExchangeServerPort() {
            ExchangeServicePortType port = null;
            try{
                    port = getExchangeServices().getExchangeServicePort();
            } catch (Exception e){
                    throw new RuntimeException(e);
            }
            return port;
    }
    
    protected ExchangeServices getExchangeServices() throws Exception{
            setupAuthenticator();
            if(service == null) service = new ExchangeServices(new URL(uri));
            return service;
    }

    public String getAdDomain() {
            return adDomain;
    }

    public void setAdDomain(String adDomain) {
            this.adDomain = adDomain;
    }

    public String getUri() {
            return uri;
    }

    public void setUri(String uri) {
            this.uri = uri;
    }

    public String getPwd() {
            return pwd;
    }

    public void setPwd(String pwd) {
            this.pwd = pwd;
    }

    public String getUid() {
            return uid;
    }

    public void setUid(String uid) {
            this.uid = uid;
    }

}
