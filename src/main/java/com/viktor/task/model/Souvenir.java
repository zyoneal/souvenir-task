package com.viktor.task.model;

import java.time.LocalDate;
import java.util.Objects;

public class Souvenir {

  private String name;

  private Producer producer;

  private LocalDate createdOn;

  private Double price;

  public Souvenir(String name, Producer producer, LocalDate createdOn, Double price) {
    this.name = name;
    this.producer = producer;
    this.createdOn = createdOn;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Producer getProducer() {
    return producer;
  }

  public void setProducer(Producer producer) {
    this.producer = producer;
  }

  public LocalDate getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(LocalDate createdOn) {
    this.createdOn = createdOn;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Souvenir souvenir = (Souvenir) o;
    return Objects.equals(name, souvenir.name) && Objects.equals(producer, souvenir.producer) && Objects.equals(createdOn, souvenir.createdOn) && Objects.equals(price, souvenir.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, producer, createdOn, price);
  }

  @Override
  public String toString() {
    return "Souvenir -> " +
            "name: " + name +
            ", " + producer +
            ", created: " + createdOn +
            ", price: " + price;
  }

}
