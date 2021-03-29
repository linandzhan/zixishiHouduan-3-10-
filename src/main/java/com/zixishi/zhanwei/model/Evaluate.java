package com.zixishi.zhanwei.model;



import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Evaluate {
    private Long id;

    private Integer stars;

    private Boolean enabled;

    private LocalDateTime createTime;

    private User user;

    private Clock clock;

    private String content;
}
