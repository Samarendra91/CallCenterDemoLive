package com.tavant.callccenter.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tavant.callccenter.model.EmployeeDetail;

@Repository
public interface EmployeeDetailRepository extends CrudRepository<EmployeeDetail, Integer>{

	public EmployeeDetail findByName(final String name);
	

}
