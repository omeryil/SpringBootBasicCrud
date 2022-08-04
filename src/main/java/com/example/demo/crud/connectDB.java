package com.example.demo.crud;

import org.springframework.data.mongodb.core.MongoTemplate;

public class connectDB {
    public MongoTemplate connect(String dbname){
        mongoDB dbt = new mongoDB();
        MongoTemplate db = null;
        try {
            db = dbt.mongoTemplate(dbname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return db;
    }
}

