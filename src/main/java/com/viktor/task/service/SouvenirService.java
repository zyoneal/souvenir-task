package com.viktor.task.service;

import com.viktor.task.model.Producer;
import com.viktor.task.model.Souvenir;

import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public interface SouvenirService {

  List<Souvenir> findAllSouvenirs(List<Souvenir> souvenirList);

  List<Producer> findAllProducers(List<Souvenir> souvenirList);

  List<Souvenir> findSouvenirsByCountry(List<Souvenir> souvenirList, String country);

  List<Producer> findProducersByPriceLessThan(List<Souvenir> souvenirList, Double price);

  Map<Producer, List<Souvenir>> findAllInformationProducerAndHisSouvenirs(List<Souvenir> souvenirList);

  List<Producer> findProducersBySouvenirAndYear(List<Souvenir> souvenirList, String souvenir, int year);

  Map<Year, List<Souvenir>> findSouvenirsByYears(List<Souvenir> souvenirList);

  void deleteProducerAndHisSouvenirs(List<Souvenir> souvenirList, Producer producer);

  void editCountryOfProducer(List<Souvenir> souvenirList, String producerName, String newCountry);

  void editPriceOfSouvenir(List<Souvenir> souvenirs, Predicate<Souvenir> souvenirPredicate, String name, double newPrice);

}
