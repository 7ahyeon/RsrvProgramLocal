package com.local.rsrvprogramlocal.service.impl;

import com.google.gson.JsonObject;
import com.local.rsrvprogramlocal.model.ReserveRequest;
import com.local.rsrvprogramlocal.model.ReserveResponse;
import com.local.rsrvprogramlocal.service.ConnectionService;
import com.local.rsrvprogramlocal.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    private final RestTemplate restTemplate;
    @Autowired
    private final ReserveService reserveService;

    public ConnectionServiceImpl(RestTemplate restTemplate, ReserveService reserveService) {
        this.restTemplate = restTemplate;
        this.reserveService = reserveService;
    }

    @Override
    public String httpConnection(int select) {
        // *******예외 추가
        // Body 생성
        JsonObject requestJson = createRequest(select);
        System.out.println("요청");
        // HttpEntity 생성
        // HttpEntity<T> : HTTP 요청/응답에 해당하는 HttpHeader와 HttpBody를 포함하는 클래스
        // RequestEntity / ResponseEntity : HttpEntity 클래스를 상속받아 구현한 클래스
        HttpEntity<JsonObject> request = new HttpEntity<>(requestJson);

        // HTTP 통신
        String url = "http://localhost:8002/rsrvResponse";
        // RestTemplate이 정의하는 12개의 메서드 중 하나
        // exchange() : 지정된 HTTP 메서드를 URL에 대해 실행하며, Response body와 연결되는 객체를 포함하는 responseEntity를 반환함
        // 요청할 서버 주소, 요청 방식, 요청 데이터, 응답 데이터 타입
        ResponseEntity<JsonObject> response = restTemplate.exchange(url, HttpMethod.POST, request, JsonObject.class);
        System.out.println("응답");
        HttpStatus statusCode = response.getStatusCode();
        HttpHeaders responseHeaders = response.getHeaders();
        JsonObject responseBody = response.getBody();
        System.out.println(responseBody.toString());
        String responseTostring = "HTTP Status : " + statusCode.toString()
                + "<br>Header : " + responseHeaders.toString()
                + "<br>응답 : " + responseBody.toString();
        return responseTostring;
    }

    @Override
    public JsonObject createRequest(int select) {
        // 요청 파일 읽기
        String jsonFileContent = reserveService.getRequestFile(select);
        // 요청 Json 전문 Object 바인딩
        ReserveRequest reserveRequest = (ReserveRequest) reserveService.bindingObject(jsonFileContent);
        // 예약 요청 저장
        //Long roomReserveId = reserveService.saveReserveRequest(reserveRequest);
        //System.out.println("예약 요청 저장 : " + roomReserveId);
        // 요청 Json 전문 생성
        JsonObject requestJson = reserveService.parsingJson(reserveRequest);

        return requestJson;
    }

    @Override
    public String handleResponse(String responseJsonContent) {
        // 응답 Json 전문 Object 바인딩
        ReserveResponse reserveResponse = (ReserveResponse) reserveService.bindingObject(responseJsonContent);
        return reserveResponse.toString();
    }
}
