package com.cherniak.thymeleaf.iterator;

import com.cherniak.thymeleaf.files.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
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
public class ExcelServiceIterator {


  private final ReportProcessingServiceIterator reportProcessingServiceIterator;
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

  private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

  /**
   * parse file
   *
   * @param inputStream is
   * @return parsed data as dictionary
   * @throws IOException
   */

  public void parseFile(InputStream inputStream, File file) throws IOException {
    AtomicBoolean isNotDataLine = new AtomicBoolean(true);
    try {
      OPCPackage xlsxPackage = OPCPackage.open(inputStream);
      ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);
      XSSFReader xssfReader = new XSSFReader(xlsxPackage);
      StylesTable styles = xssfReader.getStylesTable();
      XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
      try (InputStream stream = iter.next()) {
        processSheet(styles, strings, new SheetContentsMapperIterator(reportProcessingServiceIterator, file, isNotDataLine), stream);
      }
    } catch (OpenXML4JException | SAXException e) {
      e.printStackTrace();
    }

      System.out.println("isNotDataLine for IllegalArgumentException = " + isNotDataLine.get());


//    int size = result.keySet().size();
//    log.info("Found {} record(s) in file", size);
//    if (size == 0) {
//      throw new IllegalArgumentException(size + " record(s) in file");
//    }
  }

  private void processSheet(
      StylesTable styles,
      ReadOnlySharedStringsTable strings,
      SheetContentsMapperIterator sheetContentsMapper,
      InputStream sheetInputStream
  ) throws IOException, SAXException {
    var formatter = new DataFormatter();
    var sheetSource = new InputSource(sheetInputStream);
    try {
      XMLReader sheetParser = SAXHelper.newXMLReader();
      ContentHandler handler = new XSSFSheetXMLHandler(
          styles, null, strings, sheetContentsMapper, formatter, false);

      sheetParser.setContentHandler(handler);
      sheetParser.parse(sheetSource);

    } catch (ParserConfigurationException e) {
      throw new RuntimeException("SAX parser appears to be broken - " + e);
    }
  }

  //stream xlsx parser Implementation with anonymous SheetContentsHandler
  public void parseExcelWithoutMapping(InputStream inputStream, File file) throws IOException {

    var dataNotExist = new AtomicBoolean(true);
    try {
      OPCPackage xlsxPackage = OPCPackage.open(inputStream);
      ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);
      var xssfReader = new XSSFReader(xlsxPackage);
      StylesTable styles = xssfReader.getStylesTable();
      XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
      try (InputStream stream = iter.next()) {
          processSheetWithoutMapping(styles, strings, new SheetContentsHandler() {
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
                if (lineNumber == 1) {
                  dataNotExist.set(false);
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
                      reportProcessingServiceIterator.processFileAsyncIterator(cellValue, value, file);
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
          }, stream);
        }

    } catch (OpenXML4JException | SAXException e) {
      e.printStackTrace();
    }

    System.out.println("isNotDataLine for IllegalArgumentException = " + dataNotExist.get());

  }

  //with anonymous SheetContentsHandler
  private static void processSheetWithoutMapping(
      StylesTable styles,
      ReadOnlySharedStringsTable strings,
      SheetContentsHandler sheetContentsHandler,
      InputStream sheetInputStream) throws IOException, SAXException {
    var formatter = new DataFormatter();
    var sheetSource = new InputSource(sheetInputStream);
    try {
      XMLReader sheetParser = SAXHelper.newXMLReader();
      ContentHandler handler = new XSSFSheetXMLHandler(
          styles, null, strings, sheetContentsHandler, formatter, false);
      sheetParser.setContentHandler(handler);
      sheetParser.parse(sheetSource);
    } catch (ParserConfigurationException e) {
      throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
    }
  }








