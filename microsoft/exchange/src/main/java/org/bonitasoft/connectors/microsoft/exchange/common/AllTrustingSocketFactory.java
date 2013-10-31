package org.bonitasoft.connectors.microsoft.exchange.common;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.SecureRandom;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

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
 * Trust Everyone... These should be locked down servers we are working on...
 */
public class AllTrustingSocketFactory extends SSLSocketFactory {

  private SSLSocketFactory factory;

  public AllTrustingSocketFactory() {
    try {
      final SSLContext sslcontext = SSLContext.getInstance("TLS");
      sslcontext.init(null, // No KeyManager required
          new TrustManager[] { new AllTrustingTrustManager() }, new SecureRandom());
      factory = sslcontext.getSocketFactory();
    } catch (final Exception ex) {
      ex.printStackTrace();
    }
  }

  public static SocketFactory getDefault() {
    return new AllTrustingSocketFactory();
  }

  @Override
  public Socket createSocket(final Socket socket, final String s, final int i, final boolean flag) throws IOException {
    return factory.createSocket(socket, s, i, flag);
  }

  @Override
  public Socket createSocket(final InetAddress inaddr, final int i, final InetAddress inaddr1, final int j)
      throws IOException {
    return factory.createSocket(inaddr, i, inaddr1, j);
  }

  @Override
  public Socket createSocket(final InetAddress inaddr, final int i) throws IOException {
    return factory.createSocket(inaddr, i);
  }

  @Override
  public Socket createSocket(final String s, final int i, final InetAddress inaddr, final int j) throws IOException {
    return factory.createSocket(s, i, inaddr, j);
  }

  @Override
  public Socket createSocket(final String s, final int i) throws IOException {
    return factory.createSocket(s, i);
  }

  @Override
  public String[] getDefaultCipherSuites() {
    return factory.getSupportedCipherSuites();
  }

  @Override
  public String[] getSupportedCipherSuites() {
    return factory.getSupportedCipherSuites();
  }

  @Override
  public Socket createSocket() throws IOException {
    return factory.createSocket();
  }
}
