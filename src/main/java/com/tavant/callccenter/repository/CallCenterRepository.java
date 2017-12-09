package com.tavant.callccenter.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tavant.callccenter.model.CallCenterDetail;

@Repository
public interface CallCenterRepository extends CrudRepository<CallCenterDetail, Integer> {

	public CallCenterDetail findByName(final String name);

}