//  /**
//   * generate file
//   *
//   * @param accidentList accident list
//   * @param createDate   file creation date
//   * @return Path to xlsx file
//   * @throws IOException
//   */
//  public InputStream generateFile(List<Accident> accidentList, Instant createDate)
//      throws IOException {
//
//    XSSFWorkbook workbook = new XSSFWorkbook();
//    XSSFSheet sheet = workbook.createSheet("ЗАРЕГИСТРИРОВАННЫЕ СЛУЧАИ");
//    sheet.setDefaultColumnWidth(30);
//
//    XSSFTable table = sheet.createTable();
//    CTTable cttable = table.getCTTable();
//
//    cttable.setDisplayName("Table");
//    cttable.setId(1);
//    cttable.setName("Registred Accidents");
//    cttable.setRef("A2:M65536");
//    cttable.setTotalsRowShown(false);
//
//    CTTableStyleInfo styleInfo = cttable.addNewTableStyleInfo();
//    styleInfo.setName("TableStyleMedium2");
//    styleInfo.setShowColumnStripes(false);
//    styleInfo.setShowRowStripes(true);
//
//    CTTableColumns columns = cttable.addNewTableColumns();
//    int columnsCount = columnNames.size();
//    columns.setCount(columnsCount);
//
//    sheet.createRow(0)
//        .createCell(0)
//        .setCellValue(Optional.of(createDate)
//            .map(created -> new Date(created.toEpochMilli()))
//            .map(simpleDateFormat::format)
//            .map(s -> "Дата формирования реестра: " + s)
//            .orElse(""));
//
//    for (int i = 1; i <= columnsCount; i++) {
//      CTTableColumn column = columns.addNewTableColumn();
//      column.setId(i);
//      column.setName("Col" + i);
//    }
//
//    XSSFRow headerRow = sheet.createRow(1);
//    for (int i = 0; i < columnsCount; i++) {
//      headerRow.createCell(i).setCellValue(columnNames.get(i));
//    }
//
//    SXSSFWorkbook workbookS = new SXSSFWorkbook(workbook, 200, false);
//    SXSSFSheet sheetS = workbookS.getSheetAt(0);
//
//    for (int i = 0; i < accidentList.size(); i++) {
//      SXSSFRow tableRow = sheetS.createRow(i + sheet.getLastRowNum() + 1);
//      int cell = 0;
//      Accident a = accidentList.get(i);
//      Report r = a.getReport();
//
//      tableRow.createCell(cell++).setCellValue(i + 1);
//      tableRow.createCell(cell++).setCellValue(a.getPolicyNum());
//      tableRow.createCell(cell++).setCellValue(a.getSkName());
//      tableRow.createCell(cell++).setCellValue(Optional.ofNullable(a.getAccidentDate())
//          .map(created -> new Date(created.toEpochMilli()))
//          .map(simpleDateFormat::format).orElse(null));
//      tableRow.createCell(cell++).setCellValue(a.getDriver());
//      tableRow.createCell(cell++).setCellValue(a.getDocType());
//      tableRow.createCell(cell++).setCellValue(a.getDocNum());
//      tableRow.createCell(cell++).setCellValue(a.getVin());
//      tableRow.createCell(cell++).setCellValue(a.getRegNum());
//      tableRow.createCell(cell++).setCellValue(a.getBodyNum());
//      tableRow.createCell(cell++).setCellValue(a.getIss());
//      if (r.getCreateDate() != null && r.getCompleteDate() != null) {
//        tableRow.createCell(cell++).setCellValue(
//            r.getCompleteDate().getEpochSecond() - r.getCreateDate().getEpochSecond());
//      } else {
//        tableRow.createCell(cell++);
//      }
//      tableRow.createCell(cell).setCellValue(r.getErrorDescription());
//    }
//
//    Path filePath = Files.createTempFile(Paths.get(""), "temp-download-", ".xlsx");
//
//    try (var outFile = Files.newOutputStream(filePath)) {
//      workbookS.write(outFile);
//    }
//    workbookS.close();
//    if (!workbookS.dispose()) {
//      log.warn("Can't delete workbook temp files!");
//    }
//
//    return new InputStreamDelegator(
//        new BufferedInputStream(Files.newInputStream(filePath)),
//        () -> deleteFile(filePath)
//    );
//  }

  private void deleteFile(Path filePath) {
    try {
      Files.delete(filePath);
    } catch (IOException e) {
      log.warn("Can't delete file", e);
    }
  }
}
