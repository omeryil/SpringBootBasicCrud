package com.example.demo.crud;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Map;

@Getter
@Setter
public class collectionDTO {
    @Id
    private String _id;
    private String collectionName;
    private Map<String,Object> content;
}
