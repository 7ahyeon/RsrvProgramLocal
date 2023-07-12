package com.local.rsrvprogramlocal.service;

import com.google.gson.JsonObject;

import java.util.Map;

public interface ConnectionService {
    // HTTP 통신
    String httpConnection(int select);
    // 요청 JSON 생성
    Map<String, Object> createRequest(int select);
    // 응답 JSON 처리
    int handleResponse(JsonObject responseJson, Long roomReserveId);
}
