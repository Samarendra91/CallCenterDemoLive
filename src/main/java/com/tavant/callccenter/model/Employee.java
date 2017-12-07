package com.tavant.callccenter.model;

import java.util.Timer;
import java.util.TimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import com.tavant.callccenter.sevice.Call;
import com.tavant.callccenter.sevice.CallHandler;

public class Employee {

  protected Call currentCall = null;
  protected Designation rank;

  @Autowired
  private CallHandler handler;

  public Employee() {

  }

  public boolean receiveAndHandleCall(Call call) {
    currentCall = call;
    int minutes = 7;
    minutes =
        (call.getEmpType().getValue() == 0) ? 7 : ((call.getEmpType().getValue() == 1) ? 10 : 15);
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
      @Override
      public void run() { // Function runs every MINUTES minutes.
        // Run the code you want here
        escalateAndReassign(call.getHandler()); // If the function you wanted was static
      }
    }, 0, 1000 * 60 * minutes);

    return false;
  }

  public void callFinished() {
    if (currentCall != null) {
      currentCall.disconnect();
      currentCall = null;
    }
    assignNewCall();
  }

  public void escalateAndReassign(Employee emp) {
    if (currentCall != null) {
      currentCall.nextRank();
      // assign the current call
      emp.handler.dispatchCall(currentCall);
      currentCall = null;
    }
    assignNewCall();
  }

  public boolean assignNewCall() {
    if (!isFree()) {
      return false;
    }
    return handler.assignCall(this);
  }

  public boolean isFree() {
    return currentCall == null;
  }

  public Designation getRank() {
    return rank;
  }

}
