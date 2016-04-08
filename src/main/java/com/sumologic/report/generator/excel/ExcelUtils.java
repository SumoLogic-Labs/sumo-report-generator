/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.sumologic.report.generator.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Iterator;

public class ExcelUtils {

    private ExcelUtils() {
    }

    public static int lookupCellIndexByColumnName(String colName, Sheet workbookSheet) {
        int index = -1;
        Row columnRow = workbookSheet.getRow(0);
        Iterator iterator = columnRow.cellIterator();
        while (iterator.hasNext()) {
            Cell cell = (Cell) iterator.next();
            if (cell.getStringCellValue().equals(colName)) {
                index = cell.getColumnIndex();
            }
        }
        return index;
    }

}