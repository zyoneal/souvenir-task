package com.viktor.task.service.impl;

import com.viktor.task.model.Producer;
import com.viktor.task.model.Souvenir;
import com.viktor.task.service.SouvenirService;

import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SouvenirServiceImpl implements SouvenirService {

  @Override
  public List<Souvenir> findAllSouvenirs(List<Souvenir> souvenirList) {
    return souvenirList.stream().distinct().collect(Collectors.toList());
  }

  @Override
  public List<Producer> findAllProducers(List<Souvenir> souvenirList) {
    return souvenirList.stream().map(Souvenir::getProducer).distinct().collect(Collectors.toList());
  }

  @Override
  public List<Souvenir> findSouvenirsByCountry(List<Souvenir> souvenirList, String country) {
    return souvenirList.stream().filter(souvenir -> souvenir.getProducer().getCountry().equals(country)).collect(Collectors.toList());
  }

  @Override
  public List<Producer> findProducersByPriceLessThan(List<Souvenir> souvenirList, Double price) {
    return souvenirList.stream()
            .filter(souvenir -> souvenir.getPrice().compareTo(price) < 0)
            .map(Souvenir::getProducer)
            .distinct()
            .collect(Collectors.toList());
  }

  @Override
  public Map<Producer, List<Souvenir>> findAllInformationProducerAndHisSouvenirs(List<Souvenir> souvenirList) {
    Map<Producer, List<Souvenir>> result = new HashMap<>();
    List<Producer> producerList = souvenirList.stream().map(Souvenir::getProducer).distinct().collect(Collectors.toList());
    for (Producer producer : producerList) {
      List<Souvenir> listOfThatProducer = souvenirList.stream().filter(souvenir -> souvenir.getProducer().equals(producer)).collect(Collectors.toList());
      result.put(producer, listOfThatProducer);
    }
    return result;
  }

  @Override
  public List<Producer> findProducersBySouvenirAndYear(List<Souvenir> souvenirList, String souvenir, int year) {
    return souvenirList.stream()
            .filter(s -> s.getName().equals(souvenir))
            .filter(s -> s.getCreatedAt().getYear() == year)
            .map(Souvenir::getProducer)
            .collect(Collectors.toList());
  }

  @Override
  public Map<Year, List<Souvenir>> findSouvenirsByYears(List<Souvenir> souvenirList) {
    Map<Year, List<Souvenir>> resultMap = new HashMap<>();
    for (Year start = Year.of(0); start.compareTo(Year.now()) < 0; start = start.plusYears(1)) {
      int temp = start.getValue();
      List<Souvenir> souvenirsOfYear = souvenirList.stream()
              .filter(souvenir -> temp == souvenir.getCreatedAt().getYear())
              .collect(Collectors.toList());
      if (!souvenirsOfYear.isEmpty()) {
        resultMap.put(start, souvenirsOfYear);
      }
    }
    return resultMap;
  }

  @Override
  public void deleteProducerAndHisSouvenirs(List<Souvenir> souvenirList, Producer producer) {
    List<Souvenir> souvenirsWithProducer = souvenirList.stream()
            .filter(souvenir -> souvenir.getProducer().equals(producer))
            .collect(Collectors.toList());
    souvenirList.removeAll(souvenirsWithProducer);
  }

  public void editCountryOfProducer(List<Souvenir> souvenirList, String producerName, String newCountry) {
    souvenirList.stream()
            .map(Souvenir::getProducer)
            .filter(producer -> producer.getName().equals(producerName))
            .forEach(producer -> producer.setCountry(newCountry));
  }

  @Override
  public void editPriceOfSouvenir(List<Souvenir> souvenirs, Predicate<Souvenir> souvenirPredicate,String name, double newPrice) {
    souvenirs.stream().filter(souvenirPredicate).forEach(souvenir -> souvenir.setPrice(newPrice));
  }

}
