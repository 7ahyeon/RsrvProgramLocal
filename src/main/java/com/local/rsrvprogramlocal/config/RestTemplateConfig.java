package com.local.rsrvprogramlocal.config;

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
    /* @Configuration @Bean
      스프링 컨테이너가 @Configuration이 붙은 클래스를 자동 빈 등록 후 해당 클래스를 파싱해서 @Bean이 있는 메소드를 찾아서 빈 생성해줌
      싱글톤 빈으로 등록(proxyBeanMethods=true)해서 1개의 객체만 생성하여 여러 클래스가 공유함으로써 메모리 상의 이점을 얻음
   */
    /* 싱글톤 Singleton pattern : 어떤 클래스가 최초 생성 시에만 메모리를 할당하고 그 메모리에 하나의 인스턴스를 만들어 계속 사용하는 디자인 패턴
       최초 생성 이후에 호출된 생성자는 최초의 생성자가 생성한 객체를 리턴함
    */
    // Spring Bean : Spring의 DI 컨테이너에 의해 관리되는 POJO(Spring 애플리케이션을 구성하는 핵심 객체)
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
