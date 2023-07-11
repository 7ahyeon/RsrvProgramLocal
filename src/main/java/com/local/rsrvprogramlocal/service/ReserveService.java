package com.local.rsrvprogramlocal.service;

import com.google.gson.JsonObject;
import com.local.rsrvprogramlocal.model.ReserveRequest;

public interface ReserveService {
    // 예약 신청 요청 JSON 파일 읽기
    String getRequestFile(int select);
    // JSON 전문 Object 바인딩
    Object bindingObject(String jsonFileContent);
    // 예약 신청 정보 저장
    Long saveReserveRequest(ReserveRequest reserveRequest);
    // 요청 JSON 전문 생성
    JsonObject parsingJson(ReserveRequest reserveRequest);
}
