package com.viktor.task.model;

import java.time.LocalDate;
import java.util.Objects;

public class Souvenir {

  private String name;

  private Producer producer;

  private LocalDate createdAt;

  private Double price;

  public Souvenir(String name, Producer producer, LocalDate createdAt, Double price) {
    this.name = name;
    this.producer = producer;
    this.createdAt = createdAt;
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

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDate createdAt) {
    this.createdAt = createdAt;
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
    return Objects.equals(name, souvenir.name) && Objects.equals(producer, souvenir.producer) && Objects.equals(createdAt, souvenir.createdAt) && Objects.equals(price, souvenir.price);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, producer, createdAt, price);
  }

  @Override
  public String toString() {
    return "Souvenir -> " +
            "name: " + name +
            ", " + producer +
            ", created: " + createdAt +
            ", price: " + price;
  }

}
