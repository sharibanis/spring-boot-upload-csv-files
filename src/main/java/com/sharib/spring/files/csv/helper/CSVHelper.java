package com.sharib.spring.files.csv.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.web.multipart.MultipartFile;

import com.sharib.spring.files.csv.model.Tutorial;

public class CSVHelper {
  public static String TYPE = "text/csv";
  public static String TYPE1 = "application/vnd.ms-excel";
  static String[] HEADERs = { "id", "login", "name", "salary" };

  public static boolean hasCSVFormat(MultipartFile file) {
	  
	  System.out.println("file.getContentType() "+file.getContentType());
    if (TYPE.equals(file.getContentType()) || TYPE1.equals(file.getContentType())) {
      return true;
    }

    return false;
  }

  public static List<Tutorial> csvToTutorials(InputStream is) {
    try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader,
            CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

    	//System.out.println("csvToTutorials...");

      List<Tutorial> tutorials = new ArrayList<Tutorial>();

      Iterable<CSVRecord> csvRecords = csvParser.getRecords();

      for (CSVRecord csvRecord : csvRecords) {
      	//System.out.println("csvRecord..." + csvRecord.toString());
    	  
        Tutorial tutorial = new Tutorial(
              csvRecord.get("id"),
              csvRecord.get("login"),
              csvRecord.get("name"),
              new BigDecimal(csvRecord.get("salary"))
            );

        //System.out.println("tutorial.toString(): "+tutorial.toString());
        tutorials.add(tutorial);
      }

      return tutorials;
    } catch (IOException e) {
      throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
    }
  }

  public static ByteArrayInputStream tutorialsToCSV(List<Tutorial> tutorials) {
    final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format);) {
      for (Tutorial tutorial : tutorials) {
        List<String> data = Arrays.asList(
              String.valueOf(tutorial.getId()),
              tutorial.getLogin(),
              tutorial.getName(),
              String.valueOf(tutorial.getSalary())
            );

        csvPrinter.printRecord(data);
      }

      csvPrinter.flush();
      return new ByteArrayInputStream(out.toByteArray());
    } catch (IOException e) {
      throw new RuntimeException("fail to import data to CSV file: " + e.getMessage());
    }
  }

}
