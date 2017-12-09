package com.tavant.callccenter.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class CallCenterDetail {
	@Id
	@GeneratedValue
	private int id;
	private int number_of_calls;
	private int resolved;
	private int unresolved;
	@OneToMany(mappedBy = "callCenter", cascade = CascadeType.ALL)
	private Set<EmployeeDetail> employeeDetails;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumber_of_calls() {
		return number_of_calls;
	}

	public void setNumber_of_calls(int number_of_calls) {
		this.number_of_calls = number_of_calls;
	}

	public int getResolved() {
		return resolved;
	}

	public void setResolved(int resolved) {
		this.resolved = resolved;
	}

	public int getUnresolved() {
		return unresolved;
	}

	public void setUnresolved(int unresolved) {
		this.unresolved = unresolved;
	}

	public Set<EmployeeDetail> getEmployeeDetails() {
		return employeeDetails;
	}

	public void setEmployeeDetails(Set<EmployeeDetail> employeeDetails) {
		this.employeeDetails = employeeDetails;
	}

}
