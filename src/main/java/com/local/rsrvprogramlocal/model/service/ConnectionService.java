package com.local.rsrvprogramlocal.model.service;

public interface ConnectionService {
    // 요청 JSON 생성
    String createRequest(int select);
    // 응답 JSON 처리
    String handleResponse(String responseJsonContent);
}
