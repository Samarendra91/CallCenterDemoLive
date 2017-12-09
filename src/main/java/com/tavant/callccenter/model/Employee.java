package com.tavant.callccenter.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tavant.callccenter.repository.CallCenterRepository;
import com.tavant.callccenter.repository.EmployeeDetailRepository;
import com.tavant.callccenter.sevice.Call;
import com.tavant.callccenter.sevice.CallConfig;
import com.tavant.callccenter.sevice.CallHandler;

public class Employee {
	
	private static final Logger LOG = LoggerFactory.getLogger(Call.class);

	@Autowired
	private CallCenterRepository callCenterRepo;

	@Autowired
	private EmployeeDetailRepository employeeDetailRepo;

	private String name;

	protected Call currentCall = null;

	protected Designation rank;

	private CallHandler handler;

	public Employee(String name) {
		this.name = name;
	}

	public void receiveAndHandleCall(Call call, CallConfig callConfig) {
		saveEmpDetail(call);
		currentCall = call;
		handler = new CallHandler(callConfig);
		int callTimeInMins = new Random().nextInt(15);
		if (callTimeInMins < 7 && call.getHandler().getRank().getValue() == 0
				|| callTimeInMins < 10 && call.getHandler().getRank().getValue() == 1
				|| callTimeInMins < 15 && call.getHandler().getRank().getValue() == 2) {
			callFinished();
		} else {

			int minutes = 7;
			minutes = (call.getEmpType().getValue() == 0) ? 7 : ((call.getEmpType().getValue() == 1) ? 10 : 15);
			ExecutorService executor = Executors.newSingleThreadExecutor();
			try {
				executor.invokeAll(Arrays.asList(new Callable<Employee>() {

					@Override
					public Employee call() throws Exception {
						escalateAndReassign(call.getHandler());
						return null;
					}
				}), minutes, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			executor.shutdown();
		}
	}

	private void saveEmpDetail(Call call) {
		Employee emp = call.getHandler();
		EmployeeDetail empDetails = null;
		String name = emp.getName();
		Optional<EmployeeDetail> empDetail = employeeDetailRepo.findByName(name);
		if (empDetail.isPresent()) {
			empDetails = empDetail.get();
			empDetails.setCallsAttended(empDetails.getCallsAttended() + 1);
		} else {
			empDetails = new EmployeeDetail();
			empDetails.setName(emp.getName());
		}

		employeeDetailRepo.save(empDetails);
	}

	public void callFinished() {
		if (currentCall != null) {
			currentCall.disconnect();
			currentCall = null;
		}
		assignNewCall();
	}

	public void escalateAndReassign(Employee emp) {
		if (emp.getRank().getValue() != 2) {
			if (currentCall != null) {
				currentCall.nextRank();
				handler.dispatchCall(currentCall);
				currentCall = null;
			}
		}else {
			LOG.info("We are very sorry inform you that, we could not able to resolve your issue. Thanks for calling ");
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

	public List<String> addMobileNumbers() {
		List<String> temp = new ArrayList<>();
		for (int i = 0; i < 30; i++) {
			temp.add("mobile" + i);
			i++;
		}
		return temp;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
