package com.tavant.callccenter.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tavant.callccenter.model.EmployeeDetail;

@Repository
public interface EmployeeDetailRepository extends CrudRepository<EmployeeDetail, Integer>{

	public Optional<EmployeeDetail> findByName(final String name);
	

}
