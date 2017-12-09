/**
 * 
 */
package com.tavant.callccenter.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

import com.tavant.callccenter.model.CallCenterDetail;
import com.tavant.callccenter.model.Caller;
import com.tavant.callccenter.model.EmployeeDetail;
import com.tavant.callccenter.repository.CallCenterRepository;
import com.tavant.callccenter.repository.EmployeeDetailRepository;
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
	
	@Autowired
	private CallCenterRepository callCenterRepo;
	
	@Autowired
	private EmployeeDetailRepository employeeDetailRepo;

	@RequestMapping(value = "/calls", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public List<Call> handleCall(@RequestBody List<Caller> callers) {

		List<Call> responseCall = new ArrayList<>();
		ExecutorService executor = Executors.newFixedThreadPool(10);
		List<Future<Call>> list = new ArrayList<Future<Call>>();
		for (Caller caller : callers) {
			CallHandler handler = new CallHandler(callConfig);
			handler.setCaller(caller);
			Callable<Call> callable = handler;
			Future<Call> future = executor.submit(callable);
			list.add(future);
		}

		for (Future<Call> fut : list) {
			try {

				Call call = fut.get();
				responseCall.add(call);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return responseCall;
	}

	@RequestMapping(value = "/generate")
	public void callgenerator() {
		
		int totalCall = 4;
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for (int i = 1; i < totalCall; i++) {

			Caller caller = new Caller();
			caller.setMobile("mobile" + i);
			caller.setName("name" + i);
			
			CallHandler handler = new CallHandler(callConfig);
			handler.setCaller(caller);
			Callable<Call> callable = handler;
			executor.submit(callable);
		}
	}
	
	@RequestMapping(value = "/callcenter/{name}",method= RequestMethod.GET , produces = "application/json")
	public CallCenterDetail callCenterReport(@PathVariable String name) {
		
		CallCenterDetail callCenterDetail = callCenterRepo.findByName(name);
		return callCenterDetail;
	}
	
	@RequestMapping(value = "/empdetail",method= RequestMethod.GET , produces = "application/json")
	public Iterable<EmployeeDetail> employeeReport() {
		
		Iterable<EmployeeDetail> employeeDetails = employeeDetailRepo.findAll();
		return employeeDetails;
	}
}
