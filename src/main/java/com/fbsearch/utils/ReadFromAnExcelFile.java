package com.fbsearch.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Songkran Totiya
 */
public class ReadFromAnExcelFile {

    public List<String> readDataFromExcelFile(String fullPath, String sheetNm) throws FileNotFoundException, IOException {
        List<String> resultList = new ArrayList<String>();
        File excel = new File(fullPath);
        FileInputStream fis = new FileInputStream(excel);
        XSSFWorkbook wb;
        wb = new XSSFWorkbook(fis);
        XSSFSheet ws = wb.getSheet(sheetNm);

        int rowNum = ws.getLastRowNum() + 1;
        int colNum = ws.getRow(0).getLastCellNum();
        String[][] data = new String[rowNum][colNum];

        for (int i = 0; i < rowNum; i++) {
            XSSFRow row = ws.getRow(i);
            for (int j = 0; j < colNum; j++) {
                XSSFCell cell = row.getCell(j);
                String value = cell.toString();
                data[i][j] = value;
                resultList.add(value);
            }
        }
        return resultList;

    }
}
