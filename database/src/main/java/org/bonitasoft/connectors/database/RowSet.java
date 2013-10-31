/**
 * Copyright (C) 2010 BonitaSoft S.A.
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
package org.bonitasoft.connectors.database;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a table of data representing a database result set.
 * @author Matthieu Chaffotte
 *
 */
public class RowSet implements Serializable {

  private static final long serialVersionUID = 6888083143926506575L;
  private List<String> captions;
  private List<List<Object>> values;

  RowSet(ResultSet resultSet) throws SQLException {
    captions = new ArrayList<String>();
    values = new ArrayList<List<Object>>();
    if (resultSet != null) {
      ResultSetMetaData metaData = resultSet.getMetaData();
      int columns = metaData.getColumnCount();
      for (int i = 1; i <= columns; i++) {
        String columnName = metaData.getColumnName(i);
        captions.add(columnName);
      }
      while (resultSet.next()) {
        ArrayList<Object> row = new ArrayList<Object>();
        for (int j = 1; j <= columns; j++) {
          Object value = resultSet.getObject(j);
          row.add(value);
        }
        values.add(row);
      }
      resultSet.close();
    }
  }

  /**
   * Gets all the values of the RowSet
   * @return the values of the table
   */
  public List<List<Object>> getValues() {
    return values;
  }

  /**
   * Converts the designed column to a List object.
   * @param column the column name
   * @return a List object that contains the values stored in the specified column
   * @throws SQLException if an error occurs generating the collection or an invalid column name is provided
   */
  public List<Object> toList(String columnName) throws SQLException  {
    int column = findColumn(columnName);
    if (column > 0) {
      return toList(column);
    }
    return null;
  }

  /**
   * Gives the column index of the given column name.
   * @param columnName the name of the column
   * @return the column index of the given column name
   * @throws SQLException if the RowSet object does not contain columnName
   */
  public int findColumn(String columnName) throws SQLException {
    try {
      int index = captions.indexOf(columnName);
      if (index == -1) {
        throw new SQLException(columnName + " does not exist");
      }
      return  index + 1;
    } catch (Exception e) {
      throw new SQLException(columnName + " does not exist");
    }
  }

  /**
   * Converts the designed column to a List object.
   * @param column the column number
   * @return a List object that contains the values stored in the specified column
   * @throws SQLException if an error occurs generating the collection or an invalid column id is provided
   */
  public List<Object> toList(int column) throws SQLException {
    if (column < 1 || column > captions.size()) {
      throw new SQLException("invalid column id: " + column);
    }
    List<Object> list = new ArrayList<Object>();
    if (column > 0) {
      for (List<Object> row : values) {
        list.add(row.get(column -1));
      }
    }
    return list;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (String caption : captions) {
      builder.append(caption);
      builder.append("\t");
    }
    builder.append("\n");
    for (List<Object> row : values) {
      for (Object value : row) {
        builder.append(value);
        builder.append("\t");
      }
    builder.append("\n");  
    }
    return builder.toString();
  }

}
