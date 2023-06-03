package com.example.cdv_uczelni.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@Configuration
public class CassandraConfig {

    @Value("${sping.data.cassandra.keyspace-name}")
    private String keyspaceName;
    @Value("${sping.data.cassandra.contact-points}")
    private String contactPoints;
    @Value("${sping.data.cassandra.port}")
    private String port;
    @Value("${spring.cassandra.schema-action}")
    private String schemaAction;
    @Value("${basic.load-balancing-policy.local-datacenter}")
    private String dataCenter;

    @Bean
    public CqlSession cqlSession() {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(contactPoints, Integer.parseInt(port));


        return CqlSession.builder()
                .withKeyspace(keyspaceName)
                .addContactPoint(inetSocketAddress)
                .withLocalDatacenter(dataCenter)
                .build();
    }
}

