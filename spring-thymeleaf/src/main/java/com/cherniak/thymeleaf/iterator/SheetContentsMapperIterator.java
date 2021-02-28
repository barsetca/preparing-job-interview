package com.cherniak.thymeleaf.iterator;


import java.util.Map;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;

public class SheetContentsMapperIterator implements SheetContentsHandler {

  private final Map<String, String> map;
  private int lineNumber = 0;
  private String value;

  /**
   * Destination for data
   */
  public SheetContentsMapperIterator(Map<String, String> map) {
    this.map = map;
  }

  @Override
  public void startRow(int i) {
    lineNumber = i;
  }

  @Override
  public void endRow(int i) {
  }

  @Override
  public void cell(String cellReference, String cellValue, XSSFComment comment) {
    int columnIndex = (new CellReference(cellReference)).getCol();

    if (lineNumber > 0) {
      switch (columnIndex) {
        case 0: {
          if (cellValue != null && !cellValue.isEmpty()) {
            value = cellValue;
          }
        }
        break;
        case 1: {
          if (cellValue != null && !cellValue.isEmpty()) {
            map.put(cellValue, value);
          }
        }
        break;
        default:
      }
    }
  }

  @Override
  public void headerFooter(String s, boolean b, String s1) {
  }
}
