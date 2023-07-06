package com.local.rsrvprogramlocal.model.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.local.rsrvprogramlocal.model.dto.RsrvRequest;
import com.local.rsrvprogramlocal.model.dto.RsrvResponse;
import com.local.rsrvprogramlocal.model.service.util.LocalDateDeserializer;
import com.local.rsrvprogramlocal.model.service.util.LocalDateSerializer;
import com.local.rsrvprogramlocal.model.service.util.LocalDateTimeDeserializer;
import com.local.rsrvprogramlocal.model.service.util.LocalDateTimeSerializer;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;

public class RsrvServiceImpl implements RsrvService {

    @Override
    public String selectService(int select) { // 예약 서비스 선택에 따른 파일 이름 설정
        // 성능 차이 : if문이 3개일 때 까지는 if문이 빠르나 그 외에는 switch case문이 빠름(컴파일러 최적화시 유리함)
        String jsonFileName = null;
        switch(select) {
            case 1:
                jsonFileName = "RsrvReqRq.json";
                break;
            case 2:
                jsonFileName = "RsrvModRq.json";
                break;
            case 3:
                jsonFileName = "RsrvCnclRq.json";
                break;
        }
        return jsonFileName;
    }

    @Override
    public String getFilePath(String jsonFileName) { // JSON 파일 경로 얻기
        URL resource = getClass().getClassLoader().getResource("file/" + jsonFileName);
        String jsonFilePath = resource.getFile();

        return jsonFilePath;
    }

    // JSON 파일 읽기
    // NULL 처리 하기
    @Override
    public String readFile(String jsonFilePath) {
        File file = new File(jsonFilePath);
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        String jsonFileContentLine;

        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            br = new BufferedReader(isr);
            // StringBuilder : 문자열 연산이 많을 경우, 동기화를 고려하지 않기 때문에 단일 스레드 환경일 경우(StringBuffer보다 성능 뛰어남) 사용
            StringBuilder sb = new StringBuilder();

            while((jsonFileContentLine = br.readLine()) != null){ // 빈 문자열이 없도록 주의(NPE)
                sb.append(jsonFileContentLine);
            }
            return sb.toString();
            // 파일을 찾지 못했을 경우
        } catch (FileNotFoundException e) {
            return "FileNotFoundException";
            // 파일을 읽지 못했을 경우
        } catch (IOException e) {
            return "IOException";
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
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
    public String parsingJson(RsrvRequest rsrvRequest) { // 요청 JSON 전문 생성
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        Gson gson = gsonBuilder.serializeNulls().setPrettyPrinting().create();

        String jsonContent = gson.toJson(rsrvRequest);
        return jsonContent;
    }
}
