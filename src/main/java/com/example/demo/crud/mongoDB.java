package com.example.demo.crud;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoTypeMapper;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class mongoDB {
    public MongoClient mongo() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }
    public SimpleMongoClientDatabaseFactory mongoDbFactory(String dbName) throws Exception {
        return new SimpleMongoClientDatabaseFactory(mongo(), dbName);
    }


    public MongoTemplate mongoTemplate(String dbName) throws Exception {
        MongoTypeMapper typeMapper = new DefaultMongoTypeMapper(null);
        MappingMongoConverter converter = new MappingMongoConverter(mongoDbFactory(dbName), new MongoMappingContext());
        converter.setTypeMapper(typeMapper);

        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory(dbName), converter);
        return mongoTemplate;
    }
}

