package com.local.rsrvprogramlocal.dao;

import com.local.rsrvprogramlocal.model.ReserveRequest;
import com.local.rsrvprogramlocal.model.ReserveRequestInfo;
import com.local.rsrvprogramlocal.model.ReserveResponse;
import com.local.rsrvprogramlocal.model.ReserveResponseInfo;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

@Repository
public class ReserveRepository {
    // JdbcTemplate : DAO객체에서 DB와 연동하기 위해 SQL연산들을 수행할 수 있도록 도와줌
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final String updateQuery = "UPDATE room_reserve SET rsrv_no = :rsrvNo, rsrv_cmpl_date = :rsrvCmplDate, rsrv_cmpl_st = :rsrvCmplSt WHERE room_reserve_id = :roomReserveId";
    private final String selectQuery = "SELECT * FROM room_reserve WHERE room_reserve_id = :roomReserveId";
    private final String selectQueryByrsrvNo = "SELECT * FROM room_reserve WHERE rsrv_no = :rsrvNo";

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
        SqlParameterSource params = new BeanPropertySqlParameterSource(reserveResponseInfo);
        return namedParameterJdbcTemplate.update(updateQuery, params);
    }

    public ReserveRequest selectRequest(Long roomReserveId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("roomReserveId", roomReserveId);

        //namedParameterJdbcTemplate.query(selectQuery, params)
        ReserveRequestInfo requestInfo = namedParameterJdbcTemplate.queryForObject(selectQuery, params, new RequestMapper());
        ReserveRequest request = new ReserveRequest();
        request.setReserveRequestInfoList(new ArrayList<>());
        request.getReserveRequestInfoList().add(requestInfo);

        return request;
    }
    public ReserveRequest selectRequestComplete(Long rsrvNo) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("rsrvNo", rsrvNo);

        //namedParameterJdbcTemplate.query(selectQuery, params)
        ReserveRequestInfo requestInfo = namedParameterJdbcTemplate.queryForObject(selectQueryByrsrvNo, params, new RequestMapper());
        ReserveRequest request = new ReserveRequest();
        request.setReserveRequestInfoList(new ArrayList<>());
        request.getReserveRequestInfoList().add(requestInfo);

        return request;
    }

    // RowMapper : JDBC의 인터페이스인 ResultSet에서 원하는 객체로 변환하는 역할
    private static final class RequestMapper implements RowMapper<ReserveRequestInfo> {
        @Override
        public ReserveRequestInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

            ReserveRequestInfo requestInfo = new ReserveRequestInfo();
            requestInfo.setMembNo(rs.getString("memb_no"));
            requestInfo.setCustIdntNo(rs.getString("cust_Idnt_No"));
            requestInfo.setLocCd(rs.getString("loc_cd"));
            requestInfo.setRoomTypeCd(rs.getString("room_type_cd"));
            requestInfo.setRsrvLocDivCd(rs.getString("rsrv_loc_div_cd"));
            requestInfo.setCustNo(rs.getString("cust_no"));
            requestInfo.setRsrvNo(rs.getString("rsrv_no"));
            requestInfo.setArrvDate(LocalDate.parse(rs.getString("arrv_date")));
            requestInfo.setRsrvRoomCnt(rs.getString("rsrv_room_cnt"));
            requestInfo.setOvntCnt(rs.getString("ovnt_cnt"));
            requestInfo.setContNo(rs.getString("cont_no"));
            requestInfo.setPakgNo(rs.getString("pakg_no"));
            requestInfo.setCponNo(rs.getString("cpon_no"));
            requestInfo.setInhsNm(rs.getString("inhs_nm"));
            requestInfo.setInhsPhoneNo2(rs.getString("inhs_phone_no2"));
            requestInfo.setInhsPhoneNo3(rs.getString("inhs_phone_no3"));
            requestInfo.setInhsPhoneNo4(rs.getString("inhs_phone_no4"));
            requestInfo.setCustNm(rs.getString("cust_nm"));
            requestInfo.setCustPhoneNo2(rs.getString("cust_phone_no2"));
            requestInfo.setCustPhoneNo3(rs.getString("cust_phone_no3"));
            requestInfo.setCustPhoneNo4(rs.getString("cust_phone_no4"));
            requestInfo.setRsrvReqDate(LocalDate.parse(rs.getString("rsrv_req_date")));
            if (rs.getString("rsrv_cmpl_date") != null) {
                requestInfo.setRsrvCmplDate(LocalDate.parse(rs.getString("rsrv_cmpl_date")));
            }
            requestInfo.setRsrvCmplSt(rs.getString("rsrv_cmpl_st"));

            return requestInfo;
        }
    }

}
