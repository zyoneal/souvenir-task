package com.viktor.task.service.impl;

import com.viktor.task.model.Producer;
import com.viktor.task.model.Souvenir;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CSVProcessorTest {

  private final char DEFAULT_DELIMITER = ',';

  @Test
  void readEmployeesFromCSV_fileInUtf8_ok() throws IOException {
    Collection<Souvenir> expected = new ArrayList<>();
    Producer firstProducer = new Producer("B", "UA");
    Producer secondProducer = new Producer("C", "USA");
    expected.add(new Souvenir("A", firstProducer, LocalDate.of(2007, 7, 23), 15.5));
    expected.add(new Souvenir("A", secondProducer, LocalDate.of(2008, 5, 20), 20.5));

    Collection<Souvenir> souvenirs;
    try (InputStream in = getClass().getResourceAsStream("/test.csv")) {
      souvenirs = new CSVProcessor().readSouvenirsFromFile(in, DEFAULT_DELIMITER);
    }

    System.out.println("Expected: " + expected);
    System.out.println("============");
    System.out.println("FROM FILE: " + souvenirs);

    assertEquals(expected.size(), souvenirs.size());
  }

}