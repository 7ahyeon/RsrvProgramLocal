package com.local.rsrvprogramlocal.model.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class RsrvResponse { // 한화 리조트에서의 예약 관련 응답
    @SerializedName("ds_prcsResult") // 예약 신청/수정/삭제 JSON Key
    private ArrayList<RsrvResponseInfo> rsrvResponseInfoList1;
    @SerializedName("ds_result") // 예약 기간 조회 JSON Key
    private ArrayList<RsrvResponseInfo> rsrvResponseInfoList2;
}
