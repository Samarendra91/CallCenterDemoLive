package com.tavant.callccenter.repository;

import org.springframework.data.repository.CrudRepository;

import com.tavant.callccenter.model.CallCenterDetail;

public interface CallCenterRepository extends CrudRepository<CallCenterDetail, Integer> {

}
