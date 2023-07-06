package com.local.rsrvprogramlocal.model.service;

import com.local.rsrvprogramlocal.model.dto.RsrvRequest;

public interface RsrvService {
    // 예약 신청 요청 JSON 파일 읽기
    String getRequestFile(int select);
    // JSON 전문 Object 바인딩
    Object bindingObject(String jsonFileContent);
    // 요청 JSON 전문 생성
    String parsingJson(RsrvRequest rsrvRequest);
}
