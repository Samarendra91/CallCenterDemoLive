package com.tavant.callccenter.model;

public class SeniorExecutive extends Employee {
  private String name;
  public SeniorExecutive(String name) {
    this.name = name;
    rank = Designation.SeniorExecutive;
  }
}
