package com.tavant.callccenter.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class EmployeeDetail {

	@Id
	@GeneratedValue
	private int id;
	private String name;
	private int timeTakenInMinutes;
	private int callsAttended;
	private int resolved;
	private int escalated;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "call_center_id")
	private CallCenterDetail callCenter;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CallCenterDetail getCallCenter() {
		return callCenter;
	}

	public void setCallCenter(CallCenterDetail callCenter) {
		this.callCenter = callCenter;
	}

	public int getTimeTakenInMinutes() {
		return timeTakenInMinutes;
	}

	public void setTimeTakenInMinutes(int timeTakenInMinutes) {
		this.timeTakenInMinutes = timeTakenInMinutes;
	}

	public int getCallsAttended() {
		return callsAttended;
	}

	public void setCallsAttended(int callsAttended) {
		this.callsAttended = callsAttended;
	}

	public int getResolved() {
		return resolved;
	}

	public void setResolved(int resolved) {
		this.resolved = resolved;
	}

	public int getEscalated() {
		return escalated;
	}

	public void setEscalated(int escalated) {
		this.escalated = escalated;
	}

}
