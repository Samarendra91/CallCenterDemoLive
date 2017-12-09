package com.tavant.callccenter.sevice;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.tavant.callccenter.model.Caller;
import com.tavant.callccenter.model.Employee;

@Component
public class CallHandler implements Callable<Call> {

	private static final Logger LOG = LoggerFactory.getLogger(Call.class);

	private CallConfig callConfig;

	private Caller caller;

	public Caller getCaller() {
		return caller;
	}

	public void setCaller(Caller caller) {
		this.caller = caller;
	}

	/**
	 * @param callConfig
	 */
	@Autowired
	public CallHandler(CallConfig callConfig) {
		this.callConfig = callConfig;
	}

	@Override
	public Call call() throws Exception {
		LOG.info(Thread.currentThread().getName() + "  " + caller.getMobile() + " " + caller.getName());
		// LOG.info(caller.getMobile()+" "+caller.getName());
		Call call = new Call(caller);
		dispatchCall(call);
		// return the thread name executing this callable task
		return call;
	}

	public boolean assignCall(Employee emp) {
		// get the highest rank this employee can serve
		for (int rank = emp.getRank().getValue(); rank >= 0; rank--) {
			ArrayBlockingQueue<Call> queue = callConfig.callQueues.get(rank);

			if (queue.size() > 0) {
				Call call = null;
				try {
					call = queue.take();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (call != null) {
					// receive call
					return true;
				}
			}
		}
		return false;
	}

	public Optional<Employee> setHandlerForCall(Call call) {

		switch (call.getEmpType().getValue()) {

		/* check all junior executives */
		case 0:
			for (Employee junior : callConfig.employeeLevels.get(0)) {
				if (junior.isFree()) {
					return Optional.of(junior);
				}
			}

			/* check all senior executives */
		case 1:
			for (Employee senior : callConfig.employeeLevels.get(1)) {
				if (senior.isFree())
					return Optional.of(senior);
			}

			/* check all managers */
		case 2:
			for (Employee manager : callConfig.employeeLevels.get(2)) {
				if (manager.isFree())
					return Optional.of(manager);
			}

			// No one is free
		default:
			return Optional.empty();
		}
	}

	public void dispatchCall(Call call) {
		Optional<Employee> handler = setHandlerForCall(call);

		if (handler.isPresent()) {
			Employee executive = handler.get();
			call.setHandler(executive);
			executive.receiveAndHandleCall(call,callConfig);
		} else {
			call.reply("please wait for the next free employee");
			callConfig.callQueues.get(call.getEmpType().getValue()).add(call);
		}
	}

	/*
	 * public Call dispatchCaller(Caller caller) { Call call = new Call(caller);
	 * call = dispatchCall(call); return call; }
	 */

}
