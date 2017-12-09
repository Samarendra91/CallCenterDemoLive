/**
 * 
 */
package com.tavant.callccenter.sevice;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import org.springframework.stereotype.Service;
import com.tavant.callccenter.model.Employee;
import com.tavant.callccenter.model.JuniorExecutive;
import com.tavant.callccenter.model.ProjectManager;
import com.tavant.callccenter.model.SeniorExecutive;

/**
 * @author samarendra.sahoo
 *
 */
@Service
public final class CallConfig {

  private static CallConfig instance;
  public final static int LEVELS = 3;
  private static int NUM_JES = 5;
  private static int NUM_SES = 3;
  private static int NUM_PMS = 1;
  // list of employees by level
  ArrayList<ArrayList<Employee>> employeeLevels;
  ArrayList<ArrayBlockingQueue<Call>> callQueues;
  
  public static CallConfig configure() {
    if (instance == null) {
      instance = new CallConfig();
    }
    return instance;
  }
  
  public static CallConfig getInstance() {
    // TODO Auto-generated method stub
    return instance;
  }
  
  private CallConfig() {
    employeeLevels = new ArrayList<ArrayList<Employee>>(LEVELS);
    callQueues = new ArrayList<ArrayBlockingQueue<Call>>(LEVELS);

    ArrayList<Employee> juniorExecutives = new ArrayList<Employee>();
    for (int i = 0; i < NUM_JES; i++) {
      juniorExecutives.add(new JuniorExecutive("Junior"+i));
    }
    employeeLevels.add(juniorExecutives);
    
    if(NUM_JES<NUM_SES) {
    	NUM_SES = NUM_JES/2;
    }
    
    ArrayList<Employee> seniorExecutives = new ArrayList<Employee>();
    for (int i = 0; i < NUM_SES; i++) {
    seniorExecutives.add(new SeniorExecutive("Senior"+i));
    }
    employeeLevels.add(seniorExecutives);

    ArrayList<Employee> manager = new ArrayList<Employee>();
    for (int i = 0; i < NUM_PMS; i++) {
    manager.add(new ProjectManager("Manager"+1));
    }
    employeeLevels.add(manager);
  }
}
