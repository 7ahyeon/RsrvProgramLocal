package com.local.rsrvprogramlocal.model.service;

import com.local.rsrvprogramlocal.model.dto.RsrvRequest;

public class ConnectionServiceImpl implements ConnectionService {
    private final RsrvService rsrvService;

    public ConnectionServiceImpl(RsrvService rsrvService) {
        this.rsrvService = rsrvService;
    }

    @Override
    public String createRequest(int select) {

        // 요청 파일 읽기
        String jsonFileName = rsrvService.selectService(select);
        String jsonFilePath = rsrvService.getFilePath(jsonFileName);
        String jsonFileContent = rsrvService.readFile(jsonFilePath);

        // 요청 Json 전문 Object 바인딩
        RsrvRequest rsrvRequest = (RsrvRequest) rsrvService.bindingObject(jsonFileContent);
        // 요청 Json 전문 생성
        String requestJson = rsrvService.parsingJson(rsrvRequest);

        return requestJson;
    }
}
