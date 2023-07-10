package com.local.rsrvprogramlocal.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.local.rsrvprogramlocal.config.exception.CloseException;
import com.local.rsrvprogramlocal.config.exception.FileNotReadException;
import com.local.rsrvprogramlocal.model.RsrvRequest;
import com.local.rsrvprogramlocal.model.RsrvResponse;
import com.local.rsrvprogramlocal.service.RsrvService;
import com.local.rsrvprogramlocal.config.util.LocalDateDeserializer;
import com.local.rsrvprogramlocal.config.util.LocalDateSerializer;
import com.local.rsrvprogramlocal.config.util.LocalDateTimeDeserializer;
import com.local.rsrvprogramlocal.config.util.LocalDateTimeSerializer;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

@Service
public class RsrvServiceImpl implements RsrvService {
    @Override
    public String getRequestFile(int select) { // 예약 신청 요청 JSON 파일 읽기
        String fileName = "RsrvReqRq.json";
        URL resource = getClass().getClassLoader().getResource("static/file/" + fileName);
        String jsonFilePath = resource.getFile();

        FileReader fr = null;
        BufferedReader br = null;

        try {
            fr = new FileReader(jsonFilePath);
            br = new BufferedReader(fr);

            String jsonFileContentLine;
            // StringBuilder : 동기화 비지원 (싱글 스레드 환경 적합)
            // StringBuffer : 동기화 지원 (멀티 스레드 환경 적합)
            StringBuffer sb = new StringBuffer();

            while((jsonFileContentLine = br.readLine()) != null){ // 빈 문자열이 없도록 주의(NPE)
                sb.append(jsonFileContentLine);
            }
            return sb.toString();
            // 체크 예외
        } catch (IOException e) {
            // 파일을 읽지 못했을 때
            throw new FileNotReadException(e);
        } finally {
            if (fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    throw new CloseException("FileReader is not closed.", e);
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new CloseException("BufferedReader is not closed.", e);
                }
            }
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
    public JsonObject parsingJson(RsrvRequest rsrvRequest) { // 요청 JSON 전문 생성
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.serializeNulls().setPrettyPrinting().create();

        String jsonContent = gson.toJson(rsrvRequest);
        JsonObject responseJson = gson.fromJson(jsonContent, JsonObject.class);
        return responseJson;
    }
}
