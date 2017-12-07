package com.tavant.callccenter.model;

public class ProjectManager extends Employee {
  private String name;
  public ProjectManager(String name) {
    this.name = name;
    rank = Designation.ProjectManager;
  }

}
