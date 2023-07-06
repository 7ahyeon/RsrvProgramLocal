package com.local.rsrvprogramlocal.controller;

import com.local.rsrvprogramlocal.model.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// Representative State Transfer(REST) : 분산 시스템을 위한 아키텍처
// @RestController : JSON 형태로 객체 데이터를 반환하는 것이 주 목적
// @Controller + @ResponseBody
@RestController
public class RestTemplateController {
    final RestTemplate restTemplate;
    ConnectionService connectionService;

    public RestTemplateController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/rsrvRequest")
    public String rsrvRequest(@RequestParam int select) {

        System.out.println(select);
        // Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json");

        // Body 생성
        String requestJson = connectionService.createRequest(select);

        // HttpEntity 생성
        // HttpEntity<T> : HTTP 요청/응답에 해당하는 HttpHeader와 HttpBody를 포함하는 클래스
        // RequestEntity / ResponseEntity : HttpEntity 클래스를 상속받아 구현한 클래스
        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        // HTTP 통신
        String url = "http://localhost:8002/receiveJson.do";

        // RestTemplate이 정의하는 12개의 메서드 중 하나
        // exchange() : 지정된 HTTP 메서드를 URL에 대해 실행하며, Response body와 연결되는 객체를 포함하는 responseEntity를 반환함
        // 요청할 서버 주소, 요청 방식, 요청 데이터, 응답 데이터 타입
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        HttpStatus statusCode = response.getStatusCode();
        HttpHeaders responseHeaders = response.getHeaders();
        String responseBody = response.getBody();
        System.out.println(responseBody.toString());
        return null;
    }
}
