package com.fitbuddy.service.config.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Profile(value = {"local"})
@Configuration(value = "mongo-db-configuration")
@EnableTransactionManagement
@RequiredArgsConstructor
public class MongoDBConfiguration extends AbstractMongoClientConfiguration {
    private final Environment environment;

    private String host () {
        return environment.getProperty("spring.data.mongodb.host", String.class);
    }
    private String port () {
        return environment.getProperty("spring.data.mongodb.port", String.class);
    }
    private String authenticationDatabase () {
        return environment.getProperty("spring.data.mongodb.authentication-database", String.class);
    }
    private String userName () {
        return environment.getProperty("spring.data.mongodb.username", String.class);
    }
    private String password () {
        return URLEncoder.encode(environment.getProperty("spring.data.mongodb.password", String.class), StandardCharsets.UTF_8);
    }

    @Override
    protected String getDatabaseName() {
        return environment.getProperty("spring.data.mongodb.database", String.class);
    }


    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    @Override
    public MongoClient mongoClient() {
        String uri = String.format("mongodb://%s:%s@%s:%s/%s?authSource=%s",
                this.userName(), this.password(),
                this.host(),this.port(),
                this.getDatabaseName(), this.authenticationDatabase());
        ConnectionString connectionString = new ConnectionString(uri);
        return MongoClients.create(connectionString);
    }

    @Bean
    public MongoTemplate mongoTemplate (MongoClient mongoCli) {
        return new MongoTemplate(mongoCli, this.getDatabaseName());
    }
}


