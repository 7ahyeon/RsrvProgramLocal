package com.local.rsrvprogramlocal.model.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class RsrvResponseInfo {
    // 처리 일시
    @SerializedName("PROC_DS")
    private LocalDateTime procDs;
    // 처리 코드
    @SerializedName("PROC_CD")
    private String procCd;
    // 고객 번호(교직원공제회 0000000002 고정)
    @SerializedName("CUST_NO")
    private String custNo;
    // 회원 번호
    @SerializedName("MEMB_NO")
    private String membNo;
    // 예약 번호
    @SerializedName("RSRV_NO")
    private long rsrvNo;
    // 객실료
    @SerializedName("ROOM_RATE")
    private String roomRate;
    // 객장 코드
    @SerializedName("LOC_CD")
    private String locCd;
    // 예약 날짜(도착 일자)
    @SerializedName("ARRV_DATE")
    private LocalDate arrvDate;
    // 박수
    @SerializedName("OVNT_CNT")
    private String ovntCnt;
    // 퇴실 예정 일자
    @SerializedName("CHKOT_EXPT_DATE")
    private String chkotExptDate;
    // 투숙 고객명
    @SerializedName("RSRV_CUST_NM")
    private String rsrvCustNm;
    // 투숙 고객 전화번호 2
    @SerializedName("RSRV_CUST_TEL_NO2")
    private String rsrvCustTelNo2;
    // 투숙 고객 전화번호 3
    @SerializedName("RSRV_CUST_TEL_NO3")
    private String rsrvCustTelNo3;
    // 투숙 고객 전화번호 4
    @SerializedName("RSRV_CUST_TEL_NO4")
    private String rsrvCustTelNo4;
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
    // 예약 일자
    @SerializedName("RSRV_DATE")
    private String rsrvDate;
    // 고객 식별 번호
    @SerializedName("CUST_IDNT_NO")
    private String custIdntNo;
    // 시작 날짜
    @SerializedName("STRT_DATE")
    private LocalDate strtDate;
    // 끝 날짜
    @SerializedName("END_DATE")
    private LocalDate endDate;
}
