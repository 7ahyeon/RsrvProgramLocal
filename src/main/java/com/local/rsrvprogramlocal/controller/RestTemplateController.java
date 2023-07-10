package com.local.rsrvprogramlocal.controller;

import com.local.rsrvprogramlocal.model.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

// RestTemplte 장점 : HttpUrlConnection으로 구현되어 있어서 아래와 같은 과정들을 백그라운드에 처리되도록 하고 개발자는 반복적 코드 작성을 피할 수 있다.
// URL 객체 생성 및 연결, HTTP 요청 설정 및 실행, HTTP response 해석 등

/* Representative State Transfer(REST) : 웹 상의 자료를 HTTP위에서 별도의 전송 계층 없이 전송하기 위한 간단한 인터페이스(분산 시스템을 위한 아키텍처)
   RestTemplate : Blocking I/O기반 멀티 스레드 방식의 동기식(Synchronous) HTTP 통신 Java Servlet API
   Webclient : Non-Blocking I/O기반 싱글 스레드 방식의 비동기식(Asynchronous) HTTP 통신 API(Blocking 사용 가능)
   동시 사용자 1000명까지 처리 속도 비슷하나 그 이상은 Webclient 사용 권장됨 */
// @RestController : JSON 형태로 객체 데이터를 반환하는 것이 주 목적
// @Controller + @ResponseBody
@RestController
public class RestTemplateController {
    private final RestTemplate restTemplate;
    @Autowired
    private final ConnectionService connectionService;

    public RestTemplateController(RestTemplate restTemplate, ConnectionService connectionService) {
        this.restTemplate = restTemplate;
        this.connectionService = connectionService;
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
        String url = "http://localhost:8002/rsrvResponse";

        // RestTemplate이 정의하는 12개의 메서드 중 하나
        // exchange() : 지정된 HTTP 메서드를 URL에 대해 실행하며, Response body와 연결되는 객체를 포함하는 responseEntity를 반환함
        // 요청할 서버 주소, 요청 방식, 요청 데이터, 응답 데이터 타입
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        HttpStatus statusCode = response.getStatusCode();
        HttpHeaders responseHeaders = response.getHeaders();
        String responseBody = response.getBody();
        String responseTostring = connectionService.handleResponse(responseBody);
        return responseTostring;
    }
}
