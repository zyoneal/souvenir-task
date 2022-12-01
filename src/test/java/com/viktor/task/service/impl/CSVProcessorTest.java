package com.viktor.task.service.impl;

import com.viktor.task.model.Producer;
import com.viktor.task.model.Souvenir;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CSVProcessorTest {

  private final char DEFAULT_DELIMITER = ',';

  @Test
  void readSouvenirsFromCsvFileOK() throws IOException {
    Collection<Souvenir> expected = new ArrayList<>();
    Producer firstProducer = new Producer("B", "UA");
    Producer secondProducer = new Producer("C", "USA");
    expected.add(new Souvenir("A", firstProducer, LocalDate.of(2007, 7, 23), 15.5));
    expected.add(new Souvenir("A", secondProducer, LocalDate.of(2008, 5, 20), 20.5));

    Collection<Souvenir> souvenirs;
    try (InputStream in = getClass().getResourceAsStream("/test.csv")) {
      souvenirs = new CSVProcessor().readSouvenirsFromFile(in, DEFAULT_DELIMITER);
    }

    assertEquals(expected.size(), souvenirs.size());
  }

  @Test
  @DisplayName("method should return empty collection for an empty file (file with header only as input)")
  void emptyCollection_readSouvenirsFromCSV() throws IOException {
    Collection<Souvenir> souvenirs;
    try (InputStream in = getClass().getResourceAsStream("/test_only_header.csv")) {
      souvenirs = new CSVProcessor().readSouvenirsFromFile(in, DEFAULT_DELIMITER);
    }
    assertEquals(0, souvenirs.size());
  }

  @Test
  @DisplayName("method should fail for empty file")
  void exception_readFromEmptyFile() throws IOException {
    try (InputStream resourceAsStream = getClass().getResourceAsStream("/test_empty.csv")) {
      CSVProcessor processor = new CSVProcessor();
      assertThrows(IllegalArgumentException.class, () -> processor.readSouvenirsFromFile(resourceAsStream, DEFAULT_DELIMITER)
      );
    }
  }

  @Test
  @DisplayName("method should support produce same result regardless of order of headers")
  void ok_differentHeaders() throws IOException {
    Collection<Souvenir> rightHeaderOrder;

    try (InputStream in = getClass().getResourceAsStream("/test_right_header_order.csv")) {
      rightHeaderOrder = new CSVProcessor().readSouvenirsFromFile(in, DEFAULT_DELIMITER);
    }

    Collection<Souvenir> wrongHeaderOrder;
    try (InputStream stream = getClass().getResourceAsStream("/test_wrong_header_order.csv")) {
      wrongHeaderOrder = new CSVProcessor().readSouvenirsFromFile(stream, DEFAULT_DELIMITER);
    }
    assertEquals(rightHeaderOrder.size(), wrongHeaderOrder.size());
  }

  @Test
  @DisplayName("method should throw exception if date is wrong")
  void exception_InputDataTypeMismatching() throws IOException {
    CSVProcessor processor = new CSVProcessor();
    try (InputStream wrongDate = getClass().getResourceAsStream("/test_wrong_date.csv")) {
      assertThrows(DateTimeParseException.class, () -> processor.readSouvenirsFromFile(wrongDate, DEFAULT_DELIMITER)
      );
    }
  }

  @Test
  void exception_requiredHeaderMissed() throws IOException {
    CSVProcessor processor = new CSVProcessor();
    try (InputStream wrongHeader = getClass().getResourceAsStream("/test_required_header.csv")) {
      assertThrows(IllegalArgumentException.class, () -> processor.readSouvenirsFromFile(wrongHeader, DEFAULT_DELIMITER)
      );
    }
  }

  @Test
  void writeSouvenirsToFileOK() throws IOException {
    List<Souvenir> souvenirs = new ArrayList<>();
    Producer firstProducer = new Producer("B", "UA");
    Producer secondProducer = new Producer("C", "USA");
    souvenirs.add(new Souvenir("A", firstProducer, LocalDate.of(2007, 7, 23), 15.5));
    souvenirs.add(new Souvenir("A", secondProducer, LocalDate.of(2008, 5, 20), 20.5));

    String resourceName = "test_write_souvenirs.csv";

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource(resourceName).getFile());
    String absolutePath = file.getAbsolutePath();

    new CSVProcessor().writeSouvenirsToFile(absolutePath, souvenirs, DEFAULT_DELIMITER);

    assertNotEquals(0, file.length());
  }

}
