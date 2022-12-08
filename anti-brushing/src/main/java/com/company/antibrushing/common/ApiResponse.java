package com.company.antibrushing.common;

/**
 * @author Real
 * Date: 2022/12/9 0:38
 */
public class ApiResponse {
    private int status;
    private String message;
    private Object data;

    public ApiResponse() {
    }

    public ApiResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static ApiResponse success(String message) {
        ApiResponse response = new ApiResponse();
        response.status = 200;
        response.message = message;
        return response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

