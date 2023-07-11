package com.local.rsrvprogramlocal.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.local.rsrvprogramlocal.config.util.LocalDateDeserializer;
import com.local.rsrvprogramlocal.config.util.LocalDateSerializer;
import com.local.rsrvprogramlocal.config.util.LocalDateTimeDeserializer;
import com.local.rsrvprogramlocal.config.util.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class GsonConfig {
    @Bean
    public Gson gson(GsonBuilder gsonBuilder) {
        gsonBuilder = new GsonBuilder();
        // 직렬화시 LocalDate/LocalDateTime formatting
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateSerializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer());
        // 역직렬화시 LocalDate/LocalDateTime formatting
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer());
        //.serializeNulls() .setPrettyPrinting() : toJson (직렬화 시 사용)
        // .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES) : Underscore를 CamelCase로 자동 변환 / 작동 불가 이슈
        // Object @SerializedName 설정 부여
        Gson gson = gsonBuilder.serializeNulls().setPrettyPrinting().create();
        return gson;
    }
}
