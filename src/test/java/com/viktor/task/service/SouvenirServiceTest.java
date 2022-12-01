package com.viktor.task.service;

import com.viktor.task.model.Producer;
import com.viktor.task.model.Souvenir;
import com.viktor.task.service.impl.SouvenirServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SouvenirServiceTest {

  private final SouvenirService souvenirService = new SouvenirServiceImpl();

  List<Souvenir> testSouvenirs = new ArrayList<>();

  @BeforeEach
  void setup() {
    Producer firstProducer = new Producer("B", "UA");
    Producer secondProducer = new Producer("C", "USA");
    testSouvenirs.add(new Souvenir("A", firstProducer, LocalDate.of(2007, 7, 23), 15.5));
    testSouvenirs.add(new Souvenir("A", secondProducer, LocalDate.of(2007, 5, 20), 20.5));
    testSouvenirs.add(new Souvenir("D", secondProducer, LocalDate.of(2008, 5, 20), 29.5));
  }

  @Test
  void findSouvenirsByCountry() {
    List<Souvenir> result = souvenirService.findSouvenirsByCountry(testSouvenirs, "USA");
    assertEquals(2, result.size());
  }

  @Test
  void findProducersByPriceLessThan() {
    List<Producer> result = souvenirService.findProducersByPriceLessThan(testSouvenirs, 18.4);
    assertEquals(1, result.size());
  }

  @Test
  void findAllInformationProducerAndHisSouvenirs() {
    Map<Producer, List<Souvenir>> result = souvenirService.findAllInformationProducerAndHisSouvenirs(testSouvenirs);
    assertEquals(2, result.get(new Producer("C", "USA")).size());
  }

  @Test
  void findProducersBySouvenirAndYear() {
    List<Producer> result = souvenirService.findProducersBySouvenirAndYear(testSouvenirs, "A", 2007);
    assertEquals(2, result.size());
  }

  @Test
  void findSouvenirsByYear() {
    Map<Year, List<Souvenir>> result = souvenirService.findSouvenirsByYears(testSouvenirs);
    assertEquals(2, result.get(Year.of(2007)).size());
  }

  @Test
  void deleteProducerAndHisSouvenirs() {
    //TODO
  }

}