package com.viktor.task.service.impl;

import com.viktor.task.model.Producer;
import com.viktor.task.model.Souvenir;
import com.viktor.task.service.FileService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.viktor.task.service.impl.HeaderConstants.*;

public class CSVProcessor implements FileService {

  @Override
  public List<Souvenir> readSouvenirsFromFile(InputStream file, char delimiter) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(file));
    CSVParser records = CSVFormat.Builder
            .create(CSVFormat.newFormat(delimiter))
            .setHeader()
            .build()
            .parse(in);

    validateParser(records);

    List<Souvenir> souvenirs = new ArrayList<>();
    for (CSVRecord csvRecord : records) {
      Souvenir souvenir = extractSouvenir(csvRecord);
      souvenirs.add(souvenir);
    }
    return souvenirs;
  }

  public void writeSouvenirsToFile(String file, List<Souvenir> souvenirs, char delimiter) throws IOException {
    try (
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(file));

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.newFormat(delimiter)
                    .withHeader(SOUVENIR_NAME, DATE, PRICE, COUNTRY, PRODUCER_NAME));

    ) {
      for (Souvenir souvenir : souvenirs) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedString = souvenir.getCreatedOn().format(formatter);
        csvPrinter.printRecord(souvenir.getName(), formattedString, souvenir.getPrice(), souvenir.getProducer().getCountry(), souvenir.getProducer().getName());
      }
      csvPrinter.flush();
    }
  }

  private Producer extractProducer(CSVRecord csvRecord) {
    try {
      return new Producer(csvRecord.get(PRODUCER_NAME), csvRecord.get(COUNTRY));
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid input data", e);
    }
  }

  private Souvenir extractSouvenir(CSVRecord csvRecord) {
    return new Souvenir(
            csvRecord.get(SOUVENIR_NAME),
            extractProducer(csvRecord),
            LocalDate.parse(csvRecord.get(DATE)),
            Double.parseDouble(csvRecord.get(PRICE))
    );
  }

  private void validateParser(CSVParser records) {
    if (records.getHeaderMap().isEmpty()) {
      throw new IllegalArgumentException("File is empty");
    } else {
      if (!Stream.of(SOUVENIR_NAME, DATE, PRICE, COUNTRY, PRODUCER_NAME).allMatch(records.getHeaderNames()::contains)) {
        throw new IllegalArgumentException("Required header missed");
      }
    }
  }

}
