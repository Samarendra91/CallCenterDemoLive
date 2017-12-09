package com.tavant.callccenter.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;

import com.tavant.callccenter.repository.EmployeeDetailRepository;
import com.tavant.callccenter.sevice.Call;
import com.tavant.callccenter.sevice.CallHandler;

public class Employee {

	@Autowired
	private EmployeeDetailRepository callDetailRepository;

	protected Call currentCall = null;

	protected Designation rank;

	private List<String> mobileNumbers = addMobileNumbers();

	@Autowired
	private CallHandler handler;

	private Map<Employee, CallHandler> employeeHandlerMap = new HashMap<>();

	public Employee() {

	}

	public void receiveAndHandleCall(Call call) {
		currentCall = call;
		int callTimeInMins = new Random().nextInt(15);
		employeeHandlerMap.put(call.getHandler(), handler);
		// if (mobileNumbers.contains(call.getCaller().getMobile())) {
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
			CallHandler newhandler = employeeHandlerMap.get(emp);
			handler = newhandler;
			handler.dispatchCall(currentCall);
			currentCall = null;
		}
		assignNewCall();
	}

	public boolean assignNewCall() {
		if (!isFree()) {
			return false;
		}
		CallHandler newhandler = employeeHandlerMap.get(this);
		handler = newhandler;
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
}
