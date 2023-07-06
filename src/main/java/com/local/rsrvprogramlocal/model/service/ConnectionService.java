package com.local.rsrvprogramlocal.model.service;

public interface ConnectionService {
    String createRequest(int select);
    String handleResponse(String responseJsonContent);
}
