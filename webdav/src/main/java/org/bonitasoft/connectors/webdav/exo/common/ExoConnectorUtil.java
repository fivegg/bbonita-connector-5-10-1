/**
 * Copyright (C) 2011 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.connectors.webdav.exo.common;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yanyan Liu
 */
public class ExoConnectorUtil {

  private static Map<String, String> characterMap;
  static {
    characterMap = new HashMap<String, String>();
    characterMap.put("%", "%25");
    characterMap.put("?", "%3F");
    characterMap.put("*", "%2A");
    characterMap.put("+", "%2B");
    characterMap.put("$", "%24");
    characterMap.put("^", "%5E");
    characterMap.put(" ", "%20");
    characterMap.put("/", "%2F");
    characterMap.put("@", "%40");
    characterMap.put("#", "%23");
    characterMap.put("&", "%26");
    characterMap.put("=", "%3D");
    characterMap.put("\\", "%5C");
    characterMap.put("|", "%7C");
  }

  public static String encodeSpecialCharacter(String name) {
    System.out.println("name = " + name);
    // check % first
    if (name.indexOf('%') != -1) {
      name = name.replaceAll("%", characterMap.get("%"));
      System.out.println("name = " + name);
    }
    // character in regular expression should be escaped
    if (name.indexOf('?') != -1) {
      name = name.replaceAll("\\?", characterMap.get("?"));
      System.out.println("name = " + name);
    }
    if (name.indexOf('*') != -1) {
      name = name.replaceAll("\\*", characterMap.get("*"));
      System.out.println("name = " + name);
    }
    if (name.indexOf('+') != -1) {
      name = name.replaceAll("\\+", characterMap.get("+"));
    }
    if (name.indexOf('$') != -1) {
      name = name.replaceAll("\\$", characterMap.get("$"));
      System.out.println("name = " + name);
    }
    if (name.indexOf('^') != -1) {
      name = name.replaceAll("\\^", characterMap.get("^"));
      System.out.println("name = " + name);
    }

    if (name.indexOf(' ') != -1) {
      name = name.replaceAll(" ", characterMap.get(" "));
    }
    if (name.indexOf('/') != -1) {
      name = name.replaceAll("/", characterMap.get("/"));
    }
    if (name.indexOf('@') != -1) {
      name = name.replaceAll("@", characterMap.get("@"));
      System.out.println("name = " + name);
    }
    if (name.indexOf('#') != -1) {
      name = name.replaceAll("#", characterMap.get("#"));
      System.out.println("name = " + name);
    }
    if (name.indexOf('&') != -1) {
      name = name.replaceAll("&", characterMap.get("&"));
      System.out.println("name = " + name);
    }
    if (name.indexOf('=') != -1) {
      name = name.replaceAll("=", characterMap.get("="));
    }
    if (name.indexOf('\\') != -1) {
      name = name.replaceAll("\\", characterMap.get("\\"));
    }
    if (name.indexOf('|') != -1) {
      name = name.replaceAll("|", characterMap.get("|"));
    }
    return name;
  }

}
