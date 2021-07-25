package com.mytest.webapi.model;

import lombok.Data;

import java.util.Map;

@Data
public class Error {
    public String error;
    public Integer status;
    public String message;

    public Error(int status, Map<String, Object> errorAttributes){
        this.error = (String) errorAttributes.get("error");
        this.status = status;
        this.message = getErrorMessage(status);
    }

    private String getErrorMessage(int statusCode){
        String errorMsg = "";
        switch (statusCode){
            case 400:{
                errorMsg = "Bad Request";
                break;
            }
            case 401:{
                errorMsg = "Unauthorized";
                break;
            }
            case 403:{
                errorMsg = "Forbidden";
                break;
            }
            case 404:{
                errorMsg = "Resouce not found";
                break;
            }
            case 500:{
                errorMsg = "Internal Server Error";
                break;
            }
            default:{
                errorMsg = "Unknown error";
            }
        }
        return errorMsg;
    }

}
