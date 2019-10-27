package com.compucar.context;

import com.datastax.driver.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CassandraContext {
    @Value("${cassandra.contact-points}")
    private String[] contactPoints;

    @Value("${cassandra.port}")
    private int port;

    @Value("${cassandra.compressor}")
    private ProtocolOptions.Compression compression;

    @Value("${cassandra.consistency-level}")
    private ConsistencyLevel consistencyLevel;

    @Value("${cassandra.keyspace}")
    private String keyspaceName;

    @Bean(name = "cassandraCluster")
    public Cluster cassandraCluster() {
        Cluster.Builder builder = Cluster.builder();
        builder.addContactPoints(this.contactPoints);
        builder.withProtocolVersion(ProtocolVersion.V3);
        builder.withPort(this.port);
        builder.withCompression(this.compression);
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.setConsistencyLevel(this.consistencyLevel);
        builder.withQueryOptions(queryOptions);
        return builder.build();
    }

    @Bean(name = "cassandraSession")
    public Session getSession() {
        Cluster cassandraCluster = this.cassandraCluster();
        return cassandraCluster.connect(this.keyspaceName);
    }
}
