package com.local.rsrvprogramlocal.service.impl;

import com.google.gson.JsonObject;
import com.local.rsrvprogramlocal.model.RsrvRequest;
import com.local.rsrvprogramlocal.model.RsrvResponse;
import com.local.rsrvprogramlocal.service.ConnectionService;
import com.local.rsrvprogramlocal.service.RsrvService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    private final RestTemplate restTemplate;
    @Autowired
    private final RsrvService rsrvService;

    public ConnectionServiceImpl(RestTemplate restTemplate, RsrvService rsrvService) {
        this.restTemplate = restTemplate;
        this.rsrvService = rsrvService;
    }

    @Override
    public String httpConnection(int select) {
        // *******예외 추가
        // Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Body 생성
        JsonObject requestJson = createRequest(select);

        // HttpEntity 생성
        // HttpEntity<T> : HTTP 요청/응답에 해당하는 HttpHeader와 HttpBody를 포함하는 클래스
        // RequestEntity / ResponseEntity : HttpEntity 클래스를 상속받아 구현한 클래스
        HttpEntity<JsonObject> request = new HttpEntity<>(requestJson, headers);

        // HTTP 통신
        String url = "http://localhost:8002/rsrvResponse";

        // RestTemplate이 정의하는 12개의 메서드 중 하나
        // exchange() : 지정된 HTTP 메서드를 URL에 대해 실행하며, Response body와 연결되는 객체를 포함하는 responseEntity를 반환함
        // 요청할 서버 주소, 요청 방식, 요청 데이터, 응답 데이터 타입
        ResponseEntity<JsonObject> response = restTemplate.exchange(url, HttpMethod.POST, request, JsonObject.class);

        HttpStatus statusCode = response.getStatusCode();
        HttpHeaders responseHeaders = response.getHeaders();
        JsonObject responseBody = response.getBody();

        String responseTostring = "HTTP Status : " + statusCode.toString()
                + "<br>Header : " + responseHeaders.toString()
                + "<br>응답 : " + responseBody.toString();
        return responseTostring;
    }

    @Override
    public JsonObject createRequest(int select) {
        // 요청 파일 읽기
        String jsonFileContent = rsrvService.getRequestFile(select);
        // 요청 Json 전문 Object 바인딩
        RsrvRequest rsrvRequest = (RsrvRequest) rsrvService.bindingObject(jsonFileContent);
        // 요청 Json 전문 생성
        JsonObject requestJson = rsrvService.parsingJson(rsrvRequest);

        return requestJson;
    }

    @Override
    public String handleResponse(String responseJsonContent) {
        // 응답 Json 전문 Object 바인딩
        RsrvResponse rsrvResponse = (RsrvResponse) rsrvService.bindingObject(responseJsonContent);
        return rsrvResponse.toString();
    }
}
