package com.viktor.task.model;

import java.util.Objects;

public class Producer implements Comparable<Producer> {

  private String name;

  private String country;

  public Producer(String name, String country) {
    this.name = name;
    this.country = country;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Producer producer = (Producer) o;
    return Objects.equals(name, producer.name) && Objects.equals(country, producer.country);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, country);
  }

  @Override
  public String toString() {
    return "Producer -> " +
            "name: " + name +
            ", country: " + country;
  }

  @Override
  public int compareTo(Producer other) {
    if (this.name.compareTo(other.name) < 0) return -1;
    if (this.name.compareTo(other.name) > 0) return 1;
    else return 0;
  }

}
