package com.cherniak.thymeleaf.alexander;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import javax.xml.parsers.ParserConfigurationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;

import org.apache.poi.xssf.usermodel.XSSFComment;

import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


/**
 * Service parse xlsx file
 *
 * @author Artem Demyansky
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelServiceFinish {

  private final List<String> columnNames = List.of("№",
      "Серия и номер полиса",
      "Прямой страховщик",
      "Дата ДТП",
      "ФИО",
      "Тип документа",
      "Серия и номер документа",
      "VIN",
      "Гос. номер",
      "Номер кузова",
      "ИСС",
      "Время обработки запроса",
      "Описание ошибки");


  /**
   * parse file
   *
   * @param inputStream is
   * @param exchangerIsEmptyTable Exchanger<Boolean> true if table is Empty
   * @param consumer BiConsumer for processing
   * @throws IOException ex from POI and SAX
   */
  public void parseFile(
      BufferedInputStream inputStream,
      Exchanger<Boolean> exchangerIsEmptyTable,
      BiConsumer<String, String> consumer
  ) throws IOException {
    var dataNotExist = new AtomicBoolean(true);
    try(OPCPackage xlsxPackage = OPCPackage.open(inputStream)) {
      ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);
      var xssfReader = new XSSFReader(xlsxPackage);
      StylesTable styles = xssfReader.getStylesTable();
      XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
      try (InputStream stream = iter.next()) {
        processSheet(styles, strings, new SheetContentsHandler() {
          int lineNumber;
          String value;

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
              if (lineNumber == 1 && columnIndex == 0) {
                dataNotExist.set(false);
                try {
                  exchangerIsEmptyTable.exchange(false, 1, TimeUnit.MINUTES);
                } catch (InterruptedException | TimeoutException e) {
                  log.warn("Failed parseFile from exchanger", e);
                }
              }
              switch (columnIndex) {
                case 0: {
                  if (cellValue != null && !cellValue.isEmpty()) {
                    value = cellValue;
                  }
                }
                break;
                case 1: {
                  if (cellValue != null && !cellValue.isEmpty()) {
                    consumer.accept(cellValue, value);
                  }
                }
                break;
                default:
                  log.info("Data from nineNumber = {} and columnIndex = {} ignored", lineNumber, columnIndex);
                  break;
              }
            }
          }

          @Override
          public void headerFooter(String s, boolean b, String s1) {
          }
        }, stream);
      }
    } catch (OpenXML4JException | SAXException e) {
      e.printStackTrace();
    }

    if (dataNotExist.get()) {
      try {
        log.warn("0 record(s) in file");
        exchangerIsEmptyTable.exchange(true, 1, TimeUnit.MINUTES);
        throw new IllegalArgumentException("0 record(s) in file");
      } catch (InterruptedException | TimeoutException e) {
        log.warn("0 record(s) in file exception" , e);
        throw new IllegalArgumentException(e);
      }
    }
  }

  private void processSheet(
      StylesTable styles,
      ReadOnlySharedStringsTable strings,
      SheetContentsHandler sheetContentsHandler,
      InputStream sheetInputStream
  ) throws IOException, SAXException {
    var formatter = new DataFormatter();
    var sheetSource = new InputSource(sheetInputStream);
    try {
      XMLReader sheetParser = SAXHelper.newXMLReader();
      ContentHandler handler = new XSSFSheetXMLHandler(
          styles,
          null,
          strings,
          sheetContentsHandler,
          formatter,
          false
      );
      sheetParser.setContentHandler(handler);
      sheetParser.parse(sheetSource);
    } catch (ParserConfigurationException e) {
      throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
    }
  }
}
