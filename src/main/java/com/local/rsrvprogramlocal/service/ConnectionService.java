package com.local.rsrvprogramlocal.service;

import com.google.gson.JsonObject;

public interface ConnectionService {
    // HTTP 통신
    String httpConnection(int select);
    // 요청 JSON 생성
    JsonObject createRequest(int select);
    // 응답 JSON 처리
    String handleResponse(String responseJsonContent);
}
