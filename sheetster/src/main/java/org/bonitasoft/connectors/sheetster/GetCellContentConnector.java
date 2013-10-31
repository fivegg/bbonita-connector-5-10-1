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
package org.bonitasoft.connectors.sheetster;

/**
 * 
 * @author Matthieu Chaffotte
 * 
 */
public class GetCellContentConnector extends SheetsterConnector {

  private String workBookId;
  private String sheetName;
  private String cell;

  private Object cellContent;

  public Object getCellContent() {
    return cellContent;
  }

  public void setWorkBookId(final String workBookId) {
    this.workBookId = workBookId;
  }

  public void setSheetName(final String sheetName) {
    this.sheetName = sheetName;
  }

  public void setCell(final String cell) {
    this.cell = cell;
  }

  @Override
  protected void executeTask() throws Exception {
    final String cellResponse = "/workbook/id/" + workBookId + "/txt/cell/get/" + sheetName + "!" + cell;
    cellContent = getHTTPObject(cellResponse);
  }

}
