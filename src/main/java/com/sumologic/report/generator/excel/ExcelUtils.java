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