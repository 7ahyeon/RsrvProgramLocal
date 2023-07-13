package com.local.rsrvprogramlocal.dao;

import com.local.rsrvprogramlocal.model.ReserveRequest;

import com.local.rsrvprogramlocal.model.ReserveRequestInfo;
import com.local.rsrvprogramlocal.model.ReserveResponse;
import com.local.rsrvprogramlocal.model.ReserveResponseInfo;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;

@Repository
public class ReserveRepository {
    // JdbcTemplate : DAO객체에서 DB와 연동하기 위해 SQL연산들을 수행할 수 있도록 도와줌
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final String updateQuery = "UPDATE room_reserve SET rsrv_no = :rsrvNo, rsrv_cmpl_date = :rsrvCmplDate, rsrv_cmpl_st = :rsrvCmplSt WHERE room_reserve_id = :roomReserveId";


    public ReserveRepository(DataSource dataSource) {
        // NamedParameterJdbcTemplate : 기존의 JdbcTemplate과 달리 ?대신 :파라미터명을 이용하여 파라미터를 바인딩함(식별성 보완)
        // Java : 클래스의 인스턴스 변수명으로 주로 Camel Case 사용 / SQL : Column명으로 주로 SnakeCase 사용
        // 서로 통신할 때 parsing하는 작업을 지원함
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        // simpleJdbcInsert : 기본 InsertSQL을 구성하는데 필요한 코드를 단순화하기 위해 메타 데이터 처리를 제공하는 JDBC 확장 클래스
        // 생성 시점에 데이터베이스 테이블의 메타 데이터를 조회함
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                // withTableName : Insert할 테이블 이름 설정
                .withTableName("room_reserve")
                // usingGeneratedKeyColumns : Auto_increment를 통해 생성된 값 컬럼 자동 입력
                .usingGeneratedKeyColumns("room_reserve_id")
                .usingGeneratedKeyColumns("rsrv_cmpl_st");
    }

    public Long insertReserve(ReserveRequest reserveRequest) {
        reserveRequest.getReserveRequestInfoList().get(0).setRsrvReqDate(LocalDate.now());
        // SqlParameterSource : SQL에 들어갈 parameter(Map객체)를 처리하는 인터페이스
        // BeanPropertySqlParameterSource : Bean객체를 Map객체로 변환
        SqlParameterSource params = new BeanPropertySqlParameterSource(reserveRequest.getReserveRequestInfoList().get(0));

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public int updateReserve(ReserveResponse reserveResponse, Long roomReserveId) {
        ReserveResponseInfo reserveResponseInfo = reserveResponse.getReserveResponseInfoList().get(0);
        reserveResponseInfo.setRsrvCmplDate(LocalDate.now());
        reserveResponseInfo.setRsrvCmplSt("Y");
        reserveResponseInfo.setRoomReserveId(roomReserveId);
        System.out.println(reserveResponseInfo.toString());
        SqlParameterSource params = new BeanPropertySqlParameterSource(reserveResponseInfo);
        return namedParameterJdbcTemplate.update(updateQuery, params);
    }

}
