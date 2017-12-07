/**
 * 
 */
package com.tavant.callccenter.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.tavant.callccenter.model.Caller;
import com.tavant.callccenter.sevice.Call;
import com.tavant.callccenter.sevice.CallConfig;
import com.tavant.callccenter.sevice.CallHandler;

/**
 * @author samarendra.sahoo
 *
 */
@RestController
public class CallHandleController {
  private static final Logger LOG = LoggerFactory.getLogger(Call.class);
  
  @Autowired
  private CallConfig callConfig;
  
  @RequestMapping(value = "/calls", method = RequestMethod.POST ,consumes="application/json", produces = "application/json")
  public List<Call> handleCall(@RequestBody List<Caller> callers) {
    
    List<Call> responseCall = new ArrayList<>();
    ExecutorService executor = Executors.newFixedThreadPool(10);
    List<Future<Call>> list = new ArrayList<Future<Call>>();
    for(Caller caller : callers) {
      CallHandler handler = new CallHandler(callConfig);
      handler.setCaller(caller);
      Callable<Call> callable = handler;
      Future<Call> future = executor.submit(callable);
      list.add(future);
    }
    
    
    for(Future<Call> fut : list){
      try {
        
        Call call = fut.get();
        responseCall.add(call);
      }catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    return responseCall;
  }
}
