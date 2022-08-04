package com.example.demo.crud;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ResponseResult {
    private boolean success;
    private String message;
    private HttpStatus status;
    private Object Data;
    private boolean addData;

    public ResponseEntity<Object> response(){
        JSONObject j=new JSONObject();
        j.put("success",success);
        Map<String, Object> response = null;
        if(!isSuccess()) {
            j.put("message", message);
            if(addData){
                try {
                    response = new ObjectMapper().readValue(getData().toString(), HashMap.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                j.put("data", response);
            }
        }else {
            if(getData()!=null) {
                try {
                    response = new ObjectMapper().readValue(getData().toString(), HashMap.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                j.put("data", response);
            }
            if(getMessage()!=null)
                j.put("message", message);
        }
        try {
            return new ResponseEntity<Object>(new ObjectMapper().readValue(j.toString(), HashMap.class),getStatus());
        } catch (JsonProcessingException e) {
            return new ResponseEntity<Object>("Unexpected Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
