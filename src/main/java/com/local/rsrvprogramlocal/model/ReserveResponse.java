package com.local.rsrvprogramlocal.model;

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
public class ReserveResponse { // 한화 리조트에서의 예약 응답
    @SerializedName("ds_prcsResult") // 예약 신청 JSON Key
    private ArrayList<ReserveResponseInfo> reserveResponseInfoList;
}
