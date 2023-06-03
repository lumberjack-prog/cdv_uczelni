package com.example.cdv_uczelni;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.servererrors.AlreadyExistsException;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.datastax.oss.protocol.internal.ProtocolConstants;
import com.example.cdv_uczelni.model.User;
import com.example.cdv_uczelni.model.UserDto;
import com.example.cdv_uczelni.service.impl.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.core.cql.keyspace.CreateTableSpecification;
import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@Slf4j
public class CdvUczelniApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CqlSession cqlSession;

    public static void main(String[] args) {
        SpringApplication.run(CdvUczelniApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(UserService userService) {
        return args -> {

            try {
                String cql = "CREATE TABLE users (" +
                        "key TEXT PRIMARY KEY," +
                        "username TEXT," +
                        "email TEXT," +
                        "password TEXT);";
                cqlSession.execute(cql);
            } catch (AlreadyExistsException e) {
                log.info("Table users already exists!");
            }

            UserDto admin = userService.findUserByUsername("admin");
            if (admin == null) {

                User user = new User();
                user.setUsername("admin");
                user.setEmail("admin@gmail.com");
                user.setPassword(passwordEncoder.encode("pass"));

                userService.saveUser(user);
            }
        };
    }

}
