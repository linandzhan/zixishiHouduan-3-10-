package com.zixishi.zhanwei.dto;

import com.zixishi.zhanwei.model.Area;
import com.zixishi.zhanwei.model.Seat;
import lombok.Data;

import java.util.List;

@Data
public class AreaDto {
   private Long  areaId;

   private String areaName;


   private List<SeatDTO> seatDTO;
}
