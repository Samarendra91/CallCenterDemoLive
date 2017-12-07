package com.tavant.callccenter.sevice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tavant.callccenter.model.Caller;
import com.tavant.callccenter.model.Designation;
import com.tavant.callccenter.model.Employee;

public class Call {

  private static final Logger LOG = LoggerFactory.getLogger(Call.class);
  private Designation empType;
  private Caller caller;
  private Employee handler;

  public Call(Caller c) {
    empType = Designation.JuniorExecutive;
    caller = c;
  }

  public void setHandler(Employee e) {
    handler = e;
  }
  

  public Employee getHandler() {
    return handler;
  }

  public void reply(String message) {
    LOG.info(message);
  }

  public Designation getEmpType() {
    return empType;
  }

  public void setEmpType(Designation empType) {
    this.empType = empType;
  }

  public Designation nextRank() {
    if (empType == Designation.JuniorExecutive) {
      empType = Designation.SeniorExecutive;
    } else if (empType == Designation.SeniorExecutive) {
      empType = Designation.ProjectManager;
    }
    return empType;
  }

  public void disconnect() {
    reply("thanks for calling");
  }

  @Override
  public String toString() {
    return "Call [rank=" + empType + ", caller=" + caller.getMobile() + ", caller="
        + caller.getName() + "]";
  }

}
