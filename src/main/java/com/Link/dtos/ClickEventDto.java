package com.Link.dtos;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ClickEventDto {
   private LocalDate clickDate;
   private Long count;

}
