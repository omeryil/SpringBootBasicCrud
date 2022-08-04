package com.example.demo.crud;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class api {
    @RequestMapping(
            value = "/add/collection",
            method = RequestMethod.POST)
    public ResponseEntity<Object> create(HttpServletRequest request,
                                         @RequestHeader HttpHeaders headers,
                                         @RequestBody Map<String, Object> responseObj){
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        System.out.println(token);
        connectDB con=new connectDB();
        MongoTemplate db = con.connect("testDB");
        ResponseResult rr=new ResponseResult();
        JSONObject entity=new JSONObject();
        if(responseObj.containsKey("collectionName") && responseObj.containsKey("content")){
            collection content=new ObjectMapper().convertValue(responseObj,collection.class);
            db.insert(content);
            String id = content.get_id();
            rr.setSuccess(true);
            entity.put("id", String.valueOf(id));
            rr.setData(entity);
            rr.setStatus(HttpStatus.OK);
        }else{
            rr.setSuccess(false);
            rr.setStatus(HttpStatus.NOT_ACCEPTABLE);
            rr.setMessage("JSON not valid format");
        }
        return rr.response();
    }
    @RequestMapping(
            value = "/delete/collection",
            method = RequestMethod.POST)
    public ResponseEntity<Object> delete(HttpServletRequest request,
                                         @RequestHeader HttpHeaders headers,
                                         @RequestBody Map<String, Object> responseObj){
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        System.out.println(token);
        connectDB con=new connectDB();
        MongoTemplate db = con.connect("testDB");
        ResponseResult rr=new ResponseResult();
        JSONObject entity=new JSONObject();
        if(responseObj.containsKey("id")){
            Query q=new Query();
            q.addCriteria(Criteria.where("_id").is(responseObj.get("id")));
            db.remove(q,collection.class);
            rr.setSuccess(true);
            rr.setMessage("Collection deleted!");
            rr.setStatus(HttpStatus.OK);
        }else{
            rr.setSuccess(false);
            rr.setStatus(HttpStatus.NOT_ACCEPTABLE);
            rr.setMessage("JSON not valid format");
        }
        return rr.response();
    }
    @RequestMapping(
            value = "/update/collection",
            method = RequestMethod.POST)
    public ResponseEntity<Object> update(HttpServletRequest request,
                                         @RequestHeader HttpHeaders headers,
                                         @RequestBody Map<String, Object> responseObj){
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        System.out.println(token);
        connectDB con=new connectDB();
        MongoTemplate db = con.connect("testDB");
        ResponseResult rr=new ResponseResult();
        if(responseObj.containsKey("id") && responseObj.containsKey("fields")){
            Query q=new Query();
            q.addCriteria(Criteria.where("_id").is(responseObj.get("id")));
            collection c = db.findOne(q,collection.class);
            Map<String,Object> content=c.getContent();
            List<Map<String,Object>> fields= (List<Map<String, Object>>) responseObj.get("fields");
            for(Map<String,Object> item:fields){
                content.put((String) item.get("field"),item.get("value"));
            }
            db.save(c);
            rr.setSuccess(true);
            rr.setMessage("Collection updated!");
            rr.setStatus(HttpStatus.OK);
        }else{
            rr.setSuccess(false);
            rr.setStatus(HttpStatus.NOT_ACCEPTABLE);
            rr.setMessage("JSON not valid format");
        }
        return rr.response();
    }
    @RequestMapping(
            value = "/get/collection",
            method = RequestMethod.POST)
    public ResponseEntity<Object> read(HttpServletRequest request,
                                         @RequestHeader HttpHeaders headers,
                                         @RequestBody Map<String, Object> responseObj) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
        System.out.println(token);
        connectDB con = new connectDB();
        MongoTemplate db = con.connect("testDB");
        ResponseResult rr = new ResponseResult();
        Query q = new Query();
        for (String key : responseObj.keySet()) {
            if (key == "id"|| key=="_id")
                q.addCriteria(Criteria.where("_id").is(responseObj.get("id")));
            else if (key == "collectionName")
                q.addCriteria(Criteria.where(key).is(responseObj.get(key)));
            else
                q.addCriteria(Criteria.where("content." + key).is(responseObj.get(key)));
        }
        List<collection> c = db.find(q, collection.class);
        rr.setSuccess(true);
        rr.setData(new JSONObject().put("values",c));
        rr.setStatus(HttpStatus.OK);

        return rr.response();
    }
}
