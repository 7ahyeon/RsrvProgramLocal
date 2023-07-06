package com.local.rsrvprogramlocal.model.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.local.rsrvprogramlocal.model.dto.RsrvRequest;
import com.local.rsrvprogramlocal.model.dto.RsrvResponse;
import com.local.rsrvprogramlocal.model.service.util.LocalDateDeserializer;
import com.local.rsrvprogramlocal.model.service.util.LocalDateSerializer;
import com.local.rsrvprogramlocal.model.service.util.LocalDateTimeDeserializer;
import com.local.rsrvprogramlocal.model.service.util.LocalDateTimeSerializer;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class RsrvServiceImpl implements RsrvService {

    @Override
    public String getRequestFile(int select) { // 예약 신청 요청 JSON 파일 읽기
        ClassPathResource resource = new ClassPathResource("file/RsrvReqRq.json");
        try {
            Path path = Paths.get(resource.getURI());
            List<String> content = null;
            content = Files.readAllLines(path);
            content.forEach(System.out::println);

            StringBuffer sb = new StringBuffer();
            while (content != null) { // 빈 문자열이 없도록 주의(NPE)
                sb.append(content);
            }
            return sb.toString();
        }catch (FileNotFoundException e) {
            return "FileNotFoundException";
            // 파일을 읽지 못했을 경우
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object bindingObject(String jsonFileContent) { // JSON 전문 Object 바인딩
        //.serializeNulls() .setPrettyPrinting() : toJson (직렬화 시 사용)
        // .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES) : Underscore를 CamelCase로 자동 변환 / 작동 불가 이슈
        // Object @SerializedName 설정 부여
        GsonBuilder gsonBuilder = new GsonBuilder();
        // 역직렬화시 LocalDate formatting
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        Gson gson = gsonBuilder.setPrettyPrinting().create();

        if (jsonFileContent.contains("ds_rsrvInfo")) {
            // 요청
            RsrvRequest rsrvRequest = gson.fromJson(jsonFileContent, RsrvRequest.class);
            return rsrvRequest;
        } else if (jsonFileContent.contains("ds_prcsResult")) {
            // 응답
            RsrvResponse rsrvResponse = gson.fromJson(jsonFileContent, RsrvResponse.class);
            return rsrvResponse;
        } else {
            // 예외 처리 패턴 getOrElse : 예외 대신 기본 값을 리턴함(null이 아닌 기본 값)
            return Collections.emptyList();
        }
    }

    @Override
    public String parsingJson(RsrvRequest rsrvRequest) { // 요청 JSON 전문 생성
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.serializeNulls().setPrettyPrinting().create();

        String jsonContent = gson.toJson(rsrvRequest);
        return jsonContent;
    }
}
