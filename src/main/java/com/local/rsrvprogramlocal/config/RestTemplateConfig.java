package com.local.rsrvprogramlocal.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.*;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                // Time out 설정
                // Spring MVC : 멀티 스레드 기반
                // 원인 : 외부 API에 문제가 생겨 응답이 오지 않을 시 모든 스레드가 RestTemplate로 API 호출(대기상태)
                // 결과 : 다른 클라이언트의 요청에 응답할 스레드 남아있지 않음
                // 방지 : 일정 시간 응답이 없을시 연결 강제 중단
                .setConnectTimeout(Duration.ofSeconds(5))
                .setReadTimeout(Duration.ofSeconds(5))
                .additionalInterceptors(clientHttpRequestInterceptor())
                .build();
    }

    // Interceptor : 특정 프로세스 사이클을 변경하거나 추가적인 작업을 할 때 사용하는 디자인 패턴
    // ClientHttpRequestInterceptor interface로 intercetper가 해야할 일을 구현하여 해당 객체로 REST 요청을 보냄
    public ClientHttpRequestInterceptor clientHttpRequestInterceptor() {
        // Retry 설정
        // API 통신 : 네트워크 등과 같은 이슈로 간헐적 실패 발생
        // 일정 횟수만큼 요청 재시도(spring-retry)
        return (request, body, execution) -> {
            RetryTemplate retryTemplate = new RetryTemplate();
            retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3));
            try {
                return retryTemplate.execute(context -> execution.execute(request, body));
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        };
    }


}
