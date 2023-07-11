package com.local.rsrvprogramlocal.model;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString
@Setter
@Getter
@NoArgsConstructor
public class ReserveRequestInfo { // 한화 리조트로의 예약 요청 정보
    // 고객 번호(교직원공제회 0000000002 고정)
    @SerializedName("CUST_NO")
    private String custNo;
    // 회원 번호
    @SerializedName("MEMB_NO")
    private String membNo;
    // 고객 식별 번호
    @SerializedName("CUST_IDNT_NO")
    private String custIdntNo;
    // 예약 번호
    @SerializedName("RSRV_NO")
    private long rsrvNo;
    // 계약 번호
    @SerializedName("CONT_NO")
    private String contNo;
    // 패키지 번호
    @SerializedName("PAKG_NO")
    private String pakgNo;
    // 쿠폰 번호
    @SerializedName("CPON_NO")
    private String cponNo;
    // 객장 코드
    @SerializedName("LOC_CD")
    private String locCd;
    // 객실 타입 코드
    @SerializedName("ROOM_TYPE_CD")
    private String roomTypeCd;
    // 객장 코드 분류 Special/Common
    @SerializedName("RSRV_LOC_DIV_CD")
    private String rsrvLocDivCd;
    // 예약 날짜(도착 일자)
    @SerializedName("ARRV_DATE")
    private LocalDate arrvDate;
    // 예약 객실 수
    @SerializedName("RSRV_ROOM_CNT")
    private String rsrvRoomCnt;
    // 박수
    @SerializedName("OVNT_CNT")
    private String ovntCnt;
    // 투숙 고객명
    @SerializedName("INHS_CUST_NM")
    private String inhsCustNm;
    // 투숙 고객 전화번호 2
    @SerializedName("INHS_CUST_TEL_NO2")
    private String inhsCustTelNo2;
    // 투숙 고객 전화번호 3
    @SerializedName("INHS_CUST_TEL_NO3")
    private String inhsCustTelNo3;
    // 투숙 고객 전화번호 4
    @SerializedName("INHS_CUST_TEL_NO4")
    private String inhsCustTelNo4;
    // 예약자명
    @SerializedName("RSRV_CUST_NM")
    private String rsrvCustNm;
    // 예약자 전화번호 2
    @SerializedName("RSRV_CUST_TEL_NO2")
    private String rsrvCustTelNo2;
    // 예약자 전화번호 3
    @SerializedName("RSRV_CUST_TEL_NO3")
    private String rsrvCustTelNo3;
    // 예약자 전화번호 4
    @SerializedName("RSRV_CUST_TEL_NO4")
    private String rsrvCustTelNo4;
    // 리프레쉬 여부
    @SerializedName("REFRESH_YN")
    private String refreshYn;
}
