package com.local.rsrvprogramlocal.dao;

import com.local.rsrvprogramlocal.model.ReserveRequest;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ReserveRepository {
    // JdbcTemplate : DAO객체에서 DB와 연동하기 위해 SQL연산들을 수행할 수 있도록 도와줌
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReserveRepository(DataSource dataSource) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                // withTableName : Insert할 테이블 이름 설정
                .withTableName("room_reserve")
                // usingGeneratedKeyColumns : Auto_increment를 통해 생성된 값 컬럼 자동 입력
                .usingGeneratedKeyColumns("room_reserve_id")
                .usingGeneratedKeyColumns("rsrv_req_date")
                .usingGeneratedKeyColumns("rsrv_cmpl_st");
    }

    public Long reserveRequest(ReserveRequest reserveRequest) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(reserveRequest);
        System.out.println("insert");
        System.out.println(simpleJdbcInsert.executeAndReturnKey(params).longValue());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

}
