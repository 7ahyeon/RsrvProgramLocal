package com.local.rsrvprogramlocal.model;

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
}
