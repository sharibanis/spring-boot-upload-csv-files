package com.sharib.spring.files.csv.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employees")
public class Tutorial {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "login")
  private String login;

  @Column(name = "name")
  private String name;

  @Column(name = "salary")
  private BigDecimal salary;

  public Tutorial(String id, String login, String name, BigDecimal salary) {
    this.id = id;
    this.login = login;
    this.name = name;
    this.salary = salary;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getSalary() {
    return salary;
  }

  public void setSalary(BigDecimal salary) {
    this.salary = salary;
  }

  @Override
  public String toString() {
    return "Tutorial [id=" + id + ", login=" + login + ", name=" + name + ", salary=" + salary + "]";
  }

}
