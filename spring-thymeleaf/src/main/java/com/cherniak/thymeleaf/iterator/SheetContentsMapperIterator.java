package com.cherniak.thymeleaf.iterator;


import com.cherniak.thymeleaf.files.File;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.usermodel.XSSFComment;


public class SheetContentsMapperIterator implements SheetContentsHandler {


  private final ReportProcessingServiceIterator reportProcessingServiceIterator;
  private final File file;
  private final AtomicBoolean isNotDataLine;
  private int lineNumber = 0;
  private String value;

  /**
   * Destination for data
   */
  public SheetContentsMapperIterator(
      ReportProcessingServiceIterator reportProcessingServiceIterator, File file,
      AtomicBoolean isNotDataLine) {
    this.file = file;
    this.reportProcessingServiceIterator = reportProcessingServiceIterator;
    this.isNotDataLine = isNotDataLine;
  }

  @Override
  public void startRow(int i) {
    System.out.println("startRow " + i);
    lineNumber = i;
  }

  @Override
  public void endRow(int i) {
    System.out.println("endRow " + i);
  }

  @Override
  public void cell(String cellReference, String cellValue, XSSFComment comment) {
    int columnIndex = (new CellReference(cellReference)).getCol();
    System.out.println("public void cell =" + lineNumber + " cellValue " + cellValue);
    if (lineNumber == 1) {
      isNotDataLine.set(false);
    }
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
            reportProcessingServiceIterator.processFileAsyncIterator(cellValue, value, file);
            System.out.println("from SheetContentsMapperIterator " + cellValue + " -> " + value);
            //map.put(cellValue, value);
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
