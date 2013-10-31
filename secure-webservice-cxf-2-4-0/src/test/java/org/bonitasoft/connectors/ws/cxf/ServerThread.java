/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.bonitasoft.connectors.ws.cxf;


public class ServerThread extends Thread {

  private org.mortbay.jetty.Server server;

  public ServerThread(final org.mortbay.jetty.Server server) {
    this.server = server;
  }
  
  public void run() {
    try {
      server.start();
      System.out.println("Server ready...");
      server.join();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  
  public void shutdown() {
    try {
      server.stop();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public boolean isServerStarted() {
    return server.isStarted();
  }

}
