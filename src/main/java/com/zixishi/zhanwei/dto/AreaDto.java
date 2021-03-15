package com.zixishi.zhanwei.dto;

import com.zixishi.zhanwei.model.Area;
import com.zixishi.zhanwei.model.Seat;
import lombok.Data;

import java.util.List;

@Data
public class AreaDto {
   private Long  areaId;

   private String areaName;

   /**
    * 该区域的价格
    */
   private Double amount;

   private List<SeatDTO> seatDTO;
   /**
    * 座位总数
    */
   private Integer totalSeat;
   /**
    * 剩余座位
    */
   private Integer remainingSeat;
}
