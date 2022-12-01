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

  private final List<Souvenir> testSouvenirs = new ArrayList<>();

  @BeforeEach
  void setup() {
    Producer firstProducer = new Producer("B", "UA");
    Producer secondProducer = new Producer("C", "USA");
    this.testSouvenirs.add(new Souvenir("A", firstProducer, LocalDate.of(2007, 7, 23), 15.5));
    this.testSouvenirs.add(new Souvenir("A", secondProducer, LocalDate.of(2007, 5, 20), 20.5));
    this.testSouvenirs.add(new Souvenir("D", secondProducer, LocalDate.of(2008, 5, 20), 29.5));
  }

  @Test
  void findSouvenirsByCountryOK() {
    List<Souvenir> result = this.souvenirService.findSouvenirsByCountry(testSouvenirs, "USA");
    assertEquals(2, result.size());
  }

  @Test
  void findProducersByPriceLessThanOK() {
    List<Producer> result = this.souvenirService.findProducersByPriceLessThan(testSouvenirs, 18.4);
    assertEquals(1, result.size());
  }

  @Test
  void findAllInformationProducerAndHisSouvenirsOK() {
    Map<Producer, List<Souvenir>> result = this.souvenirService.findAllInformationProducerAndHisSouvenirs(testSouvenirs);
    assertEquals(2, result.get(new Producer("C", "USA")).size());
  }

  @Test
  void findProducersBySouvenirAndYearOK() {
    List<Producer> result = this.souvenirService.findProducersBySouvenirAndYear(testSouvenirs, "A", 2007);
    assertEquals(2, result.size());
  }

  @Test
  void findSouvenirsByYearOK() {
    Map<Year, List<Souvenir>> result = this.souvenirService.findSouvenirsByYears(testSouvenirs);
    assertEquals(2, result.get(Year.of(2007)).size());
  }

  @Test
  void deleteProducerAndHisSouvenirsOK() {
    this.souvenirService.deleteProducerAndHisSouvenirs(this.testSouvenirs,new Producer("C", "USA"));
    assertEquals(1, this.testSouvenirs.size());
  }

  @Test
  void findAllSouvenirsOk() {
    List<Souvenir> souvenirs = this.souvenirService.findAllSouvenirs(this.testSouvenirs);
    assertEquals(3, souvenirs.size());
  }

  @Test
  void findAllProducersOk() {
    List<Producer> producers = this.souvenirService.findAllProducers(this.testSouvenirs);
    assertEquals(2, producers.size());
  }

}
