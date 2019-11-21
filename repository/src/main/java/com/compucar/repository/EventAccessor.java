package com.compucar.repository;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Param;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface EventAccessor {
    @Query("SELECT event_name FROM event WHERE service_code = :service_code")
    ResultSet getNamesByServiceCode(@Param("service_code") String serviceCode);

    @Query("SELECT * FROM event WHERE service_code = :service_code AND event_name = :event_name")
    ResultSet getByServiceCodeAndName(@Param("service_code") String serviceCode, @Param("event_name") String eventName);
}
