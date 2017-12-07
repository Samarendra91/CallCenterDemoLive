package com.tavant.callccenter.model;

public class JuniorExecutive extends Employee {
  private String name;
  public JuniorExecutive(String name) {
    this.name = name;
    rank = Designation.JuniorExecutive;
  }
}
