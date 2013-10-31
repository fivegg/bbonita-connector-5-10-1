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

import com.microsoft.schemas.exchange.services._2006.types.ExchangeVersionType;

/**
 * @author Jordi Anguela
 */
public class Constants {
	
	public static final Integer MAJOR_VERSION       = 8;
	public static final Integer MINOR_VERSION       = 0;
	public static final Integer MAJOR_BUILD_VERSION = 685;
	public static final Integer MINOR_BUILD_VERSION = 8;
	public static final String  EXCHANGE_VERSION    = ExchangeVersionType.EXCHANGE_2007.name();
	public static final String  EXCHANGE_SERVER_LOCATION_APAC               = "apac";
	public static final String  EXCHANGE_SERVER_LOCATION_EMEA               = "emea";
	public static final String  EXCHANGE_SERVER_LOCATION_NORTH_AMERICA      = "northamerica";
	public static final String  EXCHANGE_SERVER_LOCATION_APAC_NAME          = "Asia-Pacific";
	public static final String  EXCHANGE_SERVER_LOCATION_EMEA_NAME          = "Europe, Middle East and Africa";
	public static final String  EXCHANGE_SERVER_LOCATION_NORTH_AMERICA_NAME = "North America";
	
}
