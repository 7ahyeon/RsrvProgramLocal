package com.local.rsrvprogramlocal.model.service;

import com.local.rsrvprogramlocal.model.dto.RsrvRequest;

public interface RsrvService {
    // 예약 서비스 선택에 따른 파일 이름 설정
    String selectService(int select);
    // JSON 파일 경로 얻기
    String getFilePath(String jsonFileName);
    // JSON 파일 읽기
    String readFile(String jsonFilePath);
    // JSON 전문 Object 바인딩
    Object bindingObject(String jsonContent);
    // 요청 JSON 전문 생성
    String parsingJson(RsrvRequest rsrvRequest);
}
