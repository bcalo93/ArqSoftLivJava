package com.compucar.model;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import lombok.Data;

@Data
@Table(name = "event")
public class Event {

    @PartitionKey
    @Column(name = "service_code")
    private String serviceCode;

    @ClusteringColumn
    @Column(name = "event_name")
    private String name;

    @Column(name = "payload")
    private String payload;
}